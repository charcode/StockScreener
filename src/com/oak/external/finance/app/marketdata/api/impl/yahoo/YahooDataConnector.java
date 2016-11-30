package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.external.finance.app.marketdata.api.DataConnector;

import yahoofinance.YahooFinance;

public class YahooDataConnector implements DataConnector {
	private final Logger log;
	private final YahooDataConverter converter;
	private final YahooFinance connector;

	public YahooDataConnector(Logger log, YahooDataConverter converter) {
		this.log = log;
		this.converter = converter;
		connector = new YahooFinance(LogManager.getLogger(YahooFinance.class));
	}

	@Override
	public Map<Stock, Map<Date, Economic>> getEconomics(Set<String> stocks, Date fromDate) {
		Map<Stock, Map<Date, Economic>> ret = new HashMap<>();

		if (stocks != null && !stocks.isEmpty()) {
			ret = getEconomicsLoop(stocks, fromDate);
		}
		return ret;
	}
	
	private Map<Stock, Map<Date, Economic>> getEconomicsLoop(Set<String> stocks, Date fromDate) {
		HashMap<Stock, Map<Date, Economic>> ret = new HashMap<Stock, Map<Date, Economic>>();;
		Collection<String[]> batches = batchStocks(stocks, 20);
		for (String[] batch : batches) {
			int attempt = 0;
			while(!getEconomicsForBatch(fromDate, ret, batch)) {
				log.warn("retrying for the time n: " + (attempt + 1 ) + " loading batch: " + StringUtils.join(batch, ", "));
				if(attempt++ >= 5) {
					break;
				}
			}
		}
		return ret;
	}

	private boolean getEconomicsForBatch(Date fromDate, HashMap<Stock, Map<Date, Economic>> econMap, String[] batch) {
		Calendar from = null;
		boolean ret = false;
		if (fromDate != null) {
			from = Calendar.getInstance();
			from.setTime(fromDate);
		}
		try {
			Map<String, yahoofinance.Stock> map = null;
			map = connector.get(batch);
			Map<Stock, Map<Date, Economic>> yahooStockToEconomyPerDate = converter.yahooStockToEconomyPerDate(map);
			econMap.putAll(yahooStockToEconomyPerDate);
			ret = true;
		} catch (IOException e) {
			log.error("error getting price from Yahoo for " + StringUtils.join(batch,", "), e);
			ret = false;
		}
		return ret;
	}
	
	

	private Collection<String[]> batchStocks(Set<String> stocks, int i) {
		Collection<String[]> batches = new ArrayList<String[]>();
		int j = 0;
		String[] stocksArray = new String[i];
		ArrayList<String> batchList = new ArrayList<String>(i);
		for (String s : stocks) {
			if (j >= i) {
				String[] array = batchList.toArray(stocksArray);
				batches.add(array);
				batchList = new ArrayList<String>(i);
				j = 0;
			}
			if(s!=null && !s.isEmpty()) {
				batchList.add(s);
			}
			j++;
		}
		stocksArray = new String [batchList.size()];
		String[] array = batchList.toArray(stocksArray);
		batches.add(array);
		return batches;
	}

}
