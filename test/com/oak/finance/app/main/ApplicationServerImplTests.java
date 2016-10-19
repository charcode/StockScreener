package com.oak.finance.app.main;

import java.util.Set;


import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import com.oak.external.finance.app.marketdata.api.MarketDataProvider;
import com.oak.finance.app.main.server.ApplicationServer;
import com.oak.finance.app.main.server.ApplicationServerImpl;
import com.oak.finance.app.monitor.MarketDataMonitorsController;
import com.oak.finance.interest.SymbolsController;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServerImplTests {

	private ApplicationServer undertest;
	@Mock
	MarketDataProvider marketDataProvider ;
	@Mock
	SymbolsController stockListProvider;
	@Mock
	MarketDataMonitorsController marketDataMonitorsFactory;
	@Mock
	Logger log;
	
	@Before
	public void setUp() throws Exception {		
	}

	@Test
	public void testStart() {
		
		Set<String> symbols = Sets.newSet("AAPL");
		Mockito.when(stockListProvider.getSymbols()).thenReturn(symbols);

		
		undertest = new ApplicationServerImpl(stockListProvider, marketDataMonitorsFactory, log);
	}
}
