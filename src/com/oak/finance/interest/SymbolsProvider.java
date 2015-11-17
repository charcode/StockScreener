package com.oak.finance.interest;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.economy.Economic;

public interface SymbolsProvider {
	Set<String>getSymbols();

	Set<String> getExcludedSymbols();

	void saveSymbolsWithoutPrice(Map<String,Date> existingStocksWithoutPrices);

	void saveGoodValueStock(Stock stock, Map<Date, Economic> economics);

	Set<String> getInterestingSymbols();
}
