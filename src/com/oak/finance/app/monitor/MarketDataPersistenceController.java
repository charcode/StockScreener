package com.oak.finance.app.monitor;

import java.util.Date;
import java.util.Map;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;

public interface MarketDataPersistenceController {

	void persist(Map<Stock, Map<Date, Economic>> marketData);


}
