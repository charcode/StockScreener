package com.oak.api.finance.repository;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Company;

@RepositoryRestResource(collectionResourceRel = "company", path = "companies")
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

	Set<Company> findByTickerIn(Set<String> tickers);

}
