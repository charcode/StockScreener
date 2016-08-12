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
import com.oak.api.finance.model.FinancialData;
import com.oak.external.finance.app.marketdata.api.FinancialDataDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooFinancialJsonDataModel.QuoteSummary.Result.BalanceSheetHistory.BalanceSheetData;
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
		FinancialData ret = null;
		YahooVal parsingvalue = null;
		try {
			YahooFinancialJsonDataModel financial = downloadFinancialData(url);
			List<BalanceSheetData> balanceSheetsYearly = financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.flatMap(r -> r.getBalanceSheetHistory()
							.getBalanceSheetStatements()
							.stream())
					.collect(Collectors.toList());
			List<BalanceSheetData> balanceSheetsQuaterly = financial
					.getQuoteSummary()
					.getResult()
					.stream()
					.flatMap(r -> r.getBalanceSheetHistoryQuarterly()
							.getBalanceSheetStatements()
							.stream())
					.collect(Collectors.toList());
			
			ImmutableMap<YahooVal, BalanceSheetData> balanceSheetPerDateYearly = Maps.uniqueIndex(balanceSheetsYearly, BalanceSheetData::getEndDate);		
			ImmutableMap<YahooVal, BalanceSheetData> balanceSheetPerDateQuaterly = Maps.uniqueIndex(balanceSheetsQuaterly, BalanceSheetData::getEndDate);		
			
			SortedMap<Date, BalanceSheet> yearlyBalanceSheet = extractBalanceSheetFromYahooRawData(symbol,
					balanceSheetPerDateYearly);
			SortedMap<Date, BalanceSheet> quaterlyBalanceSheet = extractBalanceSheetFromYahooRawData(symbol,
					balanceSheetPerDateQuaterly);
			
			ret = new FinancialData(symbol, yearlyBalanceSheet, quaterlyBalanceSheet, 
					/* annualCashflowStatement */ null, 
					/* quarterlyCashflowStatement */ null, 
					/* annualIncomeStatement */ null, 
					/* quarterlyIncomeStatement */ null);
			
		} catch (IOException e1) {
			String msg = "Can't " + (json == null ? "get " + url : "parse " + json);
			log.error(msg, e1);
		} catch (ParseException e) {
			String msg = "Symbol: " + symbol + ", Failed while parsing: " + parsingvalue;
			log.error(msg,e);
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
					getDoubleFromYahooVal(bsd.getRedeemablePreferredStocks()),  // redeemablePreferredStocks, 
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
	private Double getDoubleFromYahooVal(YahooVal yahooVal) {
		Double ret;
		if(yahooVal != null) {
			ret = yahooVal.getRaw();
		}else {
			ret = 0.0;
		}
		return ret;
	}

	private YahooFinancialJsonDataModel downloadFinancialData(String url) throws IOException, JsonParseException, JsonMappingException {
		String json;
		json = Jsoup.connect(url).ignoreContentType(true).execute().body();
		ObjectMapper mapper = new ObjectMapper();
		YahooFinancialJsonDataModel financial = mapper.readValue(json, YahooFinancialJsonDataModel.class);
		return financial;
	}

}
