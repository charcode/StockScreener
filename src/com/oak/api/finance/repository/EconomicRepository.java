package com.oak.api.finance.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.oak.api.finance.model.dto.EconomicDto;

public interface EconomicRepository extends PagingAndSortingRepository<EconomicDto, Long> {
	List<EconomicDto>findByPriceDateAndTicker(Date date, String ticker);
}
