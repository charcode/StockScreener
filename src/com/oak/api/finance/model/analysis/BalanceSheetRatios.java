package com.oak.api.finance.model.analysis;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class BalanceSheetRatios {
	
	/**
	 * Current asset to current liabilities
	 *  currentRatio > 2.0 is good
	 */
	private final SortedMap<Date, Double> currentRatios;
	
	/**
	 * very quick access cash to current liabilities
	 */
	private final SortedMap<Date, Double> quickRatios;
	
	private final SortedMap<Date,Double>totalAssetToDebt;
	
	public BalanceSheetRatios(){
		currentRatios = new TreeMap<Date, Double>();
		quickRatios = new TreeMap<Date, Double>();
		totalAssetToDebt = new TreeMap<Date, Double>();
	}
}
