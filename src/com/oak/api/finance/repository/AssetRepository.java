package com.oak.api.finance.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Asset;

@RepositoryRestResource(collectionResourceRel = "asset", path = "assets")
public interface AssetRepository extends PagingAndSortingRepository<Asset, Long> {

}
