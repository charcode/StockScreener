package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="balance_sheet")
public class BalanceSheetDto /* implements Comparable<BalanceSheetDto> */{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Long id;
	private String ticker;
	private Date endDate;
	@Enumerated(EnumType.STRING)
	private StatementPeriod statementPeriod;
	private Date releaseDate;
	private Double cashAndEquivalent;
	private Double shortTermInvestments;
	private Double netReceivables;
	private Double inventory;
	private Double otherCurrentAssets;
	private Double longTermInvestments;
	private Double propertyPlantAndEquivalent;
	private Double goodwill;
	private Double intangibleAssets;
	private Double accumulatedAmortization;
	private Double otherAssets;
	private Double deferredLongTermAssetCharges;
	private Double totalCurrentAssetsCalculated;
	private Double totalCurrentAssets;
	private Double totalAssets;
	private Double longTermDebt;
	private Double otherLiabilities;
	private Double deferredLongTermLiabilityCharges;
	private Double minorityInterest;
	private Double negativeGoodwill;
	private Double accountPayable;
	private Double shortCurrentLongTermDebt;
	private Double otherCurrentLiabilities;
	private Double totalCurrentLiabilitiesCalculated;
	private Double totalLiabilities;
	private Double totalCurrentLiabilities;
	private Double miscStocksOptionsWarrants;
	private Double redeemablePreferredStocks;
	private Double commonStock;
	private Double preferredStock;
	private Double retainedEarnings;
	private Double treasuryStock;
	private Double capitalSurplus;
	private Double otherStockholderEquity;
	
	/*
	@Override
	public int compareTo(BalanceSheetDto o) {
		int ret = 1;
		if(o != null) {
			ret = statementPeriod.getLength() - o.statementPeriod.getLength();
			if(ret == 0) {
				ret = endDate.compareTo(o.endDate);
			}
		}
		return ret;
	}
	*/

}
