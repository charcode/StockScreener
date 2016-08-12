package com.oak.external.finance.app.marketdata.repository;

public interface StockCodeProvider {
	String getStockCode(String name);

	String getStockDescription(String name);
}
