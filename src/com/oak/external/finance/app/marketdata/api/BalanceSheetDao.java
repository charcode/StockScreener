package com.oak.external.finance.app.marketdata.api;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import com.oak.api.finance.model.BalanceSheet;

public interface BalanceSheetDao {
    Map<String, SortedMap<Date, BalanceSheet>> getBalanceSheetsBySymbol(Map<String, String> symbols, boolean annual);
    SortedMap<Date, BalanceSheet> getBalanceSheetForSymbol(String symbol, String exchange, boolean annual);
}