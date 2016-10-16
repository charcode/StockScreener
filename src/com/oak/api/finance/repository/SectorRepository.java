package com.oak.api.finance.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Sector;

@RepositoryRestResource(collectionResourceRel = "sector", path = "sectors")
public interface SectorRepository extends PagingAndSortingRepository<Sector, Long> {

}
