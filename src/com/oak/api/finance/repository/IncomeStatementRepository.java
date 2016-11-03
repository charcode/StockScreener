package com.oak.api.finance.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.IncomeStatementDto;

@RepositoryRestResource(collectionResourceRel = "income_statement", path = "income_statements")
public interface IncomeStatementRepository extends PagingAndSortingRepository<IncomeStatementDto, Long> {
	List<IncomeStatementDto>findByTicker(String tickers);
	List<IncomeStatementDto>findByTickerIn(Collection<String> tickers);
}
