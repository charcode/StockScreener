package com.oak.external.finance.app.marketdata.api.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.util.StreamUtils;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.AbstractQuote;
import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.EarningsCalendar;
import com.oak.api.finance.model.dto.ErrorQuote;
import com.oak.api.finance.model.dto.Quote;
import com.oak.api.finance.model.dto.Status;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.api.finance.repository.EarningsCalendarRepository;
import com.oak.api.finance.repository.ErrorQuoteRepository;
import com.oak.api.finance.repository.QuoteProvider;
import com.oak.api.providers.control.ControlProvider;
import com.oak.api.providers.control.ControlType;
import com.oak.external.finance.app.marketdata.api.DataConnector;
import com.oak.external.finance.app.marketdata.api.EarningsCalendarDao;
import com.oak.external.finance.app.marketdata.api.FinancialStatementsProvider;
import com.oak.external.finance.app.marketdata.api.MarketDataProvider;
import com.oak.finance.interest.SymbolsController;

import io.reactivex.Observable;

public class MarketDataPollingProviderImpl implements MarketDataProvider {

	private final DataConnector dataConnector;
	private final EarningsCalendarDao earningsCalendarDao;
	private final EarningsCalendarRepository earningsCalendarRepository;
	private final FinancialStatementsProvider financialsProvider;
	private final BalanceSheetRepository balanceSheetRepository;
	private final ControlProvider controlProvider;
	private final SymbolsController symbolsController;
	private final QuoteProvider quoteProvider;
	private final ErrorQuoteRepository errorQuoteRepository;
	private final Logger log;
	private final Executor ex = Executors.newCachedThreadPool();

	public MarketDataPollingProviderImpl(DataConnector dataConnector, EarningsCalendarDao earningsCalendarDao,
			EarningsCalendarRepository earningsCalendarRepository, BalanceSheetRepository balanceSheetRepository,
			FinancialStatementsProvider financialsProvider, ControlProvider controlProvider,
			QuoteProvider quoteProvider, ErrorQuoteRepository  errorQuoteRepository, SymbolsController symbolsController, Logger log) {
		this.dataConnector = dataConnector;
		this.balanceSheetRepository = balanceSheetRepository;
		this.earningsCalendarRepository = earningsCalendarRepository;
		this.earningsCalendarDao = earningsCalendarDao;
		this.financialsProvider = financialsProvider;
		this.controlProvider = controlProvider;
		this.symbolsController = symbolsController;
		this.quoteProvider = quoteProvider;
		this.errorQuoteRepository = errorQuoteRepository;
		this.log = log == null ? LogManager.getLogger(MarketDataPollingProviderImpl.class) : log;

		new Thread() {
			@Override
			public void run() {
				initEarningsCalendarRefresh();
			}
		}.start();

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runPeriodicTasks();
			}
		};

		int timeout; 
		int hrs24InMs = 24*3600*1000;
		int min10inMs = 10*60*1000;
		timeout = hrs24InMs;
//		timeout = min10inMs;
		
		timer.schedule(task, timeout, timeout);
		
	}
	private void runPeriodicTasks() {
		ex.execute(() -> initEarningsCalendarRefresh());
		ex.execute(() -> checkHistoricalQuotes());
	}

	@Override
	public void checkHistoricalQuotes() {
		Date yahooFrom = Date.from(LocalDateTime.of(1962,1,2,0,0).toInstant(ZoneOffset.UTC));
		Set<String> tickers = symbolsController.getSymbols();
		
		Map<String, Date> latestQuoteDateByTicker =quoteProvider
				.getLatestQuoteDateByTicker();
		
		SortedMap<Date,Set<String>>tickersForDates = new TreeMap<>();
		SetView<String> difference = Sets.difference(tickers, latestQuoteDateByTicker.keySet());
		tickersForDates.put(yahooFrom, difference);
		for(String ticker: latestQuoteDateByTicker.keySet()) {
			Date date = latestQuoteDateByTicker.get(ticker);
			boolean isSameDay = sameDay(date, new Date());
			if(!isSameDay) {
				LocalDateTime plusDay = LocalDateTime.from(date.toInstant().atZone(ZoneId.systemDefault())).plusDays(1);
				Date datePlusOne = Date.from(plusDay.atZone(ZoneId.systemDefault()).toInstant());
				if(!tickersForDates.containsKey(datePlusOne)) {
					tickersForDates.put(datePlusOne, new HashSet<>());
				}
				tickersForDates.get(datePlusOne).add(ticker);
			}
		}
//		if(emitter != null) {
//    		emitter.onComplete();
//    	}
		for(Map.Entry<Date, Set<String>> e : tickersForDates.entrySet()) {
			Date date = e.getKey();
			Set<String> tckrs = e.getValue();
			loadHistoricalQuotes(date,tckrs);
		}
	}
	private boolean sameDay(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
		                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}
	private void loadHistoricalQuotes(Date from,Set<String>tickers) {
		log.info("Starting the collection of historical quotes... ");
		System.out.println("loadHistoricalQuotes " + from + " " + tickers.size());
		Observable<List<AbstractQuote>> quotesObservable = dataConnector.getHistoricalQuotes(tickers, from);
		quotesObservable.subscribe(q -> saveQuotes(q));
	}
	private Iterable<AbstractQuote> saveQuotes(List<AbstractQuote> q) {
		Iterable<AbstractQuote> ret = null;
		Iterable<ErrorQuote> errorQuotes = q.stream().filter(eq -> eq instanceof ErrorQuote)
		.map(eq -> (ErrorQuote)eq )
		.collect(Collectors.toSet());
		List<Quote> quotes = q.stream().filter(eq -> eq instanceof Quote)
				.map(eq -> (Quote)eq )
				.collect(Collectors.toList());
		if(errorQuotes.iterator().hasNext()) {
			log.info("Saving error quotes ");
			errorQuotes = errorQuoteRepository.save(errorQuotes);
			List<AbstractQuote> collect = StreamUtils.createStreamFromIterator(errorQuotes.iterator()).map(
				e -> (AbstractQuote)e	).collect(Collectors.toList());
			ret = collect;
		}
		if(quotes.iterator().hasNext()){
			log.info("Saving quotes "+quotes.size());
			 ret = StreamUtils.createStreamFromIterator(quoteProvider.save(quotes).iterator()).map(
					 e -> (Quote)e)
					 .collect(Collectors.toList()) ;
		}
		return ret;
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
			List<EarningsCalendar> newCals = downloadCalendarsForTheLastDays(savedCals, lastEarningsUpdate);
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
				if (days == null || days < 60) {
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
		log.info("loading new financial statements for ticker " + ticker);
		financialsProvider.getFinancialStatements(ticker);
		newEc.setStatmentsLoaded(true);// mark calendar as loaded
	}

	private List<EarningsCalendar> downloadCalendarsForTheLastDays(Iterable<EarningsCalendar> savedCals, Calendar lastEarningsUpdate) {
		List<EarningsCalendar> newCals = new LinkedList<>();
		Map<String, List<EarningsCalendar>> savedErnCalMap = StreamUtils.createStreamFromIterator(savedCals.iterator())
				.collect(Collectors.groupingBy(EarningsCalendar::getTicker));
		getEarningCalendarForDate(lastEarningsUpdate, newCals, savedErnCalMap);
		return newCals;
	}

	private boolean getEarningCalendarForDate(Calendar lastEarningsUpdate, List<EarningsCalendar> newCals, Map<String, List<EarningsCalendar>> savedErnCalMap) {
		boolean stop = false;
		Date lastLoadedEarningsCalendarDate = lastEarningsUpdate.getTime();
		log.info("Loading earnings calendars since " + lastLoadedEarningsCalendarDate);
		boolean noNeedToLookFurtherBack = false;
		try {
			List<EarningsCalendar> newErngs = earningsCalendarDao.getEarningsCalendarByDate(lastLoadedEarningsCalendarDate);
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
		return dataConnector.getEconomics(stocks);
	}

}
