package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Company {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private long id;
	
	private String description;
	
	private long sectorId;
}
