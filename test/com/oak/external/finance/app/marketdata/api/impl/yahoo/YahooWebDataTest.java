package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oak.external.finance.app.marketdata.api.BalanceSheetDao;

public class YahooWebDataTest {
    SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");

    BalanceSheetDao undertest;
    @Before
    public void setUp() throws Exception {
	undertest = new YahooWebDataBalanceSheetDao(null);
    }

	@Test
	public void testGetBalanceSheets() {
		HashMap<String, String> stocks = new HashMap<String, String>();
		stocks.put("GLEN.L", "LSE");
		undertest.getBalanceSheetsBySymbol(stocks, true /* annual */);

	}


    protected boolean dateEquals(Date date,String d2) {
	Date d;
	boolean ret = false;
	try {
	    d = f.parse(d2);
	    ret = date.compareTo(d) == 0;
	} catch (ParseException e) {
	    
	    e.printStackTrace();
	    Assert.fail("Cannot parse date "+ d2);
	}
	return ret;
    }
}
