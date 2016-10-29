package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import com.oak.external.utils.web.WebParsingUtils;

public class YahooWebDataSectorCompaniesDaoTest {
	private String url = "https://biz.yahoo.com/p/s_conameu.html";
	@Test
	public void testGetSectors() {
		YahooWebDataSectorCompaniesDao dao = new YahooWebDataSectorCompaniesDao(LogManager.getFormatterLogger(YahooWebDataSectorCompaniesDao.class), url, new WebParsingUtils());
		dao.getSectorsAndCompanies();
	}

}
