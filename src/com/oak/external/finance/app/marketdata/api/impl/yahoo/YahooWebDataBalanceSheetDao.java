package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.BalanceSheet;
import com.oak.external.finance.app.marketdata.api.BalanceSheetDao;

public class YahooWebDataBalanceSheetDao extends
		AbstractYahooFinanceWebParser<BalanceSheet> implements BalanceSheetDao {
	private static final String LONG_TERM_INVESTMENTS = "Long Term Investments";
	private static final String NET_TANGIBLE_ASSETS = "Net Tangible Assets";
	private static final String TOTAL_STOCKHOLDER_EQUITY = "Total Stockholder Equity";
	private static final String OTHER_STOCKHOLDER_EQUITY = "Other Stockholder Equity";
	private static final String CAPITAL_SURPLUS = "Capital Surplus";
	private static final String TREASURY_STOCK = "Treasury Stock";
	private static final String RETAINED_EARNINGS = "Retained Earnings";
	private static final String COMMON_STOCK = "Common Stock";
	private static final String PREFERRED_STOCK = "Preferred Stock";
	private static final String REDEEMABLE_PREFERRED_STOCK = "Redeemable Preferred Stock";
	private static final String MISC_STOCKS_OPTIONS_WARRANTS = "Misc Stocks Options Warrants";
	private static final String TOTAL_LIABILITIES = "Total Liabilities";
	private static final String NEGATIVE_GOODWILL = "Negative Goodwill";
	private static final String MINORITY_INTEREST = "Minority Interest";
	private static final String DEFERRED_LONG_TERM_LIABILITY_CHARGES = "Deferred Long Term Liability Charges";
	private static final String OTHER_LIABILITIES = "Other Liabilities";
	private static final String LONG_TERM_DEBT = "Long Term Debt";
	private static final String TOTAL_CURRENT_LIABILITIES = "Total Current Liabilities";
	private static final String OTHER_CURRENT_LIABILITIES = "Other Current Liabilities";
	private static final String SHORT_CURRENT_LONG_TERM_DEBT = "Short/Current Long Term Debt";
	private static final String ACCOUNTS_PAYABLE = "Accounts Payable";
	private static final String TOTAL_ASSETS = "Total Assets";
	private static final String DEFERRED_LONG_TERM_ASSET_CHARGES = "Deferred Long Term Asset Charges";
	private static final String OTHER_ASSETS = "Other Assets";
	private static final String ACCUMULATED_AMORTIZATION = "Accumulated Amortization";
	private static final String INTANGIBLE_ASSETS = "Intangible Assets";
	private static final String GOODWILL = "Goodwill";
	private static final String PROPERTY_PLANT_AND_EQUIPMENT = "Property Plant and Equipment";
	private static final String TOTAL_CURRENT_ASSETS = "Total Current Assets";
	private static final String OTHER_CURRENT_ASSETS = "Other Current Assets";
	private static final String INVENTORY = "Inventory";
	private static final String NET_RECEIVABLES = "Net Receivables";
	private static final String SHORT_TERM_INVESTMENTS = "Short Term Investments";
	private static final String CASH_AND_CASH_EQUIVALENTS = "Cash And Cash Equivalents";
	static String rootWeb = "http://finance.yahoo.com/q/bs?s=";

	public YahooWebDataBalanceSheetDao(Logger logger) {
		super(logger, "b");
	}

	protected void initializeInterestValues(Set<String> values) {
		values.add(CASH_AND_CASH_EQUIVALENTS);
		values.add(SHORT_TERM_INVESTMENTS);
		values.add(NET_RECEIVABLES);
		values.add(INVENTORY);
		values.add(OTHER_CURRENT_ASSETS);
		values.add(TOTAL_CURRENT_ASSETS);
		values.add(LONG_TERM_INVESTMENTS);
		values.add(PROPERTY_PLANT_AND_EQUIPMENT);
		values.add(GOODWILL);
		values.add(INTANGIBLE_ASSETS);
		values.add(ACCUMULATED_AMORTIZATION);
		values.add(OTHER_ASSETS);
		values.add(DEFERRED_LONG_TERM_ASSET_CHARGES);
		values.add(TOTAL_ASSETS);
		values.add(ACCOUNTS_PAYABLE);
		values.add(SHORT_CURRENT_LONG_TERM_DEBT);
		values.add(OTHER_CURRENT_LIABILITIES);
		values.add(TOTAL_CURRENT_LIABILITIES);
		values.add(LONG_TERM_DEBT);
		values.add(OTHER_LIABILITIES);
		values.add(DEFERRED_LONG_TERM_LIABILITY_CHARGES);
		values.add(MINORITY_INTEREST);
		values.add(NEGATIVE_GOODWILL);
		values.add(TOTAL_LIABILITIES);
		// Stockholders equity
		values.add(MISC_STOCKS_OPTIONS_WARRANTS);
		values.add(REDEEMABLE_PREFERRED_STOCK);
		values.add(PREFERRED_STOCK);
		values.add(COMMON_STOCK);
		values.add(RETAINED_EARNINGS);
		values.add(TREASURY_STOCK);
		values.add(CAPITAL_SURPLUS);
		values.add(OTHER_STOCKHOLDER_EQUITY);
		values.add(TOTAL_STOCKHOLDER_EQUITY);
		values.add(NET_TANGIBLE_ASSETS);
	}

	protected String getRootWeb() {
		return rootWeb;
	}

	@Override
	public SortedMap<Date, BalanceSheet> getBalanceSheetForSymbol(
			String symbol, String exchange, boolean annual) {
		SortedMap<Date, BalanceSheet> balanceSheetsOnPage = getStatementsForSymbol(
				symbol, annual);
		return balanceSheetsOnPage;
	}

	@Override
	public Map<String, SortedMap<Date, BalanceSheet>> getBalanceSheetsBySymbol(
			Map<String, String> symbols, boolean annual) {
		Map<String, SortedMap<Date, BalanceSheet>> ret = null;

		if (symbols != null) {
			ret = new HashMap<String, SortedMap<Date, BalanceSheet>>();

			for (String symbol : symbols.keySet()) {
				String exchange = symbols.get(symbol);
				SortedMap<Date, BalanceSheet> balanceSheetForSymbol = getBalanceSheetForSymbol(
						symbol, exchange, annual);
				ret.put(symbol, balanceSheetForSymbol);
			}
		}

		return ret;
	}

	protected BalanceSheet buildSingleStatementFromDoubleMap(String symbol,
			Map<String, List<Double>> values, int i) {
		BalanceSheet bs = new BalanceSheet(symbol, values.get(
				CASH_AND_CASH_EQUIVALENTS).get(i),// cashAndEquivalent,
				values.get(SHORT_TERM_INVESTMENTS).get(i),// shortTermInvestements,
				values.get(NET_RECEIVABLES).get(i),// netReceivables,
				values.get(INVENTORY).get(i),// inventory,
				values.get(OTHER_CURRENT_ASSETS).get(i),// otherCurrentAssets,
				values.get(TOTAL_CURRENT_ASSETS).get(i),// longTermInvestments,
				values.get(LONG_TERM_INVESTMENTS).get(i),// propertyPlantAndEquivalent,
				values.get(PROPERTY_PLANT_AND_EQUIPMENT).get(i),// goodwill,
				values.get(GOODWILL).get(i),// intangibleAssets,
				values.get(INTANGIBLE_ASSETS).get(i),// accumulatedAmortization,
				values.get(ACCUMULATED_AMORTIZATION).get(i),// otherAssets,
				values.get(OTHER_ASSETS).get(i),// deferredLongTermAssetCharges,
				values.get(TOTAL_ASSETS).get(i),// accountPayable,
				values.get(DEFERRED_LONG_TERM_ASSET_CHARGES).get(i),// shortCurrentLongTermDebt,
				values.get(ACCOUNTS_PAYABLE).get(i),// otherCurrentLiabilities,
				values.get(SHORT_CURRENT_LONG_TERM_DEBT).get(i),// longTermDebt,
				values.get(OTHER_CURRENT_LIABILITIES).get(i),// otherLiabilities,
				values.get(TOTAL_CURRENT_LIABILITIES).get(i),// totalCurrentLiabilities,
				values.get(LONG_TERM_DEBT).get(i),// deferredLongTermLiabilityCharges,
				values.get(OTHER_LIABILITIES).get(i),// minorityInterest,
				values.get(TOTAL_LIABILITIES).get(i),// totalLiabilities,				
				values.get(DEFERRED_LONG_TERM_LIABILITY_CHARGES).get(i),// negativeGoodwill,
				values.get(MINORITY_INTEREST).get(i),// miscStocksOptionsWarrants,
				values.get(NEGATIVE_GOODWILL).get(i),// redeemablePreferredStocks,
				values.get(MISC_STOCKS_OPTIONS_WARRANTS).get(i),// commonStock,
				values.get(REDEEMABLE_PREFERRED_STOCK).get(i),// preferredStock,
				values.get(COMMON_STOCK).get(i),// retainedEarnings,
				values.get(PREFERRED_STOCK).get(i),// treasuryStock,
				values.get(RETAINED_EARNINGS).get(i),// capitalSurplus,
				values.get(TREASURY_STOCK).get(i), 
				values.get(CAPITAL_SURPLUS).get(i), 
				values.get(OTHER_STOCKHOLDER_EQUITY).get(i)// otherstockholdersEquity
		);
		return bs;
	}


}
