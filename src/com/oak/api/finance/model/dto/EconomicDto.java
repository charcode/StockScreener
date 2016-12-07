package com.oak.api.finance.model.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="economy")
public class EconomicDto {
		@Id
		@GeneratedValue (strategy=GenerationType.AUTO)
		private Long id;
		private Long companyId;
		private String ticker;
	    private Date priceDate;
	    private Double bid;
	    private Double ask;
	    private Double eps;
	    private Double dayHigh;
	    private Double dayLow;
	    private String lastTradeDateStr;
	    private Integer lastTradeSize;
	    private String lastTradeTimeStr;
	    private Double open;
	    private Double previousClose;
	    private Double priceAvg200;
	    private Double priceAvg50;
	    private TimeZone timeZone;
	    private Long volume;
	    private Double yearHigh;
	    private Double yearLow;
	    private Double bookValuePerShare;
	    private Double ebitda;
	    private Double epsEstimateCurrentYear;
	    private Double epsEstimateNextQuarter;
	    private Double epsEstimateNextYear;	
	    private Double marketCap;
	    private Double oneYearTargetPrice;
	    private Double pe;
	    private Double peg;
	    private Double priceBook;
	    private Double priceSales;
	    private Double revenue;
	    private Double roe;
	    private Long sharesFloat;
	    private Long sharesOutstanding;
	    private Long sharesOwned;
	    private Double annualDividendYield;
	    private Double annualDividendYieldPercent;
	    private Calendar dividendExDate;
	    private Calendar dividendPayDate;
}
