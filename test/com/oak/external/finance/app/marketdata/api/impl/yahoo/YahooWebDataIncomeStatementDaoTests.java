package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.util.Date;
import java.util.SortedMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.oak.external.finance.model.economy.IncomeStatement;

public class YahooWebDataIncomeStatementDaoTests extends YahooWebDataTest {

    private YahooWebDataIncomeStatementDao isDao;
    @Before
    public void setUp() throws Exception {
	Logger log = LogManager.getLogger(YahooWebDataIncomeStatementDaoTests.class);
	isDao = new YahooWebDataIncomeStatementDao(log);
    }

    @Test
    public void testGetIncomeStatementForSymbol() {
	SortedMap<Date, IncomeStatement> incomeStatements = isDao.getIncomeStatementForSymbol("AAPL", true/*annual*/);
	if(incomeStatements.size()<1){
	    Assert.fail("couldn't read any income statement");
	}
	for(Date date: incomeStatements.keySet()){
	    IncomeStatement is=incomeStatements.get(date);
	    if (dateEquals(date,"26-Sep-2015")) {
	  		Assert.assertEquals("Expected income for "+date, 53394000,is.getNetIncome(), 1e-10);
	  	    } else if (dateEquals(date,"27-Sep-2014")) {                 
	  		Assert.assertEquals("Expected income for "+date, 39510000,is.getNetIncome(), 1e-10);
	  	    } else if (dateEquals(date,"28-Sep-2013")) {                 
	  		Assert.assertEquals("Expected income for "+date, 37037000,is.getNetIncome(), 1e-10);
	  	    }
	    }
    }

}
