package com.oak.api.finance.repository;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.oak.api.finance.model.dto.Quote;

public interface QuoteRepository extends PagingAndSortingRepository<Quote, Long> {
	Collection<Quote> findByTicker(String ticker);
}
