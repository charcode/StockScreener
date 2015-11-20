package com.oak.finance.interest;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.finance.app.dao.SymbolsDao;

public class SymbolProviderImpl implements SymbolsProvider {

	private final SymbolsDao symbolsDao;
	private final Logger log;

	public SymbolProviderImpl(SymbolsDao symbolsDao, Logger log) {
		this.symbolsDao = symbolsDao;
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
		log.debug("reading symbol list from dao:"+symbolsDao);
		Set<String> stocksList = symbolsDao.getSymbols();
		return stocksList;
	}

	@Override
	public Set<String> getExcludedSymbols() {
		Set<String> savedStocksWithoutPrices = symbolsDao.getSavedSymbolsWithoutPrices();
		return savedStocksWithoutPrices;
	}
	
	@Override
	public Set<String> getInterestingSymbols() {
	    Set<String> interestingSymbols = symbolsDao.getInterestingSymbols();
	    return interestingSymbols;
	}
	

	@Override
	public void saveSymbolsWithoutPrice(Map<String,Date> existingStocksWithoutPrices) {
		symbolsDao.saveSymbolsWithoutPrice(existingStocksWithoutPrices);
	}

	@Override
	public void saveGoodValueStock(Stock stock, Map<Date, Economic> economics) {
	    symbolsDao.saveGoodValueStock(stock,economics);
	}

}
