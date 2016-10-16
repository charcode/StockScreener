package com.oak.api.finance.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Exchange;

@RepositoryRestResource(collectionResourceRel = "exchange", path = "exchanges")
public interface ExchangeRepository extends PagingAndSortingRepository<Exchange, Long> {

}
