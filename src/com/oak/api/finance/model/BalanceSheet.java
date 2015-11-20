package com.oak.api.finance.model;

public class BalanceSheet {
	String symbol;

	class Assets {
		class CurrentAssets {
			// current assets
			private final double cashAndEquivalent;
			private final double shortTermInvestements;
			private final double netReceivables;
			private final double inventory;
			private final double otherCurrentAssets;
			private final double totalCurrentAssetsCalculated;
			private final double totalCurrentAssets;

			public CurrentAssets(double cashAndEquivalent,
					double shortTermInvestements, double netReceivables,
					double inventory, double otherCurrentAssets,
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

			private double getTotalCurrentAssets() {
				return totalCurrentAssets;
			}

			private double getCashAndEquivalent() {
				return cashAndEquivalent;
			}

			private double getShortTermInvestements() {
				return shortTermInvestements;
			}

			private double getNetReceivables() {
				return netReceivables;
			}

			private double getInventory() {
				return inventory;
			}

			private double getOtherCurrentAssets() {
				return otherCurrentAssets;
			}

			private double getTotalCurrentAssetsCalculated() {
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
			private final double longTermInvestments;
			private final double propertyPlantAndEquivalent;
			private final double goodwill;
			private final double intangibleAssets;
			private final double accumulatedAmortization;
			private final double otherAssets;
			private final double deferredLongTermAssetCharges;

			private IlliquidAssets(double longTermInvestments,
					double propertyPlantAndEquivalent, double goodwill,
					double intangibleAssets, double accumulatedAmortization,
					double otherAssets, double deferredLongTermAssetCharges) {
				super();
				this.longTermInvestments = longTermInvestments;
				this.propertyPlantAndEquivalent = propertyPlantAndEquivalent;
				this.goodwill = goodwill;
				this.intangibleAssets = intangibleAssets;
				this.accumulatedAmortization = accumulatedAmortization;
				this.otherAssets = otherAssets;
				this.deferredLongTermAssetCharges = deferredLongTermAssetCharges;
			}

			private double getLongTermInvestments() {
				return longTermInvestments;
			}

			private double getPropertyPlantAndEquivalent() {
				return propertyPlantAndEquivalent;
			}

			private double getGoodwill() {
				return goodwill;
			}

			private double getIntangibleAssets() {
				return intangibleAssets;
			}

			private double getAccumulatedAmortization() {
				return accumulatedAmortization;
			}

			private double getOtherAssets() {
				return otherAssets;
			}

			private double getDeferredLongTermAssetCharges() {
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
		private final double totalAssets;

		private Assets(double cashAndEquivalent, double shortTermInvestements,
				double netReceivables, double inventory,
				double otherCurrentAssets, double totalCurrentAssets,
				double longTermInvestments, double propertyPlantAndEquivalent,
				double goodwill, double intangibleAssets,
				double accumulatedAmortization, double otherAssets,
				double deferredLongTermAssetCharges, double totalAssets) {
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

		private double getTotalAssets() {
			return totalAssets;
		}

		private double getCashAndEquivalent() {
			return currentAssets.getCashAndEquivalent();
		}

		private double getShortTermInvestements() {
			return currentAssets.getShortTermInvestements();
		}

		private double getNetReceivables() {
			return currentAssets.getNetReceivables();
		}

		private double getInventory() {
			return currentAssets.getInventory();
		}

		private double getOtherCurrentAssets() {
			return currentAssets.getOtherCurrentAssets();
		}

		private double getTotalCurrentAssetsCalculated() {
			return currentAssets.getTotalCurrentAssetsCalculated();
		}

		private double getTotalCurrentAssets() {
			return currentAssets.getTotalCurrentAssets();
		}

		private double getLongTermInvestments() {
			return illiquidAssets.getLongTermInvestments();
		}

		private double getPropertyPlantAndEquivalent() {
			return illiquidAssets.getPropertyPlantAndEquivalent();
		}

		private double getGoodwill() {
			return illiquidAssets.getGoodwill();
		}

		private double getIntangibleAssets() {
			return illiquidAssets.getIntangibleAssets();
		}

		private double getAccumulatedAmortization() {
			return illiquidAssets.getAccumulatedAmortization();
		}

		private double getOtherAssets() {
			return illiquidAssets.getOtherAssets();
		}

		private double getDeferredLongTermAssetCharges() {
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
			private final double accountPayable;
			private final double shortCurrentLongTermDebt;
			private final double otherCurrentLiabilities;
			private final double totalCurrentLiabilities;
			private final double totalCurrentLiabilitiesCalculated;

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

			private double getAccountPayable() {
				return accountPayable;
			}

			private double getShortCurrentLongTermDebt() {
				return shortCurrentLongTermDebt;
			}

			private double getTotalCurrentLiabilities() {
				return totalCurrentLiabilities;
			}

			private double getOtherCurrentLiabilities() {
				return otherCurrentLiabilities;
			}

			private double getTotalCurrentLiabilitiesCalculated() {
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
			final double longTermDebt;
			final double otherLiabilities;
			final double deferredLongTermLiabilityCharges;
			final double minorityInterest;
			final double negativeGoodwill;

			private LongTermLiabilities(double longTermDebt,
					double otherLiabilities,
					double deferredLongTermLiabilityCharges,
					double minorityInterest, double negativeGoodwill) {
				super();
				this.longTermDebt = longTermDebt;
				this.otherLiabilities = otherLiabilities;
				this.deferredLongTermLiabilityCharges = deferredLongTermLiabilityCharges;
				this.minorityInterest = minorityInterest;
				this.negativeGoodwill = negativeGoodwill;
			}

			private double getLongTermDebt() {
				return longTermDebt;
			}

			private double getOtherLiabilities() {
				return otherLiabilities;
			}

			private double getDeferredLongTermLiabilityCharges() {
				return deferredLongTermLiabilityCharges;
			}

			private double getMinorityInterest() {
				return minorityInterest;
			}

			private double getNegativeGoodwill() {
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
		private final double totalLiabilities;

		private Liabilities(double accountPayable,
				double shortCurrentLongTermDebt,
				double otherCurrentLiabilities, double totalCurrentLiabilities,
				double longTermDebt, double otherLiabilities,
				double deferredLongTermLiabilityCharges,
				double minorityInterest, double negativeGoodwill,
				double totalLiabilities) {
			this.currentLiabilities = new CurrentLiabilities(accountPayable,
					shortCurrentLongTermDebt, otherCurrentLiabilities,
					totalCurrentLiabilities);
			this.longTermLiabilities = new LongTermLiabilities(longTermDebt,
					otherLiabilities, deferredLongTermLiabilityCharges,
					minorityInterest, negativeGoodwill);
			this.totalLiabilities = totalLiabilities;
		}

		private double getLongTermDebt() {
			return longTermLiabilities.getLongTermDebt();
		}

		private double getTotalCurrentLiabilities() {
			return currentLiabilities.getTotalCurrentLiabilities();
		}

		private double getTotalLiabilities() {
			return totalLiabilities;
		}

		private double getOtherLiabilities() {
			return longTermLiabilities.getOtherLiabilities();
		}

		private double getDeferredLongTermLiabilityCharges() {
			return longTermLiabilities.getDeferredLongTermLiabilityCharges();
		}

		private double getMinorityInterest() {
			return longTermLiabilities.getMinorityInterest();
		}

		private double getNegativeGoodwill() {
			return longTermLiabilities.getNegativeGoodwill();
		}

		private double getAccountPayable() {
			return currentLiabilities.getAccountPayable();
		}

		private double getShortCurrentLongTermDebt() {
			return currentLiabilities.getShortCurrentLongTermDebt();
		}

		private double getOtherCurrentLiabilities() {
			return currentLiabilities.getOtherCurrentLiabilities();
		}

		private double getTotalCurrentLiabilitiesCalculated() {
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
				double redeemablePreferredStocks, double commonStock,
				double preferredStock, double retainedEarnings,
				double treasuryStock, double capitalSurplus,
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

		private double getMiscStocksOptionsWarrants() {
			return miscStocksOptionsWarrants;
		}

		private double getRedeemablePreferredStocks() {
			return redeemablePreferredStocks;
		}

		private double getCommonStock() {
			return commonStock;
		}

		private double getPreferredStock() {
			return preferredStock;
		}

		private double getRetainedEarnings() {
			return retainedEarnings;
		}

		private double getTreasuryStock() {
			return treasuryStock;
		}

		private double getCapitalSurplus() {
			return capitalSurplus;
		}

		private double getOtherstockholdersEquity() {
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

	public BalanceSheet(String symbol, double cashAndEquivalent,
			double shortTermInvestements, double netReceivables,
			double inventory, double otherCurrentAssets,
			double totalCurrentAssets, double longTermInvestments,
			double propertyPlantAndEquivalent, double goodwill,
			double intangibleAssets, double accumulatedAmortization,
			double otherAssets, double totalAssets,
			double deferredLongTermAssetCharges, double accountPayable,
			double shortCurrentLongTermDebt, double otherCurrentLiabilities,
			double totalCurrentLiabilities, double longTermDebt,
			double otherLiabilities, double totalLiabilities,
			double deferredLongTermLiabilityCharges, double minorityInterest,
			double negativeGoodwill, double miscStocksOptionsWarrants,
			double redeemablePreferredStocks, double commonStock,
			double preferredStock, double retainedEarnings,
			double treasuryStock, double capitalSurplus,
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

	public double getCashAndEquivalent() {
		return assets.getCashAndEquivalent();
	}

	public double getShortTermInvestements() {
		return assets.getShortTermInvestements();
	}

	public double getNetReceivables() {
		return assets.getNetReceivables();
	}

	public double getInventory() {
		return assets.getInventory();
	}

	public double getOtherCurrentAssets() {
		return assets.getOtherCurrentAssets();
	}

	public double getLongTermInvestments() {
		return assets.getLongTermInvestments();
	}

	public double getPropertyPlantAndEquivalent() {
		return assets.getPropertyPlantAndEquivalent();
	}

	public double getGoodwill() {
		return assets.getGoodwill();
	}

	public double getIntangibleAssets() {
		return assets.getIntangibleAssets();
	}

	public double getAccumulatedAmortization() {
		return assets.getAccumulatedAmortization();
	}

	public double getOtherAssets() {
		return assets.getOtherAssets();
	}

	public double getDeferredLongTermAssetCharges() {
		return assets.getDeferredLongTermAssetCharges();
	}

	public double getTotalCurrentAssetsCalculated() {
		return assets.getTotalCurrentAssetsCalculated();
	}

	public double getTotalCurrentAssets() {
		return assets.getTotalCurrentAssets();
	}

	public double getTotalAssets() {
		return assets.getTotalAssets();
	}

	public double getLongTermDebt() {
		return liabilities.getLongTermDebt();
	}

	public double getOtherLiabilities() {
		return liabilities.getOtherLiabilities();
	}

	public double getDeferredLongTermLiabilityCharges() {
		return liabilities.getDeferredLongTermLiabilityCharges();
	}

	public double getMinorityInterest() {
		return liabilities.getMinorityInterest();
	}

	public double getNegativeGoodwill() {
		return liabilities.getNegativeGoodwill();
	}

	public double getAccountPayable() {
		return liabilities.getAccountPayable();
	}

	public double getShortCurrentLongTermDebt() {
		return liabilities.getShortCurrentLongTermDebt();
	}

	public double getOtherCurrentLiabilities() {
		return liabilities.getOtherCurrentLiabilities();
	}

	public double getTotalCurrentLiabilitiesCalculated() {
		return liabilities.getTotalCurrentLiabilitiesCalculated();
	}

	public double getTotalLiabilities() {
		return liabilities.getTotalLiabilities();
	}

	public double getTotalCurrentLiabilities() {
		return liabilities.getTotalCurrentLiabilities();
	}

	public double getMiscStocksOptionsWarrants() {
		return stockholdersEquity.getMiscStocksOptionsWarrants();
	}

	public double getRedeemablePreferredStocks() {
		return stockholdersEquity.getRedeemablePreferredStocks();
	}

	public double getCommonStock() {
		return stockholdersEquity.getCommonStock();
	}

	public double getPreferredStock() {
		return stockholdersEquity.getPreferredStock();
	}

	public double getRetainedEarnings() {
		return stockholdersEquity.getRetainedEarnings();
	}

	public double getTreasuryStock() {
		return stockholdersEquity.getTreasuryStock();
	}

	public double getCapitalSurplus() {
		return stockholdersEquity.getCapitalSurplus();
	}

	public double getOtherstockholdersEquity() {
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
