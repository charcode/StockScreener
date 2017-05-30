
package yahoofinance;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.fx.FxQuote;

/**
 * YahooFinance can be used to retrieve quotes and some extra information on stocks.
 * There is also the possibility to include historical quotes on the requested stocks.
 * <p>
 * When trying to get information on multiple stocks at once, please use the provided
 * methods that accept a <code>String[]</code> of symbols to get the best performance.
 * To retrieve the basic quote, statistics and dividend data, a single request can be sent to 
 * Yahoo Finance for multiple stocks at once.
 * For the historical data however, a separate request has to be sent to Yahoo Finance
 * for each of the requested stocks. The provided methods will retrieve 
 * all of the required information in the least amount of 
 * requests possible towards Yahoo Finance.
 * <p>
 * Please be aware that the data received from Yahoo Finance is not always 
 * complete for every single stock. Stocks on the American stock exchanges
 * usually have a lot more data available than stocks on other exchanges.
 * <p>
 * This API can also be used to send requests for retrieving FX rates.
 * <p>
 * Since the data is provided by Yahoo, please check their Terms of Service
 * at https://info.yahoo.com/legal/us/yahoo/
 * 
 * @author      Stijn Strickx
 * @version     %I%, %G%
 */
public class YahooFinanceWrapper {
    
    public static final String QUOTES_BASE_URL = "http://finance.yahoo.com/d/quotes.csv";
    public static final String HISTQUOTES_BASE_URL = "http://ichart.yahoo.com/table.csv";
    public static final String QUOTES_CSV_DELIMITER = ",";
    public static final String TIMEZONE = "America/New_York";
    
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
        Map<String, Stock> ret = YahooFinance.get(symbols,includeHistorical);
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
        Map<String, Stock> ret = YahooFinance.get(symbols,from,interval);
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
        Map<String, Stock> ret = YahooFinance.get(symbols,from,to,interval);
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