package com.oak.api.finance.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class FinancialAnalysis {
	public enum Acceptance {
		STRONG_BUY, BUY, HOLD, SELL, STRONG_SELL
	}


	private class SafeMap extends ConcurrentSkipListMap<Date, Double> {
		private static final long serialVersionUID = 2874300412209007915L;

		@Override
		public void putAll(Map<? extends Date, ? extends Double> map) {
			if (map != null) {
				super.putAll(map);
			}
		}
	}

	private final Stock stock;
	private final Economic economic;
	private final List<FinancialComment> comments;
	private final Acceptance acceptance;
	private final SortedMap<Date, Double> currentRatiosQutr = new SafeMap();
	private final SortedMap<Date, Double> quickRatiosQutr = new SafeMap();
	private final SortedMap<Date, Double> assetToDebtRatiosQutr = new SafeMap();
	private final SortedMap<Date, Double> currentRatiosAnnual = new SafeMap();
	private final SortedMap<Date, Double> quickRatiosAnnual = new SafeMap();
	private final SortedMap<Date, Double> assetToDebtRatiosAnnual = new SafeMap();

	public FinancialAnalysis(Stock stock, Economic economic,
			List<FinancialComment> comments, Acceptance acceptance,
			SortedMap<Date, Double> currentRatiosQutr,
			SortedMap<Date, Double> quickRatiosQutr,
			SortedMap<Date, Double> assetToDebtRatiosQutr,
			SortedMap<Date, Double> currentRatiosAnnual,
			SortedMap<Date, Double> quickRatiosAnnual,
			SortedMap<Date, Double> assetToDebtRatiosAnnual) {
		super();
		this.stock = stock;
		this.economic = economic;
		this.comments = comments;
		this.acceptance = acceptance;

		this.currentRatiosQutr.putAll(currentRatiosQutr);
		this.quickRatiosQutr.putAll(quickRatiosQutr);
		this.assetToDebtRatiosQutr.putAll(assetToDebtRatiosQutr);
		this.currentRatiosAnnual.putAll(currentRatiosAnnual);
		this.quickRatiosAnnual.putAll(quickRatiosAnnual);
		this.assetToDebtRatiosAnnual.putAll(assetToDebtRatiosAnnual);
	}

	public Stock getStock() {
		return stock;
	}

	public Economic getEconomic() {
		return economic;
	}

	public void addComment(FinancialComment comment) {
		this.comments.add(comment);
	}

	public Collection<FinancialComment> comments() {
		return Collections.unmodifiableCollection(comments);
	}

	public Acceptance getAcceptance() {
		return acceptance;
	}

	public List<FinancialComment> getComments() {
		return comments;
	}
	private <K,V>SortedMap<K,V> finalSortedMap(SortedMap<K,V>m){
		return Collections.unmodifiableSortedMap(m);
	}
	public SortedMap<Date, Double> getCurrentRatiosQutr() {
		return finalSortedMap(currentRatiosQutr);
	}

	public SortedMap<Date, Double> getQuickRatiosQutr() {
		return finalSortedMap(quickRatiosQutr);
	}

	public SortedMap<Date, Double> getAssetToDebtRatiosQutr() {
		return finalSortedMap(assetToDebtRatiosQutr);
	}

	public SortedMap<Date, Double> getCurrentRatiosAnnual() {
		return finalSortedMap(currentRatiosAnnual);
	}

	public SortedMap<Date, Double> getQuickRatiosAnnual() {
		return finalSortedMap(quickRatiosAnnual);
	}

	public SortedMap<Date, Double> getAssetToDebtRatiosAnnual() {
		return finalSortedMap(assetToDebtRatiosAnnual);
	}

}
