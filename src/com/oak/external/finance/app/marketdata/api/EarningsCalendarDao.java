package com.oak.external.finance.app.marketdata.api;

import java.util.Date;
import java.util.List;

import com.oak.api.finance.model.dto.EarningsCalendar;

public interface EarningsCalendarDao {

	List<EarningsCalendar> getEarningsCalendarByDate(Date lastLoadedEarningsCalendarDate);

}