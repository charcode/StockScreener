package com.oak.finance.interest;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.SectorsIndustriesCompanies;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.ControlType;
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
		Set<Company> companies = sectorsAndCompanies.companies();
		companyRepository.save(companies);
		sectorRepository.save(sectorsAndCompanies.sectors());
		sectorRepository.save(sectorsAndCompanies.industries());		
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
