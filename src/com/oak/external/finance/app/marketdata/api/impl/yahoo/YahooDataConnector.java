package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import yahoofinance.YahooFinance;

import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.economy.Economic;
import com.oak.external.finance.app.marketdata.api.DataConnector;

public class YahooDataConnector implements DataConnector {
    private final Logger log;
    private final YahooDataConverter converter;
    private final YahooFinance connector;
    private final BalanceSheetDao yahooWeb;

    public YahooDataConnector(Logger log, YahooDataConverter converter) {
	this.log = log;
	this.converter = converter;
	this.yahooWeb = new YahooWebDataBalanceSheetDao(log);
	connector = new YahooFinance(log);
    }

    @Override
    public Map<Stock, Map<Date, Economic>> getEconomics(Set<String> stocks, Date fromDate) {
	Map<Stock, Map<Date, Economic>> ret = null;

	if (stocks != null && !stocks.isEmpty()) {
	    Collection<String[]> batches = batchStocks(stocks, 20);
	    for (String[] batch : batches) {
		ret = new HashMap<Stock, Map<Date, Economic>>();

		Calendar from = null;
		if (fromDate != null) {
		    from = Calendar.getInstance();
		    from.setTime(fromDate);
		}
		try {
		    Map<String, yahoofinance.Stock> map = null;
		    map = connector.get(batch);
		    Map<Stock, Map<Date, Economic>> yahooStockToEconomyPerDate = converter.yahooStockToEconomyPerDate(map);
		    ret.putAll(yahooStockToEconomyPerDate);
		} catch (IOException e) {
		    log.error("error getting price from Yahoo for " + batch, e);
		}
	    }
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
	    batchList.add(s);
	    j++;
	}
	String[] array = batchList.toArray(stocksArray);
	batches.add(array);
	return batches;
    }

}
