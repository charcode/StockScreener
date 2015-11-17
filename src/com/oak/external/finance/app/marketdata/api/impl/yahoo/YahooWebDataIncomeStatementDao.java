package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;
import org.jsoup.select.Elements;

import com.oak.api.finance.model.BalanceSheet;
import com.oak.external.finance.model.economy.IncomeStatement;

public class YahooWebDataIncomeStatementDao extends
	AbstractYahooFinanceWebParser<IncomeStatement> implements IncomeStatementDao {

    private static final String NET_INCOME_APPLICABLE_TO_COMMON_SHARES = "Net Income Applicable To Common Shares";
    private static final String PREFERRED_STOCK_AND_OTHER_ADJUSTMENTS = "Preferred Stock And Other Adjustments";
    private static final String NET_INCOME = "Net Income";
    private static final String OTHER_ITEMS = "Other Items";
    private static final String EFFECT_OF_ACCOUNTING_CHANGES = "Effect Of Accounting Changes";
    private static final String EXTRAORDINARY_ITEMS = "Extraordinary Items";
    private static final String DISCONTINUED_OPERATIONS = "Discontinued Operations";
    private static final String NET_INCOME_FROM_CONTINUING_OPS = "Net Income From Continuing Ops";
    private static final String MINORITY_INTEREST = "Minority Interest";
    private static final String INCOME_TAX_EXPENSE = "Income Tax Expense";
    private static final String INCOME_BEFORE_TAX = "Income Before Tax";
    private static final String INTEREST_EXPENSE = "Interest Expense";
    private static final String EARNINGS_BEFORE_INTEREST_AND_TAXES = "Earnings Before Interest And Taxes";
    private static final String TOTAL_OTHER_INCOME_EXPENSES_NET = "Total Other Income/Expenses Net";
    private static final String TOTAL_REVENUE = "Total Revenue";
    private static final String COST_OF_REVENUE = "Cost of Revenue";
    private static final String GROSS_PROFIT = "Gross Profit";
    private static final String RESEARCH_DEVELOPMENT = "Research Development";
    private static final String SELLING_GENERAL_AND_ADMINISTRATIVE = "Selling General and Administrative";
    private static final String NON_RECURRING = "Non Recurring";
    private static final String OTHERS = "Others";
    private static final String TOTAL_OPERATING_EXPENSES = "Total Operating Expenses";
    private static final String OPERATING_INCOME_OR_LOSS = "Operating Income or Loss";
    private static String rootWeb = "http://finance.yahoo.com/q/is?s=";

    public YahooWebDataIncomeStatementDao(Logger log) {
	super(log, "th");
    }

    @Override
    protected void initializeInterestValues(Set<String> values) {
	values.add(TOTAL_REVENUE);
	values.add(COST_OF_REVENUE);
	values.add(GROSS_PROFIT);
	values.add(RESEARCH_DEVELOPMENT);
	values.add(SELLING_GENERAL_AND_ADMINISTRATIVE);
	values.add(NON_RECURRING);
	values.add(OTHERS);
	values.add(TOTAL_OPERATING_EXPENSES);
	values.add(OPERATING_INCOME_OR_LOSS);
	values.add(TOTAL_OTHER_INCOME_EXPENSES_NET);
	values.add(EARNINGS_BEFORE_INTEREST_AND_TAXES);
	values.add(INTEREST_EXPENSE);
	values.add(INCOME_BEFORE_TAX);
	values.add(INCOME_TAX_EXPENSE);
	values.add(MINORITY_INTEREST);
	values.add(NET_INCOME_FROM_CONTINUING_OPS);
	values.add(DISCONTINUED_OPERATIONS);
	values.add(EXTRAORDINARY_ITEMS);
	values.add(EFFECT_OF_ACCOUNTING_CHANGES);
	values.add(OTHER_ITEMS);
	values.add(NET_INCOME);
	values.add(PREFERRED_STOCK_AND_OTHER_ADJUSTMENTS);
	values.add(NET_INCOME_APPLICABLE_TO_COMMON_SHARES);

    }

    
    protected String getRootWeb() {
	return rootWeb;
    }
 

    @Override
    public SortedMap<Date, IncomeStatement> getIncomeStatementForSymbol( String symbol, boolean annual) {

	SortedMap<Date, IncomeStatement> incomeStatements = getStatementsForSymbol(symbol, annual);
	return incomeStatements;
    }

  
    protected IncomeStatement buildSingleStatementFromDoubleMap(String symbol,
	    Map<String, List<Double>> valueMap, int i) {
	IncomeStatement is = new IncomeStatement(
		symbol,
		valueMap.get(RESEARCH_DEVELOPMENT).get(i),
		valueMap.get(SELLING_GENERAL_AND_ADMINISTRATIVE).get(i),
		valueMap.get(NON_RECURRING).get(i),
		valueMap.get(OTHERS).get(i),
		valueMap.get(TOTAL_OPERATING_EXPENSES).get(i),
		valueMap.get(TOTAL_OTHER_INCOME_EXPENSES_NET).get(i),
		valueMap.get(EARNINGS_BEFORE_INTEREST_AND_TAXES).get(i),
		valueMap.get(INTEREST_EXPENSE).get(i), 
		valueMap.get(INCOME_BEFORE_TAX).get(i),
		valueMap.get(INCOME_TAX_EXPENSE).get(i), 
		valueMap.get(MINORITY_INTEREST).get(i), 
		valueMap.get(NET_INCOME_FROM_CONTINUING_OPS).get(i),
		valueMap.get(DISCONTINUED_OPERATIONS).get(i), 
		valueMap.get(EXTRAORDINARY_ITEMS).get(i), 
		valueMap.get(EFFECT_OF_ACCOUNTING_CHANGES).get(i), 
		valueMap.get(OTHER_ITEMS).get(i), 
		valueMap.get(NET_INCOME).get(i), 
		valueMap.get(PREFERRED_STOCK_AND_OTHER_ADJUSTMENTS).get(i),
		valueMap.get(NET_INCOME_APPLICABLE_TO_COMMON_SHARES) .get(i));
	return is;
    }

}
