package com.oak.api.finance.repository;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.ControlType;

@RepositoryRestResource(collectionResourceRel = "exchange", path = "exchanges")
public interface ControlRepository extends PagingAndSortingRepository<Control, Long> {
	
	Collection<Control> findByType(ControlType type);
}
