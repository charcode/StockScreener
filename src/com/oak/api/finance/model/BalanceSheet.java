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
	    private double totalCurrentAssetCalculated;

	    public CurrentAssets(double cashAndEquivalent,
		    double shortTermInvestements, double netReceivables,
		    double inventory, double otherCurrentAssets) {
		super();
		this.cashAndEquivalent = cashAndEquivalent;
		this.shortTermInvestements = shortTermInvestements;
		this.netReceivables = netReceivables;
		this.inventory = inventory;
		this.otherCurrentAssets = otherCurrentAssets;
		totalCurrentAssetCalculated = getOtherCurrentAssets()
			+ getInventory() + getNetReceivables()
			+ getShortTermInvestements() + getCashAndEquivalent();
	    }

	    public double getCashAndEquivalent() {
		return cashAndEquivalent;
	    }

	    public double getShortTermInvestements() {
		return shortTermInvestements;
	    }

	    public double getNetReceivables() {
		return netReceivables;
	    }

	    public double getInventory() {
		return inventory;
	    }

	    public double getOtherCurrentAssets() {
		return otherCurrentAssets;
	    }

	    public double getTotalCurrentAssetsCalculated() {
		return totalCurrentAssetCalculated;
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
		temp = Double.doubleToLongBits(totalCurrentAssetCalculated);
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
		if (Double.doubleToLongBits(totalCurrentAssetCalculated) != Double
			.doubleToLongBits(other.totalCurrentAssetCalculated))
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

	    public IlliquidAssets(double longTermInvestments,
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

	    public double getLongTermInvestments() {
		return longTermInvestments;
	    }

	    public double getPropertyPlantAndEquivalent() {
		return propertyPlantAndEquivalent;
	    }

	    public double getGoodwill() {
		return goodwill;
	    }

	    public double getIntangibleAssets() {
		return intangibleAssets;
	    }

	    public double getAccumulatedAmortization() {
		return accumulatedAmortization;
	    }

	    public double getOtherAssets() {
		return otherAssets;
	    }

	    public double getDeferredLongTermAssetCharges() {
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

	public Assets(double cashAndEquivalent, double shortTermInvestements,
		double netReceivables, double inventory,
		double otherCurrentAssets, double longTermInvestments,
		double propertyPlantAndEquivalent, double goodwill,
		double intangibleAssets, double accumulatedAmortization,
		double otherAssets, double deferredLongTermAssetCharges) {
	    super();
	    this.currentAssets = new CurrentAssets(cashAndEquivalent,
		    shortTermInvestements, netReceivables, inventory,
		    otherCurrentAssets);
	    this.illiquidAssets = new IlliquidAssets(longTermInvestments,
		    propertyPlantAndEquivalent, goodwill, intangibleAssets,
		    accumulatedAmortization, otherAssets,
		    deferredLongTermAssetCharges);
	}

	public double getCashAndEquivalent() {
	    return currentAssets.getCashAndEquivalent();
	}

	public double getShortTermInvestements() {
	    return currentAssets.getShortTermInvestements();
	}

	public double getNetReceivables() {
	    return currentAssets.getNetReceivables();
	}

	public double getInventory() {
	    return currentAssets.getInventory();
	}

	public double getOtherCurrentAssets() {
	    return currentAssets.getOtherCurrentAssets();
	}

	public double getTotalCurrentAssetsCalculated() {
	    return currentAssets.getTotalCurrentAssetsCalculated();
	}

	public double getLongTermInvestments() {
	    return illiquidAssets.getLongTermInvestments();
	}

	public double getPropertyPlantAndEquivalent() {
	    return illiquidAssets.getPropertyPlantAndEquivalent();
	}

	public double getGoodwill() {
	    return illiquidAssets.getGoodwill();
	}

	public double getIntangibleAssets() {
	    return illiquidAssets.getIntangibleAssets();
	}

	public double getAccumulatedAmortization() {
	    return illiquidAssets.getAccumulatedAmortization();
	}

	public double getOtherAssets() {
	    return illiquidAssets.getOtherAssets();
	}

	public double getDeferredLongTermAssetCharges() {
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
	    private final double totalCurrentLiabilitiesCalculated;

	    public CurrentLiabilities(double accountPayable,
		    double shortCurrentLongTermDebt,
		    double otherCurrentLiabilities) {
		super();
		this.accountPayable = accountPayable;
		this.shortCurrentLongTermDebt = shortCurrentLongTermDebt;
		this.otherCurrentLiabilities = otherCurrentLiabilities;
		this.totalCurrentLiabilitiesCalculated = getAccountPayable()
			+ getShortCurrentLongTermDebt()
			+ getOtherCurrentLiabilities();
	    }

	    public double getAccountPayable() {
		return accountPayable;
	    }

	    public double getShortCurrentLongTermDebt() {
		return shortCurrentLongTermDebt;
	    }

	    public double getOtherCurrentLiabilities() {
		return otherCurrentLiabilities;
	    }

	    public double getTotalCurrentLiabilitiesCalculated() {
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

	    public LongTermLiabilities(double longTermDebt,
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

	    public double getLongTermDebt() {
		return longTermDebt;
	    }

	    public double getOtherLiabilities() {
		return otherLiabilities;
	    }

	    public double getDeferredLongTermLiabilityCharges() {
		return deferredLongTermLiabilityCharges;
	    }

	    public double getMinorityInterest() {
		return minorityInterest;
	    }

	    public double getNegativeGoodwill() {
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

	public Liabilities(double accountPayable,
		double shortCurrentLongTermDebt,
		double otherCurrentLiabilities, double longTermDebt,
		double otherLiabilities,
		double deferredLongTermLiabilityCharges,
		double minorityInterest, double negativeGoodwill) {
	    this.currentLiabilities = new CurrentLiabilities(accountPayable,
		    shortCurrentLongTermDebt, otherCurrentLiabilities);
	    this.longTermLiabilities = new LongTermLiabilities(longTermDebt,
		    otherLiabilities, deferredLongTermLiabilityCharges,
		    minorityInterest, negativeGoodwill);
	}

	public double getLongTermDebt() {
	    return longTermLiabilities.getLongTermDebt();
	}

	public double getOtherLiabilities() {
	    return longTermLiabilities.getOtherLiabilities();
	}

	public double getDeferredLongTermLiabilityCharges() {
	    return longTermLiabilities.getDeferredLongTermLiabilityCharges();
	}

	public double getMinorityInterest() {
	    return longTermLiabilities.getMinorityInterest();
	}

	public double getNegativeGoodwill() {
	    return longTermLiabilities.getNegativeGoodwill();
	}

	public double getAccountPayable() {
	    return currentLiabilities.getAccountPayable();
	}

	public double getShortCurrentLongTermDebt() {
	    return currentLiabilities.getShortCurrentLongTermDebt();
	}

	public double getOtherCurrentLiabilities() {
	    return currentLiabilities.getOtherCurrentLiabilities();
	}

	public double getTotalCurrentLiabilitiesCalculated() {
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

	public StockholdersEquity(double miscStocksOptionsWarrants,
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

	public double getMiscStocksOptionsWarrants() {
	    return miscStocksOptionsWarrants;
	}

	public double getRedeemablePreferredStocks() {
	    return redeemablePreferredStocks;
	}

	public double getCommonStock() {
	    return commonStock;
	}

	public double getPreferredStock() {
	    return preferredStock;
	}

	public double getRetainedEarnings() {
	    return retainedEarnings;
	}

	public double getTreasuryStock() {
	    return treasuryStock;
	}

	public double getCapitalSurplus() {
	    return capitalSurplus;
	}

	public double getOtherstockholdersEquity() {
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
	    double longTermInvestments, double propertyPlantAndEquivalent,
	    double goodwill, double intangibleAssets,
	    double accumulatedAmortization, double otherAssets,
	    double deferredLongTermAssetCharges, double accountPayable,
	    double shortCurrentLongTermDebt, double otherCurrentLiabilities,
	    double longTermDebt, double otherLiabilities,
	    double deferredLongTermLiabilityCharges, double minorityInterest,
	    double negativeGoodwill, double miscStocksOptionsWarrants,
	    double redeemablePreferredStocks, double commonStock,
	    double preferredStock, double retainedEarnings,
	    double treasuryStock, double capitalSurplus,
	    double otherstockholdersEquity) {
	this.symbol = symbol;
	this.assets = new Assets(cashAndEquivalent, shortTermInvestements,
		netReceivables, inventory, otherCurrentAssets,
		longTermInvestments, propertyPlantAndEquivalent, goodwill,
		intangibleAssets, accumulatedAmortization, otherAssets,
		deferredLongTermAssetCharges);
	this.liabilities = new Liabilities(accountPayable,
		shortCurrentLongTermDebt, otherCurrentLiabilities,
		longTermDebt, otherLiabilities,
		deferredLongTermLiabilityCharges, minorityInterest,
		negativeGoodwill);
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
