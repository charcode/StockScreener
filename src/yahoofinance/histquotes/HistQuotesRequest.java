package yahoofinance.histquotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.logging.log4j.Logger;

import yahoofinance.Utils;
import yahoofinance.YahooFinance;

/**
 *
 * @author Stijn Strickx
 */
public class HistQuotesRequest {

	private final Logger logger;
    private final String symbol;

    private final Calendar from;
    private final Calendar to;

    private final Interval interval;
	private final Utils utils;

    public static final Calendar DEFAULT_FROM = Calendar.getInstance();

    static {
        DEFAULT_FROM.add(Calendar.YEAR, -1);
    }
    public static final Calendar DEFAULT_TO = Calendar.getInstance();
    public static final Interval DEFAULT_INTERVAL = Interval.MONTHLY;

    public HistQuotesRequest(String symbol, Logger logger) {
        this(symbol, DEFAULT_INTERVAL, logger);
    }

    public HistQuotesRequest(String symbol, Interval interval, Logger logger) {
        this.symbol = symbol;
        this.interval = interval;
        this.logger = logger;
        this.utils = new Utils(logger);

        this.from = DEFAULT_FROM;
        this.to = DEFAULT_TO;
    }

    public HistQuotesRequest(String symbol, Calendar from, Calendar to, Logger logger) {
        this(symbol, from, to, DEFAULT_INTERVAL, logger);
    }

    public HistQuotesRequest(String symbol, Calendar from, Calendar to, Interval interval, Logger logger) {
        this.symbol = symbol;
        this.from = from;
        this.to = to;
        this.interval = interval;
        this.logger = logger;
        this.utils = new Utils(logger);
    }

    public HistQuotesRequest(String symbol, Date from, Date to, Logger logger) {
        this(symbol, from, to, DEFAULT_INTERVAL, logger);
    }

    public HistQuotesRequest(String symbol, Date from, Date to, Interval interval, Logger logger) {
        this(symbol, interval, logger);
        this.from.setTime(from);
        this.to.setTime(to);
    }

    public List<HistoricalQuote> getResult() throws IOException {

        List<HistoricalQuote> result = new ArrayList<HistoricalQuote>();

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("s", this.symbol);

        params.put("a", String.valueOf(this.from.get(Calendar.MONTH)));
        params.put("b", String.valueOf(this.from.get(Calendar.DAY_OF_MONTH)));
        params.put("c", String.valueOf(this.from.get(Calendar.YEAR)));

        params.put("d", String.valueOf(this.to.get(Calendar.MONTH)));
        params.put("e", String.valueOf(this.to.get(Calendar.DAY_OF_MONTH)));
        params.put("f", String.valueOf(this.to.get(Calendar.YEAR)));

        params.put("g", this.interval.getTag());

        params.put("ignore", ".csv");

        String url = YahooFinance.HISTQUOTES_BASE_URL + "?" + utils.getURLParameters(params);

        // Get CSV from Yahoo
        logger.info("Sending request: " + url);

        URL request = new URL(url);
        URLConnection connection = request.openConnection();
        InputStreamReader is = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(is);
        br.readLine(); // skip the first line
        // Parse CSV
        for (String line = br.readLine(); line != null; line = br.readLine()) {

            logger.info("Parsing CSV line: " + utils.unescape(line));
            HistoricalQuote quote = this.parseCSVLine(line);
            result.add(quote);
        }
        return result;
    }

    private HistoricalQuote parseCSVLine(String line) {
        String[] data = line.split(YahooFinance.QUOTES_CSV_DELIMITER);
        return new HistoricalQuote(this.symbol,
                utils.parseHistDate(data[0]),
                utils.getBigDecimal(data[1]),
                utils.getBigDecimal(data[3]),
                utils.getBigDecimal(data[2]),
                utils.getBigDecimal(data[4]),
                utils.getBigDecimal(data[6]),
                utils.getLong(data[5])
        );
    }

}
