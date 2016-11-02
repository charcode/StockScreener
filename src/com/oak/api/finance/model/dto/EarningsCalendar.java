package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class EarningsCalendar {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String companyName;
	private String ticker;
	private Date announcementDate;
	/**
	 * that's the date when the calendar entry was last read
	 */
	private Date asOfCalendar;
	
	@Enumerated(EnumType.STRING)
	private AnnouncementTime time;
	
	private Boolean statmentsLoaded;

}
