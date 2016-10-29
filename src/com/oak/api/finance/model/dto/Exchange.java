package com.oak.api.finance.model.dto;

import java.sql.Time;
import java.util.TimeZone;

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
public class Exchange {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	@ManyToOne(targetEntity=Asset.class, fetch=FetchType.EAGER)
	@JoinColumn(name="exchangeCode")
	private String code;
	private String country;
	private String city;
	private TimeZone timeZone;
	private Time openTime;
	private Time closeTime;
}
