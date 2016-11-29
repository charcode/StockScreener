package com.oak.finance.interest;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.Screen0Result;
import com.oak.api.providers.control.ControlType;

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

	void saveNewCompanies(Set<Company> companies);

	Pair<Boolean,Set<String>> refreshSymbolsIfNeeded();
}
