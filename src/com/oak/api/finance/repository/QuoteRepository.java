package com.oak.api.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Quote;

@RepositoryRestResource(collectionResourceRel = "historic_quote", path = "historic_quote")
public interface QuoteRepository extends PagingAndSortingRepository<Quote, Long> {
	
	@Query(value = "select ticker, max(date) from quote group by ticker", 
			nativeQuery = true, name="LatestQuoteDateByTicker")
	List<Object[]> getLatestQuoteDateByTicker();

}
