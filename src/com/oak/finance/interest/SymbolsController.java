package com.oak.finance.interest;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Screen0Result;

/**
 * Provides many survices around reading the tickers and sectors of different companies, and storing this in the DB
 * making sure this list is up-to-date and refereshed
 *  
 * @author charb
 */
public interface SymbolsController {
	Set<String>getSymbols();

	Set<String> getExcludedSymbols();

	void saveSymbolsWithoutPrice(Map<String,Date> existingStocksWithoutPrices);

	void saveGoodValueStock(Stock stock, Map<Date, Economic> economics);

	Set<String> getInterestingSymbols();

	Collection<Screen0Result> retriedLegacyResultsFromFile();
}
