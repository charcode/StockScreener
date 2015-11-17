package com.oak.finance.app.monitor.analysis;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.oak.api.finance.model.FinancialAnalysis;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.economy.Economic;

public interface FinanceAnalysisController {
    public interface FinanceAnalysisCallback {
	void onPriceMissing(Stock stock, Map<Date, Economic> economics);

	void onBuy(Stock stock, Map<Date, Economic> economics, FinancialAnalysis financialAnalysis);

	void onSell(Stock stock, Map<Date, Economic> economics);

	void onWatchList(Stock stock, Map<Date, Economic> economics,
		FinancialAnalysis stockAnalysis);
    }

    void onEconomicsUpdate(FinanceAnalysisCallback callback, Stock stock,
	    Map<Date, Economic> economics, Set<String> alwaysWatch);
}
