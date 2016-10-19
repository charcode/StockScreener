package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Sector implements Comparable<Sector>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	private String description;
	private String industry;
	private Long parentSectorId; // null when top level sector
	private String parentSectorDescription;  // null when top level sector
	
	private double oneDayPriceChangePercent;
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
	
	@Override
	public int compareTo(Sector o) {
		int compareTo;
		if(description != null) {
			compareTo = description.compareTo(o.description);
		}else {
			if(o.description == null) {
				compareTo = 0;
			}else {
				compareTo = -1;
			}
		}
		return compareTo;
	}

}
