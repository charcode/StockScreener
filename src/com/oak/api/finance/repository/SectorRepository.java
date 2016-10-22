package com.oak.api.finance.repository;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Sector;

@RepositoryRestResource(collectionResourceRel = "sector", path = "sectors")
public interface SectorRepository extends PagingAndSortingRepository<Sector, Long> {
	Collection<Sector>findSectorsById(Collection<Long>ids);
}
