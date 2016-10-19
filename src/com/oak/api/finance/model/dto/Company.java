package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Company implements Comparable<Company>{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private long id;
	
	private String name;
	private String ticker;
	private String description;
	
	private long sectorId;
	private long industryId;
	private String sectorDescription;
	private String industryDescription;
	
	@Override
	public int compareTo(Company o) {
		int compareTo;
		if(description != null) {
			compareTo = description.compareTo(o.description);
		}else {
			if(o.description == null) {
				compareTo = 0;
			}else {
				compareTo = -1;
			}
		}
		return compareTo;
	}
}
