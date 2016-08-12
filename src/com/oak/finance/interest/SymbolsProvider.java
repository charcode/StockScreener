package com.oak.finance.interest;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;

public interface SymbolsProvider {
	Set<String>getSymbols();

	Set<String> getExcludedSymbols();

	void saveSymbolsWithoutPrice(Map<String,Date> existingStocksWithoutPrices);

	void saveGoodValueStock(Stock stock, Map<Date, Economic> economics);

	Set<String> getInterestingSymbols();
}
