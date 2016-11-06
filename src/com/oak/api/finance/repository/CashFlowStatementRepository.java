package com.oak.api.finance.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.CashFlowStatementDto;
import com.oak.api.finance.utils.repository.ReconcilingRepository;

@RepositoryRestResource(collectionResourceRel = "cashflow_statement", path = "cashflow_statements")
public interface CashFlowStatementRepository extends PagingAndSortingRepository<CashFlowStatementDto, Long>, ReconcilingRepository {
	List<CashFlowStatementDto>findByTicker(String tickers);
	List<CashFlowStatementDto>findByTickerIn(Collection<String> tickers);

	@Query("SELECT DISTINCT c.ticker FROM CashFlowStatementDto c WHERE c.ticker NOT IN ?1")
	Set<String> findDistinctTickerNotIn(Set<String> tickers);
}
