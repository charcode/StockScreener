package com.oak.api.finance.model.dto;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Control implements Comparable<Control>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private ControlType type;
	private Date timeStamp;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String comments;
	@Override
	public int compareTo(Control o) {
		int ret = 0;
		if(o!=null) {
			if(timeStamp!=null) {
				ret = timeStamp.compareTo(o.timeStamp);
			}
		}
		return ret;
	}
}
