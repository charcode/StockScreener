package com.oak.api.finance.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.EarningsCalendar;

@RepositoryRestResource(collectionResourceRel = "balance_sheet", path = "balance_sheets")
public interface EarningsCalendarRepository extends PagingAndSortingRepository<EarningsCalendar, Long> {
	List<EarningsCalendar> findByTicker(String ticker);
}
