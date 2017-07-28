package com.oak.api.finance.repository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.oak.api.finance.model.dto.Quote;

public class QuoteProviderImpl implements QuoteProvider {
	
	private final QuoteRepository repository;
	private static final Logger logger = LogManager.getLogger(QuoteProviderImpl.class);
	public QuoteProviderImpl(QuoteRepository repository){
		this.repository = repository;
	}
	@Override
	public Map<String, Date> getLatestQuoteDateByTicker() {
		logger.debug("Getting latest historical quotes per ticker, this will take a little while! Please wait....");
		long startTime = System.nanoTime();
		List<Object[]> objs = repository.getLatestQuoteDateByTicker();

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		logger.debug("Getting latest historical quotes per ticker, .... received " + 
				objs.size() + ", took " + duration +" ns");
		Map<String, Date> latestQuoteDateByTicker = new HashMap<>();
		for(Object[]o:objs) {
			Object o0 = o[0];
			Object o1 = o[1];
			latestQuoteDateByTicker.put((String)o0, (Date) o1);
		}
		logger.debug("Getting latest historical quotes per ticker, converted to map");
		return latestQuoteDateByTicker;
	}

	@Override
	public Iterable<Quote> save(List<Quote> q) {
		return repository.save(q);
	}

}
