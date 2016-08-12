package com.oak.api.finance.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Economic {

    private final Date priceDate;
    private final Double bid;
    private final Double ask;
    private final Double eps;
    private final Double dayHigh;
    private final Double dayLow;
    private final String lastTradeDateStr;
    private final Integer lastTradeSize;
    private final String lastTradeTimeStr;
    private final Double open;
    private final Double previousClose;
    private final Double priceAvg200;
    private final Double priceAvg50;
    private final String symbol;
    private final TimeZone timeZone;
    private final Long volume;
    private final Double yearHigh;
    private final Double yearLow;
    private final Double bookValuePerShare;
    private final Double ebitda;
    private final Double epsEstimateCurrentYear;
    private final Double epsEstimateNextQuarter;
    private final Double epsEstimateNextYear;	
    private final Double marketCap;
    private final Double oneYearTargetPrice;
    private final Double pe;
    private final Double peg;
    private final Double priceBook;
    private final Double priceSales;
    private final Double revenue;
    private final Double roe;
    private final Long sharesFloat;
    private final Long sharesOutstanding;
    private final Long sharesOwned;
    private final Double   annualDividendYield;
    private final Double   annualDividendYieldPercent;
    private final Calendar dividendExDate;
    private final Calendar dividendPayDate;
    
    private Double perCalculated;

    public Economic(Date priceDate, Double bid, Integer bidSize, Double ask,
	    Integer askSize, Double change, Double changeFromAvg200,
	    Double changeFromAvg200InPercent, Double changeFromAvg50,
	    Double changeFromAvg50InPercent, Double changeFromYearHigh,
	    Double changeFromYearHighInPercent, Double changeFromYearLow,
	    Double changeFromYearLowInPercent, Double changeInPercent,
	    Double dayHigh, Double dayLow, String lastTradeDateStr,
	    Integer lastTradeSize, String lastTradeTimeStr, Double open,
	    Double previousClose, Double priceAvg200, Double priceAvg50,
	    String symbol, TimeZone timeZone, Long volume, Double yearHigh,
	    Double yearLow, Double eps, Double bookValuePerShare,
	    Double ebitda, Double epsEstimateCurrentYear,
	    Double epsEstimateNextQuarter, Double epsEstimateNextYear,
	    Double marketCap, Double oneYearTargetPrice, Double pe, Double peg,
	    Double priceBook, Double priceSales, Double revenue, Double roe,
	    Long sharesFloat, Long sharesOutstanding, Long sharesOwned,
	    Double annualDividendYield, Double annualDividendYieldPercent,
	    Calendar dividendExDate, Calendar dividendPayDate) {
	super();
	this.priceDate = priceDate;
	this.bid = bid;
	this.ask = ask;
	this.eps = eps;
	this.dayHigh = dayHigh;
	this.dayLow = dayLow;
	this.lastTradeDateStr = lastTradeDateStr;
	this.lastTradeSize = lastTradeSize;
	this.lastTradeTimeStr = lastTradeTimeStr;
	this.open = open;
	this.previousClose = previousClose;
	this.priceAvg200 = priceAvg200;
	this.priceAvg50 = priceAvg50;
	this.symbol = symbol;
	this.timeZone = timeZone;
	this.volume = volume;
	this.yearHigh = yearHigh;
	this.yearLow = yearLow;
	this.bookValuePerShare = bookValuePerShare;
	this.ebitda = ebitda;
	this.epsEstimateCurrentYear = epsEstimateCurrentYear;
	this.epsEstimateNextQuarter = epsEstimateNextQuarter;
	this.epsEstimateNextYear = epsEstimateNextYear;
	this.marketCap = marketCap;
	this.oneYearTargetPrice = oneYearTargetPrice;
	this.pe = pe;
	this.peg = peg;
	this.priceBook = priceBook;
	this.priceSales = priceSales;
	this.revenue = revenue;
	this.roe = roe;
	this.sharesFloat = sharesFloat;
	this.sharesOutstanding = sharesOutstanding;
	this.sharesOwned = sharesOwned;
	this.annualDividendYield = annualDividendYield;
	this.annualDividendYieldPercent = annualDividendYieldPercent;
	this.dividendExDate = dividendExDate;
	this.dividendPayDate = dividendPayDate;
    }

    public Date getPriceDate() {
	return priceDate;
    }

    public Double getDayHigh() {
	return dayHigh;
    }

    public Double getDayLow() {
	return dayLow;
    }

    public String getLastTradeDateStr() {
	return lastTradeDateStr;
    }

    public Integer getLastTradeSize() {
	return lastTradeSize;
    }

    public String getLastTradeTimeStr() {
	return lastTradeTimeStr;
    }

    public Double getOpen() {
	return open;
    }

    public Double getPreviousClose() {
	return previousClose;
    }

    public Double getPriceAvg200() {
	return priceAvg200;
    }

    public Double getPriceAvg50() {
	return priceAvg50;
    }

    public String getSymbol() {
	return symbol;
    }

    public TimeZone getTimeZone() {
	return timeZone;
    }

    public long getVolume() {
	return volume;
    }

    public Double getYearHigh() {
	return yearHigh;
    }

    public Double getYearLow() {
	return yearLow;
    }

    public Date getDate() {
	return priceDate;
    }

    public Double getBid() {
	return bid;
    }

    public Double getAsk() {
	return ask;
    }

    public Double getEps() {
	return eps;
    }

    public Double getBookValuePerShare() {
	return bookValuePerShare;
    }

    public Double getEbitda() {
	return ebitda;
    }

    public Double getEpsEstimateCurrentYear() {
	return epsEstimateCurrentYear;
    }

    public Double getEpsEstimateNextQuarter() {
	return epsEstimateNextQuarter;
    }

    public Double getEpsEstimateNextYear() {
	return epsEstimateNextYear;
    }

    public Double getMarketCap() {
	return marketCap;
    }

    public Double getOneYearTargetPrice() {
	return oneYearTargetPrice;
    }

    public Double getPe() {
	return pe;
    }

    public Double getPeg() {
	return peg;
    }

    public Double getPriceBook() {
	return priceBook;
    }

    public Double getPriceSales() {
	return priceSales;
    }

    public Double getRevenue() {
	return revenue;
    }

    public Double getRoe() {
	return roe;
    }

    public Long getSharesFloat() {
	return sharesFloat;
    }

    public Long getSharesOutstanding() {
	return sharesOutstanding;
    }

    public Long getSharesOwned() {
	return sharesOwned;
    }

    public Double getAnnualDividendYield() {
        return annualDividendYield;
    }

    public Double getAnnualDividendYieldPercent() {
        return annualDividendYieldPercent;
    }

    public Calendar getDividendExDate() {
        return dividendExDate;
    }

    public Calendar getDividendPayDate() {
        return dividendPayDate;
    }
    
    
    

    public Double getPerCalculated() {
        return perCalculated;
    }

    public void setPerCalculated(Double perCalculated) {
        this.perCalculated = perCalculated;
    }

    @Override
    public String toString() {
	return "Economic [date=" + priceDate + ", price=" + bid + ", eps="
		+ eps + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime
		* result
		+ ((annualDividendYield == null) ? 0 : annualDividendYield
			.hashCode());
	result = prime
		* result
		+ ((annualDividendYieldPercent == null) ? 0
			: annualDividendYieldPercent.hashCode());
	result = prime * result + ((ask == null) ? 0 : ask.hashCode());
	result = prime * result + ((bid == null) ? 0 : bid.hashCode());
	result = prime
		* result
		+ ((bookValuePerShare == null) ? 0 : bookValuePerShare
			.hashCode());
	result = prime * result + ((dayHigh == null) ? 0 : dayHigh.hashCode());
	result = prime * result + ((dayLow == null) ? 0 : dayLow.hashCode());
	result = prime * result
		+ ((dividendExDate == null) ? 0 : dividendExDate.hashCode());
	result = prime * result
		+ ((dividendPayDate == null) ? 0 : dividendPayDate.hashCode());
	result = prime * result + ((ebitda == null) ? 0 : ebitda.hashCode());
	result = prime * result + ((eps == null) ? 0 : eps.hashCode());
	result = prime
		* result
		+ ((epsEstimateCurrentYear == null) ? 0
			: epsEstimateCurrentYear.hashCode());
	result = prime
		* result
		+ ((epsEstimateNextQuarter == null) ? 0
			: epsEstimateNextQuarter.hashCode());
	result = prime
		* result
		+ ((epsEstimateNextYear == null) ? 0 : epsEstimateNextYear
			.hashCode());
	result = prime
		* result
		+ ((lastTradeDateStr == null) ? 0 : lastTradeDateStr.hashCode());
	result = prime * result
		+ ((lastTradeSize == null) ? 0 : lastTradeSize.hashCode());
	result = prime
		* result
		+ ((lastTradeTimeStr == null) ? 0 : lastTradeTimeStr.hashCode());
	result = prime * result
		+ ((marketCap == null) ? 0 : marketCap.hashCode());
	result = prime
		* result
		+ ((oneYearTargetPrice == null) ? 0 : oneYearTargetPrice
			.hashCode());
	result = prime * result + ((open == null) ? 0 : open.hashCode());
	result = prime * result + ((pe == null) ? 0 : pe.hashCode());
	result = prime * result + ((peg == null) ? 0 : peg.hashCode());
	result = prime * result
		+ ((previousClose == null) ? 0 : previousClose.hashCode());
	result = prime * result
		+ ((priceAvg200 == null) ? 0 : priceAvg200.hashCode());
	result = prime * result
		+ ((priceAvg50 == null) ? 0 : priceAvg50.hashCode());
	result = prime * result
		+ ((priceBook == null) ? 0 : priceBook.hashCode());
	result = prime * result
		+ ((priceDate == null) ? 0 : priceDate.hashCode());
	result = prime * result
		+ ((priceSales == null) ? 0 : priceSales.hashCode());
	result = prime * result + ((revenue == null) ? 0 : revenue.hashCode());
	result = prime * result + ((roe == null) ? 0 : roe.hashCode());
	result = prime * result
		+ ((sharesFloat == null) ? 0 : sharesFloat.hashCode());
	result = prime
		* result
		+ ((sharesOutstanding == null) ? 0 : sharesOutstanding
			.hashCode());
	result = prime * result
		+ ((sharesOwned == null) ? 0 : sharesOwned.hashCode());
	result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
	result = prime * result
		+ ((timeZone == null) ? 0 : timeZone.hashCode());
	result = prime * result + ((volume == null) ? 0 : volume.hashCode());
	result = prime * result
		+ ((yearHigh == null) ? 0 : yearHigh.hashCode());
	result = prime * result + ((yearLow == null) ? 0 : yearLow.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Economic other = (Economic) obj;
	if (annualDividendYield == null) {
	    if (other.annualDividendYield != null)
		return false;
	} else if (!annualDividendYield.equals(other.annualDividendYield))
	    return false;
	if (annualDividendYieldPercent == null) {
	    if (other.annualDividendYieldPercent != null)
		return false;
	} else if (!annualDividendYieldPercent
		.equals(other.annualDividendYieldPercent))
	    return false;
	if (ask == null) {
	    if (other.ask != null)
		return false;
	} else if (!ask.equals(other.ask))
	    return false;
	if (bid == null) {
	    if (other.bid != null)
		return false;
	} else if (!bid.equals(other.bid))
	    return false;
	if (bookValuePerShare == null) {
	    if (other.bookValuePerShare != null)
		return false;
	} else if (!bookValuePerShare.equals(other.bookValuePerShare))
	    return false;
	if (dayHigh == null) {
	    if (other.dayHigh != null)
		return false;
	} else if (!dayHigh.equals(other.dayHigh))
	    return false;
	if (dayLow == null) {
	    if (other.dayLow != null)
		return false;
	} else if (!dayLow.equals(other.dayLow))
	    return false;
	if (dividendExDate == null) {
	    if (other.dividendExDate != null)
		return false;
	} else if (!dividendExDate.equals(other.dividendExDate))
	    return false;
	if (dividendPayDate == null) {
	    if (other.dividendPayDate != null)
		return false;
	} else if (!dividendPayDate.equals(other.dividendPayDate))
	    return false;
	if (ebitda == null) {
	    if (other.ebitda != null)
		return false;
	} else if (!ebitda.equals(other.ebitda))
	    return false;
	if (eps == null) {
	    if (other.eps != null)
		return false;
	} else if (!eps.equals(other.eps))
	    return false;
	if (epsEstimateCurrentYear == null) {
	    if (other.epsEstimateCurrentYear != null)
		return false;
	} else if (!epsEstimateCurrentYear.equals(other.epsEstimateCurrentYear))
	    return false;
	if (epsEstimateNextQuarter == null) {
	    if (other.epsEstimateNextQuarter != null)
		return false;
	} else if (!epsEstimateNextQuarter.equals(other.epsEstimateNextQuarter))
	    return false;
	if (epsEstimateNextYear == null) {
	    if (other.epsEstimateNextYear != null)
		return false;
	} else if (!epsEstimateNextYear.equals(other.epsEstimateNextYear))
	    return false;
	if (lastTradeDateStr == null) {
	    if (other.lastTradeDateStr != null)
		return false;
	} else if (!lastTradeDateStr.equals(other.lastTradeDateStr))
	    return false;
	if (lastTradeSize == null) {
	    if (other.lastTradeSize != null)
		return false;
	} else if (!lastTradeSize.equals(other.lastTradeSize))
	    return false;
	if (lastTradeTimeStr == null) {
	    if (other.lastTradeTimeStr != null)
		return false;
	} else if (!lastTradeTimeStr.equals(other.lastTradeTimeStr))
	    return false;
	if (marketCap == null) {
	    if (other.marketCap != null)
		return false;
	} else if (!marketCap.equals(other.marketCap))
	    return false;
	if (oneYearTargetPrice == null) {
	    if (other.oneYearTargetPrice != null)
		return false;
	} else if (!oneYearTargetPrice.equals(other.oneYearTargetPrice))
	    return false;
	if (open == null) {
	    if (other.open != null)
		return false;
	} else if (!open.equals(other.open))
	    return false;
	if (pe == null) {
	    if (other.pe != null)
		return false;
	} else if (!pe.equals(other.pe))
	    return false;
	if (peg == null) {
	    if (other.peg != null)
		return false;
	} else if (!peg.equals(other.peg))
	    return false;
	if (previousClose == null) {
	    if (other.previousClose != null)
		return false;
	} else if (!previousClose.equals(other.previousClose))
	    return false;
	if (priceAvg200 == null) {
	    if (other.priceAvg200 != null)
		return false;
	} else if (!priceAvg200.equals(other.priceAvg200))
	    return false;
	if (priceAvg50 == null) {
	    if (other.priceAvg50 != null)
		return false;
	} else if (!priceAvg50.equals(other.priceAvg50))
	    return false;
	if (priceBook == null) {
	    if (other.priceBook != null)
		return false;
	} else if (!priceBook.equals(other.priceBook))
	    return false;
	if (priceDate == null) {
	    if (other.priceDate != null)
		return false;
	} else if (!priceDate.equals(other.priceDate))
	    return false;
	if (priceSales == null) {
	    if (other.priceSales != null)
		return false;
	} else if (!priceSales.equals(other.priceSales))
	    return false;
	if (revenue == null) {
	    if (other.revenue != null)
		return false;
	} else if (!revenue.equals(other.revenue))
	    return false;
	if (roe == null) {
	    if (other.roe != null)
		return false;
	} else if (!roe.equals(other.roe))
	    return false;
	if (sharesFloat == null) {
	    if (other.sharesFloat != null)
		return false;
	} else if (!sharesFloat.equals(other.sharesFloat))
	    return false;
	if (sharesOutstanding == null) {
	    if (other.sharesOutstanding != null)
		return false;
	} else if (!sharesOutstanding.equals(other.sharesOutstanding))
	    return false;
	if (sharesOwned == null) {
	    if (other.sharesOwned != null)
		return false;
	} else if (!sharesOwned.equals(other.sharesOwned))
	    return false;
	if (symbol == null) {
	    if (other.symbol != null)
		return false;
	} else if (!symbol.equals(other.symbol))
	    return false;
	if (timeZone == null) {
	    if (other.timeZone != null)
		return false;
	} else if (!timeZone.equals(other.timeZone))
	    return false;
	if (volume == null) {
	    if (other.volume != null)
		return false;
	} else if (!volume.equals(other.volume))
	    return false;
	if (yearHigh == null) {
	    if (other.yearHigh != null)
		return false;
	} else if (!yearHigh.equals(other.yearHigh))
	    return false;
	if (yearLow == null) {
	    if (other.yearLow != null)
		return false;
	} else if (!yearLow.equals(other.yearLow))
	    return false;
	return true;
    }

}
