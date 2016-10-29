package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Asset {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String exchangeCode;
	private long companyId;
	private String ticker;
	private SecurityType securityType;

}
