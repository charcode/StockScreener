package com.oak.external.finance.app.marketdata.api;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import com.oak.api.finance.model.BalanceSheet;


/**
 * Yahoo changed their website, this parser doesn't work anymore
 * @author charb
 *
 * @param <T>
 */
@Deprecated
public interface BalanceSheetDao {
    Map<String, SortedMap<Date, BalanceSheet>> getBalanceSheetsBySymbol(Map<String, String> symbols, boolean annual);
    SortedMap<Date, BalanceSheet> getBalanceSheetForSymbol(String symbol, String exchange, boolean annual);
}