package com.oak.api.finance.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.finance.app.screeners.Filter;

@RepositoryRestResource(collectionResourceRel = "filter", path = "filter")
public interface FilterRepository extends PagingAndSortingRepository<Filter, Long> {

}
