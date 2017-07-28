package com.oak.api.finance.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oak.api.finance.model.dto.Quote;

public interface QuoteProvider {

	Map<String,Date>getLatestQuoteDateByTicker();

	Iterable<Quote> save(List<Quote> q);
	
}
