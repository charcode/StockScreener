package com.oak.api.finance.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.CashFlowStatementDto;

@RepositoryRestResource(collectionResourceRel = "cashflow_statement", path = "cashflow_statements")
public interface CashFlowStatementRepository extends PagingAndSortingRepository<CashFlowStatementDto, Long> {
	List<CashFlowStatementDto>findByTicker(String tickers);
	List<CashFlowStatementDto>findByTickerIn(Collection<String> tickers);

}
