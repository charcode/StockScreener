package com.oak.api.finance.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.utils.repository.ReconcilingRepository;

@RepositoryRestResource(collectionResourceRel = "balance_sheet", path = "balance_sheets")
public interface BalanceSheetRepository extends PagingAndSortingRepository<BalanceSheetDto, Long>, ReconcilingRepository {
	
	List<BalanceSheetDto>findByTicker(String ticker);
	List<BalanceSheetDto>findByTickerIn(Collection<String> tickers);
	
	@Override
	@Query("SELECT DISTINCT b.ticker FROM BalanceSheetDto b WHERE b.ticker NOT IN ?1")
	Set<String> findDistinctTickerNotIn(Set<String> tickers);
}
