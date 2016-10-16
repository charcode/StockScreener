package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Sector {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	private String description;
	private String industry;
	
	private double oneDatePriceChangePercent;
	private double marketCap;
	/**
	 * Price to Earning Ratio
	 */
	private double peRatio; 
	/**
	 * Return on equity in percentage
	 */
	private double roePercent;
	private double dividendYield;
	private double longTermDebtToEquity;
	private double priceToBookValue;
	private double netProfitMarginPercent;
	private double priceToFreeCashFlow;

}
