package yahoofinance;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.logging.log4j.Logger;

/**
 *
 * @author Stijn Strickx
 */
public class Utils {

    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal THOUSAND = new BigDecimal(1000);
    public static final BigDecimal MILLION = new BigDecimal(1000000);
    public static final BigDecimal BILLION = new BigDecimal(1000000000);
    
    private final Logger logger; 
    public Utils(Logger logger){
    	this.logger = logger;
    }
        
    public  String join(String[] data, String d) {
        if (data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i;

        for (i = 0; i < (data.length - 1); i++) {
            sb.append(data[i]).append(d);
        }
        return sb.append(data[i]).toString();
    }

    private  String cleanNumberString(String data) {
        return join(data.trim().split(","), "");
    }

    private  boolean isParseable(String data) {
        return !(data == null || data.equals("N/A") || data.equals("-") || data.equals(""));
    }

    public  BigDecimal getBigDecimal(String data) {
        BigDecimal result = BigDecimal.ZERO;
        if (!isParseable(data)) {
            return result;
        }
        try {
            data = cleanNumberString(data);
            char lastChar = data.charAt(data.length() - 1);
            BigDecimal multiplier = BigDecimal.ONE;
            switch (lastChar) {
                case 'B':
                    data = data.substring(0, data.length() - 1);
                    multiplier = BILLION;
                    break;
                case 'M':
                    data = data.substring(0, data.length() - 1);
                    multiplier = MILLION;
                    break;
                case 'K':
                    data = data.substring(0, data.length() - 1);
                    multiplier = THOUSAND;
                    break;
            }
			if ("nan".equals(data.toLowerCase())) {
				result = null;
			} else {
				result = new BigDecimal(data).multiply(multiplier);
			}
		} catch (NumberFormatException e) {
			logger.info("Failed to parse: " + data, e);
		}
        return result;
    }
    
    public  BigDecimal getBigDecimal(String dataMain, String dataSub) {
        BigDecimal main = getBigDecimal(dataMain);
        BigDecimal sub = getBigDecimal(dataSub);
        if(main.compareTo(BigDecimal.ZERO) == 0) {
            return sub;
        }
        return main;
    }

    public  double getDouble(String data) {
        double result = 0.00;
        if (!isParseable(data)) {
            return result;
        }
        try {
            data = cleanNumberString(data);
            char lastChar = data.charAt(data.length() - 1);
            int multiplier = 1;
            switch (lastChar) {
                case 'B':
                    data = data.substring(0, data.length() - 1);
                    multiplier = 1000000000;
                    break;
                case 'M':
                    data = data.substring(0, data.length() - 1);
                    multiplier = 1000000;
                    break;
                case 'K':
                    data = data.substring(0, data.length() - 1);
                    multiplier = 1000;
                    break;
            }
            result = Double.parseDouble(data) * multiplier;
        } catch (NumberFormatException e) {
            logger.info("Failed to parse: " + data, e);
        }
        return result;
    }

    public  int getInt(String data) {
        int result = 0;
        if (!isParseable(data)) {
            return result;
        }
        try {
            data = cleanNumberString(data);
            result = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            logger.info("Failed to parse: " + data, e);
        }
        return result;
    }

    public  long getLong(String data) {
        long result = 0;
        if (!isParseable(data)) {
            return result;
        }
        try {
            data = cleanNumberString(data);
            result = Long.parseLong(data);
        } catch (NumberFormatException e) {
            logger.info("Failed to parse: " + data, e);
        }
        return result;
    }

    public  BigDecimal getPercent(BigDecimal numerator, BigDecimal denominator) {
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(denominator, 4, BigDecimal.ROUND_HALF_EVEN)
                .multiply(HUNDRED).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
    
    public  double getPercent(double numerator, double denominator) {
        if (denominator == 0) {
            return 0;
        }
        return (numerator / denominator) * 100;
    }

    private  String getDividendDateFormat(String date) {
        if (date.matches("[0-9][0-9]-...-[0-9][0-9]")) {
            return "dd-MMM-yy";
        } else if (date.matches("[0-9]-...-[0-9][0-9]")) {
            return "d-MMM-yy";
        } else if (date.matches("...[ ]+[0-9]+")) {
            return "MMM d";
        } else {
            return "M/d/yy";
        }
    }

    /**
     * Used to parse the dividend dates. Returns null if the date cannot be
     * parsed.
     *
     * @param date String received that represents the date
     * @return Calendar object representing the parsed date
     */
    public  Calendar parseDividendDate(String date) {
        if (!isParseable(date)) {
            return null;
        }
        date = date.trim();
        SimpleDateFormat format = new SimpleDateFormat(getDividendDateFormat(date), Locale.US);
        format.setTimeZone(TimeZone.getTimeZone(YahooFinance.TIMEZONE));
        try {
            Calendar today = Calendar.getInstance(TimeZone.getTimeZone(YahooFinance.TIMEZONE));
            Calendar parsedDate = Calendar.getInstance(TimeZone.getTimeZone(YahooFinance.TIMEZONE));
            parsedDate.setTime(format.parse(date));

            if (parsedDate.get(Calendar.YEAR) == 1970) {
                // Not really clear which year the dividend date is... making a reasonable guess.
                int monthDiff = parsedDate.get(Calendar.MONTH) - today.get(Calendar.MONTH);
                int year = today.get(Calendar.YEAR);
                if (monthDiff > 6) {
                    year -= 1;
                } else if (monthDiff < -6) {
                    year += 1;
                }
                parsedDate.set(Calendar.YEAR, year);
            }

            return parsedDate;
        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Used to parse the last trade date / time. Returns null if the date / time
     * cannot be parsed.
     *
     * @param date String received that represents the date
     * @param time String received that represents the time
     * @param timeZone time zone to use for parsing the date time
     * @return Calendar object with the parsed datetime
     */
    public  Calendar parseDateTime(String date, String time, TimeZone timeZone) {
        String datetime = date + " " + time;
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mma", Locale.US);
        
        format.setTimeZone(timeZone);
        try {
            if (isParseable(date) && isParseable(time)) {
                Calendar c = Calendar.getInstance();
                c.setTime(format.parse(datetime));
                return c;
            }
        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public  Calendar parseHistDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone(YahooFinance.TIMEZONE));
        try {
            if (isParseable(date)) {
                Calendar c = Calendar.getInstance();
                c.setTime(format.parse(date));
                return c;
            }
        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public  String getURLParameters(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();

        for (Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                key = URLEncoder.encode(key, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                logger.error(ex.getMessage(), ex);
                // Still try to continue with unencoded values
            }
            sb.append(String.format("%s=%s", key, value));
        }
        return sb.toString();
    }

    /**
     * Strips the unwanted chars from a line returned in the CSV 
     * Used for parsing the FX CSV lines
     *
     * @param line the original CSV line
     * @return the stripped line
     */
    public  String stripOverhead(String line) {
        return line.replaceAll("\"", "");
    }

    public  String unescape(String data) {
        StringBuilder buffer = new StringBuilder(data.length());
        for (int i = 0; i < data.length(); i++) {
            if ((int) data.charAt(i) > 256) {
                buffer.append("\\u").append(Integer.toHexString((int) data.charAt(i)));
            } else {
                if (data.charAt(i) == '\n') {
                    buffer.append("\\n");
                } else if (data.charAt(i) == '\t') {
                    buffer.append("\\t");
                } else if (data.charAt(i) == '\r') {
                    buffer.append("\\r");
                } else if (data.charAt(i) == '\b') {
                    buffer.append("\\b");
                } else if (data.charAt(i) == '\f') {
                    buffer.append("\\f");
                } else if (data.charAt(i) == '\'') {
                    buffer.append("\\'");
                } else if (data.charAt(i) == '\"') {
                    buffer.append("\\\"");
                } else if (data.charAt(i) == '\\') {
                    buffer.append("\\\\");
                } else {
                    buffer.append(data.charAt(i));
                }
            }
        }
        return buffer.toString();
    }

}
