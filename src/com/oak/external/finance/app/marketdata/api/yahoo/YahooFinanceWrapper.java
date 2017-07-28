
package com.oak.external.finance.app.marketdata.api.yahoo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.oak.api.finance.model.dto.AbstractQuote;
import com.oak.api.finance.model.dto.ErrorQuote;
import com.oak.api.finance.model.dto.Quote;
import com.oak.api.finance.model.dto.QuoteErrorType;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.fx.FxQuote;

public class YahooFinanceWrapper {
    
    public static final String QUOTES_BASE_URL = "http://finance.yahoo.com/d/quotes.csv";
    public static final String HISTQUOTES_BASE_URL = "http://ichart.yahoo.com/table.csv";
    public static final String QUOTES_CSV_DELIMITER = ",";
    public static final String TIMEZONE = "America/New_York";
    public static final int BATCH_SIZE = 20;
    public static final int HISTORICAL_BATCH_SIZE = 1;
    
    private final Logger logger;
    
    public YahooFinanceWrapper (Logger logger){
    	this.logger = logger;
    }
    
    /**
    * Sends a basic quotes request to Yahoo Finance. This will return a {@link Map} object
    * that links the symbols to their respective {@link Stock} objects.
    * The Stock objects have their {@link yahoofinance.quotes.stock.StockQuote}, {@link yahoofinance.quotes.stock.StockStats} 
    * and {@link yahoofinance.quotes.stock.StockDividend} member fields 
    * filled in with the available data.
    * <p>
    * All the information is retrieved in a single request to Yahoo Finance.
    * The returned Map only includes the Stocks that could 
    * successfully be retrieved from Yahoo Finance.
    * 
    * @param symbols    the symbols of the stocks for which you want to retrieve information
    * @return           a Map that links the symbols to their respective Stock objects
     * @throws java.io.IOException when there's a connection problem
    */
    public  Map<String, Stock> get(String[] symbols) throws IOException {
        return get(symbols, false);
    }
    
    /**
     * Same as the <code>get(String[])</code> method, but with the option to include
     * historical stock quote data. Including historical data will cause the {@link Stock}
     * objects their member field {@link yahoofinance.histquotes.HistoricalQuote} to be filled in
     * with the default past year term at monthly intervals.
     * <p>
     * The latest quotes will be retrieved in a single request to Yahoo Finance.
     * For the historical quotes (if includeHistorical), 
     * a separate request will be sent for each requested stock.
     * The returned Map only includes the Stocks that could 
     * successfully be retrieved from Yahoo Finance.
     * 
     * @param symbols               the symbols of the stocks for which you want to retrieve information
     * @param includeHistorical     indicates if the historical quotes should be included
     * @return                      a Map that links the symbols to their respective Stock objects
     * @throws java.io.IOException when there's a connection problem
     */
    public  Map<String, Stock> get(String[] symbols, boolean includeHistorical) throws IOException {
        Map<String, Stock> ret = batchGet(symbols, b -> YahooFinance.get(b,includeHistorical),null);
		return ret;
    }
    
    
    /**
     * Sends a request for multiple stocks with the historical quotes included
     * starting from the specified {@link Calendar} date until today, 
     * at the specified interval.
     * <p>
     * The latest quotes will be retrieved in a single request to Yahoo Finance.
     * For the historical quotes, a separate request will be sent for each requested stock.
     * The returned Map only includes the Stocks that could 
     * successfully be retrieved from Yahoo Finance.
     * 
     * @param symbols               the symbols of the stocks for which you want to retrieve information
     * @param from                  start date of the historical data
     * @param interval              the interval of the included historical data
     * @return                      a Map that links the symbols to their respective Stock objects.
     * @throws java.io.IOException when there's a connection problem
     */
    public  Map<String, Stock> get(String[] symbols, Calendar from, Interval interval) throws IOException {
        Map<String, Stock> ret = batchGet(symbols, b -> getYahooData(from, interval, b),null);
		return ret;
    }
    public Observable<List<AbstractQuote>>getStream(String[]symbols, Calendar from, Interval interval){

//		java.util.logging.Logger logger = java.util.logging.Logger.getLogger("yahoofinance.YahooFinance");
//		logger.setLevel(Level.SEVERE);
    	Observable<List<AbstractQuote>>ret = Observable.create(y -> 
    		batchGet(symbols, batch -> getYahooData(from, interval, batch),y, BATCH_SIZE)
    	);
    	
    	return ret;
    }

	private Map<String, Stock> getYahooData(Calendar from, Interval interval, String[] tickersBatch) throws IOException {
		logger.info("getYahooData " + tickersBatch[0] + " ... " + tickersBatch.length);
		Map<String, Stock> ret = null;
		try {
			 ret = YahooFinance.get(tickersBatch,from,interval);
		}catch (Exception e) {
			logger.error("error getting historical data for: " + tickersBatch+" for: "+from,e);
			if (e.getMessage().contains( "Server returned HTTP response code: 999")){
				// Yahoo server is declinding, we better back off for 2 hours
				try {
					logger.error("Yahoo server is declining request, we will wait two hours before proceeding!!!");
					Thread.sleep(120*60*1000);
					logger.error("Resuming after... Yahoo server declined request, (wait of two hours ended !!!)");
				}catch (InterruptedException ie) {
				}				
			}else {
				
//				logger.error("Error in getYahooData " +e.getMessage(),e);
				throw e;
			}
		}
		logger.info("getYahooData " + tickersBatch[0] + " ... " + tickersBatch.length + " completed");
		return ret;
	}

    private Map<String,Stock> batchGet(String[] symbols, Applicator<String[], Map<String, Stock>> f,
			ObservableEmitter<List<AbstractQuote>> emitter, int batchSize) {
    	logger.info("batchGet() " + symbols[0] + " ..." + symbols.length);
    	Map<String,Stock> ret = new HashMap<>();
    	List<List<String>> partitions = Lists.partition(Lists.newArrayList(symbols), batchSize);
    	int batchNum = 0;
    	for(List<String>batchList : partitions) {
    		logger.error("Batch "+ (++batchNum)+" out of "+partitions.size());
    		String[] batch = new String[batchList.size()];
    		batchList.toArray(batch);
    		try {
    			Map<String, Stock> batchResult = f.apply(batch);
    			if(batchResult != null) {
	    			streamResult(emitter, batchResult);
	    			if(emitter == null) {
	    				ret.putAll(batchResult);
	    			}
    			}
    		}catch (Exception e) {
    			if(batchList.size()>1) {
    				for(String s:batchList) {
    					String[] partTicker = new String[1];
    					partTicker[0]=s;
    					try {
	    					Map<String,Stock> partRet = batchGet(partTicker,f,emitter);
	    					streamResult(emitter, partRet);
	    					if(emitter == null) {
	    						ret.putAll(partRet);
	    					}
    					}catch(Exception ex) {
    						logger.error("cannot get stock quote history.. ["+s+"]",ex);
    						streamErrorResults(ret, emitter,Arrays.stream(partTicker),ex);
    					}
    				}
    			}else {
    				logger.error("error getting historical data for: " + batchList,e);
    				streamErrorResults(ret, emitter,batchList.stream(),e);
    			}
    		}
    	}
    	
    	return ret;
	}
    private void streamErrorResults(Map<String,Stock>ret, ObservableEmitter<List<AbstractQuote>> emitter,Stream<String> stream,Exception e) {
		List<AbstractQuote> strm = stream.map(t -> 
    	ErrorQuote.newQuoteInError(t,e,e.getMessage(),QuoteErrorType.HISTORY)).collect(Collectors.toList());
    	if(emitter == null) {
//			ret.put(t,strm);
    		logger.error("Due to ["+e.getMessage()+"],can't get historical quote for "+stream.collect(Collectors.toList()),e);
		}else {
			emitter.onNext(strm);
		}
    }

	private void streamResult(ObservableEmitter<List<AbstractQuote>> emitter, Map<String, Stock> batchResult) {
		logger.info("streamResult");
		if (emitter != null) {
			for (Stock ss : batchResult.values()) {
				List<HistoricalQuote> historicalQuotes;
				try {
					historicalQuotes = ss.getHistory();
					List<List<HistoricalQuote>> quotes = Lists.partition(historicalQuotes, 2000);
					for (List<HistoricalQuote>hq : quotes) {
						List<AbstractQuote> histQs = hq.stream().filter(s -> s != null)
								.map(
										s -> createQuote(s,ss.getSymbol())
								).collect(Collectors.toList());
						logger.info("emitter(" + histQs.size() + ")");
						emitter.onNext(histQs);
					}
				} catch (IOException e) {
					logger.error("cannot get stock quote history.. [" + ss.getName() + ", " + ss.getSymbol() + "]", e);
					ErrorQuote q = ErrorQuote.newQuoteInError(ss.getName(),e,e.getMessage(),QuoteErrorType.HISTORY);
					emitter.onNext(Lists.newArrayList(q));
				}
			}
			//emitter.onComplete();
		}
	}

	private Quote createQuote(HistoricalQuote s, String symbol) {
			
			Quote quote = new Quote((Long) null, symbol, 
				safeGetDouble(s.getOpen()),
				safeGetDouble(s.getClose()), safeGetDouble(s.getHigh()), safeGetDouble(s.getLow()),safeDivide(s.getAdjClose(), s.getClose()),
				s.getVolume(), safeGetDate(s.getDate()));
			return quote;
	}

	private Double safeDivide(BigDecimal num, BigDecimal denum) {
		Double n = safeGetDouble(num);
		Double den = safeGetDouble(denum);
		Double ret = null;
		if(n!= null && den != null) {
			ret = n/den;
		}
		
		return ret;
	}

	private Date safeGetDate(Calendar val) {
		Date ret = null;
		if(val != null) {
			ret = val.getTime();
		}
		return ret;
	}
	private Double safeGetDouble(BigDecimal val) {
		Double ret = null;
		if(val != null) {
			ret = val.doubleValue();
		}
			
		return ret;
	}

	interface Applicator <I,O>{
    	O apply(I a)throws IOException ;
    }
    private Map<String,Stock> batchGet(String[] symbols,Applicator<String[],Map<String,Stock>> f,ObservableEmitter<List<AbstractQuote>> emitter) {
    	Map<String, Stock> ret = batchGet(symbols, f,emitter, BATCH_SIZE);
    	return ret;
    }
    
    /**
     * Sends a request for multiple stocks with the historical quotes included
     * starting from the specified {@link Calendar} date 
     * until the specified Calendar date (to) 
     * at the specified interval.
     * <p>
     * The latest quotes will be retrieved in a single request to Yahoo Finance.
     * For the historical quotes, a separate request will be sent for each requested stock.
     * The returned Map only includes the Stocks that could 
     * successfully be retrieved from Yahoo Finance.
     * 
     * @param symbols               the symbols of the stocks for which you want to retrieve information
     * @param from                  start date of the historical data
     * @param to                    end date of the historical data
     * @param interval              the interval of the included historical data
     * @return                      a Map that links the symbols to their respective Stock objects.
     * @throws java.io.IOException when there's a connection problem
     */
    public  Map<String, Stock> get(String[] symbols, Calendar from, Calendar to, Interval interval) throws IOException {
        Map<String, Stock> ret = batchGet(symbols, b -> YahooFinance.get(b,from,to,interval),null);
    	return ret;
    }
    
    /**
     * Sends a request for a single FX rate.
     * Some common symbols can easily be found in the ENUM {@link yahoofinance.quotes.fx.FxSymbols}
     * Some examples of accepted symbols:
     * <ul>
     * <li> EURUSD=X
     * <li> USDEUR=X
     * <li> USDGBP=X
     * <li> AUDGBP=X
     * <li> CADUSD=X
     * </ul>
     * 
     * @param symbol    symbol for the FX rate you want to request
     * @return          a quote for the requested FX rate
     * @throws java.io.IOException when there's a connection problem
     */
    public  FxQuote getFx(String symbol) throws IOException {
    	FxQuote fx = YahooFinance.getFx(symbol);
        return fx;
    }
    
    /**
     * Sends a single request to Yahoo Finance to retrieve a quote 
     * for all the requested FX symbols.
     * See <code>getFx(String)</code> for more information on the
     * accepted FX symbols.
     * 
     * @param symbols   an array of FX symbols
     * @return          the requested FX symbols mapped to their respective quotes
     * @throws java.io.IOException when there's a connection problem or the request is incorrect
     * @see             #getFx(java.lang.String) 
     */
    public  Map<String, FxQuote> getFx(String[] symbols) throws IOException {
        Map<String, FxQuote> result = YahooFinance.getFx(symbols);
        return result;
    }    
    
}