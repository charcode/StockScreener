package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name="balance_sheet")
public class BalanceSheetDto extends AbstractFinancialStatement{

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
