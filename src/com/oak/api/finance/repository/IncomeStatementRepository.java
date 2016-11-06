package com.oak.api.finance.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.IncomeStatementDto;
import com.oak.api.finance.utils.repository.ReconcilingRepository;

@RepositoryRestResource(collectionResourceRel = "income_statement", path = "income_statements")
public interface IncomeStatementRepository extends PagingAndSortingRepository<IncomeStatementDto, Long>, ReconcilingRepository {
	List<IncomeStatementDto>findByTicker(String tickers);
	List<IncomeStatementDto>findByTickerIn(Collection<String> tickers);
	
	
	@Query("SELECT DISTINCT i.ticker FROM IncomeStatementDto i WHERE i.ticker NOT IN ?1")
	Set<String> findDistinctTickerNotIn(Set<String> tickers);
}
