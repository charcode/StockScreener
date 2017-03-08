package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "excluded_ticker")
public class ExcludedCompany {
	@Id 
	private String ticker;
	private Date date;
}
