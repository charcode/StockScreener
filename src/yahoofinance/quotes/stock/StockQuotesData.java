package yahoofinance.quotes.stock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import yahoofinance.Stock;
import yahoofinance.Utils;
import yahoofinance.exchanges.ExchangeTimeZone;
import yahoofinance.quotes.QuotesProperty;

/**
 *
 * @author Stijn Strickx
 */
public class StockQuotesData {
    
    private final String[] data;
    private final Utils utils ;
    private final Logger logger;
    private final ExchangeTimeZone etimeZone;
    
    public StockQuotesData(String[] data, Logger logger) {
        this.data = data;
        this.logger = logger;
        this.utils = new Utils(logger);
        etimeZone = new ExchangeTimeZone(logger);
    }
    
    public String getValue(QuotesProperty property) {
        int i = StockQuotesRequest.DEFAULT_PROPERTIES.indexOf(property);
        if(i >= 0 && i < this.data.length) {
            return this.data[i];
        }
        return null;
    }
    
    public StockQuote getQuote() {
        String symbol = this.getValue(QuotesProperty.Symbol);
        StockQuote quote = new StockQuote(symbol, logger);
        
        quote.setPrice(utils.getBigDecimal(this.getValue(QuotesProperty.LastTradePriceOnly)));
        quote.setLastTradeSize(utils.getInt(this.getValue(QuotesProperty.LastTradeSize)));
        quote.setAsk(utils.getBigDecimal(this.getValue(QuotesProperty.AskRealtime), this.getValue(QuotesProperty.Ask)));
        quote.setAskSize(utils.getInt(this.getValue(QuotesProperty.AskSize)));
        quote.setBid(utils.getBigDecimal(this.getValue(QuotesProperty.BidRealtime), this.getValue(QuotesProperty.Bid)));
        quote.setBidSize(utils.getInt(this.getValue(QuotesProperty.BidSize)));
        quote.setOpen(utils.getBigDecimal(this.getValue(QuotesProperty.Open)));
        quote.setPreviousClose(utils.getBigDecimal(this.getValue(QuotesProperty.PreviousClose)));
        quote.setDayHigh(utils.getBigDecimal(this.getValue(QuotesProperty.DaysHigh)));
        quote.setDayLow(utils.getBigDecimal(this.getValue(QuotesProperty.DaysLow)));
        
        quote.setTimeZone(etimeZone.getStockTimeZone(symbol));
        quote.setLastTradeTime(utils.parseDateTime(this.getValue(QuotesProperty.LastTradeDate), this.getValue(QuotesProperty.LastTradeTime), quote.getTimeZone()));
        
        quote.setYearHigh(utils.getBigDecimal(this.getValue(QuotesProperty.YearHigh)));
        quote.setYearLow(utils.getBigDecimal(this.getValue(QuotesProperty.YearLow)));
        quote.setPriceAvg50(utils.getBigDecimal(this.getValue(QuotesProperty.FiftydayMovingAverage)));
        quote.setPriceAvg200(utils.getBigDecimal(this.getValue(QuotesProperty.TwoHundreddayMovingAverage)));
        
        quote.setVolume(utils.getLong(this.getValue(QuotesProperty.Volume)));
        quote.setAvgVolume(utils.getLong(this.getValue(QuotesProperty.AverageDailyVolume)));
        
        return quote;
    }
    
    public StockStats getStats() {
        String symbol = this.getValue(QuotesProperty.Symbol);
        StockStats stats = new StockStats(symbol);
        
        stats.setMarketCap(utils.getBigDecimal(this.getValue(QuotesProperty.MarketCapitalization)));
        stats.setSharesFloat(utils.getLong(this.getValue(QuotesProperty.SharesFloat)));
        stats.setSharesOutstanding(utils.getLong(this.getValue(QuotesProperty.SharesOutstanding)));
        stats.setSharesOwned(utils.getLong(this.getValue(QuotesProperty.SharesOwned)));
        
        stats.setEps(utils.getBigDecimal(this.getValue(QuotesProperty.DilutedEPS)));
        stats.setPe(utils.getBigDecimal(this.getValue(QuotesProperty.PERatio)));
        stats.setPeg(utils.getBigDecimal(this.getValue(QuotesProperty.PEGRatio)));
        
        stats.setEpsEstimateCurrentYear(utils.getBigDecimal(this.getValue(QuotesProperty.EPSEstimateCurrentYear)));
        stats.setEpsEstimateNextQuarter(utils.getBigDecimal(this.getValue(QuotesProperty.EPSEstimateNextQuarter)));
        stats.setEpsEstimateNextYear(utils.getBigDecimal(this.getValue(QuotesProperty.EPSEstimateNextYear)));
        
        stats.setPriceBook(utils.getBigDecimal(this.getValue(QuotesProperty.PriceBook)));
        stats.setPriceSales(utils.getBigDecimal(this.getValue(QuotesProperty.PriceSales)));
        stats.setBookValuePerShare(utils.getBigDecimal(this.getValue(QuotesProperty.BookValuePerShare)));
        
        stats.setOneYearTargetPrice(utils.getBigDecimal(this.getValue(QuotesProperty.OneyrTargetPrice)));
        stats.setEBITDA(utils.getBigDecimal(this.getValue(QuotesProperty.EBITDA)));
        stats.setRevenue(utils.getBigDecimal(this.getValue(QuotesProperty.Revenue)));
        
        return stats;
    }
    
    public StockDividend getDividend() {
        String symbol = this.getValue(QuotesProperty.Symbol);
        StockDividend dividend = new StockDividend(symbol);
        
        dividend.setPayDate(utils.parseDividendDate(this.getValue(QuotesProperty.DividendPayDate)));
        dividend.setExDate(utils.parseDividendDate(this.getValue(QuotesProperty.ExDividendDate)));
        dividend.setAnnualYield(utils.getBigDecimal(this.getValue(QuotesProperty.TrailingAnnualDividendYield)));
        dividend.setAnnualYieldPercent(utils.getBigDecimal(this.getValue(QuotesProperty.TrailingAnnualDividendYieldInPercent)));
        
        return dividend;
    }
    
    public Stock getStock() {
        String symbol = this.getValue(QuotesProperty.Symbol);
        Stock stock = new Stock(symbol, logger);
        
        stock.setName(this.getValue(QuotesProperty.Name));
        stock.setCurrency(this.getValue(QuotesProperty.Currency));
        stock.setStockExchange(this.getValue(QuotesProperty.StockExchange));
        
        stock.setQuote(this.getQuote());
        stock.setStats(this.getStats());
        stock.setDividend(this.getDividend());
        
        return stock;
    }
    
}
