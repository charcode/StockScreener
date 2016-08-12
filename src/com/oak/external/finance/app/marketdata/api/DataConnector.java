package com.oak.external.finance.app.marketdata.api;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;

public interface DataConnector {

	Map<Stock, Map<Date, Economic>> 
	getEconomics(
			Set<String> stocks,
			Date fromDate);
	
}
