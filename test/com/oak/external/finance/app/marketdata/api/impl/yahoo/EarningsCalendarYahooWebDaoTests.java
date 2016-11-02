package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import com.oak.api.finance.model.dto.EarningsCalendar;
import com.oak.external.finance.app.marketdata.api.EarningsCalendarDao;
import com.oak.external.utils.web.WebParsingUtils;

public class EarningsCalendarYahooWebDaoTests {
	String url = "https://biz.yahoo.com/research/earncal/";
	EarningsCalendarDao underTest = new EarningsCalendarYahooWebDao(LogManager.getLogger(EarningsCalendarYahooWebDao.class), url, new WebParsingUtils());
	
	@Test
	public void testGetEarningsCalendarByDate() {
		Date now = Date.from(ZonedDateTime.now().toInstant());
		List<EarningsCalendar> earningsCalendars = underTest.getEarningsCalendarByDate(now);
		
	}

}
