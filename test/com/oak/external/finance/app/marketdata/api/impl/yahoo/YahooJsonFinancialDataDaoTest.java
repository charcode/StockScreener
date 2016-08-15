package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import com.oak.api.finance.model.FinancialData;
import com.oak.finance.app.monitor.analysis.FinanceFundamentalAnalysisControllerImpl;

public class YahooJsonFinancialDataDaoTest {

	YahooJsonFinancialDataDao underTest = new YahooJsonFinancialDataDao(LogManager.getFormatterLogger(FinanceFundamentalAnalysisControllerImpl.class));
	@Test
	public void testGetBalanceSheetForSymbol() {
		
		// AAPL,LCI,COP , AEL, IMH
		String symbol = "IMH";
		String exchange = null;
		FinancialData financialDataForSymbol = underTest.getFinancialDataForSymbol(symbol, exchange);
		assertNotNull("no financial data returned",financialDataForSymbol );
	}

}
