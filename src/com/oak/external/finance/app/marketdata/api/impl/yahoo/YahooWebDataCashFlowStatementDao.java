package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.CashFlowStatement;
import com.oak.external.finance.app.marketdata.api.CashFlowStatementDao;

public class YahooWebDataCashFlowStatementDao extends
	AbstractYahooFinanceWebParser<CashFlowStatement> implements CashFlowStatementDao {

    
    private static final String NET_INCOME = "Net Income";
    private static final String CHANGE_IN_CASH_AND_CASH_EQUIVALENTS = "Change In Cash and Cash Equivalents";
    private static final String EFFECT_OF_EXCHANGE_RATE_CHANGES = "Effect Of Exchange Rate Changes";
    private static final String TOTAL_CASH_FLOWS_FROM_FINANCING_ACTIVITIES = "Total Cash Flows From Financing Activities";
    private static final String OTHER_CASH_FLOWS_FROM_FINANCING_ACTIVITIES = "Other Cash Flows from Financing Activities";
    private static final String NET_BORROWINGS = "Net Borrowings";
    private static final String SALE_PURCHASE_OF_STOCK = "Sale Purchase of Stock";
    private static final String DIVIDENDS_PAID = "Dividends Paid";
    private static final String TOTAL_CASH_FLOWS_FROM_INVESTING_ACTIVITIES = "Total Cash Flows From Investing Activities";
    private static final String OTHER_CASH_FLOWS_FROM_INVESTING_ACTIVITIES = "Other Cash flows from Investing Activities";
    private static final String INVESTMENTS = "Investments";
    private static final String CAPITAL_EXPENDITURES = "Capital Expenditures";
    private static final String TOTAL_CASH_FLOW_FROM_OPERATING_ACTIVITIES = "Total Cash Flow From Operating Activities";
    private static final String CHANGES_IN_OTHER_OPERATING_ACTIVITIES = "Changes In Other Operating Activities";
    private static final String CHANGES_IN_INVENTORIES = "Changes In Inventories";
    private static final String CHANGES_IN_LIABILITIES = "Changes In Liabilities";
    private static final String CHANGES_IN_ACCOUNTS_RECEIVABLES = "Changes In Accounts Receivables";
    private static final String ADJUSTMENTS_TO_NET_INCOME = "Adjustments To Net Income";
    private static final String DEPRECIATION = "Depreciation";
    private static String rootWeb = "http://finance.yahoo.com/q/cf?s=";

    public YahooWebDataCashFlowStatementDao(Logger log) {
	super(log, "td");
    }

    @Override
    public SortedMap<Date, CashFlowStatement> getCashFlowStatementForSymbol( String symbol, boolean annual) {
	SortedMap<Date, CashFlowStatement>  cashflowStatementsForSymbol = getStatementsForSymbol(symbol, annual);
	return cashflowStatementsForSymbol;
    }
    
    @Override
    protected String getRootWeb() {
	return rootWeb;
    }

    @Override
    protected void initializeInterestValues(Set<String> values) {
	values.add(NET_INCOME);
	values.add(DEPRECIATION);
	values.add(ADJUSTMENTS_TO_NET_INCOME);
	values.add(CHANGES_IN_ACCOUNTS_RECEIVABLES);
	values.add(CHANGES_IN_LIABILITIES);
	values.add(CHANGES_IN_INVENTORIES);
	values.add(CHANGES_IN_OTHER_OPERATING_ACTIVITIES);
	values.add(TOTAL_CASH_FLOW_FROM_OPERATING_ACTIVITIES);
	values.add(CAPITAL_EXPENDITURES);
	values.add(INVESTMENTS);
	values.add(OTHER_CASH_FLOWS_FROM_INVESTING_ACTIVITIES);
	values.add(TOTAL_CASH_FLOWS_FROM_INVESTING_ACTIVITIES);
	values.add(DIVIDENDS_PAID);
	values.add(SALE_PURCHASE_OF_STOCK);
	values.add(NET_BORROWINGS);
	values.add(OTHER_CASH_FLOWS_FROM_FINANCING_ACTIVITIES);
	values.add(TOTAL_CASH_FLOWS_FROM_FINANCING_ACTIVITIES);
	values.add(EFFECT_OF_EXCHANGE_RATE_CHANGES);
	values.add(CHANGE_IN_CASH_AND_CASH_EQUIVALENTS);
    }

    @Override
    protected CashFlowStatement buildSingleStatementFromDoubleMap(String symbol,
	    Map<String, List<Double>> valueMap, int i) {
	
	CashFlowStatement cf = new CashFlowStatement(
		symbol,
		valueMap.get(DEPRECIATION).get(i),
		valueMap.get(ADJUSTMENTS_TO_NET_INCOME).get(i),
		valueMap.get(CHANGES_IN_ACCOUNTS_RECEIVABLES).get(i),
		valueMap.get(CHANGES_IN_LIABILITIES).get(i),
		valueMap.get(CHANGES_IN_INVENTORIES).get(i),
		valueMap.get(CHANGES_IN_OTHER_OPERATING_ACTIVITIES).get(i),
		valueMap.get(TOTAL_CASH_FLOW_FROM_OPERATING_ACTIVITIES).get(i),
		valueMap.get(CAPITAL_EXPENDITURES).get(i),
		valueMap.get(INVESTMENTS).get(i),
		valueMap.get(OTHER_CASH_FLOWS_FROM_INVESTING_ACTIVITIES).get(i),
		valueMap.get(TOTAL_CASH_FLOWS_FROM_INVESTING_ACTIVITIES).get(i),
		valueMap.get(DIVIDENDS_PAID).get(i),
		valueMap.get(SALE_PURCHASE_OF_STOCK).get(i),
		valueMap.get(NET_BORROWINGS).get(i),
		valueMap.get(OTHER_CASH_FLOWS_FROM_FINANCING_ACTIVITIES).get(i),
		valueMap.get(TOTAL_CASH_FLOWS_FROM_FINANCING_ACTIVITIES).get(i),
		valueMap.get(NET_INCOME).get(i),
		valueMap.get(EFFECT_OF_EXCHANGE_RATE_CHANGES).get(i), 
		valueMap.get(CHANGE_IN_CASH_AND_CASH_EQUIVALENTS).get(i));
	
	return cf;
    }
    

}
