package com.oak.api.finance.model.dto;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class CompanyWithProblems {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Long id;
	
	private String name;
	private String ticker;
	private String description;
	
	private String sectorDescription;
	private String industryDescription;
	private Date errorDate;
	private String problem;
	private ErrorType errorType;
}
