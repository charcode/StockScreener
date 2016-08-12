package com.oak.external.finance.app.marketdata.api;

import java.util.Date;
import java.util.SortedMap;

import com.oak.api.finance.model.CashFlowStatement;

public interface CashFlowStatementDao {
    SortedMap<Date, CashFlowStatement> getCashFlowStatementForSymbol(String symbol, boolean annual);

}
