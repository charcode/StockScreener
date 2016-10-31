package com.oak.external.finance.app.marketdata.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.BalanceSheet;
import com.oak.api.finance.model.FinancialData;
import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.StatementPeriod;

public class FinancialStatementsConverter {

	private final Logger logger;
	public FinancialStatementsConverter(org.apache.logging.log4j.Logger logger) {
		this.logger = logger;
	}

	private BalanceSheet convertDbToYahooBalanceSheet(BalanceSheetDto i) {
		String symbol = i.getTicker();
		Double cashAndEquivalent = i.getCashAndEquivalent();
		Double shortTermInvestements  = i.getShortTermInvestments();
		Double netReceivables  = i.getNetReceivables();
		Double inventory  = i.getInventory();
		Double otherCurrentAssets  = i.getOtherCurrentAssets();
		Double totalCurrentAssets  = i.getTotalCurrentAssets();
		Double longTermInvestments  = i.getLongTermInvestments();
		Double propertyPlantAndEquivalent  = i.getPropertyPlantAndEquivalent();
		Double goodwill  = i.getGoodwill();
		Double intangibleAssets  = i.getIntangibleAssets();
		Double accumulatedAmortization  = i.getAccumulatedAmortization();
		Double otherAssets  = i.getOtherAssets();
		Double totalAssets  = i.getTotalAssets();
		Double deferredLongTermAssetCharges  = i.getDeferredLongTermAssetCharges();
		Double accountPayable  = i.getAccountPayable();
		Double shortCurrentLongTermDebt  = i.getShortCurrentLongTermDebt();
		Double otherCurrentLiabilities  = i.getOtherCurrentLiabilities();
		Double totalCurrentLiabilities  = i.getTotalCurrentLiabilities();
		Double longTermDebt  = i.getLongTermDebt();
		Double otherLiabilities  = i.getOtherLiabilities();
		Double totalLiabilities  = i.getTotalLiabilities();
		Double deferredLongTermLiabilityCharges  = i.getDeferredLongTermLiabilityCharges();
		Double minorityInterest  = i.getMinorityInterest();
		Double negativeGoodwill  = i.getNegativeGoodwill();
		Double miscStocksOptionsWarrants  = i.getMiscStocksOptionsWarrants();
		Double redeemablePreferredStocks  = i.getRedeemablePreferredStocks() == null? 0.0 : i.getRedeemablePreferredStocks();
		Double commonStock  = i.getCommonStock();
		Double preferredStock  = i.getPreferredStock();
		Double retainedEarnings  = i.getRetainedEarnings();
		Double treasuryStock  = i.getTreasuryStock();
		Double capitalSurplus  = i.getCapitalSurplus();
		Double otherstockholdersEquity  = i.getOtherStockholderEquity();
		
		BalanceSheet o = new BalanceSheet(symbol, cashAndEquivalent, shortTermInvestements, netReceivables, inventory,
				otherCurrentAssets, totalCurrentAssets, longTermInvestments, propertyPlantAndEquivalent, goodwill,
				intangibleAssets, accumulatedAmortization, otherAssets, totalAssets, deferredLongTermAssetCharges,
				accountPayable, shortCurrentLongTermDebt, otherCurrentLiabilities, totalCurrentLiabilities,
				longTermDebt, otherLiabilities, totalLiabilities, deferredLongTermLiabilityCharges, minorityInterest,
				negativeGoodwill, miscStocksOptionsWarrants, redeemablePreferredStocks, commonStock, preferredStock,
				retainedEarnings, treasuryStock, capitalSurplus, otherstockholdersEquity);
		return o;
	}
	public BalanceSheetDto convertYahooToDbBalanceSheet(BalanceSheet balanceSheet, Date d, StatementPeriod period) {
		
		BalanceSheetDto ret = new BalanceSheetDto();
		
		ret.setEndDate(d);
		ret.setStatementPeriod(period);
		ret.setTicker(balanceSheet.getSymbol());
		ret.setAccountPayable(balanceSheet.getAccountPayable());
		ret.setAccumulatedAmortization(balanceSheet.getAccumulatedAmortization());
		ret.setCashAndEquivalent(balanceSheet.getCashAndEquivalent());
		ret.setShortTermInvestments(balanceSheet.getShortTermInvestments());
		ret.setNetReceivables(balanceSheet.getNetReceivables());
		ret.setInventory(balanceSheet.getInventory());
		ret.setOtherCurrentAssets(balanceSheet.getOtherCurrentAssets());
		ret.setLongTermInvestments(balanceSheet.getLongTermInvestments());
		ret.setPropertyPlantAndEquivalent(balanceSheet.getPropertyPlantAndEquivalent());
		ret.setGoodwill(balanceSheet.getGoodwill());
		ret.setIntangibleAssets(balanceSheet.getIntangibleAssets());
		ret.setAccumulatedAmortization(balanceSheet.getAccumulatedAmortization());
		ret.setOtherAssets(balanceSheet.getOtherAssets());
		ret.setDeferredLongTermAssetCharges(balanceSheet.getDeferredLongTermAssetCharges());
		ret.setTotalCurrentAssetsCalculated(balanceSheet.getTotalCurrentAssetsCalculated());
		ret.setTotalCurrentAssets(balanceSheet.getTotalCurrentAssets());
		ret.setTotalAssets(balanceSheet.getTotalAssets());
		ret.setLongTermDebt(balanceSheet.getLongTermDebt());
		ret.setOtherLiabilities(balanceSheet.getOtherLiabilities());
		ret.setDeferredLongTermLiabilityCharges(balanceSheet.getDeferredLongTermLiabilityCharges());
		ret.setMinorityInterest(balanceSheet.getMinorityInterest());
		ret.setNegativeGoodwill(balanceSheet.getNegativeGoodwill());
		ret.setAccountPayable(balanceSheet.getAccountPayable());
		ret.setShortCurrentLongTermDebt(balanceSheet.getShortCurrentLongTermDebt());
		ret.setOtherCurrentLiabilities(balanceSheet.getOtherCurrentLiabilities());
		ret.setTotalCurrentLiabilitiesCalculated(balanceSheet.getTotalCurrentLiabilitiesCalculated());
		ret.setTotalLiabilities(balanceSheet.getTotalLiabilities());
		ret.setTotalCurrentLiabilities(balanceSheet.getTotalCurrentLiabilities());
		ret.setMiscStocksOptionsWarrants(balanceSheet.getMiscStocksOptionsWarrants());
		ret.setCommonStock(balanceSheet.getCommonStock());
		ret.setPreferredStock(balanceSheet.getPreferredStock());
		ret.setRetainedEarnings(balanceSheet.getRetainedEarnings());
		ret.setTreasuryStock(balanceSheet.getTreasuryStock());
		ret.setCapitalSurplus(balanceSheet.getCapitalSurplus());
		ret.setOtherStockholderEquity(balanceSheet.getOtherStockholdersEquity());
		 
		return ret;
		
	}
	public FinancialData getFinancialData(String ticker, List<BalanceSheetDto> balanceSheetsInDb) {
		SortedMap<Date, BalanceSheet> yearlyBalanceSheet = convertToSortedMap(balanceSheetsInDb,StatementPeriod.ANNUAL);
		SortedMap<Date, BalanceSheet> quaterlyBalanceSheet = convertToSortedMap(balanceSheetsInDb,StatementPeriod.QUARTERLY);
		
		FinancialData ret = new FinancialData(ticker, yearlyBalanceSheet, quaterlyBalanceSheet, 
				/* annualCashflowStatement */ null, 
				/* quarterlyCashflowStatement */ null, 
				/* annualIncomeStatement */ null, 
				/* quarterlyIncomeStatement */ null);
		
		return ret;


	}

	private SortedMap<Date, BalanceSheet> convertToSortedMap(List<BalanceSheetDto> balanceSheetsInDb,StatementPeriod p) {
		Map<Date, BalanceSheet> statements = balanceSheetsInDb.stream()
			.filter(b -> b.getStatementPeriod().equals(p))
			.collect(Collectors.toMap(BalanceSheetDto::getEndDate, b-> convertDbToYahooBalanceSheet(b)));
		SortedMap<Date,BalanceSheet> sortedStatements =new TreeMap<>(statements);
		return sortedStatements;
	}
	
}
