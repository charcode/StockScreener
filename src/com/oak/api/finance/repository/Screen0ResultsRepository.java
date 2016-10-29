package com.oak.api.finance.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Screen0Result;
@RepositoryRestResource(collectionResourceRel = "screen0_result", path = "screen0_results")
public interface Screen0ResultsRepository extends PagingAndSortingRepository<Screen0Result, Long> {

}
