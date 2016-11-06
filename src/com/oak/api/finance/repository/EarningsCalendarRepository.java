package com.oak.api.finance.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.EarningsCalendar;
import com.oak.api.finance.utils.repository.ReconcilingRepository;

@RepositoryRestResource(collectionResourceRel = "balance_sheet", path = "balance_sheets")
public interface EarningsCalendarRepository extends PagingAndSortingRepository<EarningsCalendar, Long>,ReconcilingRepository {
	List<EarningsCalendar> findByTicker(String ticker);
	
	@Query("SELECT DISTINCT c.ticker FROM EarningsCalendar c WHERE c.ticker NOT IN ?1")
	Set<String> findDistinctTickerNotIn(Set<String> tickers);

}
