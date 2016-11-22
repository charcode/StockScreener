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
	protected Long id;
	protected String ticker;
	protected Date endDate;
	@Enumerated(EnumType.STRING)
	protected StatementPeriod statementPeriod;
	protected Date releaseDate;

	public AbstractFinancialStatement() {
		super();
	}

}