package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.oak.api.finance.model.BalanceSheet;
import com.oak.api.finance.model.CashFlowStatement;
import com.oak.api.finance.model.FinancialData;
import com.oak.external.finance.app.marketdata.api.FinancialDataDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooFinancialJsonDataModel.QuoteSummary.Result.BalanceSheetHistory.BalanceSheetData;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooFinancialJsonDataModel.QuoteSummary.Result.CashflowStatement;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooFinancialJsonDataModel.QuoteSummary.Result.IncomeStatementHistory.IncomeStatement;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooFinancialJsonDataModel.QuoteSummary.Result.YahooVal;

public class YahooJsonFinancialDataDao implements FinancialDataDao {

	private static String root = "https://query1.finance.yahoo.com/v10/finance/quoteSummary/";
	private static String ext = "?formatted=true&modules=incomeStatementHistory%2CcashflowStatementHistory%2CbalanceSheetHistory%2CincomeStatementHistoryQuarterly%2CcashflowStatementHistoryQuarterly%2CbalanceSheetHistoryQuarterly%2Cearnings";
	private final Logger log;
	private final SimpleDateFormat f;//2014-06-30
	public YahooJsonFinancialDataDao(Logger log ) {
		f = new SimpleDateFormat("yyyy-MM-dd");//2014-06-30
		this.log = log;
	}

	@Override
	public FinancialData getFinancialDataForSymbol(String symbol, String exchange) {
		String url = root + symbol + ext;
		String json = null;
		FinancialData ret = new FinancialData(symbol, Maps.newTreeMap(), Maps.newTreeMap(), 
				/* annualCashflowStatement */ null, 
				/* quarterlyCashflowStatement */ null, 
				/* annualIncomeStatement */ null, 
				/* quarterlyIncomeStatement */ null);;
		YahooVal parsingvalue = null;
		try {
			YahooFinancialJsonDataModel financial = downloadFinancialData(url);
			List<BalanceSheetData> balanceSheetsYearly = financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.filter(r -> r.getBalanceSheetHistory() != null)
					.flatMap(r -> r.getBalanceSheetHistory()
							.getBalanceSheetStatements()
							.stream())
					.collect(Collectors.toList());
			List<BalanceSheetData> balanceSheetsQuaterly = financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.filter(r -> r.getBalanceSheetHistoryQuarterly() != null)
					.flatMap(r -> r.getBalanceSheetHistoryQuarterly()
							.getBalanceSheetStatements()
							.stream())
					.collect(Collectors.toList());
			List<CashflowStatement>cashflowStatementYearly = financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.filter(r -> r.getCashflowStatementHistory() != null)
					.flatMap(r -> r.getCashflowStatementHistory()
							.getCashflowStatements()
							.stream())
					.collect(Collectors.toList());
			List<CashflowStatement>cashflowStatementQuaterly= financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.filter(r -> r.getCashflowStatementHistoryQuarterly() != null)
					.flatMap(r -> r.getCashflowStatementHistoryQuarterly()
							.getCashflowStatements()
							.stream())
					.collect(Collectors.toList());
			List<IncomeStatement>incomeStatementYearly = financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.filter(r -> r.getIncomeStatementHistory() != null)
					.flatMap(r -> r.getIncomeStatementHistory()
							.getIncomeStatementHistory()
							.stream())
					.collect(Collectors.toList());
			List<IncomeStatement>incomeStatementQuaterly = financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.filter(r -> r.getIncomeStatementHistoryQuarterly() != null)
					.flatMap(r -> r.getIncomeStatementHistoryQuarterly()
							.getIncomeStatementHistory()
							.stream())
					.collect(Collectors.toList());

			ImmutableMap<YahooVal, BalanceSheetData> balanceSheetPerDateYearly = Maps.uniqueIndex(balanceSheetsYearly, BalanceSheetData::getEndDate);		
			ImmutableMap<YahooVal, BalanceSheetData> balanceSheetPerDateQuaterly = Maps.uniqueIndex(balanceSheetsQuaterly, BalanceSheetData::getEndDate);		
			ImmutableMap<YahooVal, CashflowStatement> cashflowStatementPerDateYearly = Maps.uniqueIndex(cashflowStatementYearly, CashflowStatement::getEndDate);		
			ImmutableMap<YahooVal, CashflowStatement> cashflowStatementPerDateQuaterly = Maps.uniqueIndex(cashflowStatementQuaterly, CashflowStatement::getEndDate);		
			ImmutableMap<YahooVal, IncomeStatement> incomeStatementPerDateYearly = Maps.uniqueIndex(incomeStatementYearly , IncomeStatement::getEndDate);		
			ImmutableMap<YahooVal, IncomeStatement> incomeStatementPerDateQuaterly = Maps.uniqueIndex(incomeStatementQuaterly, IncomeStatement::getEndDate);		
			
			SortedMap<Date, BalanceSheet> yearlyBalanceSheet = extractBalanceSheetFromYahooRawData(symbol,
					balanceSheetPerDateYearly);
			SortedMap<Date, BalanceSheet> quaterlyBalanceSheet = extractBalanceSheetFromYahooRawData(symbol,
					balanceSheetPerDateQuaterly);
			SortedMap<Date, CashFlowStatement> yearlyCashflowStatement = 
					extractCashflowStatementFromYahooRawData(symbol,cashflowStatementPerDateYearly);
			SortedMap<Date, CashFlowStatement> quarterlyCashflowStatement = 
					extractCashflowStatementFromYahooRawData(symbol,cashflowStatementPerDateQuaterly);
			SortedMap<Date, com.oak.external.finance.model.economy.IncomeStatement> yearlyIncomeStatement = 
					extractIncomeStatementFromYahooRawData(symbol,incomeStatementPerDateYearly);
			SortedMap<Date, com.oak.external.finance.model.economy.IncomeStatement> quaterlyIncomeStatement = 
					extractIncomeStatementFromYahooRawData(symbol,incomeStatementPerDateQuaterly);
			
			ret = new FinancialData(symbol, yearlyBalanceSheet, quaterlyBalanceSheet, 
					/* annualCashflowStatement */ yearlyCashflowStatement, 
					/* quarterlyCashflowStatement */ quarterlyCashflowStatement, 
					/* annualIncomeStatement */ yearlyIncomeStatement, 
					/* quarterlyIncomeStatement */ quaterlyIncomeStatement);
			
		} catch (IOException e1) {
			String msg = "Can't " + (json == null ? "get " + url : "parse " + json);
			log.error(msg, e1);
		} catch (ParseException e) {
			String msg = "Symbol: " + symbol + ", Failed while parsing: " + parsingvalue;
			log.error(msg,e);
		}catch(Throwable t) {
			String msg = "Unexpected error while getting Symbol: " + symbol + ", Failed while parsing: " + parsingvalue;
			log.error(msg,t);
		}

		return ret;
	}
	private SortedMap<Date,com.oak.external.finance.model.economy.IncomeStatement> extractIncomeStatementFromYahooRawData(String symbol,
			ImmutableMap<YahooVal, IncomeStatement> incomeStatementRawData) throws ParseException {
		SortedMap<Date,com.oak.external.finance.model.economy.IncomeStatement> ret = new TreeMap<>();
	
		for(YahooVal dateVal : incomeStatementRawData.keySet()) {
			IncomeStatement isd = incomeStatementRawData.get(dateVal);
			Date date = f.parse(dateVal.getFmt());
			com.oak.external.finance.model.economy.IncomeStatement is = new com.oak.external.finance.model.economy.IncomeStatement(
					symbol,
			getDoubleFromYahooVal(isd.getResearchDevelopment()),
			getDoubleFromYahooVal(isd.getSellingGeneralAdministrative()),
			getDoubleFromYahooVal(isd.getNonRecurring()),
			getDoubleFromYahooVal(isd.getOtherOperatingExpenses()),
			getDoubleFromYahooVal(isd.getTotalOperatingExpenses()),
			getDoubleFromYahooVal(isd.getTotalOtherIncomeExpenseNet()),
			getDoubleFromYahooVal(isd.getEbit()),
			getDoubleFromYahooVal(isd.getInterestExpense()),
			getDoubleFromYahooVal(isd.getIncomeBeforeTax()),
			getDoubleFromYahooVal(isd.getIncomeTaxExpense()),
			getDoubleFromYahooVal(isd.getMinorityInterest()),
			getDoubleFromYahooVal(isd.getNetIncomeFromContinuingOps()),
			getDoubleFromYahooVal(isd.getDiscontinuedOperations()),
			getDoubleFromYahooVal(isd.getExtraordinaryItems()),
			getDoubleFromYahooVal(isd.getEffectOfAccountingCharges()),
			getDoubleFromYahooVal(isd.getOtherItems()),
			getDoubleFromYahooVal(isd.getNetIncome()),
			getDoubleFromYahooVal(isd.getPreferredStockAndOtherAdjustments()), // TODO CAN'T FIND getPreferredstockandotheradjustments()),
			getDoubleFromYahooVal(isd.getNetIncomeApplicableToCommonShares())
			);
			ret.put(date,is);
		}
		return ret;
	}

	private SortedMap<Date, BalanceSheet> extractBalanceSheetFromYahooRawData(String symbol,
			ImmutableMap<YahooVal, BalanceSheetData> balanceSheetRawData) throws ParseException {
		SortedMap<Date,BalanceSheet>yearlyBalanceSheet = new TreeMap<>();
		for(YahooVal dateVal : balanceSheetRawData.keySet()) {
			BalanceSheetData bsd = balanceSheetRawData.get(dateVal);
			Date date = f.parse(dateVal.getFmt());
			BalanceSheet bs = new BalanceSheet(symbol, 
					getDoubleFromYahooVal(bsd.getCash()), // cashAndEquivalent, 
					getDoubleFromYahooVal(bsd.getShortTermInvestments()), // shortTermInvestements, 
					getDoubleFromYahooVal(bsd.getNetReceivables()), // netReceivables, 
					getDoubleFromYahooVal(bsd.getInventory()), // inventory, 
					getDoubleFromYahooVal(bsd.getOtherCurrentAssets()), // otherCurrentAssets, 
					getDoubleFromYahooVal(bsd.getTotalCurrentAssets()), // totalCurrentAssets, 
					getDoubleFromYahooVal(bsd.getLongTermInvestments()),// longTermInvestments, 
					getDoubleFromYahooVal(bsd.getPropertyPlantEquipment()), // propertyPlantAndEquivalent, 
					getDoubleFromYahooVal(bsd.getGoodWill()), // goodwill, 
					getDoubleFromYahooVal(bsd.getIntangibleAssets()), // intangibleAssets, 
					getDoubleFromYahooVal(bsd.getAccumulatedAmortization()), // accumulatedAmortization, 
					getDoubleFromYahooVal(bsd.getOtherAssets()), // otherAssets, 
					getDoubleFromYahooVal(bsd.getTotalAssets()), // totalAssets, 
					getDoubleFromYahooVal(bsd.getDeferredLongTermAssetCharges()), // deferredLongTermAssetCharges, 
					getDoubleFromYahooVal(bsd.getAccountsPayable()), // accountPayable, 
					getDoubleFromYahooVal(bsd.getShortLongTermDebt()), // shortCurrentLongTermDebt, 
					getDoubleFromYahooVal(bsd.getAccountsPayable()), // accountsPayable , 
					getDoubleFromYahooVal(bsd.getTotalCurrentLiabilities()), // totalCurrentLiabilities, 
					getDoubleFromYahooVal(bsd.getLongTermDebt()), // longTermDebt, 
					getDoubleFromYahooVal(bsd.getOtherLiab()), //  otherLiabilities, 
					getDoubleFromYahooVal(bsd.getTotalLiab()), // totalLiabilities, 
					getDoubleFromYahooVal(bsd.getDeferredLongTermLiab()), //  deferredLongTermLiabilityCharges, 
					getDoubleFromYahooVal(bsd.getMinorityInterest()), // minorityInterest,
					getDoubleFromYahooVal(bsd.getNegativeGoodwill()),  // negativeGoodwill, 
					getDoubleFromYahooVal(bsd.getStockOptionWarrants()), // miscStocksOptionsWarrants, 
					getDoubleFromYahooVal(bsd.getRedeemablePreferredStock()),  // redeemablePreferredStocks, 
					getDoubleFromYahooVal(bsd.getCommonStock()), // commonStock, 
					getDoubleFromYahooVal(bsd.getPreferredStock()), // preferredStock, 
					getDoubleFromYahooVal(bsd.getRetainedEarnings()), // retainedEarnings, 
					getDoubleFromYahooVal(bsd.getTreasuryStock()), // treasuryStock, 
					getDoubleFromYahooVal(bsd.getCapitalSurplus()), //  capitalSurplus, 
					getDoubleFromYahooVal(bsd.getOtherStockholderEquity()) // otherstockholdersEquity);
					);
			yearlyBalanceSheet.put(date, bs);
			
		}
		return yearlyBalanceSheet;
	}
	
	private SortedMap<Date,com.oak.api.finance.model.CashFlowStatement> extractCashflowStatementFromYahooRawData(
			String symbol,
			ImmutableMap<YahooVal, CashflowStatement> cashflowStatementRawData
			) throws ParseException{
		SortedMap<Date,com.oak.api.finance.model.CashFlowStatement>ret = new TreeMap<>();
		for(YahooVal dateVal : cashflowStatementRawData.keySet()) {
			CashflowStatement cfs = cashflowStatementRawData.get(dateVal);
			Date date = f.parse(dateVal.getFmt());
			
			com.oak.api.finance.model.CashFlowStatement cf = new com.oak.api.finance.model.CashFlowStatement(symbol,
					getDoubleFromYahooVal(cfs.getDepreciation()), // depreciation
					getDoubleFromYahooVal(cfs.getChangeToNetincome()), // adjustmentToNetIncome
					getDoubleFromYahooVal(cfs.getChangeToAccountReceivables()), // changeToAccountReceivable
					getDoubleFromYahooVal(cfs.getChangeToLiabilities()),
					getDoubleFromYahooVal(cfs.getChangeToInventory()),
					getDoubleFromYahooVal(cfs.getChangeToOperatingActivities()),
					getDoubleFromYahooVal(cfs.getTotalCashFromOperatingActivities()),
					getDoubleFromYahooVal(cfs.getCapitalExpenditures()), 
					getDoubleFromYahooVal(cfs.getInvestments()),
					getDoubleFromYahooVal(cfs.getOtherCashflowsFromInvestingActivities()),
					getDoubleFromYahooVal(cfs.getTotalCashflowsFromInvestingActivities()),
					getDoubleFromYahooVal(cfs.getDividendsPaid()), 
					getDoubleFromYahooVal(cfs.getSalePurchaseOfStock()),
					getDoubleFromYahooVal(cfs.getNetBorrowings()),
					getDoubleFromYahooVal(cfs.getOtherCashflowsFromFinancingActivities()),
					getDoubleFromYahooVal(cfs.getTotalCashFromFinancingActivities()),
					getDoubleFromYahooVal(cfs.getNetIncome()), 
					getDoubleFromYahooVal(cfs.getEffectOfExchangeRate()),
					getDoubleFromYahooVal(cfs.getChangeInCash()));
			ret.put(date,cf);
		}
	     
	    return ret;
	} 
	private Double getDoubleFromYahooVal(YahooVal yahooVal) {
		Double ret;
		if(yahooVal != null && yahooVal.getRaw() != null) {
			ret = yahooVal.getRaw();
		}else {
			ret = 0.0;
		}
		return ret;
	}
	/**
	 * 
	 * @param url = https://query1.finance.yahoo.com/v10/finance/quoteSummary/IQE.L?formatted=true&crumb=lXTeLpxBk2G&lang=en-US&region=US&modules=incomeStatementHistory%2CcashflowStatementHistory%2CbalanceSheetHistory%2CincomeStatementHistoryQuarterly%2CcashflowStatementHistoryQuarterly%2CbalanceSheetHistoryQuarterly%2Cearnings&corsDomain=finance.yahoo.com
	 * @returnurl = https://query1.finance.yahoo.com/v10/finance/quoteSummary/AAIF.L?formatted=true&modules=incomeStatementHistory%2CcashflowStatementHistory%2CbalanceSheetHistory%2CincomeStatementHistoryQuarterly%2CcashflowStatementHistoryQuarterly%2CbalanceSheetHistoryQuarterly%2Cearnings
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private YahooFinancialJsonDataModel downloadFinancialData(String url) throws IOException, JsonParseException, JsonMappingException {
		String json;
		json = Jsoup
				.connect(url)
			 	.header("Accept-Encoding", "gzip, deflate")
			    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
			    .maxBodySize(0)
			    .timeout(600000)
				.ignoreContentType(true).execute().body();
		
		if(json.indexOf("preferred" )>0) {
			System.out.println(json);
			log.info("\n"+json);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		YahooFinancialJsonDataModel financial = mapper.readValue(json, YahooFinancialJsonDataModel.class);
		return financial;
	}

}
