package com.oak.api.finance.model.dto;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public enum AnnouncementTime {
	BEFORE_OPEN("Before Market Open"), AFTER_CLOSE("After Market Close"), NO_TIME_SUPPLIED(
			"Time Not Supplied"), MID_DAY(""), CANT_UNDERSTAND("ERROR");

	private static Logger log = LogManager.getLogger(AnnouncementTime.class);
	private String text;

	private AnnouncementTime(String t) {
		this.text = t;
	}

	public static AnnouncementTime fromText(String t) {
		AnnouncementTime ret = null;					// 02:00 am	
		DateTimeFormatter f =
				DateTimeFormatter.ofPattern("HH:mm a");
		SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
		for (AnnouncementTime e : values()) {
			if (e.text.equals(t)) {
				ret = e;
				break;
			}
		}
		if (ret == null) {
			if (t.contains("am") || t.contains("pm")) {
				try {
					String s = t.substring(0,t.length()-3).toUpperCase();
					Date datetime = format.parse(s);
					LocalTime lt = LocalTime.of(datetime.getHours(), datetime.getMinutes());
					if (lt.isBefore(LocalTime.of(8, 0, 0))) {
						ret = AnnouncementTime.BEFORE_OPEN;
					} else if (lt.isAfter(LocalTime.of(17, 0, 0))) {
						ret = AnnouncementTime.AFTER_CLOSE;
					} else {
						ret = AnnouncementTime.MID_DAY;
					}
				} catch (Throwable e) {
					log.error("cant parse time " + t, e);
					ret = AnnouncementTime.CANT_UNDERSTAND;
				}
			}
		}
		return ret;
	}
}
