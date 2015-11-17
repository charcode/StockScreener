package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.oak.api.finance.model.BalanceSheet;

public interface BalanceSheetDao {
    Map<String, SortedMap<Date, BalanceSheet>> getBalanceSheetsBySymbol(Set<String> symbols, boolean annual);
    SortedMap<Date, BalanceSheet> getBalanceSheetForSymbol(String symbol, boolean annual);
}