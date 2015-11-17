
package yahoofinance.quotes.fx;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import yahoofinance.YahooFinance;
import yahoofinance.quotes.QuotesProperty;
import yahoofinance.quotes.QuotesRequest;

/**
 *
 * @author Stijn Strickx
 */
public class FxQuotesRequest extends QuotesRequest<FxQuote> {
    
    public static final List<QuotesProperty> DEFAULT_PROPERTIES = new ArrayList<QuotesProperty>();
    static {
        DEFAULT_PROPERTIES.add(QuotesProperty.Symbol);
        DEFAULT_PROPERTIES.add(QuotesProperty.LastTradePriceOnly);
    }

    public FxQuotesRequest(String query, Logger logger) {
        super(query, FxQuotesRequest.DEFAULT_PROPERTIES, logger);
    }

    @Override
    protected FxQuote parseCSVLine(String line) {
        String[] split = utils.stripOverhead(line).split(YahooFinance.QUOTES_CSV_DELIMITER);
        if(split.length >= 2) {
            return new FxQuote(split[0], utils.getBigDecimal(split[1]), logger);
        }
        return null;
    }
    
}
