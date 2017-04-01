package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import com.oak.api.finance.model.dto.EarningsCalendar;
import com.oak.external.finance.app.marketdata.api.EarningsCalendarDao;
import com.oak.external.utils.web.WebParsingUtils;


public class EarningsCalendarYahooWebDaoTests {
	String url = "http://finance.yahoo.com/calendar/earnings";
	CompletionService<List<EarningsCalendar>> executor = new ExecutorCompletionService<>( Executors.newCachedThreadPool());
	EarningsCalendarDao underTest = new EarningsCalendarYahooWebDao(LogManager.getLogger(EarningsCalendarYahooWebDao.class), url, new WebParsingUtils(), executor);
	
	
	@Test
	public void testGetEarningsCalendarByDate() {
		
//		Date tenDaysAgo = Date.from(LocalDateTime.now().minusDays(10).toInstant(ZoneOffset.UTC));
		Calendar c = Calendar.getInstance();
		c.set(2017, 2, 22);
		Date time = c.getTime();
		List<EarningsCalendar> earningsCalendars = underTest.getEarningsCalendarByDate(time);
		Map<Date, List<EarningsCalendar>> byDate = earningsCalendars.stream().collect(Collectors.groupingBy(EarningsCalendar::getAsOfCalendar));
		
		c.set(Calendar.DAY_OF_MONTH,19);
		for(Date k:byDate.keySet()) {
			List<EarningsCalendar> list = byDate.get(k);
			System.out.println(k+" earnings  "+ list.size());
		}
		
	}

}
