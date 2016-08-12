package com.oak.external.finance.app.marketdata.api;

import com.oak.api.finance.model.FinancialData;

public interface FinancialDataDao {
	FinancialData getFinancialDataForSymbol(String symbol, String exchange);
}
