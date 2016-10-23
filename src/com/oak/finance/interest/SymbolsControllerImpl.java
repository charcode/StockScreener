package com.oak.finance.interest;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.util.StreamUtils;

import com.google.common.collect.Maps;
import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.SectorsIndustriesCompanies;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.CompanyWithProblems;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.ControlType;
import com.oak.api.finance.model.dto.ErrorType;
import com.oak.api.finance.model.dto.Sector;
import com.oak.api.finance.model.dto.Status;
import com.oak.api.finance.repository.CompanyRepository;
import com.oak.api.finance.repository.CompanyWithProblemsRepository;
import com.oak.api.finance.repository.ControlRepository;
import com.oak.api.finance.repository.SectorRepository;
import com.oak.external.finance.app.marketdata.api.SectorsCompaniesYahooWebDao;
import com.oak.finance.app.dao.SymbolsDao;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

public class SymbolsControllerImpl implements SymbolsController {

	private final SymbolsDao symbolsTextFileDao;
	private final SectorsCompaniesYahooWebDao sectorCompaniesDao;
	private final ControlRepository controlRepository;
	private final CompanyRepository companyRepository;
	private final CompanyWithProblemsRepository companyWithErrorsRepository;
	private final SectorRepository sectorRepository;
	private final Logger log;
	private final Period maxTimeBeforeRefresh = Period.ofMonths(3);

	public SymbolsControllerImpl(SymbolsDao symbolsDao, SectorsCompaniesYahooWebDao sectorCompaniesDao,
			ControlRepository controlRepository, CompanyRepository companyRepository,
			CompanyWithProblemsRepository companyWithErrorsRepository, SectorRepository sectorRepository, Logger log) {
		this.symbolsTextFileDao = symbolsDao;
		this.sectorCompaniesDao = sectorCompaniesDao;
		this.controlRepository = controlRepository;
		this.companyRepository = companyRepository;
		this.companyWithErrorsRepository = companyWithErrorsRepository;
		this.sectorRepository = sectorRepository;
		Logger mylog;
		if (log == null) {
			mylog = LogManager.getLogger(this.getClass());
		} else {
			mylog = log;
		}
		this.log = mylog;
	}

	@Override
	public Set<String> getSymbols() {
		//
		// log.debug("reading symbol list from dao:"+symbolsTextFileDao);
		// Set<String> stocksList = symbolsTextFileDao.getSymbols();

		Collection<Control> symbolAndSectorRefresh = controlRepository.findByType(ControlType.SYMBOL_SECTOR_REFRESH);
		boolean symbolsReloadNeeded = true;
		if (symbolAndSectorRefresh != null && !symbolAndSectorRefresh.isEmpty()) {
			TreeMap<java.sql.Date, Control> refreshes = new TreeMap<>(
					Maps.uniqueIndex(symbolAndSectorRefresh, Control::getTimeStamp));
			LocalDate lastRefresh = null;
			for (java.sql.Date refreshDate : refreshes.descendingKeySet()) {
				Control ref = refreshes.get(refreshDate);
				if (ref.getStatus().equals(Status.SUCCESS)) {
					lastRefresh = refreshDate.toLocalDate();
					break;
				}
			}
			LocalDate now = LocalDate.now();
			if (lastRefresh == null) {
				log.info("No record of successful symbols refresh");
				symbolsReloadNeeded = true;
			} else {
				int daysSinceLastRefresh = Period.between(now, lastRefresh).getDays();
				if (daysSinceLastRefresh > maxTimeBeforeRefresh.getDays()) {
					log.info("It's been " + daysSinceLastRefresh
							+ " days since last symbol refresh, we need to refresh");
					symbolsReloadNeeded = true;
				} else {
					log.info("It's been only" + daysSinceLastRefresh
							+ " days since last symbol refresh, no need for refresh");
					symbolsReloadNeeded = false;
				}
			}
		}
		Set<String> symbols;
		if (symbolsReloadNeeded) {
			Pair<Control, Set<String>> symbolsAndControl = refrehSymbols();
			Control control = symbolsAndControl.getLeft();
			controlRepository.save(control);
			symbols = symbolsAndControl.getRight();
		} else {
			Iterable<Company> companies = companyRepository.findAll();
			symbols = StreamUtils.createStreamFromIterator(companies.iterator()).map(a -> a.getTicker())
					.collect(Collectors.toSet());
		}
		return symbols;
	}

	private Pair<Control, Set<String>> refrehSymbols() {
		SectorsIndustriesCompanies sectorsAndCompanies;
		sectorsAndCompanies = sectorCompaniesDao.getSectorsAndCompanies();
		Pair<Control, Set<String>> c = processAndSaveCompaniesSectorsIndustries(sectorsAndCompanies);
		return c;
	}

	private Pair<Control, Set<String>> processAndSaveCompaniesSectorsIndustries(
			SectorsIndustriesCompanies sectorsAndCompanies) {
		Control c = new Control();
		Set<String> firms;
		try {
			Set<Sector> allRefreshedSectors = sectorsAndCompanies.sectors();
			Set<Sector> allRefreshedIndustries = sectorsAndCompanies.industries();
			Iterable<Sector> savedSectorsAndIndustries = sectorRepository.findAll();
			Set<Sector> unsavedSectors = findUnsavedSectors(allRefreshedSectors.stream(), savedSectorsAndIndustries);

			log.info("saving sectors " + unsavedSectors.size());
			sectorRepository.save(unsavedSectors);
			savedSectorsAndIndustries = sectorRepository.findAll();
			wireSectorsAndIndustries(savedSectorsAndIndustries, allRefreshedSectors);
			Set<Sector> unsavedIndustries = findUnsavedSectors(allRefreshedIndustries.stream(),
					savedSectorsAndIndustries);

			log.info("saving industries: " + unsavedIndustries.size());
			sectorRepository.save(unsavedIndustries);
			Iterable<Sector> allSavedSectorsAndIndustries = sectorRepository.findAll();

			Map<String, Sector> industriesByDescription = StreamUtils
					.createStreamFromIterator(allSavedSectorsAndIndustries.iterator())
					.collect(Collectors.toMap(Sector::getDescription, Function.identity()));

			Set<Company> companies = sectorsAndCompanies.companies();
			Iterable<Company> savedCompanies = companyRepository.findAll();
			Set<Company> companiesToSave = findUnsavedCompanies(companies, savedCompanies);
			companies.stream().forEach(f -> setIndustryAndSectorId(f, industriesByDescription));
			log.info("saving companies " + companiesToSave.size());
			companyRepository.save(companiesToSave);
			log.info("done refreshing sectors, industries and companies " + companiesToSave.size());

			c.setComments("Still testing");
			c.setStatus(Status.IN_TEST);
			c.setTimeStamp(java.sql.Date.valueOf(LocalDate.now()));
			c.setType(ControlType.SYMBOL_SECTOR_REFRESH);
			firms = companies.stream().map(a -> a.getTicker()).collect(Collectors.toSet());
		} catch (Throwable t) {
			firms = new HashSet<>();
			c.setStatus(Status.FAIL);
		}
		Pair<Control, Set<String>> ret = new ImmutablePair<Control, Set<String>>(c, firms);
		return ret;
	}

	private Set<Company> findUnsavedCompanies(Set<Company> companies, Iterable<Company> savedCompanies) {
		Set<Company> ret = new HashSet<>();
		Map<String, Long> grouped = StreamUtils.createStreamFromIterator(savedCompanies.iterator())
				.collect(Collectors.groupingBy(c -> c.getTicker(), Collectors.counting()));

		Set<String> duplicatedTickers = new HashSet<String>();
		for (String ticker : grouped.keySet()) {
			if (grouped.get(ticker) > 1) {
				duplicatedTickers.add(ticker);
			}
		}
		Set<CompanyWithProblems> problematicCompanies = companies.stream()
				.filter(c -> duplicatedTickers.contains(c.getTicker())).map(c -> convertCompanyToDuplicate(c))
				.collect(Collectors.toSet());
		companyWithErrorsRepository.save(problematicCompanies);
		Set<String> duplicateTickers = problematicCompanies.stream().map(c -> c.getTicker())
				.collect(Collectors.toSet());
		Set<Company> savedCompaniesSet = StreamUtils.createStreamFromIterator(savedCompanies.iterator())
				.filter(c -> !duplicateTickers.contains(c.getTicker())).collect(Collectors.toSet());
		if (!problematicCompanies.isEmpty()) {
			Set<Company> toRemove = companies.stream().filter(c -> duplicatedTickers.contains(c.getTicker()))
					.collect(Collectors.toSet());

			companies.removeAll(toRemove);
			savedCompaniesSet.removeAll(toRemove);
			System.out.println(companies.size() + "  " + savedCompaniesSet.size());
		}

		Map<String, Company> companiesByTicker = savedCompaniesSet.stream()
				.collect(Collectors.toMap(Company::getTicker, Function.identity()));

		companies.forEach(n -> {
			// check if it exists already
			if (companiesByTicker.containsKey(n.getTicker())) {
				// already exist - check relevant data
				Company existing = companiesByTicker.get(n.getTicker());
				if (!existing.equals(n)) {
					n.setId(existing.getId());
					ret.add(n);
				}
			} else {
				ret.add(n);
			}
		});

		return ret;
	}

	private CompanyWithProblems convertCompanyToDuplicate(Company c) {
		CompanyWithProblems cp = new CompanyWithProblems();
		cp.setDescription(c.getDescription());
		cp.setErrorDate(java.sql.Date.valueOf(LocalDate.now()));
		cp.setIndustryDescription(c.getIndustryDescription());
		cp.setSectorDescription(c.getSectorDescription());
		cp.setName(c.getName());
		cp.setTicker(c.getTicker());
		cp.setErrorType(ErrorType.DUPLICATE);
		return cp;
	}

	private Set<Sector> findUnsavedSectors(Stream<Sector> sectors, Iterable<Sector> allSectorsAndIndustries) {
		Set<Sector> ret = new HashSet<>();
		Map<String, Sector> sectorsByDescription = StreamSupport.stream(allSectorsAndIndustries.spliterator(), false)
				.collect(Collectors.toMap(Sector::getDescription, Function.identity()));

		sectors.forEach(n -> {
			// check if it exists already
			if (sectorsByDescription.containsKey(n.getDescription())) {
				// already exist - check relevant data
				Sector existing = sectorsByDescription.get(n.getDescription());
				if (!existing.equals(n)) {
					n.setId(existing.getId());
					if (!existing.equals(n) &&
					// something about Conglomerates make them weird
					!existing.getDescription().equals("Conglomerates")) {
						ret.add(n);
						diff(n, existing);
					}
				}
			} else {
				ret.add(n);
			}
		});
		return ret;
	}

	private void diff(Sector working, Sector base) {
		DiffNode diff = ObjectDifferBuilder.buildDefault().compare(base, working);
		diff.visit(new DiffNode.Visitor() {
			public void node(DiffNode node, Visit visit) {
				final Object baseValue = node.canonicalGet(base);
				final Object workingValue = node.canonicalGet(working);
				final String message = node.getPath() + " changed from " + baseValue + " to " + workingValue;
				System.out.println(message);
			}
		});

	}

	private void setIndustryAndSectorId(Company c, Map<String, Sector> industriesByDescription) {
		Sector sector = industriesByDescription.get(c.getSectorDescription());
		Sector industry = industriesByDescription.get(c.getIndustryDescription());
		if (sector != null && sector.getId() != null) {
			c.setSectorId(sector.getId());
		}
		if (industry != null && industry.getId() != null) {
			c.setIndustryId(industry.getId());
		}
	}

	private Collection<Sector> wireSectorsAndIndustries(Iterable<Sector> allSectorsAndIndustries,
			Collection<Sector> newSectors) {
		Map<String, Sector> sectors = StreamUtils.createStreamFromIterator(allSectorsAndIndustries.iterator())
				.collect(Collectors.toMap(Sector::getDescription, Function.identity()));

		newSectors.stream().filter(s -> s.getParentSectorDescription() != null)
				.forEach(s -> setIndustrySectorId(s, sectors));
		return newSectors;
	}

	private void setIndustrySectorId(Sector s, Map<String, Sector> allSectorsAndIndustries) {
		Sector sector = allSectorsAndIndustries.get(s.getParentSectorDescription());
		try {
			if (sector.getId() != null) {
				s.setParentSectorId(sector.getId());
			}
		} catch (Throwable t) {
			log.error("Some error happened when getting sector for " + s.getDescription() + " s: " + s, t);
		}
	}

	@Override
	public Set<String> getExcludedSymbols() {
		Set<String> savedStocksWithoutPrices = symbolsTextFileDao.getSavedSymbolsWithoutPrices();
		return savedStocksWithoutPrices;
	}

	@Override
	public Set<String> getInterestingSymbols() {
		Set<String> interestingSymbols = symbolsTextFileDao.getInterestingSymbols();
		return interestingSymbols;
	}

	@Override
	public void saveSymbolsWithoutPrice(Map<String, Date> existingStocksWithoutPrices) {
		symbolsTextFileDao.saveSymbolsWithoutPrice(existingStocksWithoutPrices);
	}

	@Override
	public void saveGoodValueStock(Stock stock, Map<Date, Economic> economics) {
		symbolsTextFileDao.saveGoodValueStock(stock, economics);
	}

}
