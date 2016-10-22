package com.oak.finance.interest;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.SectorsIndustriesCompanies;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.ControlType;
import com.oak.api.finance.model.dto.Sector;
import com.oak.api.finance.repository.CompanyRepository;
import com.oak.api.finance.repository.ControlRepository;
import com.oak.api.finance.repository.SectorRepository;
import com.oak.external.finance.app.marketdata.api.SectorsCompaniesYahooWebDao;
import com.oak.finance.app.dao.SymbolsDao;

public class SymbolsControllerImpl implements SymbolsController {

	private final SymbolsDao symbolsTextFileDao;
	private final SectorsCompaniesYahooWebDao sectorCompaniesDao;
	private final ControlRepository controlRepository;
	private final CompanyRepository companyRepository;
	private final SectorRepository sectorRepository;
	private final Logger log;
	private final Period maxTimeBeforeRefresh = Period.ofMonths(3);

	public SymbolsControllerImpl(SymbolsDao symbolsDao, SectorsCompaniesYahooWebDao sectorCompaniesDao, ControlRepository controlRepository, CompanyRepository companyRepository, SectorRepository sectorRepository, Logger log) {
		this.symbolsTextFileDao = symbolsDao;
		this.sectorCompaniesDao = sectorCompaniesDao;
		this.controlRepository = controlRepository;
		this.companyRepository = companyRepository;
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
//		log.debug("reading symbol list from dao:"+symbolsTextFileDao);
//		Set<String> stocksList = symbolsTextFileDao.getSymbols();
		
		Collection<Control> symbolAndSectorRefresh = controlRepository.findByType(ControlType.SYMBOL_SECTOR_REFRESH);
		boolean symbolsReloadNeeded = true;
		if(symbolAndSectorRefresh != null && !symbolAndSectorRefresh.isEmpty()) {
			SortedMap<java.sql.Date, Control> refreshes = new TreeMap<>( Maps.uniqueIndex(symbolAndSectorRefresh, Control::getTimeStamp));
			LocalDate lastRefresh = refreshes.lastKey().toLocalDate();
			LocalDate now = LocalDate.now();
			int daysSinceLastRefresh = Period.between(now, lastRefresh).getDays();
			if(daysSinceLastRefresh > maxTimeBeforeRefresh.getDays()) {
				log.info("It's been "+daysSinceLastRefresh+ " days since last symbol refresh, we need to refresh");
				symbolsReloadNeeded = true;
			}else {
				log.info("It's been only"+daysSinceLastRefresh+ " days since last symbol refresh, no need for refresh");
				symbolsReloadNeeded = false;
			}
		}
		if(symbolsReloadNeeded) {
			refrehSymbols();
		}
		
		Iterable<Company> companies = companyRepository.findAll();
		Set<String>symbols = new TreeSet<>();
		for(Company c:companies) {
			symbols.add(c.getTicker());
		}
		return symbols;
	}

	private void refrehSymbols() {
		SectorsIndustriesCompanies sectorsAndCompanies = sectorCompaniesDao.getSectorsAndCompanies();
		Set<Sector> allRefreshedSectorsAndIndustries = sectorsAndCompanies.sectors();
		Stream<Sector> sectors = filterSectors(allRefreshedSectorsAndIndustries);// choose sector without parent (not industries)
		log.info("saving sectors");
		Iterable<Sector> savedSectorsAndIndustries = sectorRepository.findAll();
		Set<Sector> unsavedSectors = findUnsavedSectors(sectors, savedSectorsAndIndustries);
		
		sectorRepository.save(unsavedSectors);
		savedSectorsAndIndustries = sectorRepository.findAll();

		List<Sector> industries = wireSectorsAndIndustries(savedSectorsAndIndustries);	
		sectorRepository.save(allRefreshedSectorsAndIndustries);
		Map<String, Sector> industriesByDescription = industries.stream().collect(Collectors.toMap(Sector::getDescription, Function.identity()));

		Set<Company> companies = sectorsAndCompanies.companies();
		Iterable<Company> savedCompanies = companyRepository.findAll();
		Set<Company> companiesToSave = findUnsavedCompanies(companies,savedCompanies);
		companies.stream().forEach(c -> setIndustryAndSectorId(c,industriesByDescription));
		
		log.info("saving industries");
		sectorRepository.save(industries);		
		log.info("saving companies");
		companyRepository.save(companiesToSave);
		log.info("done refreshing sectors, industries and companies");
	}

	private Set<Company> findUnsavedCompanies(Set<Company> companies, Iterable<Company> savedCompanies) {
		Set<Company> ret = new HashSet<>();
		Map<String, Long> grouped = companies.stream().collect(Collectors.groupingBy(c -> c.getTicker() ,Collectors.counting()));

		Set<String>duplicatedTickers = new HashSet<String>();
		for(String ticker:grouped.keySet()) {
			if(grouped.get(ticker)>1) {
				duplicatedTickers.add(ticker);
			}
		}
		Set<Company> problematicCompanies = companies.stream()
		.filter(c-> duplicatedTickers.contains(c.getTicker()))
		.collect(Collectors.toSet());
		if(!problematicCompanies.isEmpty()) {
			companies.removeAll(problematicCompanies);
		}
		
		Map<String, Company> companiesByTicker = StreamSupport.stream(companies.spliterator(),false)
				.collect(Collectors.toMap(Company::getTicker, Function.identity()));
		
		companies.forEach(n -> {
			// check if it exists already
			if(companiesByTicker.containsKey(n.getDescription())) {
				// already exist - check relevant data
				Company existing = companiesByTicker.get(n.getDescription());
				if(!existing.equals(n)) {
					n.setId(existing.getId());
					ret.add(n);
				}
			}else {
				ret.add(n);
			}
		});
		return ret;
	}

	private Set<Sector> findUnsavedSectors(Stream<Sector> sectors,
			Iterable<Sector> allSectorsAndIndustries) {
		Set<Sector> ret = new HashSet<>();
		Map<String, Sector> sectorsByDescription = StreamSupport.stream(allSectorsAndIndustries.spliterator(),false)
		.collect(Collectors.toMap(Sector::getDescription, Function.identity()));
		
		sectors.forEach(n -> {
			// check if it exists already
			if(sectorsByDescription.containsKey(n.getDescription())) {
				// already exist - check relevant data
				Sector existing = sectorsByDescription.get(n.getDescription());
				if(!existing.equals(n)) {
					n.setId(existing.getId());
					ret.add(n);
				}
			}else {
				ret.add(n);
			}
		});
		return ret;
	}

	private void setIndustryAndSectorId(Company c, Map<String, Sector> industriesByDescription) {
		Sector sector = industriesByDescription.get(c.getSectorDescription());
		Sector industry = industriesByDescription.get(c.getIndustryDescription());
		if(sector != null && sector.getId() != null) {
			c.setSectorId(sector.getId());
		}
		if(industry != null && industry.getId() != null) {
			c.setIndustryId(industry.getId());
		}
	}

	private List<Sector> wireSectorsAndIndustries(Iterable<Sector> allSectorsAndIndustries) {
		Map<String,Sector> sectors = filterSectors(allSectorsAndIndustries)
			.collect(Collectors.toMap(Sector::getDescription,Function.identity()));
		List<Sector> industries = StreamSupport.stream(allSectorsAndIndustries.spliterator(), false)
			.filter(s -> s.getParentSectorId() != null /*designate a industry*/)
			.collect(Collectors.toList());
		industries.stream().forEach(s -> setIndustrySectorId(s,sectors))
			;
		return industries;
	}

	private Stream<Sector> filterSectors(Iterable<Sector> allSectorsAndIndustries) {
		return StreamSupport.stream(allSectorsAndIndustries.spliterator(), false)
			.filter(s -> isSector(s) /*designate a sector*/);
	}

	private boolean isSector(Sector s) {
		return s.getParentSectorId() == null;
	}

	private void setIndustrySectorId(Sector s, Map<String,Sector> allSectorsAndIndustries) {
		Sector sector = allSectorsAndIndustries.get(
				s.getParentSectorDescription()
		);
		if(sector.getId()!=null) {
			s.setParentSectorId(sector.getId());
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
	public void saveSymbolsWithoutPrice(Map<String,Date> existingStocksWithoutPrices) {
		symbolsTextFileDao.saveSymbolsWithoutPrice(existingStocksWithoutPrices);
	}

	@Override
	public void saveGoodValueStock(Stock stock, Map<Date, Economic> economics) {
	    symbolsTextFileDao.saveGoodValueStock(stock,economics);
	}

}
