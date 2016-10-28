package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="screen0_result")
public class Screen0Result {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String ticker;
	@Column(name = "company_name")
	private String companyName;
	@Column(name = "exchange_code")
	private String exchangeCode;
	private String currency;
	@Column(name = "run_date")
	private Date runDate;
	@Column(name = "bid")
	private Double priceBid;
	@Column(name = "book_value_per_shr")
	private Double bookValuePerShare;
	@Column(name = "per_clc")
	private Double perCalculated; // Price to Earning - as calculated
	private Double eps; // Earnings per share;
	private Double per; // price to earning ratio
	private Double peg; // price to earning growth
	@Column(name = "eps_est_0_yr")
	private Double epsEstThisYear; // earning per share for this year
	@Column(name = "eps_est_1_yr")
	private Double epsEstNextYear; // earning per share for next year
	@Column(name = "eps_est_1_qtr")
	private Double epsEstNextQuarter; // earning per share for next quarter
	@Column(name = "market_cap")
	private Double marketCap;
	@Column(name = "div_yld")
	private Double dividendYield;
	@Column(name = "price_trgt")
	private Double targetPrice;
	private Double price;
}
