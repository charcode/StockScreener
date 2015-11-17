package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.Map;

import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.economy.Economic;

public interface YahooDataConverter {

	Map<Stock,Map<Date,Economic>>
	yahooStockToEconomyPerDate(
			Map<String, yahoofinance.Stock> stocks);
}
