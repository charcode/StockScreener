package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.Map;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;

public interface YahooDataConverter {

	Map<Stock,Map<Date,Economic>>
	yahooStockToEconomyPerDate(
			Map<String, yahoofinance.Stock> stocks);
}
