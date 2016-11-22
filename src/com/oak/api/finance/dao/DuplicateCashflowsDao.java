package com.oak.api.finance.dao;

import java.util.Date;
import java.util.List;

import com.oak.api.finance.model.dto.AbstractFinancialStatement;
import com.oak.api.finance.model.dto.StatementPeriod;

import lombok.Data;
import lombok.EqualsAndHashCode;

public interface DuplicateCashflowsDao {

	List<Duplicate> findDuplicateCashflows();
	
	
	@Data
	@EqualsAndHashCode(callSuper=true)
	public class Duplicate extends AbstractFinancialStatement{
		private String ticker;
		private StatementPeriod statementPeriod;
		private Date endDate;
		private int size;		
	}	

}