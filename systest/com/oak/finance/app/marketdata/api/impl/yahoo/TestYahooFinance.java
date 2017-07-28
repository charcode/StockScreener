package com.oak.finance.app.marketdata.api.impl.yahoo;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.oak.api.finance.model.dto.AbstractQuote;
import com.oak.api.finance.model.dto.Quote;
import com.oak.external.finance.app.marketdata.api.yahoo.YahooFinanceWrapper;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

public class TestYahooFinance {
	private static Logger log = LogManager.getLogger(TestYahooFinance.class);
	private YahooFinanceWrapper yahooFinance = new YahooFinanceWrapper(LogManager.getLogger(YahooFinanceWrapper.class));

	@Test
	public void testGetSingleStock() {
		try {
			String[] symbols = {"CPST"};
			for (Stock stock : yahooFinance.get(symbols).values()) {
				System.out.println(stock);
				String currency = stock.getCurrency();
				StockDividend dividend = stock.getDividend();
				if (dividend != null) {
					BigDecimal annualYield = dividend.getAnnualYield();
					BigDecimal annualYieldPercent = dividend.getAnnualYieldPercent();
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
					long askSize = quote.getAskSize();
					long avgVolume = quote.getAvgVolume();
					BigDecimal bid = quote.getBid();
					long bidSize = quote.getBidSize();
					BigDecimal change = quote.getChange();
					BigDecimal changeFromAvg200 = quote.getChangeFromAvg200();
					BigDecimal changeFromAvg200InPercent = quote.getChangeFromAvg200InPercent();
					BigDecimal changeFromAvg50 = quote.getChangeFromAvg50();
					BigDecimal changeFromAvg50InPercent = quote.getChangeFromAvg50InPercent();
					BigDecimal changeFromYearHigh = quote.getChangeFromYearHigh();
					BigDecimal changeFromYearHighInPercent = quote.getChangeFromYearHighInPercent();
					BigDecimal changeFromYearLow = quote.getChangeFromYearLow();
					BigDecimal changeFromYearLowInPercent = quote.getChangeFromYearLowInPercent();
					BigDecimal changeInPercent = quote.getChangeInPercent();
					BigDecimal dayHigh = quote.getDayHigh();
					BigDecimal dayLow = quote.getDayLow();
					String lastTradeDateStr = quote.getLastTradeDateStr();
					long lastTradeSize = quote.getLastTradeSize();
					Calendar lastTradeTime = quote.getLastTradeTime();
					Calendar lastTradeTimeBst = quote.getLastTradeTime(TimeZone.getTimeZone("BST"));
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
					BigDecimal epsEstimateCurrentYear = stats.getEpsEstimateCurrentYear();
					BigDecimal epsEstimateNextQuarter = stats.getEpsEstimateNextQuarter();
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
			}
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
				Map<String, Stock> map = yahooFinance.get(batch);
				log.info("Batch "+count++ +" of "+total);
				result.putAll(map);
			} catch (IOException e) {
				log.error("Error while getting stocks prices from yahoo for: "+batch,e);
				e.printStackTrace();
			}
		}

	}
	@Test
	public void testGetStream() {
		String[] syms = {"SNAP","FB"};
		Instant instant = LocalDateTime.of(1962, 1, 2, 1, 0).toInstant(ZoneOffset.UTC);
		Date date = Date.from(instant);
		
		Calendar from = Calendar.getInstance();
		from.setTime(date);
		
		Observable<List<AbstractQuote>> observable = yahooFinance.getStream(syms,from,Interval.DAILY);
		Cons cons = new Cons() ;
		observable.subscribe(cons);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(2,cons.getCount());
	}
	class Cons implements Consumer<List<AbstractQuote>>{
		private Map<String,Integer>counts = new ConcurrentHashMap<>();
		@Override
		public void accept(List<AbstractQuote> quotes) throws Exception {
			for(AbstractQuote t:quotes) {
				String ticker = t.getTicker();
				int i = 1;
				if(counts.containsKey(ticker)) {
					i = counts.get(ticker) + 1;
				}
				counts.put(ticker,i);
			}
		}
		public int getCount(){
			return counts.size();
		}
	}
	
	

	@Test
	public void getHistoricalPrices() {
		String[] syms = {"SNAP","FB"};
		Instant instant = LocalDateTime.of(1962, 1, 2, 1, 0).toInstant(ZoneOffset.UTC);
		Date date = Date.from(instant);
		
		Calendar from = Calendar.getInstance();
		from.setTime(date);
		try {
			Map<String, Stock> quotes = yahooFinance.get(syms,from,Interval.DAILY);
			for(Stock s:quotes.values()) {
				List<HistoricalQuote> history = s.getHistory();
				
				for(HistoricalQuote hq:history) {
					Calendar dt = hq.getDate();
					BigDecimal high = hq.getHigh();
					BigDecimal low = hq.getLow();
					BigDecimal open = hq.getOpen();
					BigDecimal close = hq.getClose();
					BigDecimal adjClose = hq.getAdjClose();
					long volume = hq.getVolume();
					String symbol = s.getSymbol();
					StockStats stats = s.getStats();
					System.out.println(hq);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
