package com.oak.api.finance.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.BalanceSheetDto;

@RepositoryRestResource(collectionResourceRel = "balance_sheet", path = "balance_sheets")
public interface BalanceSheetRepository extends PagingAndSortingRepository<BalanceSheetDto, Long> {
	
	List<BalanceSheetDto>findByTicker(String ticker);
}
