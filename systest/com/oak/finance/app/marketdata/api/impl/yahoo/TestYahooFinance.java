package com.oak.finance.app.marketdata.api.impl.yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

public class TestYahooFinance {
	private static Logger log = LogManager.getLogger(TestYahooFinance.class);
	private YahooFinance YahooFinance = new YahooFinance(LogManager.getLogger(YahooFinance.class));

	@Test
	public void testGetSingleStock() {
		try {
			Stock stock = YahooFinance.get("CPST");
			System.out.println(stock);
			String currency = stock.getCurrency();
			StockDividend dividend = stock.getDividend();
			if (dividend != null) {
				BigDecimal annualYield = dividend.getAnnualYield();
				BigDecimal annualYieldPercent = dividend
						.getAnnualYieldPercent();
				Calendar exDate = dividend.getExDate();
				Calendar payDate = dividend.getPayDate();
				String symbol = dividend.getSymbol();
			}
			StockDividend dividendRefresh = stock.getDividend(true);
			StockDividend dividendWithoutRefresh = stock.getDividend(false);
			String name = stock.getName();
			StockQuote quote = stock.getQuote();
			if (quote != null) {
				BigDecimal ask = quote.getAsk();
				int askSize = quote.getAskSize();
				long avgVolume = quote.getAvgVolume();
				BigDecimal bid = quote.getBid();
				int bidSize = quote.getBidSize();
				BigDecimal change = quote.getChange();
				BigDecimal changeFromAvg200 = quote.getChangeFromAvg200();
				BigDecimal changeFromAvg200InPercent = quote
						.getChangeFromAvg200InPercent();
				BigDecimal changeFromAvg50 = quote.getChangeFromAvg50();
				BigDecimal changeFromAvg50InPercent = quote
						.getChangeFromAvg50InPercent();
				BigDecimal changeFromYearHigh = quote.getChangeFromYearHigh();
				BigDecimal changeFromYearHighInPercent = quote
						.getChangeFromYearHighInPercent();
				BigDecimal changeFromYearLow = quote.getChangeFromYearLow();
				BigDecimal changeFromYearLowInPercent = quote
						.getChangeFromYearLowInPercent();
				BigDecimal changeInPercent = quote.getChangeInPercent();
				BigDecimal dayHigh = quote.getDayHigh();
				BigDecimal dayLow = quote.getDayLow();
				String lastTradeDateStr = quote.getLastTradeDateStr();
				int lastTradeSize = quote.getLastTradeSize();
				Calendar lastTradeTime = quote.getLastTradeTime();
				Calendar lastTradeTimeBst = quote.getLastTradeTime(TimeZone
						.getTimeZone("BST"));
				String lastTradeTimeStr = quote.getLastTradeTimeStr();
				BigDecimal open = quote.getOpen();
				BigDecimal previousClose = quote.getPreviousClose();
				BigDecimal price = quote.getPrice();
				BigDecimal priceAvg200 = quote.getPriceAvg200();
				BigDecimal priceAvg50 = quote.getPriceAvg50();
				String symbol = quote.getSymbol();
				TimeZone timeZone = quote.getTimeZone();
				long volume = quote.getVolume();
				BigDecimal yearHigh = quote.getYearHigh();
				BigDecimal yearLow = quote.getYearLow();
			}
			StockStats stats = stock.getStats();
			if (stats != null) {
				BigDecimal bookValuePerShare = stats.getBookValuePerShare();
				BigDecimal ebitda = stats.getEBITDA();
				BigDecimal eps = stats.getEps();
				BigDecimal epsEstimateCurrentYear = stats
						.getEpsEstimateCurrentYear();
				BigDecimal epsEstimateNextQuarter = stats
						.getEpsEstimateNextQuarter();
				BigDecimal epsEstimateNextYear = stats.getEpsEstimateNextYear();
				BigDecimal marketCap = stats.getMarketCap();
				BigDecimal oneYearTargetPrice = stats.getOneYearTargetPrice();
				BigDecimal pe = stats.getPe();
				BigDecimal peg = stats.getPeg();
				BigDecimal priceBook = stats.getPriceBook();
				BigDecimal priceSales = stats.getPriceSales();
				BigDecimal revenue = stats.getRevenue();
				BigDecimal roe = stats.getROE();
				long sharesFloat = stats.getSharesFloat();
				long sharesOutstanding = stats.getSharesOutstanding();
				long sharesOwned = stats.getSharesOwned();
				String symbol = stats.getSymbol();
			}
			String stockExchange = stock.getStockExchange();
			stock.print();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> readStocksListFromCsv(String csvFile,
			String cvsSplitBy, String ignoreString) {
		List<String> ret = new ArrayList<String>();
		BufferedReader br = null;
		String line = "";
		cvsSplitBy = ",";
		InputStream stream = this.getClass().getResourceAsStream(csvFile);
		br = new BufferedReader(new InputStreamReader(stream));
		try {
			while ((line = br.readLine()) != null) {
				line = line.trim();
				log.debug("reading line: "+line);
				if (line.length() > 0 && !line.startsWith("#")) {
					String[] split = line.split(";");
					if (split.length > 1) {
						ret.add(split[0]);
					}
				}
			}
		} catch (IOException io) {
			log.error("IO Error trying to read from " + csvFile, io);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("IO Error trying to close buffer", e);
				}
			}
		}

		return ret;
	}

	@Test
	public void testGetStocksFromCsv() {
		int batchSize = 5;
		Map<String, Stock> result = new HashMap<String, Stock>(23000);
		List<String> stocks = readStocksListFromCsv("/stocks/yahoo.csv", ";",
				"#");
		String[] batch = new String[batchSize];
		int count = 1;
		int total = stocks.size()/batchSize;
		while (stocks.size() > 0) {

			for (int i = 0; i < batchSize; i++) {
				String s = stocks.get(0);
				batch[i] = s;
				stocks.remove(s);
			}
			try {
				Map<String, Stock> map = YahooFinance.get(batch);
				log.info("Batch "+count++ +" of "+total);
				result.putAll(map);
			} catch (IOException e) {
				log.error("Error while getting stocks prices from yahoo for: "+batch,e);
				e.printStackTrace();
			}
		}

	}
}
