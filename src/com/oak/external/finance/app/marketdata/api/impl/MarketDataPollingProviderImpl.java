package com.oak.external.finance.app.marketdata.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.util.StreamUtils;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.EarningsCalendar;
import com.oak.api.finance.model.dto.Status;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.api.finance.repository.EarningsCalendarRepository;
import com.oak.api.providers.control.ControlProvider;
import com.oak.api.providers.control.ControlType;
import com.oak.external.finance.app.marketdata.api.DataConnector;
import com.oak.external.finance.app.marketdata.api.EarningsCalendarDao;
import com.oak.external.finance.app.marketdata.api.FinancialStatementsProvider;
import com.oak.external.finance.app.marketdata.api.MarketDataProvider;

public class MarketDataPollingProviderImpl implements MarketDataProvider {

	private final DataConnector dataConnector;
	private final EarningsCalendarDao earningsCalendarDao;
	private final EarningsCalendarRepository earningsCalendarRepository;
	private final FinancialStatementsProvider financialsProvider;
	private final BalanceSheetRepository balanceSheetRepository;
	private final int earningsCalendarWindowBackInDays;
	private final int earningsCalendarWindowForwardInDays;
	private final ControlProvider controlProvider;
	private final Logger log;

	public MarketDataPollingProviderImpl(DataConnector dataConnector, EarningsCalendarDao earningsCalendarDao,
			EarningsCalendarRepository earningsCalendarRepository, BalanceSheetRepository balanceSheetRepository,
			FinancialStatementsProvider financialsProvider, ControlProvider controlProvider,
			int earningsCalendarWindowBackInDays, int earningsCalendarWindowForwardInDays, Logger log) {
		this.earningsCalendarWindowBackInDays = earningsCalendarWindowBackInDays;
		this.earningsCalendarWindowForwardInDays = earningsCalendarWindowForwardInDays;
		this.dataConnector = dataConnector;
		this.balanceSheetRepository = balanceSheetRepository;
		this.earningsCalendarRepository = earningsCalendarRepository;
		this.earningsCalendarDao = earningsCalendarDao;
		this.financialsProvider = financialsProvider;
		this.controlProvider = controlProvider;
		this.log = log == null ? LogManager.getLogger(MarketDataPollingProviderImpl.class) : log;

		new Thread() {
			@Override
			public void run() {
				initEarningsCalendarRefresh();
			}
		}.start();;

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				initEarningsCalendarRefresh();				
			}
		};

		int timeout; 
		int hrs24InMs = 24*3600*1000;
		int min10inMs = 10*60*1000;
//		timeout = hrs24InMs;
		timeout = min10inMs;
		
		timer.schedule(task, timeout, timeout);
		
	}

	private void initEarningsCalendarRefresh() {
		Control latestEarningCalendar = controlProvider.getLatestControlByType(ControlType.EARNINGS_CALENDAR);
		Calendar lastEarningsUpdate = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());
		lastEarningsUpdate.setTime(latestEarningCalendar.getTimeStamp());
		boolean sameDay = lastEarningsUpdate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
				lastEarningsUpdate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);

		if (!sameDay) {
			log.info("Starting earnings calendar and financial statement collection thread");

			Iterable<EarningsCalendar> savedCals = earningsCalendarRepository.findAll();
			List<EarningsCalendar> newCals = downloadCalendarsForTheLastDays(savedCals);
			List<String> calendarsTickers = newCals.stream().map(n -> n.getTicker()).collect(Collectors.toList());
			List<BalanceSheetDto> savedBalanceSheets = balanceSheetRepository.findByTickerIn(calendarsTickers);
			Map<String, List<BalanceSheetDto>> bsSavedPerTicker = savedBalanceSheets.stream()
					.collect(Collectors.groupingBy(BalanceSheetDto::getTicker));
			// save to db
			for (EarningsCalendar newEc : newCals) {

				refreshFinancialStatementsForCompanyIfNecessary(bsSavedPerTicker, newEc);
			}
			earningsCalendarRepository.save(newCals);
			Control control = Control.newControl(ControlType.EARNINGS_CALENDAR,Status.SUCCESS,"loaded earnings calendars");
			controlProvider.save(control);
		}else {
			log.info("Calendars already loaded today.. skipping, last load: "+latestEarningCalendar);
		}
		log.info("Calendars refresh initialized");
	}

	private void refreshFinancialStatementsForCompanyIfNecessary(Map<String, List<BalanceSheetDto>> bsSavedPerTicker,
			EarningsCalendar newEc) {
		String ticker = newEc.getTicker();
		List<BalanceSheetDto> savedBs = bsSavedPerTicker.get(ticker);
		if (savedBs != null && !savedBs.isEmpty()) {
			Optional<BalanceSheetDto> maxDateBs = savedBs.stream().collect(Collectors.maxBy((a, b) -> {
				int ret = 1;
				if (a.getReleaseDate() != null && b.getReleaseDate() != null) {
					ret = a.getReleaseDate().compareTo(b.getReleaseDate());
				} else if (a.getEndDate() != null && b.getEndDate() != null) {
					ret = a.getEndDate().compareTo(b.getEndDate());
				}
				return ret;
			}));
			if (maxDateBs.isPresent()) {
				Date webDate = newEc.getAnnouncementDate();
				Date savedDate = maxDateBs.get().getReleaseDate();
				Long days = null;
				if (savedDate != null) {
					days = TimeUnit.DAYS.convert(webDate.getTime() - savedDate.getTime(), TimeUnit.MILLISECONDS);
				}
				if (days == null || days < earningsCalendarWindowBackInDays) {
					loadFinancialStatments(newEc);
				} else {
					log.info("no need to load financial statements for date");
				}
			}
		} else {
			loadFinancialStatments(newEc);
		}
	}

	private void loadFinancialStatments(EarningsCalendar newEc) {
		String ticker = newEc.getTicker();
		log.info("loading new financial statements for date " + ticker);
		financialsProvider.getFinancialStatements(ticker);
		newEc.setStatmentsLoaded(true);// mark calendar as loaded
	}

	private List<EarningsCalendar> downloadCalendarsForTheLastDays(Iterable<EarningsCalendar> savedCals) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		List<EarningsCalendar> newCals = new LinkedList<>();
		Map<String, List<EarningsCalendar>> savedErnCalMap = StreamUtils.createStreamFromIterator(savedCals.iterator())
				.collect(Collectors.groupingBy(EarningsCalendar::getTicker));
		for (int i = -1; i > -earningsCalendarWindowBackInDays; i--) {
			boolean stop = getEarningCalendarForDate(date, c, newCals, savedErnCalMap, i);
			if(stop) {
				break;
			}
		}
		for (int i = 0; i < earningsCalendarWindowForwardInDays ; i++) {
			getEarningCalendarForDate(date, c, newCals, savedErnCalMap, i);
		}
		return newCals;
	}

	private boolean getEarningCalendarForDate(Date date, Calendar c, List<EarningsCalendar> newCals,
			Map<String, List<EarningsCalendar>> savedErnCalMap, int dateOffset) {
		boolean stop = false;
		c.add(Calendar.DATE, dateOffset);
		Date d = c.getTime();
		log.info("Loading earnings calendars for " + d);
		boolean noNeedToLookFurtherBack = false;
		try {
			List<EarningsCalendar> newErngs = earningsCalendarDao.getEarningsCalendarByDate(d);
			for (EarningsCalendar n : newErngs) {
				List<EarningsCalendar> savedEarningsForTicker = savedErnCalMap.get(n.getTicker());
				List<EarningsCalendar> foundEarnings = null;
				if (savedEarningsForTicker != null) {
					foundEarnings = savedEarningsForTicker.stream()
							.filter(e -> !e.getAnnouncementDate().equals(
									n.getAnnouncementDate()))
							.collect(Collectors.toList());
				}
				if (foundEarnings == null || foundEarnings.isEmpty()) {
					newCals.add(n);
				} else {
					noNeedToLookFurtherBack = true;
				}
				c.setTime(date);
			}
		} catch (RuntimeException e) {
			log.warn("going too far back.. yahoo data unavailable");
			noNeedToLookFurtherBack = true;
		}
		if (noNeedToLookFurtherBack) {
			log.info("Done loading earnings calendars... ");
			stop = true;
		}
		return stop;
	}

	@Override
	public Map<Stock, Map<Date, Economic>> retrieveMarketData(Set<String> stocks) {
		return dataConnector.getEconomics(stocks, null);
	}

}
