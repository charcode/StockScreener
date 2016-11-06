package com.oak.api.finance.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.Status;
import com.oak.api.providers.control.ControlType;

@RepositoryRestResource(collectionResourceRel = "exchange", path = "exchanges")
public interface ControlRepository extends PagingAndSortingRepository<Control, Long> {
	
	Collection<Control> findByTypeAndStatusIn(ControlType type,Set<Status>status);
}
