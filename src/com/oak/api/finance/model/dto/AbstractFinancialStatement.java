package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractFinancialStatement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String ticker;
	private Date endDate;
	@Enumerated(EnumType.STRING)
	private StatementPeriod statementPeriod;
	private Date releaseDate;

	public AbstractFinancialStatement() {
		super();
	}

}