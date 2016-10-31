package com.oak.api.finance.model;

public class BalanceSheet {
	String symbol;

	class Assets {
		class CurrentAssets {
			// current assets
			private final Double cashAndEquivalent;
			private final Double shortTermInvestements;
			private final Double netReceivables;
			private final Double inventory;
			private final Double otherCurrentAssets;
			private final Double totalCurrentAssetsCalculated;
			private final Double totalCurrentAssets;

			public CurrentAssets(double cashAndEquivalent,
					double shortTermInvestements, Double netReceivables,
					double inventory, Double otherCurrentAssets,
					double totalCurrentAssets) {
				super();
				this.cashAndEquivalent = cashAndEquivalent;
				this.shortTermInvestements = shortTermInvestements;
				this.netReceivables = netReceivables;
				this.inventory = inventory;
				this.otherCurrentAssets = otherCurrentAssets;
				this.totalCurrentAssets = totalCurrentAssets;
				totalCurrentAssetsCalculated = getOtherCurrentAssets()
						+ getInventory() + getNetReceivables()
						+ getShortTermInvestements() + getCashAndEquivalent();
			}

			private Double getTotalCurrentAssets() {
				return totalCurrentAssets;
			}

			private Double getCashAndEquivalent() {
				return cashAndEquivalent;
			}

			private Double getShortTermInvestements() {
				return shortTermInvestements;
			}

			private Double getNetReceivables() {
				return netReceivables;
			}

			private Double getInventory() {
				return inventory;
			}

			private Double getOtherCurrentAssets() {
				return otherCurrentAssets;
			}

			private Double getTotalCurrentAssetsCalculated() {
				return totalCurrentAssetsCalculated;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				long temp;
				temp = Double.doubleToLongBits(cashAndEquivalent);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(inventory);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(netReceivables);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(otherCurrentAssets);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(shortTermInvestements);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(totalCurrentAssetsCalculated);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				CurrentAssets other = (CurrentAssets) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (Double.doubleToLongBits(cashAndEquivalent) != Double
						.doubleToLongBits(other.cashAndEquivalent))
					return false;
				if (Double.doubleToLongBits(inventory) != Double
						.doubleToLongBits(other.inventory))
					return false;
				if (Double.doubleToLongBits(netReceivables) != Double
						.doubleToLongBits(other.netReceivables))
					return false;
				if (Double.doubleToLongBits(otherCurrentAssets) != Double
						.doubleToLongBits(other.otherCurrentAssets))
					return false;
				if (Double.doubleToLongBits(shortTermInvestements) != Double
						.doubleToLongBits(other.shortTermInvestements))
					return false;
				if (Double.doubleToLongBits(totalCurrentAssetsCalculated) != Double
						.doubleToLongBits(other.totalCurrentAssetsCalculated))
					return false;
				return true;
			}

			private Assets getOuterType() {
				return Assets.this;
			}

		}

		class IlliquidAssets {
			private final Double longTermInvestments;
			private final Double propertyPlantAndEquivalent;
			private final Double goodwill;
			private final Double intangibleAssets;
			private final Double accumulatedAmortization;
			private final Double otherAssets;
			private final Double deferredLongTermAssetCharges;

			private IlliquidAssets(double longTermInvestments,
					double propertyPlantAndEquivalent, Double goodwill,
					double intangibleAssets, Double accumulatedAmortization,
					double otherAssets, Double deferredLongTermAssetCharges) {
				super();
				this.longTermInvestments = longTermInvestments;
				this.propertyPlantAndEquivalent = propertyPlantAndEquivalent;
				this.goodwill = goodwill;
				this.intangibleAssets = intangibleAssets;
				this.accumulatedAmortization = accumulatedAmortization;
				this.otherAssets = otherAssets;
				this.deferredLongTermAssetCharges = deferredLongTermAssetCharges;
			}

			private Double getLongTermInvestments() {
				return longTermInvestments;
			}

			private Double getPropertyPlantAndEquivalent() {
				return propertyPlantAndEquivalent;
			}

			private Double getGoodwill() {
				return goodwill;
			}

			private Double getIntangibleAssets() {
				return intangibleAssets;
			}

			private Double getAccumulatedAmortization() {
				return accumulatedAmortization;
			}

			private Double getOtherAssets() {
				return otherAssets;
			}

			private Double getDeferredLongTermAssetCharges() {
				return deferredLongTermAssetCharges;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				long temp;
				temp = Double.doubleToLongBits(accumulatedAmortization);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(deferredLongTermAssetCharges);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(goodwill);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(intangibleAssets);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(longTermInvestments);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(otherAssets);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(propertyPlantAndEquivalent);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				IlliquidAssets other = (IlliquidAssets) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (Double.doubleToLongBits(accumulatedAmortization) != Double
						.doubleToLongBits(other.accumulatedAmortization))
					return false;
				if (Double.doubleToLongBits(deferredLongTermAssetCharges) != Double
						.doubleToLongBits(other.deferredLongTermAssetCharges))
					return false;
				if (Double.doubleToLongBits(goodwill) != Double
						.doubleToLongBits(other.goodwill))
					return false;
				if (Double.doubleToLongBits(intangibleAssets) != Double
						.doubleToLongBits(other.intangibleAssets))
					return false;
				if (Double.doubleToLongBits(longTermInvestments) != Double
						.doubleToLongBits(other.longTermInvestments))
					return false;
				if (Double.doubleToLongBits(otherAssets) != Double
						.doubleToLongBits(other.otherAssets))
					return false;
				if (Double.doubleToLongBits(propertyPlantAndEquivalent) != Double
						.doubleToLongBits(other.propertyPlantAndEquivalent))
					return false;
				return true;
			}

			private Assets getOuterType() {
				return Assets.this;
			}

		}

		// illiquide assets
		private final CurrentAssets currentAssets;
		private final IlliquidAssets illiquidAssets;
		private final Double totalAssets;

		private Assets(double cashAndEquivalent, Double shortTermInvestements,
				double netReceivables, Double inventory,
				double otherCurrentAssets, Double totalCurrentAssets,
				double longTermInvestments, Double propertyPlantAndEquivalent,
				double goodwill, Double intangibleAssets,
				double accumulatedAmortization, Double otherAssets,
				double deferredLongTermAssetCharges, Double totalAssets) {
			super();
			this.currentAssets = new CurrentAssets(cashAndEquivalent,
					shortTermInvestements, netReceivables, inventory,
					otherCurrentAssets, totalCurrentAssets);
			this.illiquidAssets = new IlliquidAssets(longTermInvestments,
					propertyPlantAndEquivalent, goodwill, intangibleAssets,
					accumulatedAmortization, otherAssets,
					deferredLongTermAssetCharges);
			this.totalAssets = totalAssets;
		}

		private Double getTotalAssets() {
			return totalAssets;
		}

		private Double getCashAndEquivalent() {
			return currentAssets.getCashAndEquivalent();
		}

		private Double getShortTermInvestments() {
			return currentAssets.getShortTermInvestements();
		}

		private Double getNetReceivables() {
			return currentAssets.getNetReceivables();
		}

		private Double getInventory() {
			return currentAssets.getInventory();
		}

		private Double getOtherCurrentAssets() {
			return currentAssets.getOtherCurrentAssets();
		}

		private Double getTotalCurrentAssetsCalculated() {
			return currentAssets.getTotalCurrentAssetsCalculated();
		}

		private Double getTotalCurrentAssets() {
			return currentAssets.getTotalCurrentAssets();
		}

		private Double getLongTermInvestments() {
			return illiquidAssets.getLongTermInvestments();
		}

		private Double getPropertyPlantAndEquivalent() {
			return illiquidAssets.getPropertyPlantAndEquivalent();
		}

		private Double getGoodwill() {
			return illiquidAssets.getGoodwill();
		}

		private Double getIntangibleAssets() {
			return illiquidAssets.getIntangibleAssets();
		}

		private Double getAccumulatedAmortization() {
			return illiquidAssets.getAccumulatedAmortization();
		}

		private Double getOtherAssets() {
			return illiquidAssets.getOtherAssets();
		}

		private Double getDeferredLongTermAssetCharges() {
			return illiquidAssets.getDeferredLongTermAssetCharges();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((currentAssets == null) ? 0 : currentAssets.hashCode());
			result = prime
					* result
					+ ((illiquidAssets == null) ? 0 : illiquidAssets.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Assets other = (Assets) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (currentAssets == null) {
				if (other.currentAssets != null)
					return false;
			} else if (!currentAssets.equals(other.currentAssets))
				return false;
			if (illiquidAssets == null) {
				if (other.illiquidAssets != null)
					return false;
			} else if (!illiquidAssets.equals(other.illiquidAssets))
				return false;
			return true;
		}

		private BalanceSheet getOuterType() {
			return BalanceSheet.this;
		}

	}

	class Liabilities {
		class CurrentLiabilities {
			private final Double accountPayable;
			private final Double shortCurrentLongTermDebt;
			private final Double otherCurrentLiabilities;
			private final Double totalCurrentLiabilities;
			private final Double totalCurrentLiabilitiesCalculated;

			private CurrentLiabilities(double accountPayable,
					double shortCurrentLongTermDebt,
					double otherCurrentLiabilities,
					double totalCurrentLiabilities) {
				super();
				this.accountPayable = accountPayable;
				this.shortCurrentLongTermDebt = shortCurrentLongTermDebt;
				this.otherCurrentLiabilities = otherCurrentLiabilities;
				this.totalCurrentLiabilities = totalCurrentLiabilities;
				this.totalCurrentLiabilitiesCalculated = getAccountPayable()
						+ getShortCurrentLongTermDebt()
						+ getOtherCurrentLiabilities();
			}

			private Double getAccountPayable() {
				return accountPayable;
			}

			private Double getShortCurrentLongTermDebt() {
				return shortCurrentLongTermDebt;
			}

			private Double getTotalCurrentLiabilities() {
				return totalCurrentLiabilities;
			}

			private Double getOtherCurrentLiabilities() {
				return otherCurrentLiabilities;
			}

			private Double getTotalCurrentLiabilitiesCalculated() {
				return totalCurrentLiabilitiesCalculated;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				long temp;
				temp = Double.doubleToLongBits(accountPayable);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(otherCurrentLiabilities);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(shortCurrentLongTermDebt);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double
						.doubleToLongBits(totalCurrentLiabilitiesCalculated);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				CurrentLiabilities other = (CurrentLiabilities) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (Double.doubleToLongBits(accountPayable) != Double
						.doubleToLongBits(other.accountPayable))
					return false;
				if (Double.doubleToLongBits(otherCurrentLiabilities) != Double
						.doubleToLongBits(other.otherCurrentLiabilities))
					return false;
				if (Double.doubleToLongBits(shortCurrentLongTermDebt) != Double
						.doubleToLongBits(other.shortCurrentLongTermDebt))
					return false;
				if (Double.doubleToLongBits(totalCurrentLiabilitiesCalculated) != Double
						.doubleToLongBits(other.totalCurrentLiabilitiesCalculated))
					return false;
				return true;
			}

			private Liabilities getOuterType() {
				return Liabilities.this;
			}

		}

		class LongTermLiabilities {
			final Double longTermDebt;
			final Double otherLiabilities;
			final Double deferredLongTermLiabilityCharges;
			final Double minorityInterest;
			final Double negativeGoodwill;

			private LongTermLiabilities(double longTermDebt,
					double otherLiabilities,
					double deferredLongTermLiabilityCharges,
					double minorityInterest, Double negativeGoodwill) {
				super();
				this.longTermDebt = longTermDebt;
				this.otherLiabilities = otherLiabilities;
				this.deferredLongTermLiabilityCharges = deferredLongTermLiabilityCharges;
				this.minorityInterest = minorityInterest;
				this.negativeGoodwill = negativeGoodwill;
			}

			private Double getLongTermDebt() {
				return longTermDebt;
			}

			private Double getOtherLiabilities() {
				return otherLiabilities;
			}

			private Double getDeferredLongTermLiabilityCharges() {
				return deferredLongTermLiabilityCharges;
			}

			private Double getMinorityInterest() {
				return minorityInterest;
			}

			private Double getNegativeGoodwill() {
				return negativeGoodwill;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				long temp;
				temp = Double
						.doubleToLongBits(deferredLongTermLiabilityCharges);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(longTermDebt);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(minorityInterest);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(negativeGoodwill);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(otherLiabilities);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				LongTermLiabilities other = (LongTermLiabilities) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (Double.doubleToLongBits(deferredLongTermLiabilityCharges) != Double
						.doubleToLongBits(other.deferredLongTermLiabilityCharges))
					return false;
				if (Double.doubleToLongBits(longTermDebt) != Double
						.doubleToLongBits(other.longTermDebt))
					return false;
				if (Double.doubleToLongBits(minorityInterest) != Double
						.doubleToLongBits(other.minorityInterest))
					return false;
				if (Double.doubleToLongBits(negativeGoodwill) != Double
						.doubleToLongBits(other.negativeGoodwill))
					return false;
				if (Double.doubleToLongBits(otherLiabilities) != Double
						.doubleToLongBits(other.otherLiabilities))
					return false;
				return true;
			}

			private Liabilities getOuterType() {
				return Liabilities.this;
			}
		}

		private final LongTermLiabilities longTermLiabilities;
		private final CurrentLiabilities currentLiabilities;
		private final Double totalLiabilities;

		private Liabilities(double accountPayable,
				double shortCurrentLongTermDebt,
				double otherCurrentLiabilities, Double totalCurrentLiabilities,
				double longTermDebt, Double otherLiabilities,
				double deferredLongTermLiabilityCharges,
				double minorityInterest, Double negativeGoodwill,
				double totalLiabilities) {
			this.currentLiabilities = new CurrentLiabilities(accountPayable,
					shortCurrentLongTermDebt, otherCurrentLiabilities,
					totalCurrentLiabilities);
			this.longTermLiabilities = new LongTermLiabilities(longTermDebt,
					otherLiabilities, deferredLongTermLiabilityCharges,
					minorityInterest, negativeGoodwill);
			this.totalLiabilities = totalLiabilities;
		}

		private Double getLongTermDebt() {
			return longTermLiabilities.getLongTermDebt();
		}

		private Double getTotalCurrentLiabilities() {
			return currentLiabilities.getTotalCurrentLiabilities();
		}

		private Double getTotalLiabilities() {
			return totalLiabilities;
		}

		private Double getOtherLiabilities() {
			return longTermLiabilities.getOtherLiabilities();
		}

		private Double getDeferredLongTermLiabilityCharges() {
			return longTermLiabilities.getDeferredLongTermLiabilityCharges();
		}

		private Double getMinorityInterest() {
			return longTermLiabilities.getMinorityInterest();
		}

		private Double getNegativeGoodwill() {
			return longTermLiabilities.getNegativeGoodwill();
		}

		private Double getAccountPayable() {
			return currentLiabilities.getAccountPayable();
		}

		private Double getShortCurrentLongTermDebt() {
			return currentLiabilities.getShortCurrentLongTermDebt();
		}

		private Double getOtherCurrentLiabilities() {
			return currentLiabilities.getOtherCurrentLiabilities();
		}

		private Double getTotalCurrentLiabilitiesCalculated() {
			return currentLiabilities.getTotalCurrentLiabilitiesCalculated();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime
					* result
					+ ((currentLiabilities == null) ? 0 : currentLiabilities
							.hashCode());
			result = prime
					* result
					+ ((longTermLiabilities == null) ? 0 : longTermLiabilities
							.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Liabilities other = (Liabilities) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (currentLiabilities == null) {
				if (other.currentLiabilities != null)
					return false;
			} else if (!currentLiabilities.equals(other.currentLiabilities))
				return false;
			if (longTermLiabilities == null) {
				if (other.longTermLiabilities != null)
					return false;
			} else if (!longTermLiabilities.equals(other.longTermLiabilities))
				return false;
			return true;
		}

		private BalanceSheet getOuterType() {
			return BalanceSheet.this;
		}

	}

	class StockholdersEquity {
		double miscStocksOptionsWarrants;
		double redeemablePreferredStocks;
		double commonStock;
		double preferredStock;
		double retainedEarnings;
		double treasuryStock;
		double capitalSurplus;
		double otherstockholdersEquity;

		private StockholdersEquity(double miscStocksOptionsWarrants,
				double redeemablePreferredStocks, Double commonStock,
				double preferredStock, Double retainedEarnings,
				double treasuryStock, Double capitalSurplus,
				double otherstockholdersEquity) {
			super();
			this.miscStocksOptionsWarrants = miscStocksOptionsWarrants;
			this.redeemablePreferredStocks = redeemablePreferredStocks;
			this.commonStock = commonStock;
			this.preferredStock = preferredStock;
			this.retainedEarnings = retainedEarnings;
			this.treasuryStock = treasuryStock;
			this.capitalSurplus = capitalSurplus;
			this.otherstockholdersEquity = otherstockholdersEquity;
		}

		private Double getMiscStocksOptionsWarrants() {
			return miscStocksOptionsWarrants;
		}

		private Double getRedeemablePreferredStocks() {
			return redeemablePreferredStocks;
		}

		private Double getCommonStock() {
			return commonStock;
		}

		private Double getPreferredStock() {
			return preferredStock;
		}

		private Double getRetainedEarnings() {
			return retainedEarnings;
		}

		private Double getTreasuryStock() {
			return treasuryStock;
		}

		private Double getCapitalSurplus() {
			return capitalSurplus;
		}

		private Double getOtherstockholdersEquity() {
			return otherstockholdersEquity;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(capitalSurplus);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(commonStock);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(miscStocksOptionsWarrants);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(otherstockholdersEquity);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(preferredStock);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(redeemablePreferredStocks);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(retainedEarnings);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(treasuryStock);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StockholdersEquity other = (StockholdersEquity) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Double.doubleToLongBits(capitalSurplus) != Double
					.doubleToLongBits(other.capitalSurplus))
				return false;
			if (Double.doubleToLongBits(commonStock) != Double
					.doubleToLongBits(other.commonStock))
				return false;
			if (Double.doubleToLongBits(miscStocksOptionsWarrants) != Double
					.doubleToLongBits(other.miscStocksOptionsWarrants))
				return false;
			if (Double.doubleToLongBits(otherstockholdersEquity) != Double
					.doubleToLongBits(other.otherstockholdersEquity))
				return false;
			if (Double.doubleToLongBits(preferredStock) != Double
					.doubleToLongBits(other.preferredStock))
				return false;
			if (Double.doubleToLongBits(redeemablePreferredStocks) != Double
					.doubleToLongBits(other.redeemablePreferredStocks))
				return false;
			if (Double.doubleToLongBits(retainedEarnings) != Double
					.doubleToLongBits(other.retainedEarnings))
				return false;
			if (Double.doubleToLongBits(treasuryStock) != Double
					.doubleToLongBits(other.treasuryStock))
				return false;
			return true;
		}

		private BalanceSheet getOuterType() {
			return BalanceSheet.this;
		}

	}

	private final Assets assets;
	private final Liabilities liabilities;
	private final StockholdersEquity stockholdersEquity;

	public BalanceSheet(String symbol, Double cashAndEquivalent,
			double shortTermInvestements, Double netReceivables,
			double inventory, Double otherCurrentAssets,
			double totalCurrentAssets, Double longTermInvestments,
			double propertyPlantAndEquivalent, Double goodwill,
			double intangibleAssets, Double accumulatedAmortization,
			double otherAssets, Double totalAssets,
			double deferredLongTermAssetCharges, Double accountPayable,
			double shortCurrentLongTermDebt, Double otherCurrentLiabilities,
			double totalCurrentLiabilities, Double longTermDebt,
			double otherLiabilities, Double totalLiabilities,
			double deferredLongTermLiabilityCharges, Double minorityInterest,
			double negativeGoodwill, Double miscStocksOptionsWarrants,
			double redeemablePreferredStocks, Double commonStock,
			double preferredStock, Double retainedEarnings,
			double treasuryStock, Double capitalSurplus,
			double otherstockholdersEquity) {
		this.symbol = symbol;
		this.assets = new Assets(cashAndEquivalent, shortTermInvestements,
				netReceivables, inventory, otherCurrentAssets,
				totalCurrentAssets, longTermInvestments,
				propertyPlantAndEquivalent, goodwill, intangibleAssets,
				accumulatedAmortization, otherAssets,
				deferredLongTermAssetCharges, totalAssets);
		this.liabilities = new Liabilities(accountPayable,
				shortCurrentLongTermDebt, otherCurrentLiabilities,
				totalCurrentLiabilities, longTermDebt, otherLiabilities,
				deferredLongTermLiabilityCharges, minorityInterest,
				negativeGoodwill, totalLiabilities);
		this.stockholdersEquity = new StockholdersEquity(
				miscStocksOptionsWarrants, redeemablePreferredStocks,
				commonStock, preferredStock, retainedEarnings, treasuryStock,
				capitalSurplus, otherstockholdersEquity);
	}

	public String getSymbol() {
		return symbol;
	}

	public Double getCashAndEquivalent() {
		return assets.getCashAndEquivalent();
	}

	public Double getShortTermInvestments() {
		return assets.getShortTermInvestments();
	}

	public Double getNetReceivables() {
		return assets.getNetReceivables();
	}

	public Double getInventory() {
		return assets.getInventory();
	}

	public Double getOtherCurrentAssets() {
		return assets.getOtherCurrentAssets();
	}

	public Double getLongTermInvestments() {
		return assets.getLongTermInvestments();
	}

	public Double getPropertyPlantAndEquivalent() {
		return assets.getPropertyPlantAndEquivalent();
	}

	public Double getGoodwill() {
		return assets.getGoodwill();
	}

	public Double getIntangibleAssets() {
		return assets.getIntangibleAssets();
	}

	public Double getAccumulatedAmortization() {
		return assets.getAccumulatedAmortization();
	}

	public Double getOtherAssets() {
		return assets.getOtherAssets();
	}

	public Double getDeferredLongTermAssetCharges() {
		return assets.getDeferredLongTermAssetCharges();
	}

	public Double getTotalCurrentAssetsCalculated() {
		return assets.getTotalCurrentAssetsCalculated();
	}

	public Double getTotalCurrentAssets() {
		return assets.getTotalCurrentAssets();
	}

	public Double getTotalAssets() {
		return assets.getTotalAssets();
	}

	public Double getLongTermDebt() {
		return liabilities.getLongTermDebt();
	}

	public Double getOtherLiabilities() {
		return liabilities.getOtherLiabilities();
	}

	public Double getDeferredLongTermLiabilityCharges() {
		return liabilities.getDeferredLongTermLiabilityCharges();
	}

	public Double getMinorityInterest() {
		return liabilities.getMinorityInterest();
	}

	public Double getNegativeGoodwill() {
		return liabilities.getNegativeGoodwill();
	}

	public Double getAccountPayable() {
		return liabilities.getAccountPayable();
	}

	public Double getShortCurrentLongTermDebt() {
		return liabilities.getShortCurrentLongTermDebt();
	}

	public Double getOtherCurrentLiabilities() {
		return liabilities.getOtherCurrentLiabilities();
	}

	public Double getTotalCurrentLiabilitiesCalculated() {
		return liabilities.getTotalCurrentLiabilitiesCalculated();
	}

	public Double getTotalLiabilities() {
		return liabilities.getTotalLiabilities();
	}

	public Double getTotalCurrentLiabilities() {
		return liabilities.getTotalCurrentLiabilities();
	}

	public Double getMiscStocksOptionsWarrants() {
		return stockholdersEquity.getMiscStocksOptionsWarrants();
	}

	public Double getRedeemablePreferredStocks() {
		return stockholdersEquity.getRedeemablePreferredStocks();
	}

	public Double getCommonStock() {
		return stockholdersEquity.getCommonStock();
	}

	public Double getPreferredStock() {
		return stockholdersEquity.getPreferredStock();
	}

	public Double getRetainedEarnings() {
		return stockholdersEquity.getRetainedEarnings();
	}

	public Double getTreasuryStock() {
		return stockholdersEquity.getTreasuryStock();
	}

	public Double getCapitalSurplus() {
		return stockholdersEquity.getCapitalSurplus();
	}

	public Double getOtherStockholdersEquity() {
		return stockholdersEquity.getOtherstockholdersEquity();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assets == null) ? 0 : assets.hashCode());
		result = prime * result
				+ ((liabilities == null) ? 0 : liabilities.hashCode());
		result = prime
				* result
				+ ((stockholdersEquity == null) ? 0 : stockholdersEquity
						.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BalanceSheet other = (BalanceSheet) obj;
		if (assets == null) {
			if (other.assets != null)
				return false;
		} else if (!assets.equals(other.assets))
			return false;
		if (liabilities == null) {
			if (other.liabilities != null)
				return false;
		} else if (!liabilities.equals(other.liabilities))
			return false;
		if (stockholdersEquity == null) {
			if (other.stockholdersEquity != null)
				return false;
		} else if (!stockholdersEquity.equals(other.stockholdersEquity))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

}
