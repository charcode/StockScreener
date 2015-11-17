package com.oak.external.finance.app.marketdata.api.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.economy.Economic;
import com.oak.external.finance.app.marketdata.api.DataConnector;
import com.oak.external.finance.app.marketdata.api.MarketDataProvider;

public class MarketDataPollingProviderImpl implements MarketDataProvider {
	private final int waitInMilliseconds;
	private final long historyBackInMilliSeconds;
	private final DataConnector dataConnector;
	private boolean stop;
	private boolean started;
	private final Logger log;

	public MarketDataPollingProviderImpl(DataConnector dataConnector,
			int waitInMilliseconds, long historyBackInMilliSeconds, Logger log) {
		this.waitInMilliseconds = waitInMilliseconds;
		this.historyBackInMilliSeconds = historyBackInMilliSeconds;
		this.dataConnector = dataConnector;
		this.stop = false;
		this.log = log == null ? LogManager
				.getLogger(MarketDataPollingProviderImpl.class) : log;
	}

	@Override
	public Map<Stock, Map<Date, Economic>> retrieveMarketData(Set<String> stocks) {
		return dataConnector.getEconomics(stocks, null);
	}

}
