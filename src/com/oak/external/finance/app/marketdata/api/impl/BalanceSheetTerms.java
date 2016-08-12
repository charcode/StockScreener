package com.oak.external.finance.app.marketdata.api.impl;

import static org.mockito.Matchers.anyByte;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum BalanceSheetTerms {

	 LONG_TERM_INVESTMENTS ( "Long Term Investments"),
	 NET_TANGIBLE_ASSETS ( "Net Tangible Assets"),
	 TOTAL_STOCKHOLDER_EQUITY ( "Total Stockholder Equity"),
	 OTHER_STOCKHOLDER_EQUITY ( "Other Stockholder Equity"),
	 CAPITAL_SURPLUS ( "Capital Surplus"),
	 TREASURY_STOCK ( "Treasury Stock"),
	 RETAINED_EARNINGS ( "Retained Earnings"),
	 COMMON_STOCK ( "Common Stock"),
	 PREFERRED_STOCK ( "Preferred Stock"),
	 REDEEMABLE_PREFERRED_STOCK ( "Redeemable Preferred Stock"),
	 MISC_STOCKS_OPTIONS_WARRANTS ( "Misc Stocks Options Warrants"),
	 TOTAL_LIABILITIES ( "Total Liabilities"),
	 NEGATIVE_GOODWILL ( "Negative Goodwill"),
	 MINORITY_INTEREST ( "Minority Interest"),
	 DEFERRED_LONG_TERM_LIABILITY_CHARGES ( "Deferred Long Term Liability Charges"),
	 OTHER_LIABILITIES ( "Other Liabilities"),
	 LONG_TERM_DEBT ( "Long Term Debt"),
	 TOTAL_CURRENT_LIABILITIES ( "Total Current Liabilities"),
	 OTHER_CURRENT_LIABILITIES ( "Other Current Liabilities"),
	 SHORT_CURRENT_LONG_TERM_DEBT ( "Short/Current Long Term Debt"),
	 ACCOUNTS_PAYABLE ( "Accounts Payable"),
	 TOTAL_ASSETS ( "Total Assets"),
	 DEFERRED_LONG_TERM_ASSET_CHARGES ( "Deferred Long Term Asset Charges"),
	 OTHER_ASSETS ( "Other Assets"),
	 ACCUMULATED_AMORTIZATION ( "Accumulated Amortization"),
	 INTANGIBLE_ASSETS ( "Intangible Assets"),
	 GOODWILL ( "Goodwill"),
	 PROPERTY_PLANT_AND_EQUIPMENT ( "Property Plant and Equipment"),
	 TOTAL_CURRENT_ASSETS ( "Total Current Assets"),
	 OTHER_CURRENT_ASSETS ( "Other Current Assets"),
	 INVENTORY ( "Inventory"),
	 NET_RECEIVABLES ( "Net Receivables"),
	 SHORT_TERM_INVESTMENTS ( "Short Term Investments"),
	 CASH_AND_CASH_EQUIVALENTS ( "Cash And Cash Equivalents"),
	;
	
	private String text; 
	
	private BalanceSheetTerms(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public static Optional<BalanceSheetTerms> fromText(String text) {
		 List<BalanceSheetTerms> first = Arrays.asList(values()).stream()
			.filter( t -> t.getText().equals(text) )
			.collect(Collectors.toList());
		 Optional<BalanceSheetTerms> ret;
		 if(first.isEmpty()) {
			 ret = Optional.empty();
		 }else if(first.size()>1){
			 ret = Optional.empty();
			 throw new RuntimeException("unexpected, found more than one: "+text +" in "+values());
		 }else {
			 ret = Optional.of(first.get(0));
		 }
		return ret;
	}
}
