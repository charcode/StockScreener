package com.oak.external.finance.app.marketdata.api.yahoo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;

import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooDataConverter;

public class YahooDataConverterImpl implements YahooDataConverter {

    private final Logger log;

    public YahooDataConverterImpl(Logger log) {
	this.log = log;
    }

    @Override
    public Map<Stock, Map<Date, Economic>> yahooStockToEconomyPerDate(
	    Map<String, yahoofinance.Stock> stocks) {
	Map<Stock, Map<Date, Economic>> ret = null;
	if (stocks != null) {
	    ret = new HashMap<Stock, Map<Date, Economic>>(stocks.size());
	    for (String symbol : stocks.keySet()) {
		yahoofinance.Stock s = stocks.get(symbol);
		Stock stock = createStock(s);
		Map<Date, Economic> economics = createEconomicPerDate(s);
		if (stock != null && economics != null && !economics.isEmpty()) {
		    ret.put(stock, economics);
		}
	    }
	}
	return ret;
    }

    private Stock createStock(yahoofinance.Stock s) {
	String name = s.getName();
	String symbol = s.getSymbol();
	String stockExchange = s.getStockExchange();
	String currency = s.getCurrency();
	Stock ret = new Stock(name, symbol, symbol, name, stockExchange, currency);
	return ret;
    }

    private Map<Date, Economic> createEconomicPerDate(
	    yahoofinance.Stock yahooStock) {
	Map<Date, Economic> ret = null;
	if (yahooStock != null) {
	    List<HistoricalQuote> history;
	    try {
		Map<Date, Economic> economyMap = new TreeMap<Date, Economic>(
			new Comparator<Date>() {
			    // reverse comparer to put the latest first
			    @Override
			    public int compare(Date first, Date second) {
				return first.after(second) ? -1 : 1;
			    }
			});
		Economic economicForToday = extractTodayEconomic(yahooStock);
		if (economicForToday != null
			&& economicForToday.getDate() != null) {
		    economyMap
			    .put(economicForToday.getDate(), economicForToday);
		}

		// Calendar now = Calendar.getInstance();
		// Long calendarMs = now.getTimeInMillis();
		// Long days_7 = 7*24*60*60*1000L;
		//
		//
		// Calendar lastWeek = Calendar.getInstance();
		// lastWeek.setTimeInMillis(calendarMs - days_7);
		// history = yahooStock.getHistory(lastWeek,now,Interval.DAILY);
		// if (history != null) {
		//
		// for (HistoricalQuote h : history) {
		// Calendar date = h.getDate();
		// h.
		// }
		// }
		ret = new ConcurrentHashMap<Date, Economic>(economyMap);
	    } catch (Exception e) {
		log.error("cannot get history", e);
	    }

	}
	return ret;
    }

    private Economic extractTodayEconomic(yahoofinance.Stock yahooStock) {
	Economic ret = null;
	if (yahooStock != null) {
	    StockQuote quote = yahooStock.getQuote();
	    StockStats stats = yahooStock.getStats();
	    StockDividend dividend = yahooStock.getDividend();
	    String stockExchange = yahooStock.getStockExchange();

	    Double ask = null;
	    Double bid = null;
	    Date lastTradeTime = null;
	    Integer askSize = null;
	    Long avgVolume = null;
	    Integer bidSize = null;

	    Double change = null;
	    Double changeFromAvg200 = null;
	    Double changeFromAvg200InPercent = null;
	    Double changeFromAvg50 = null;
	    Double changeFromAvg50InPercent = null;
	    Double changeFromYearHigh = null;
	    Double changeFromYearHighInPercent = null;
	    Double changeFromYearLow = null;
	    Double changeFromYearLowInPercent = null;
	    Double changeInPercent = null;
	    Double dayHigh = null;
	    Double dayLow = null;
	    String lastTradeDateStr = null;
	    Integer lastTradeSize = null;
	    String lastTradeTimeStr = null;
	    Double open = null;
	    Double previousClose = null;
	    Double priceAvg200 = null;
	    Double priceAvg50 = null;
	    String symbol = null;
	    TimeZone timeZone = null;
	    Long volume = null;
	    Double yearHigh = null;
	    Double yearLow = null;

	    if (quote != null) {
		Calendar lastTradeCalendar = quote.getLastTradeTime();
		if (lastTradeCalendar != null) {
		    lastTradeTime = lastTradeCalendar.getTime();
		}
		BigDecimal bdAsk = quote.getAsk();
		ask = getDoubleValue(bdAsk);
		bid = getDoubleValue(quote.getBid());
		askSize = quote.getAskSize();
		avgVolume = quote.getAvgVolume();
		bidSize = quote.getBidSize();

		change = getDoubleValue(quote.getChange());
		changeFromAvg200 = getDoubleValue(quote.getChangeFromAvg200());
		changeFromAvg200InPercent = getDoubleValue(quote
			.getChangeFromAvg200InPercent());
		changeFromAvg50 = getDoubleValue(quote.getChangeFromAvg50());
		changeFromAvg50InPercent = getDoubleValue(quote
			.getChangeFromAvg50InPercent());
		changeFromYearHigh = getDoubleValue(quote
			.getChangeFromYearHigh());
		changeFromYearHighInPercent = getDoubleValue(quote
			.getChangeFromYearHighInPercent());
		changeFromYearLow = getDoubleValue(quote.getChangeFromYearLow());
		changeFromYearLowInPercent = getDoubleValue(quote
			.getChangeFromYearLowInPercent());
		changeInPercent = getDoubleValue(quote.getChangeInPercent());

		dayHigh = getDoubleValue(quote.getDayHigh());
		dayLow = getDoubleValue(quote.getDayLow());
		lastTradeDateStr = quote.getLastTradeDateStr();
		lastTradeSize = quote.getLastTradeSize();
		lastTradeTimeStr = quote.getLastTradeTimeStr();
		open = getDoubleValue(quote.getOpen());
		previousClose = getDoubleValue(quote.getPreviousClose());
		priceAvg200 = getDoubleValue(quote.getPriceAvg200());
		priceAvg50 = getDoubleValue(quote.getPriceAvg50());
		symbol = quote.getSymbol();
		timeZone = quote.getTimeZone();
		volume = quote.getVolume();
		yearHigh = getDoubleValue(quote.getYearHigh());
		yearLow = getDoubleValue(quote.getYearLow());

	    }
	    Double eps = null;
	    Double bookValuePerShare = null;
	    Double ebitda = null;
	    Double epsEstimateCurrentYear = null;
	    Double epsEstimateNextQuarter = null;
	    Double epsEstimateNextYear = null;
	    Double marketCap = null;
	    Double oneYearTargetPrice = null;
	    Double pe = null;
	    Double peg = null;
	    Double priceBook = null;
	    Double priceSales = null;
	    Double revenue = null;
	    Double roe = null;
	    Long sharesFloat = null;
	    Long sharesOutstanding = null;
	    Long sharesOwned = null;

	    if (stats != null) {
		eps = getDoubleValue(stats.getEps());

		bookValuePerShare = getDoubleValue(stats.getBookValuePerShare());
		ebitda = getDoubleValue(stats.getEBITDA());
		epsEstimateCurrentYear = getDoubleValue(stats
			.getEpsEstimateCurrentYear());
		epsEstimateNextQuarter = getDoubleValue(stats
			.getEpsEstimateNextQuarter());
		epsEstimateNextYear = getDoubleValue(stats
			.getEpsEstimateNextYear());
		marketCap = getDoubleValue(stats.getMarketCap());
		oneYearTargetPrice = getDoubleValue(stats
			.getOneYearTargetPrice());
		pe = getDoubleValue(stats.getPe());
		peg = getDoubleValue(stats.getPeg());
		priceBook = getDoubleValue(stats.getPriceBook());
		priceSales = getDoubleValue(stats.getPriceSales());
		revenue = getDoubleValue(stats.getRevenue());
		roe = getDoubleValue(stats.getROE());
		sharesFloat = stats.getSharesFloat();
		sharesOutstanding = stats.getSharesOutstanding();
		sharesOwned = stats.getSharesOwned();

	    }

	    Double annualDividendYield = null;
	    Double annualDividendYieldPercent = null;
	    Calendar dividendExDate = null;
	    Calendar dividendPayDate = null;
	    if (dividend != null) {
		annualDividendYield = getDoubleValue(dividend.getAnnualYield());
		annualDividendYieldPercent = getDoubleValue(dividend .getAnnualYieldPercent());
		dividendExDate = dividend.getExDate();
		dividendPayDate = dividend.getPayDate();
	    }
	    
	    ret = new Economic(lastTradeTime, bid, bidSize, ask, askSize,
		    change, changeFromAvg200, changeFromAvg200InPercent,
		    changeFromAvg50, changeFromAvg50InPercent,
		    changeFromYearHigh, changeFromYearHighInPercent,
		    changeFromYearLow, changeFromYearLowInPercent,
		    changeInPercent, dayHigh, dayLow, lastTradeDateStr,
		    lastTradeSize, lastTradeTimeStr, open, previousClose,
		    priceAvg200, priceAvg50, symbol, timeZone, volume,
		    yearHigh, yearLow, eps, bookValuePerShare, ebitda,
		    epsEstimateCurrentYear, epsEstimateNextQuarter,
		    epsEstimateNextYear, marketCap, oneYearTargetPrice, pe,
		    peg, priceBook, priceSales, revenue, roe, sharesFloat,
		    sharesOutstanding, sharesOwned,annualDividendYield,annualDividendYieldPercent, dividendExDate, dividendPayDate);
	}
	return ret;
    }

    private Double getDoubleValue(BigDecimal val) {
	Double ret = null;
	if (val != null) {
	    ret = val.doubleValue();
	}
	return ret;
    }

}
