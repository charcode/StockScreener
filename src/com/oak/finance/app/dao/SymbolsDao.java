package com.oak.finance.app.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Screen0Result;

public interface SymbolsDao {

	Set<String> getSavedSymbolsWithoutPrices();

	void saveSymbolsWithoutPrice(Map<String,Date> existingStocksWithoutPrices);

	Set<String> getSymbols();

	void saveGoodValueStock(Stock stock, Map<Date, Economic> economics);

	Set<String> getInterestingSymbols();

	Collection<Screen0Result> readPreviousResults();
}
