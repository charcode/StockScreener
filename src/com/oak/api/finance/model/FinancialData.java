package com.oak.api.finance.model;

import java.util.Date;
import java.util.SortedMap;

import com.oak.external.finance.model.economy.IncomeStatement;

import lombok.Data;

@Data
public class FinancialData {
	
	private final String symbol;
	private final SortedMap<Date,BalanceSheet>annualBalanceSheet;
	private final SortedMap<Date,BalanceSheet>quarterlyBalanceSheet;
	private final SortedMap<Date,CashFlowStatement>annualCashflowStatement;
	private final SortedMap<Date,CashFlowStatement>quarterlyCashflowStatement;
	private final SortedMap<Date,IncomeStatement>annualIncomeStatement;
	private final SortedMap<Date,IncomeStatement>quarterlyIncomeStatement;
	
}
