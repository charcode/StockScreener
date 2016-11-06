package com.oak.api.finance.utils.repository;

import java.util.Set;

public interface ReconcilingRepository {
	Set<String> findDistinctTickerNotIn(Set<String>tickers);
	
	default String getName() {
		return this.getClass().getName();
	}
}
