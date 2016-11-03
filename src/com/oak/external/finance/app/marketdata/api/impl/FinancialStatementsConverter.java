package com.oak.external.finance.app.marketdata.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.BalanceSheet;
import com.oak.api.finance.model.CashFlowStatement;
import com.oak.api.finance.model.FinancialData;
import com.oak.api.finance.model.dto.AbstractFinancialStatement;
import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.CashFlowStatementDto;
import com.oak.api.finance.model.dto.IncomeStatementDto;
import com.oak.api.finance.model.dto.StatementPeriod;
import com.oak.external.finance.model.economy.IncomeStatement;

public class FinancialStatementsConverter {

	private final Logger logger;
	public FinancialStatementsConverter(org.apache.logging.log4j.Logger logger) {
		this.logger = logger;
	}

	private interface Converter<T,DT>{
		DT convert (T t);
	}
	private class BalanceSheetConvert implements Converter<BalanceSheetDto,BalanceSheet>{

		@Override
		public BalanceSheet convert(BalanceSheetDto i) {
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
		
	}
	private class IncomeStatementConvert implements Converter<IncomeStatementDto,IncomeStatement>{

		@Override
		public IncomeStatement convert(IncomeStatementDto t) {
			String ticker = t.getTicker();
			Double researchDevelopment = t.getResearchDevelopment();
			Double sellingGeneralAndAdministrative = t.getSellingGeneralAndAdministrative();
			Double nonRecurring = t.getNonRecurring();
			Double otherOperatingExpenses = t.getOtherOperatingExpenses();
			Double totalOperatingExpenses = t.getTotalOperatingExpenses();
			Double totalOtherIncomeExpensesNet = t.getTotalOtherIncomeExpensesNet();
			Double earningsBeforeInterestAndTaxes = t.getEarningsBeforeInterestAndTaxes();
			Double interestExpense = t.getInterestExpense();
			Double incomeBeforeTax = t.getIncomeBeforeTax();
			Double incomeTaxExpense = t.getIncomeTaxExpense();
			Double minorityInterest = t.getMinorityInterest();
			Double netIncomeFromContinuingOps = t.getNetIncomeFromContinuingOps();
			Double discontinuedOperations = t.getDiscontinuedOperations();
			Double extraordinaryItems = t.getExtraordinaryItems();
			Double effectOfAccountingChanges = t.getEffectOfAccountingChanges();
			Double otherItems = t.getOtherItems();
			Double netIncome = t.getNetIncome();
			Double preferredStockAndOtherAdjustments = t.getPreferredStockAndOtherAdjustments();
			Double netIncomeApplicableToCommonShares = t.getNetIncomeApplicableToCommonShares();

			return new IncomeStatement(ticker, researchDevelopment, sellingGeneralAndAdministrative, nonRecurring,
					otherOperatingExpenses, totalOperatingExpenses, totalOtherIncomeExpensesNet,
					earningsBeforeInterestAndTaxes, interestExpense, incomeBeforeTax, incomeTaxExpense,
					minorityInterest, netIncomeFromContinuingOps, discontinuedOperations, extraordinaryItems,
					effectOfAccountingChanges, otherItems, netIncome, preferredStockAndOtherAdjustments,
					netIncomeApplicableToCommonShares);
		}
	}
	private class CashflowStatementConvert implements Converter<CashFlowStatementDto,CashFlowStatement>{
		
		@Override
		public CashFlowStatement convert(CashFlowStatementDto t) {
			String ticker = t.getTicker();
			Double depreciation = t.getDepreciation();
			Double adjustmentsToNetIncome = t.getAdjustmentsToNetIncome();
			Double changesInAccountsReceivables = t.getChangesInAccountsReceivables();
			Double changesInLiabilities = t.getChangesInLiabilities();
			Double changesInInventories = t.getChangesInInventories();
			Double changesInOtherOperatingActivities = t.getChangesInOtherOperatingActivities();
			Double totalCashFlowFromOperatingActivities = t.getTotalCashFlowFromOperatingActivities();
			Double capitalExpenditures = t.getCapitalExpenditures();
			Double investments = t.getInvestments();
			Double otherCashFlowsFromInvestingActivities = t.getOtherCashFlowsFromInvestingActivities();
			Double totalCashFlowsFromInvestingActivities = t.getTotalCashFlowsFromInvestingActivities();
			Double dividendsPaid = t.getDividendsPaid();
			Double salePurchaseOfStock = t.getSalePurchaseOfStock();
			Double netBorrowings = t.getNetBorrowings();
			Double otherCashFlowsFromFinancingActivities = t.getOtherCashFlowsFromFinancingActivities();
			Double totalCashFlowsFromFinancingActivities = t.getTotalCashFlowsFromFinancingActivities();
			Double netIncome = t.getNetIncome();
			Double effectOfExchangeRateChanges = t.getEffectOfExchangeRateChanges();
			Double changeInCashAndCashEquivalents = t.getChangeInCashAndCashEquivalents();

			return new CashFlowStatement(ticker, depreciation, adjustmentsToNetIncome, changesInAccountsReceivables,
					changesInLiabilities, changesInInventories, changesInOtherOperatingActivities,
					totalCashFlowFromOperatingActivities, capitalExpenditures, investments,
					otherCashFlowsFromInvestingActivities, totalCashFlowsFromInvestingActivities, dividendsPaid,
					salePurchaseOfStock, netBorrowings, otherCashFlowsFromFinancingActivities,
					totalCashFlowsFromFinancingActivities, netIncome, effectOfExchangeRateChanges,
					changeInCashAndCashEquivalents);
		}
	}
	
	public IncomeStatementDto convertYahooToDbIncomeStatement(IncomeStatement incomeStatement, Date d, StatementPeriod period) {
		IncomeStatementDto ret = new IncomeStatementDto();
		commontSetup(incomeStatement.getSymbol(), d, period, ret);
		ret.setResearchDevelopment(incomeStatement.getResearchDevelopment());
		ret.setSellingGeneralAndAdministrative(incomeStatement.getSellingGeneralAndAdministrative());
		ret.setNonRecurring(incomeStatement.getNonRecurring());
		ret.setOtherOperatingExpenses(incomeStatement.getOtherOperatingExpenses());
		ret.setTotalOperatingExpenses(incomeStatement.getTotalOperatingExpenses());
		ret.setTotalOtherIncomeExpensesNet(incomeStatement.getTotalOtherIncomeExpensesNet());
		ret.setEarningsBeforeInterestAndTaxes(incomeStatement.getEarningsBeforeInterestAndTaxes());
		ret.setInterestExpense(incomeStatement.getInterestExpense());
		ret.setIncomeBeforeTax(incomeStatement.getIncomeBeforeTax());
		ret.setIncomeTaxExpense(incomeStatement.getIncomeTaxExpense());
		ret.setMinorityInterest(incomeStatement.getMinorityInterest());
		ret.setNetIncomeFromContinuingOps(incomeStatement.getNetIncomeFromContinuingOps());
		ret.setDiscontinuedOperations(incomeStatement.getDiscontinuedOperations());
		ret.setExtraordinaryItems(incomeStatement.getExtraordinaryItems());
		ret.setEffectOfAccountingChanges(incomeStatement.getEffectOfAccountingChanges());
		ret.setOtherItems(incomeStatement.getOtherItems());
		ret.setNetIncome(incomeStatement.getNetIncome());
		ret.setPreferredStockAndOtherAdjustments(incomeStatement.getPreferredStockAndOtherAdjustments());
		ret.setNetIncomeApplicableToCommonShares(incomeStatement.getNetIncomeApplicableToCommonShares());

		return ret;
	}
	
	public CashFlowStatementDto convertYahooToDbCashFlowStatement(CashFlowStatement cashFlowStatement, Date d, StatementPeriod period) {
		CashFlowStatementDto ret = new CashFlowStatementDto();
		commontSetup(cashFlowStatement.getSymbol(), d, period, ret);
		ret.setDepreciation(cashFlowStatement.getDepreciation());
		ret.setAdjustmentsToNetIncome(cashFlowStatement.getAdjustmentsToNetIncome());
		ret.setChangesInAccountsReceivables(cashFlowStatement.getChangesInAccountsReceivables());
		ret.setChangesInLiabilities(cashFlowStatement.getChangesInLiabilities());
		ret.setChangesInInventories(cashFlowStatement.getChangesInInventories());
		ret.setChangesInOtherOperatingActivities(cashFlowStatement.getChangesInOtherOperatingActivities());
		ret.setTotalCashFlowFromOperatingActivities(cashFlowStatement.getTotalCashFlowFromOperatingActivities());
		ret.setCapitalExpenditures(cashFlowStatement.getCapitalExpenditures());
		ret.setInvestments(cashFlowStatement.getInvestments());
		ret.setOtherCashFlowsFromInvestingActivities(cashFlowStatement.getOtherCashFlowsFromInvestingActivities());
		ret.setTotalCashFlowsFromInvestingActivities(cashFlowStatement.getTotalCashFlowsFromInvestingActivities());
		ret.setDividendsPaid(cashFlowStatement.getDividendsPaid());
		ret.setSalePurchaseOfStock(cashFlowStatement.getSalePurchaseOfStock());
		ret.setNetBorrowings(cashFlowStatement.getNetBorrowings());
		ret.setOtherCashFlowsFromFinancingActivities(cashFlowStatement.getOtherCashFlowsFromFinancingActivities());
		ret.setTotalCashFlowsFromFinancingActivities(cashFlowStatement.getTotalCashFlowsFromFinancingActivities());
		ret.setNetIncome(cashFlowStatement.getNetIncome());
		ret.setEffectOfExchangeRateChanges(cashFlowStatement.getEffectOfExchangeRateChanges());
		ret.setChangeInCashAndCashEquivalents(cashFlowStatement.getChangeInCashAndCashEquivalents());
    
		return ret;
	}
	
	public BalanceSheetDto convertYahooToDbBalanceSheet(BalanceSheet balanceSheet, Date d, StatementPeriod period) {
		
		BalanceSheetDto ret = new BalanceSheetDto();
		
		commontSetup(balanceSheet.getSymbol(), d, period, ret);
		
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

	private void commontSetup(String ticker, Date d, StatementPeriod period, AbstractFinancialStatement ret) {
		ret.setEndDate(d);
		ret.setStatementPeriod(period);
		ret.setTicker(ticker);
	}
	public FinancialData getFinancialData(String ticker, List<BalanceSheetDto> balanceSheetsInDb, List<CashFlowStatementDto> cashflowStatementsInDb, List<IncomeStatementDto> incomeStatementsInDb) {
		SortedMap<Date, BalanceSheet> yearlyBalanceSheet = convertToSortedMap(balanceSheetsInDb,StatementPeriod.ANNUAL,new BalanceSheetConvert());
		SortedMap<Date, BalanceSheet> quaterlyBalanceSheet = convertToSortedMap(balanceSheetsInDb,StatementPeriod.QUARTERLY,new BalanceSheetConvert());
		SortedMap<Date, CashFlowStatement> yearlyCashFlowStatement = convertToSortedMap(cashflowStatementsInDb,StatementPeriod.ANNUAL,new CashflowStatementConvert());
		SortedMap<Date, CashFlowStatement> quaterlyCashFlowStatement = convertToSortedMap(cashflowStatementsInDb,StatementPeriod.QUARTERLY,new CashflowStatementConvert());
		SortedMap<Date, IncomeStatement> yearlyIncomeStatement = convertToSortedMap(incomeStatementsInDb,StatementPeriod.ANNUAL,new IncomeStatementConvert());
		SortedMap<Date, IncomeStatement> quaterlyIncomeStatement = convertToSortedMap(incomeStatementsInDb,StatementPeriod.QUARTERLY,new IncomeStatementConvert());
		
		FinancialData ret = new FinancialData(ticker, yearlyBalanceSheet, quaterlyBalanceSheet, 
				/* annualCashflowStatement */ yearlyCashFlowStatement, 
				/* quarterlyCashflowStatement */ quaterlyCashFlowStatement, 
				/* annualIncomeStatement */ yearlyIncomeStatement, 
				/* quarterlyIncomeStatement */ quaterlyIncomeStatement);
		
		return ret;


	}

	private <T extends AbstractFinancialStatement,DT> SortedMap<Date, DT> convertToSortedMap(List<T> financialStatementInDb,StatementPeriod p, Converter<T,DT>c) {
		// the section below deal with a bug that was fixed where items were found to be duplicated
		////////////////////////////////////////////////////////////////////////
		Map<Date, List<T>> statementsByDate = financialStatementInDb.stream()
				.filter(s->s.getStatementPeriod().equals(p))
				.collect(Collectors.groupingBy(T::getEndDate));
		Set<Date> datesWithMultipleStatments = statementsByDate.keySet().stream()
		.filter(d -> statementsByDate.get(d).size()>1)
		.collect(Collectors.toSet());
		int i = 0;
		for(Date d: datesWithMultipleStatments) {
			logger.warn("found duplicates for ["+financialStatementInDb.iterator().next().getTicker()+"] " + p + " on date: "+d+ ", ids: " +
		statementsByDate.get(d).stream().map(s->s.getId()).collect(Collectors.toSet()));
			for(T statement:statementsByDate.get(d)) {
				if(i++ <= 0) {
					financialStatementInDb.remove(statement);
				}
			}
		}
		//////////////////end /////////////////////////////
		Map<Date, DT> statements = financialStatementInDb.stream()
			.filter(b -> b.getStatementPeriod().equals(p))
			.collect(Collectors.toMap(T::getEndDate, b-> c.convert(b)));
		SortedMap<Date,DT> sortedStatements =new TreeMap<>(statements);
		return sortedStatements;
	}
	
}
