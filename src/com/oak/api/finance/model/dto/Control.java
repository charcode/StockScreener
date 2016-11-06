package com.oak.api.finance.model.dto;


import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.oak.api.providers.control.ControlType;

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
	public static Control nonExistant() {
		Control t = new Control();
		t.setType(ControlType.NOT_AVAILABLE);
		t.setComments("NO CONTROL AVAILABLE");
		t.setStatus(Status.UNKNOWN);
		Calendar cal = Calendar.getInstance();
		cal.set(2000, 1, 1);
		t.setTimeStamp(cal.getTime());
		return t;
	}
	public static Control newControl(ControlType controlType,Status status,String comment) {
		Control t = new Control();
		t.setType(controlType);
		t.setComments(comment);
		t.setStatus(status);
		t.setTimeStamp(new Date());
		return t;
	}
}
