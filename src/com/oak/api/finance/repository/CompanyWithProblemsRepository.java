package com.oak.api.finance.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.CompanyWithProblems;

@RepositoryRestResource(collectionResourceRel = "company_with_problems", path = "companies")

public interface CompanyWithProblemsRepository extends PagingAndSortingRepository<CompanyWithProblems, Long> {

}
