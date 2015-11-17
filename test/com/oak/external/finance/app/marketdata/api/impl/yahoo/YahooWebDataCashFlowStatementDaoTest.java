package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SortedMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oak.api.finance.model.CashFlowStatement;

public class YahooWebDataCashFlowStatementDaoTest extends YahooWebDataTest {
    YahooWebDataCashFlowStatementDao undertest;
    SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
    @Before
    public void setUp() throws Exception {
	Logger logger = LogManager.getLogger(YahooWebDataCashFlowStatementDaoTest.class);
	undertest = new YahooWebDataCashFlowStatementDao(logger);
    }

    @Test
    public void testGetCashFlowStatementForSymbol() {
	SortedMap<Date, CashFlowStatement> cashflowStatements = undertest
		.getCashFlowStatementForSymbol("AAPL", true);
	if (cashflowStatements.size() < 1) {
	    Assert.fail("couldn't read any cash flow statement");
	}
	for (Date date : cashflowStatements.keySet()) {
	    CashFlowStatement cf = cashflowStatements.get(date);

	    if (dateEquals(date,"26-Sep-2015")) {
		Assert.assertEquals("Expected income for "+date, 53394000,cf.getNetIncome(), 1e-10);
	    } else if (dateEquals(date,"27-Sep-2014")) {                 
		Assert.assertEquals("Expected income for "+date, 39510000,cf.getNetIncome(), 1e-10);
	    } else if (dateEquals(date,"28-Sep-2013")) {                 
		Assert.assertEquals("Expected income for "+date, 37037000,cf.getNetIncome(), 1e-10);
	    }
	}
    }
}
