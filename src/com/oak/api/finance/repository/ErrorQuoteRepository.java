package com.oak.api.finance.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.ErrorQuote;

@RepositoryRestResource(collectionResourceRel = "historic_quote_error", path = "historic_quote_errors")
public interface ErrorQuoteRepository extends PagingAndSortingRepository<ErrorQuote, Long> {

}
