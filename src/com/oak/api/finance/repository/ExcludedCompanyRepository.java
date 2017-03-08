package com.oak.api.finance.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.ExcludedCompany;

@RepositoryRestResource(collectionResourceRel = "excluded_ticker", path = "excluded_companies")
public interface ExcludedCompanyRepository extends CrudRepository<ExcludedCompany, String> {

}
