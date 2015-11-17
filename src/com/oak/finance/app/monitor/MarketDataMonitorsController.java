package com.oak.finance.app.monitor;

import java.util.Set;

public interface MarketDataMonitorsController {

	void startStocksAnalysis(Set<String> symbolList, Set<String> interestingSymbols);
}
