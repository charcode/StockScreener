package com.oak.finance.interest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
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

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.SectorsIndustriesCompanies;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.CompanyWithProblems;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.ErrorType;
import com.oak.api.finance.model.dto.Screen0Result;
import com.oak.api.finance.model.dto.Sector;
import com.oak.api.finance.model.dto.Status;
import com.oak.api.finance.repository.CompanyRepository;
import com.oak.api.finance.repository.CompanyWithProblemsRepository;
import com.oak.api.finance.repository.Screen0ResultsRepository;
import com.oak.api.finance.repository.SectorRepository;
import com.oak.api.providers.control.ControlProvider;
import com.oak.api.providers.control.ControlType;
import com.oak.external.finance.app.marketdata.api.DataConnector;
import com.oak.external.finance.app.marketdata.api.SectorsCompaniesYahooWebDao;
import com.oak.finance.app.dao.SymbolsDao;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

public class SymbolsControllerImpl implements SymbolsController {

	private final SymbolsDao symbolsTextFileDao;
	private final SectorsCompaniesYahooWebDao sectorCompaniesDao;
	private final ControlProvider controlProvider;
	private final CompanyRepository companyRepository;
	private final CompanyWithProblemsRepository companyWithErrorsRepository;
	private final SectorRepository sectorRepository;
	private final Logger log;
	private final Screen0ResultsRepository screeningResultsRepository;
	private final DataConnector dataConnector;
	private final CompanyRepository newCompRep;

	public SymbolsControllerImpl(SymbolsDao symbolsDao, SectorsCompaniesYahooWebDao sectorCompaniesDao,
			ControlProvider controlProvider, CompanyRepository companyRepository,
			CompanyWithProblemsRepository companyWithErrorsRepository, SectorRepository sectorRepository,
			Screen0ResultsRepository screeningResultsRepository, DataConnector dataConnector, Logger log) {
		this.symbolsTextFileDao = symbolsDao;
		this.sectorCompaniesDao = sectorCompaniesDao;
		this.controlProvider = controlProvider;
		this.companyRepository = companyRepository;
		this.companyWithErrorsRepository = companyWithErrorsRepository;
		this.sectorRepository = sectorRepository;
		this.screeningResultsRepository = screeningResultsRepository;
		this.dataConnector = dataConnector;
		this.newCompRep = companyRepository;
		Logger mylog;
		if (log == null) {
			mylog = LogManager.getLogger(this.getClass());
		} else {
			mylog = log;
		}
		this.log = mylog;
	}

	/**
	 * Maintenance code that copies summary results of previous runs from csv to
	 * db
	 */
	private void populateExistingResultsInDb() {
		Iterable<Screen0Result> savedResults = screeningResultsRepository.findAll();
		SortedMap<Date, List<Screen0Result>> resultsByDate = new TreeMap<>(
				StreamUtils.createStreamFromIterator(savedResults.iterator())
						.collect(Collectors.groupingBy(Screen0Result::getRunDate)));
		Date firstResult;
		if (!resultsByDate.isEmpty()) {
			firstResult = resultsByDate.firstKey();
		} else {
			firstResult = null;
		}

		if (firstResult != null && firstResult.toInstant().isAfter(new GregorianCalendar(2014, 2, 11).toInstant())) {
			// nothing to do
			log.info("results in db are up-to-date");
		} else {
			// need to back fill the result
			Collection<Screen0Result> results = retriedLegacyResultsFromFile();
			screeningResultsRepository.save(results);

		}
	}

	/**
	 * Maintenance for cleaning up wrong dividend yield and market cap fields
	 */
	private void cleanupMarketCapAndYieldDividenInExistingResultsInDb() {
		Iterable<Screen0Result> results = screeningResultsRepository.findAll();
		List<Screen0Result> wrongResults = StreamUtils.createStreamFromIterator(results.iterator())
				.filter(r -> r.getMarketCap() < r.getDividendYield()).collect(Collectors.toList());
		wrongResults.stream().forEach(r -> {
			Double yld = r.getDividendYield();
			r.setDividendYield(r.getMarketCap());
			r.setMarketCap(yld);
		});
		screeningResultsRepository.save(wrongResults);
	}

	private void addExchangeToCompaniesAndPriceToResult() {
		saveExchangeToCompanies();

		// Iterable<Screen0Result> results =
		// screeningResultsRepository.findAll();
		//
		// savePriceWithResult(results);
	}

	private void saveExchangeToCompanies() {
		Iterable<Company> companies = companyRepository.findAll();
		Set<String> cmpnies = StreamUtils.createStreamFromIterator(companies.iterator()).map(c -> c.getTicker())
				.collect(Collectors.toSet());
		// stock exch date price
		Map<Pair<String, String>, Map<Date, Double>> companiesExchangePrice = getMarketData(cmpnies, null);
		saveNewCompanies(companies, companiesExchangePrice);
	}

	private void savePriceWithResult(Iterable<Screen0Result> results) {
		Map<Date, List<Screen0Result>> resultByDate = StreamUtils.createStreamFromIterator(results.iterator())
				.collect(Collectors.groupingBy(Screen0Result::getRunDate));
		for (Date d : resultByDate.keySet()) {
			Set<Screen0Result> newResults = new HashSet<>();
			List<Screen0Result> resList = resultByDate.get(d);
			Set<String> ticks = resList.stream().map(r -> r.getTicker()).collect(Collectors.toSet());
			Map<String, List<Screen0Result>> resPerStockPerDate = resList.stream()
					.collect(Collectors.groupingBy(Screen0Result::getTicker));

			Map<Pair<String, String>, Map<Date, Double>> marketData = getMarketData(ticks, d);
			for (Pair<String, String> stEx : marketData.keySet()) {
				Map<Date, Double> map = marketData.get(stEx);
				for (Date date : map.keySet()) {
					try {
						Double price = map.get(date);
						for (Screen0Result r : resPerStockPerDate.get(stEx.getKey())) {

							Screen0Result nr = new Screen0Result();
							nr.setId(r.getId());
							nr.setPrice(price);
							nr.setBookValuePerShare(r.getBookValuePerShare());
							nr.setCompanyName(r.getCompanyName());
							nr.setCurrency(r.getCurrency());
							nr.setDividendYield(r.getDividendYield());
							nr.setEps(r.getEps());
							nr.setEpsEstNextQuarter(r.getEpsEstNextQuarter());
							nr.setEpsEstNextYear(r.getEpsEstNextYear());
							nr.setEpsEstThisYear(r.getEpsEstThisYear());
							nr.setExchangeCode(r.getExchangeCode());
							nr.setMarketCap(r.getMarketCap());
							nr.setPeg(r.getPeg());
							nr.setPer(r.getPer());
							nr.setPerCalculated(r.getPerCalculated());
							nr.setPriceBid(r.getPriceBid());
							nr.setRunDate(r.getRunDate());
							nr.setTargetPrice(r.getTargetPrice());
							nr.setTicker(r.getTicker());

							newResults.add(nr);
						}
					} catch (Exception e) {
						log.error("somehting went wrong ", e);
					}
				}
			}
			screeningResultsRepository.save(newResults);
		}
	}

	private void saveNewCompanies(Iterable<Company> companies,
			Map<Pair<String, String>, Map<Date, Double>> companiesExchangePrice) {
		Map<String, String> companyExchMap = companiesExchangePrice.keySet().stream()
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));

		Set<Company> newCompanies = StreamUtils.createStreamFromIterator(companies.iterator())
				.map(c -> cloneCompanyAndAssignExchange(c, companyExchMap)).collect(Collectors.toSet());

		newCompRep.save(newCompanies);
	}

	private Company cloneCompanyAndAssignExchange(Company c, Map<String, String> companyExchMap) {

		Company c0 = new Company();
		c0.setId(c.getId());
		c0.setDescription(c.getDescription());
		c0.setExchange(companyExchMap.get(c.getTicker()));
		c0.setIndustryDescription(c.getIndustryDescription());
		c0.setIndustryId(c.getIndustryId());
		c0.setName(c.getName());
		c0.setSectorDescription(c.getSectorDescription());
		c0.setSectorId(c.getSectorId());
		c0.setTicker(c.getTicker());
		return c0;

	}

	private <T> Collection<Set<T>> batch(Set<T> set, int size) {
		Collection<Set<T>> ret = null;

		if (set != null) {
			ret = new ArrayList<Set<T>>();
			int i = 0;
			Set<T> batch = new HashSet<T>();
			for (T e : set) {
				batch.add(e);
				if (i >= size - 1) {
					ret.add(batch);
					batch = new HashSet<T>();
					i = 0;
				} else {
					i++;
				}
			}
			if (!batch.isEmpty()) {
				ret.add(batch);
			}
		}
		return ret;
	}

	private Map<Pair<String, String>, Map<Date, Double>> getMarketData(Set<String> symbolList, Date date) {
		Collection<Set<String>> batches = batch(symbolList, 10);
		Map<Pair<String, String>, Map<Date, Double>> pricePerDatePerSock = new HashMap<>();
		for (Set<String> batch : batches) {
			Map<Stock, Map<Date, Economic>> batchResult = dataConnector.getEconomics(batch, date);
			for (Stock s : batchResult.keySet()) {
				Map<Date, Economic> economyMaps = batchResult.get(s);
				Map<Date, Double> m = new HashMap<>();
				pricePerDatePerSock.put(Pair.of(s.getSymbol(), s.getStockExchange()), m);
				for (Date d : economyMaps.keySet()) {
					Economic e = economyMaps.get(d);
					m.put(d, e.getBid());
				}
			}
		}
		return pricePerDatePerSock;

	}

	@Override
	public Set<String> getSymbols() {
		/*
		addExchangeToCompaniesAndPriceToResult();
	    cleanupExistingResultsInDb(); 
	    populateExistingResultsInDb();
		 */
//		Collection<Control> symbolAndSectorRefresh = controlRepository.findByType(ControlType.SYMBOL_SECTOR_REFRESH);
		
//		if (symbolAndSectorRefresh != null && !symbolAndSectorRefresh.isEmpty()) {
//			TreeMap<Date, List<Control>> refreshes = new TreeMap<>(
//					symbolAndSectorRefresh.stream().collect(Collectors.groupingBy(Control::getTimeStamp)));
//			LocalDate lastRefresh = null;
//			for (Date refreshDate : refreshes.descendingKeySet()) {
//				for (Control ref : refreshes.get(refreshDate)) {
//					if (ref.getStatus().equals(Status.SUCCESS)) {
//						lastRefresh = refreshDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//						break;
//					}
//				}
//				if (lastRefresh != null) {
//					break;
//				}
//			}
		Set<String> symbols;
		Pair<Boolean, Set<String>> refreshSymbolsIfNeeded = refreshSymbolsIfNeeded();
		if(refreshSymbolsIfNeeded.getLeft()) {
			symbols = refreshSymbolsIfNeeded.getRight();
		} else {
			Iterable<Company> companies = companyRepository.findAll();
			symbols = StreamUtils.createStreamFromIterator(companies.iterator()).map(a -> a.getTicker())
					.collect(Collectors.toSet());
		}
		return symbols;
	}

	@Override
	public Pair<Boolean,Set<String>> refreshSymbolsIfNeeded() {
		boolean symbolsReloadNeeded = true;
		Control lastSymbolLoad = controlProvider.getLatestControlByType(ControlType.SYMBOL_SECTOR_REFRESH);
		LocalDate lastRefresh = null;
		if (Control.isExist(lastSymbolLoad)) {
			lastRefresh = lastSymbolLoad.getTimeStamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		LocalDate now = LocalDate.now();
		if (lastRefresh == null) {
			log.info("No record of successful symbols refresh");
			symbolsReloadNeeded = true;
		} else {
			long daysSinceLastRefresh = ChronoUnit.DAYS.between(lastRefresh, now);
			long maxTimeBeforeRefresh = 60;
//			int daysSinceLastRefresh = Period.between(lastRefresh, now).getDays();
			if (daysSinceLastRefresh > maxTimeBeforeRefresh) {
				log.info("It's been " + daysSinceLastRefresh + " days since last symbol refresh, we need to refresh");
				symbolsReloadNeeded = true;
			} else {
				log.info("It's been only " + daysSinceLastRefresh
						+ " days since last symbol refresh, no need for refresh");
				symbolsReloadNeeded = false;
			}
		}
		Set<String> symbols = null;
		if (symbolsReloadNeeded) {
			Pair<Control, Set<String>> symbolsAndControl = refrehSymbols();
			Control control = symbolsAndControl.getLeft();
			controlProvider.save(control);
			symbols = symbolsAndControl.getRight();
		}
		Pair<Boolean,Set<String>>ret = Pair.of(symbolsReloadNeeded,symbols);
		return ret;
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

			Map<String, List<Sector>> industriesByDescription = StreamUtils
					.createStreamFromIterator(allSavedSectorsAndIndustries.iterator())
					.collect(Collectors.groupingBy(Sector::getDescription));

			Set<Company> companies = sectorsAndCompanies.companies();
			Iterable<Company> savedCompanies = companyRepository.findAll();
			Set<Company> companiesToSave = findUnsavedCompanies(companies, savedCompanies);
			companies.stream().forEach(f -> setIndustryAndSectorId(f, industriesByDescription));
			log.info("saving companies " + companiesToSave.size());
			companyRepository.save(companiesToSave);
			log.info("done refreshing sectors, industries and companies " + companiesToSave.size());

			c.setComments("Successful");
			c.setStatus(Status.SUCCESS);
			c.setTimeStamp(java.sql.Date.valueOf(LocalDate.now()));
			c.setType(ControlType.SYMBOL_SECTOR_REFRESH);
			firms = companies.stream().map(a -> a.getTicker()).collect(Collectors.toSet());
		} catch (Throwable t) {
			log.error(t.getMessage(),t);
			firms = new HashSet<>();
			c.setComments(t.getMessage());
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
		// if (!problematicCompanies.isEmpty()) {
		// Set<Company> toRemove = companies.stream().filter(c ->
		// duplicatedTickers.contains(c.getTicker()))
		// .collect(Collectors.toSet());
		//
		// companies.removeAll(toRemove);
		// savedCompaniesSet.removeAll(toRemove);
		// }

		Map<String, Company> companiesByTicker = savedCompaniesSet.stream()
				.collect(Collectors.toMap(Company::getTicker, Function.identity()));

		companies.forEach(n -> {
			// check if it exists already
			if (companiesByTicker.containsKey(n.getTicker())) {
				// already exist - check relevant data
				Company existing = companiesByTicker.get(n.getTicker());
				if (!existing.equals(n)) {
					n.setId(existing.getId());
					n.setExchange(existing.getExchange());
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
		Map<String, List<Sector>> sectorsByDescription = StreamSupport.stream(allSectorsAndIndustries.spliterator(), false)
				.collect(Collectors.groupingBy(Sector::getDescription));

		sectors.forEach(n -> {
			// check if it exists already
			if (sectorsByDescription.containsKey(n.getDescription())) {
				// already exist - check relevant data
				List<Sector> sectorsForDesc = sectorsByDescription.get(n.getDescription());
				if(sectorsForDesc.size()>1) {
					log.warn("Sector desc duplicated "+sectorsForDesc);
				}	
				Sector existing = sectorsForDesc.iterator().next();
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

	private void setIndustryAndSectorId(Company c, Map<String, List<Sector>> industriesByDescription) {
		List<Sector> sectors = industriesByDescription.get(c.getSectorDescription());
		List<Sector> industries = industriesByDescription.get(c.getIndustryDescription());
		if(sectors.size()>1) {
			log.warn("sectors with same desc: "+sectors);
		}
		if(industries.size()>1) {
			log.warn("industries with same desc: "+industries);
		}
		Sector sector = sectors.iterator().next();
		Sector industry = industries.iterator().next();
		if (sector != null && sector.getId() != null) {
			c.setSectorId(sector.getId());
		}
		if (industry != null && industry.getId() != null) {
			c.setIndustryId(industry.getId());
		}
	}

	private Collection<Sector> wireSectorsAndIndustries(Iterable<Sector> allSectorsAndIndustries,
			Collection<Sector> newSectors) {
		Map<String, List<Sector>> sectors = StreamUtils.createStreamFromIterator(allSectorsAndIndustries.iterator())
				.collect(Collectors.groupingBy(Sector::getDescription));

		newSectors.stream().filter(s -> s.getParentSectorDescription() != null)
				.forEach(s -> setIndustrySectorId(s, sectors));
		return newSectors;
	}

	private void setIndustrySectorId(Sector s, Map<String, List<Sector>> allSectorsAndIndustries) {
		List<Sector> sectors = allSectorsAndIndustries.get(s.getParentSectorDescription());
		if(sectors.size()>1) {
			log.warn("Found multiple sectors for desc: "+s.getParentSectorDescription()+" : "+sectors);
		}
		Sector sector = sectors.iterator().next();
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
		// to do // replace this with
		symbolsTextFileDao.saveGoodValueStock(stock, economics);
		Screen0Result res = convertToScreen0Result(stock, economics);
		log.info("Saving to db "+res);
		screeningResultsRepository.save(res);
	}

	private Screen0Result convertToScreen0Result(Stock stock, Map<Date, Economic> economics) {
		DateFormat df = new SimpleDateFormat();
		Screen0Result res = new Screen0Result();
		for (Date date : economics.keySet()) {
			Economic e = economics.get(date);
			res.setTicker(stock.getSymbol());
			res.setCompanyName(stock.getName());
			res.setExchangeCode(stock.getStockExchange());
			res.setCurrency(stock.getCurrency());
			res.setRunDate(date);
			res.setPriceBid(e.getBid());
			res.setBookValuePerShare(e.getBookValuePerShare());
			res.setPerCalculated(e.getPerCalculated());
			res.setEps(e.getEps());
			res.setPer(e.getPe());
			res.setPeg(e.getPeg());
			res.setEpsEstThisYear(e.getEpsEstimateCurrentYear());
			res.setEpsEstNextQuarter(e.getEpsEstimateNextQuarter());
			res.setEpsEstNextYear(e.getEpsEstimateNextYear());
			res.setMarketCap(e.getMarketCap());
			res.setDividendYield(e.getAnnualDividendYieldPercent());
			res.setTargetPrice(e.getOneYearTargetPrice());
			res.setPrice(e.getBid());
		}
		return res;
	}

	@Override
	public Collection<Screen0Result> retriedLegacyResultsFromFile() {
		Collection<Screen0Result> results = symbolsTextFileDao.readPreviousResults();
		return results;
	}

	@Override
	public void saveNewCompanies(Set<Company> companies) {
		companyRepository.save(companies);
	}

}
