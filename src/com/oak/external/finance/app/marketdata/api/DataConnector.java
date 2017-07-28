package com.oak.external.finance.app.marketdata.api;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.AbstractQuote;

import io.reactivex.Observable;

public interface DataConnector {

	Map<Stock, Map<Date, Economic>> getEconomics(Set<String> stocks);

	Observable<List<AbstractQuote>> getHistoricalQuotes(Set<String> tickers, Date fromDate);
	
}
