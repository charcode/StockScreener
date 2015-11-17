package com.oak.finance.app.main;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.economy.Economic;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooDataConnector;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooDataConverter;
import com.oak.external.finance.app.marketdata.api.yahoo.YahooDataConverterImpl;

@RunWith(MockitoJUnitRunner.class)
public class YahooDataConnectorTests {

	@Mock
	private Logger log;

	private YahooDataConverter converter;
	
	private Set<String> stocks = Sets.newSet("AAPL");
	private YahooDataConnector undertest;

	@Before
	public void setUp() throws Exception {
		converter = new YahooDataConverterImpl(log);
		undertest = new YahooDataConnector(log, converter);
	}

	@Test
	public void testGetEconomics() {
		Map<Stock, Map<Date, Economic>> economics = undertest.getEconomics(stocks, null);
		log.info(economics);
	}
}
