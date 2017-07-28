package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
public class YahooFinancialJsonDataModel {
	@Data 
	@NoArgsConstructor 
	public static class QuoteSummary{
		@Data 
		public static class Result{
			public Result() {}
			@Data 
			@NoArgsConstructor 
			public static class YahooVal{
				Double raw;
				String fmt;
			}
			
			@Data  
			@EqualsAndHashCode(callSuper=true) 
			@NoArgsConstructor 
			public static class YahooLongVal extends YahooVal{
				String longFmt;
			}
			
			@Data 
			@NoArgsConstructor 
			public static class CashflowStatement{
				Long maxAge;
				YahooVal endDate;
				YahooLongVal netIncome;
				YahooLongVal depreciation;
				YahooLongVal changeToNetincome;
				YahooLongVal changeToAccountReceivables;
				YahooLongVal changeToLiabilities;
				YahooLongVal changeToInventory;
				YahooLongVal changeToOperatingActivities;
				YahooLongVal totalCashFromOperatingActivities;
				YahooLongVal capitalExpenditures;
				YahooLongVal investments;
				YahooLongVal otherCashflowsFromInvestingActivities;
				YahooLongVal totalCashflowsFromInvestingActivities;
				YahooLongVal salePurchaseOfStock;
				YahooLongVal netBorrowings;
				YahooLongVal otherCashflowsFromFinancingActivities;
				YahooLongVal totalCashFromFinancingActivities;
				YahooLongVal effectOfExchangeRate;
				YahooLongVal changeInCash;
				YahooLongVal dividendsPaid;
			}
			
			@Data 
			@NoArgsConstructor 
			public static class CashflowStatementHistory{
				List<CashflowStatement>cashflowStatements;
				Long maxAge;
				
			}

			@Data 
			@NoArgsConstructor 
			public static class IncomeStatementHistory {
				@Data 
				@NoArgsConstructor 
				public static class IncomeStatement {
					Long maxAge;
					YahooVal endDate;
					YahooLongVal totalRevenue;
					YahooLongVal costOfRevenue;
					YahooLongVal grossProfit;
					YahooLongVal researchDevelopment;
					YahooLongVal sellingGeneralAdministrative;
					YahooLongVal nonRecurring;
					YahooLongVal otherOperatingExpenses;
					YahooLongVal totalOperatingExpenses;
					YahooLongVal operatingIncome;
					YahooLongVal totalOtherIncomeExpenseNet;
					YahooLongVal ebit;
					YahooLongVal interestExpense;
					YahooLongVal incomeBeforeTax;
					YahooLongVal incomeTaxExpense;
					YahooLongVal minorityInterest;
					YahooLongVal netIncomeFromContinuingOps;
					YahooLongVal discontinuedOperations;
					YahooLongVal extraordinaryItems;
					YahooLongVal effectOfAccountingCharges;
					YahooLongVal otherItems;
					YahooLongVal netIncome;
					YahooLongVal preferredStockAndOtherAdjustments;
					YahooLongVal netIncomeApplicableToCommonShares;
				}

				List<IncomeStatement> incomeStatementHistory;
				Long maxAge;
			}
			
			@Data 
			@NoArgsConstructor 
			public static class Earnings{
				@Data 
				@NoArgsConstructor 
				public static class EarningsChart {
					@Data 
					@NoArgsConstructor 
					public static class Earning{
						String date;
						YahooVal actual;
						YahooVal estimate;
					}
					List<Earning>quarterly;
					YahooVal currentQuarterEstimate;
					String currentQuarterEstimateDate;
					Integer currentQuarterEstimateYear;
				}
				@Data 
				@NoArgsConstructor 
				public static class FinancialsChart{
					@Data 
					@NoArgsConstructor 
					public static class Financial{
						String date;
						YahooLongVal revenue;
						YahooLongVal earnings;
					}
					List<Financial>quarterly;
					List<Financial>yearly;
				}
				Long maxAge;
				EarningsChart earningsChart;
				FinancialsChart financialsChart;
				String financialCurrency;
			}
			
			@Data 
			@NoArgsConstructor 
			public static class BalanceSheetHistory{
				@Data 
				@NoArgsConstructor 
				public static class BalanceSheetData
				{
					Long maxAge;
					YahooVal endDate;
					YahooLongVal cash;
					YahooLongVal shortTermInvestments;
					YahooLongVal netReceivables;
					YahooLongVal inventory;
					YahooLongVal otherCurrentAssets;
					YahooLongVal totalCurrentAssets;
					YahooLongVal longTermInvestments;					
					YahooLongVal propertyPlantEquipment;
					YahooLongVal goodWill;
					YahooLongVal intangibleAssets;
					YahooLongVal accumulatedAmortization;
					YahooLongVal otherAssets;
					YahooLongVal deferredLongTermAssetCharges;
					YahooLongVal totalAssets;
					YahooLongVal accountsPayable;
					YahooLongVal shortLongTermDebt;
					YahooLongVal otherCurrentLiab;
					YahooLongVal longTermDebt;
					YahooLongVal otherLiab;
					YahooLongVal minorityInterest;
					YahooLongVal negativeGoodwill;
//					YahooLongVal miscStocksOptionsWarrants;
					YahooLongVal redeemablePreferredStock;
					YahooLongVal totalCurrentLiabilities;
					YahooLongVal totalLiab;
					YahooLongVal stockOptionWarrants;
					YahooLongVal deferredLongTermLiab;
					YahooLongVal commonStock;
					YahooLongVal preferredStock;
					YahooLongVal retainedEarnings;
					YahooLongVal treasuryStock;
					YahooLongVal capitalSurplus;
					YahooLongVal otherStockholderEquity;
					YahooLongVal totalStockholderEquity;
					YahooLongVal netTangibleAssets;
					
				}
				List<BalanceSheetData>balanceSheetStatements;
				Long maxAge;
			}
			
			
			CashflowStatementHistory cashflowStatementHistoryQuarterly;
			CashflowStatementHistory cashflowStatementHistory;
			IncomeStatementHistory incomeStatementHistoryQuarterly;
			IncomeStatementHistory incomeStatementHistory;
			Earnings earnings;
			BalanceSheetHistory balanceSheetHistory;
			BalanceSheetHistory balanceSheetHistoryQuarterly;
		}
		
		List<Result>result;
		String error;
	}
	
	
	private QuoteSummary quoteSummary;
	
	protected boolean isError = false;
	
	public static ErrorYahooFinancialData errorData(String ticker, Date timestamp, String message) {
		return new ErrorYahooFinancialData( ticker,  timestamp,  message);
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class ErrorYahooFinancialData extends YahooFinancialJsonDataModel{
		private final String ticker;
		private final Date timestamp;
		private final String message;
		public ErrorYahooFinancialData(String ticker, Date timestamp, String message) {
			super();
			isError = true;
			
			this.ticker = ticker;
			this.timestamp = timestamp;
			this.message = message;
		}
		
	}
}
