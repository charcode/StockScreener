package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.SortedMap;

import com.oak.external.finance.model.economy.IncomeStatement;

public interface IncomeStatementDao {
    SortedMap<Date, IncomeStatement> getIncomeStatementForSymbol(String symbol, boolean annual);

}
