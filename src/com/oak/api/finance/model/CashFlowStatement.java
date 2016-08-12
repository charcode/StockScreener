package com.oak.api.finance.model;

public class CashFlowStatement {

    class OperatingActivities {
	private final double depreciation;
	private final double adjustmentsToNetIncome;
	private final double changesInAccountsReceivables;
	private final double changesInLiabilities;
	private final double changesInInventories;
	private final double changesInOtherOperatingActivities;
	private final double totalCashFlowFromOperatingActivities;

	public OperatingActivities(double depreciation,
		double adjustmentsToNetIncome,
		double changesInAccountsReceivables,
		double changesInLiabilities, double changesInInventories,
		double changesInOtherOperatingActivities,
		double totalCashFlowFromOperatingActivities) {
	    super();
	    this.depreciation = depreciation;
	    this.adjustmentsToNetIncome = adjustmentsToNetIncome;
	    this.changesInAccountsReceivables = changesInAccountsReceivables;
	    this.changesInLiabilities = changesInLiabilities;
	    this.changesInInventories = changesInInventories;
	    this.changesInOtherOperatingActivities = changesInOtherOperatingActivities;
	    this.totalCashFlowFromOperatingActivities = totalCashFlowFromOperatingActivities;
	}

	public double getDepreciation() {
	    return depreciation;
	}

	public double getAdjustmentsToNetIncome() {
	    return adjustmentsToNetIncome;
	}

	public double getChangesInAccountsReceivables() {
	    return changesInAccountsReceivables;
	}

	public double getChangesInLiabilities() {
	    return changesInLiabilities;
	}

	public double getChangesInInventories() {
	    return changesInInventories;
	}

	public double getChangesInOtherOperatingActivities() {
	    return changesInOtherOperatingActivities;
	}

	public double getTotalCashFlowFromOperatingActivities() {
	    return totalCashFlowFromOperatingActivities;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOuterType().hashCode();
	    long temp;
	    temp = Double.doubleToLongBits(adjustmentsToNetIncome);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(changesInAccountsReceivables);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(changesInInventories);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(changesInLiabilities);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(changesInOtherOperatingActivities);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(depreciation);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double
		    .doubleToLongBits(totalCashFlowFromOperatingActivities);
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
	    OperatingActivities other = (OperatingActivities) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (Double.doubleToLongBits(adjustmentsToNetIncome) != Double
		    .doubleToLongBits(other.adjustmentsToNetIncome))
		return false;
	    if (Double.doubleToLongBits(changesInAccountsReceivables) != Double
		    .doubleToLongBits(other.changesInAccountsReceivables))
		return false;
	    if (Double.doubleToLongBits(changesInInventories) != Double
		    .doubleToLongBits(other.changesInInventories))
		return false;
	    if (Double.doubleToLongBits(changesInLiabilities) != Double
		    .doubleToLongBits(other.changesInLiabilities))
		return false;
	    if (Double.doubleToLongBits(changesInOtherOperatingActivities) != Double
		    .doubleToLongBits(other.changesInOtherOperatingActivities))
		return false;
	    if (Double.doubleToLongBits(depreciation) != Double
		    .doubleToLongBits(other.depreciation))
		return false;
	    if (Double.doubleToLongBits(totalCashFlowFromOperatingActivities) != Double
		    .doubleToLongBits(other.totalCashFlowFromOperatingActivities))
		return false;
	    return true;
	}

	private CashFlowStatement getOuterType() {
	    return CashFlowStatement.this;
	}
    }

    class InvestingActivities {
	private final double capitalExpenditures;
	private final double investments;
	private final double otherCashFlowsFromInvestingActivities;
	private final double totalCashFlowsFromInvestingActivities;

	public InvestingActivities(double capitalExpenditures,
		double investments,
		double otherCashFlowsFromInvestingActivities,
		double totalCashFlowsFromInvestingActivities) {
	    super();
	    this.capitalExpenditures = capitalExpenditures;
	    this.investments = investments;
	    this.otherCashFlowsFromInvestingActivities = otherCashFlowsFromInvestingActivities;
	    this.totalCashFlowsFromInvestingActivities = totalCashFlowsFromInvestingActivities;
	}

	public double getCapitalExpenditures() {
	    return capitalExpenditures;
	}

	public double getInvestments() {
	    return investments;
	}

	public double getOtherCashFlowsFromInvestingActivities() {
	    return otherCashFlowsFromInvestingActivities;
	}

	public double getTotalCashFlowsFromInvestingActivities() {
	    return totalCashFlowsFromInvestingActivities;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOuterType().hashCode();
	    long temp;
	    temp = Double.doubleToLongBits(capitalExpenditures);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(investments);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double
		    .doubleToLongBits(otherCashFlowsFromInvestingActivities);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double
		    .doubleToLongBits(totalCashFlowsFromInvestingActivities);
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
	    InvestingActivities other = (InvestingActivities) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (Double.doubleToLongBits(capitalExpenditures) != Double
		    .doubleToLongBits(other.capitalExpenditures))
		return false;
	    if (Double.doubleToLongBits(investments) != Double
		    .doubleToLongBits(other.investments))
		return false;
	    if (Double.doubleToLongBits(otherCashFlowsFromInvestingActivities) != Double
		    .doubleToLongBits(other.otherCashFlowsFromInvestingActivities))
		return false;
	    if (Double.doubleToLongBits(totalCashFlowsFromInvestingActivities) != Double
		    .doubleToLongBits(other.totalCashFlowsFromInvestingActivities))
		return false;
	    return true;
	}

	private CashFlowStatement getOuterType() {
	    return CashFlowStatement.this;
	}

    }

    class FinancingActivities {
	private final double dividendsPaid;
	private final double salePurchaseOfStock;
	private final double netBorrowings;
	private final double otherCashFlowsFromFinancingActivities;
	private final double totalCashFlowsFromFinancingActivities;

	public FinancingActivities(double dividendsPaid,
		double salePurchaseOfStock, double netBorrowings,
		double otherCashFlowsFromFinancingActivities,
		double totalCashFlowsFromFinancingActivities) {
	    super();
	    this.dividendsPaid = dividendsPaid;
	    this.salePurchaseOfStock = salePurchaseOfStock;
	    this.netBorrowings = netBorrowings;
	    this.otherCashFlowsFromFinancingActivities = otherCashFlowsFromFinancingActivities;
	    this.totalCashFlowsFromFinancingActivities = totalCashFlowsFromFinancingActivities;
	}

	public double getDividendsPaid() {
	    return dividendsPaid;
	}

	public double getSalePurchaseOfStock() {
	    return salePurchaseOfStock;
	}

	public double getNetBorrowings() {
	    return netBorrowings;
	}

	public double getOtherCashFlowsFromFinancingActivities() {
	    return otherCashFlowsFromFinancingActivities;
	}

	public double getTotalCashFlowsFromFinancingActivities() {
	    return totalCashFlowsFromFinancingActivities;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOuterType().hashCode();
	    long temp;
	    temp = Double.doubleToLongBits(dividendsPaid);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(netBorrowings);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double .doubleToLongBits(otherCashFlowsFromFinancingActivities);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(salePurchaseOfStock);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double .doubleToLongBits(totalCashFlowsFromFinancingActivities);
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
	    FinancingActivities other = (FinancingActivities) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (Double.doubleToLongBits(dividendsPaid) != Double
		    .doubleToLongBits(other.dividendsPaid))
		return false;
	    if (Double.doubleToLongBits(netBorrowings) != Double
		    .doubleToLongBits(other.netBorrowings))
		return false;
	    if (Double.doubleToLongBits(otherCashFlowsFromFinancingActivities) != Double
		    .doubleToLongBits(other.otherCashFlowsFromFinancingActivities))
		return false;
	    if (Double.doubleToLongBits(salePurchaseOfStock) != Double
		    .doubleToLongBits(other.salePurchaseOfStock))
		return false;
	    if (Double.doubleToLongBits(totalCashFlowsFromFinancingActivities) != Double
		    .doubleToLongBits(other.totalCashFlowsFromFinancingActivities))
		return false;
	    return true;
	}

	private CashFlowStatement getOuterType() {
	    return CashFlowStatement.this;
	}

    }

    private final String symbol;
    private final OperatingActivities operatingActivities;
    private final InvestingActivities investingActivities;
    private final FinancingActivities financingActivities;
    private final double netIncome;
    private final double effectOfExchangeRateChanges;
    private final double changeInCashAndCashEquivalents;

    public CashFlowStatement(String symbol, double depreciation,
	    double adjustmentsToNetIncome, double changesInAccountsReceivables,
	    double changesInLiabilities, double changesInInventories,
	    double changesInOtherOperatingActivities,
	    double totalCashFlowFromOperatingActivities,
	    double capitalExpenditures, double investments,
	    double otherCashFlowsFromInvestingActivities,
	    double totalCashFlowsFromInvestingActivities, double dividendsPaid,
	    double salePurchaseOfStock, double netBorrowings,
	    double otherCashFlowsFromFinancingActivities,
	    double totalCashFlowsFromFinancingActivities, double netIncome,
	    double effectOfExchangeRateChanges,
	    double changeInCashAndCashEquivalents) {
	super();
	this.symbol = symbol;
	this.operatingActivities = new OperatingActivities(depreciation,
		adjustmentsToNetIncome, changesInAccountsReceivables,
		changesInLiabilities, changesInInventories,
		changesInOtherOperatingActivities,
		totalCashFlowFromOperatingActivities);
	this.investingActivities = new InvestingActivities(capitalExpenditures,
		investments, otherCashFlowsFromInvestingActivities,
		totalCashFlowsFromInvestingActivities);
	this.financingActivities = new FinancingActivities(dividendsPaid,
		salePurchaseOfStock, netBorrowings,
		otherCashFlowsFromFinancingActivities,
		totalCashFlowsFromFinancingActivities);

	this.netIncome = netIncome;
	this.effectOfExchangeRateChanges = effectOfExchangeRateChanges;
	this.changeInCashAndCashEquivalents = changeInCashAndCashEquivalents;
    }

    public double getDepreciation() {
	return operatingActivities.getDepreciation();
    }

    public double getAdjustmentsToNetIncome() {
	return operatingActivities.getAdjustmentsToNetIncome();
    }

    public double getChangesInAccountsReceivables() {
	return operatingActivities.getChangesInAccountsReceivables();
    }

    public double getChangesInLiabilities() {
	return operatingActivities.getChangesInLiabilities();
    }

    public double getChangesInInventories() {
	return operatingActivities.getChangesInInventories();
    }

    public double getChangesInOtherOperatingActivities() {
	return operatingActivities.getChangesInOtherOperatingActivities();
    }

    public double getTotalCashFlowFromOperatingActivities() {
	return operatingActivities.getTotalCashFlowFromOperatingActivities();
    }

    public double getCapitalExpenditures() {
	return investingActivities.getCapitalExpenditures();
    }

    public double getInvestments() {
	return investingActivities.getInvestments();
    }

    public double getOtherCashFlowsFromInvestingActivities() {
	return investingActivities.getOtherCashFlowsFromInvestingActivities();
    }

    public double getTotalCashFlowsFromInvestingActivities() {
	return investingActivities.getTotalCashFlowsFromInvestingActivities();
    }

    public double getDividendsPaid() {
	return financingActivities.getDividendsPaid();
    }

    public double getSalePurchaseOfStock() {
	return financingActivities.getSalePurchaseOfStock();
    }

    public double getNetBorrowings() {
	return financingActivities.getNetBorrowings();
    }

    public double getOtherCashFlowsFromFinancingActivities() {
	return financingActivities.getOtherCashFlowsFromFinancingActivities();
    }

    public double getTotalCashFlowsFromFinancingActivities() {
	return financingActivities.getTotalCashFlowsFromFinancingActivities();
    }

    public double getNetIncome() {
	return netIncome;
    }

    public double getEffectOfExchangeRateChanges() {
	return effectOfExchangeRateChanges;
    }

    public double getChangeInCashAndCashEquivalents() {
	return changeInCashAndCashEquivalents;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	long temp;
	temp = Double.doubleToLongBits(changeInCashAndCashEquivalents);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(effectOfExchangeRateChanges);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime
		* result
		+ ((financingActivities == null) ? 0 : financingActivities
			.hashCode());
	result = prime
		* result
		+ ((investingActivities == null) ? 0 : investingActivities
			.hashCode());
	temp = Double.doubleToLongBits(netIncome);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime
		* result
		+ ((operatingActivities == null) ? 0 : operatingActivities
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
	CashFlowStatement other = (CashFlowStatement) obj;
	if (Double.doubleToLongBits(changeInCashAndCashEquivalents) != Double
		.doubleToLongBits(other.changeInCashAndCashEquivalents))
	    return false;
	if (Double.doubleToLongBits(effectOfExchangeRateChanges) != Double
		.doubleToLongBits(other.effectOfExchangeRateChanges))
	    return false;
	if (financingActivities == null) {
	    if (other.financingActivities != null)
		return false;
	} else if (!financingActivities.equals(other.financingActivities))
	    return false;
	if (investingActivities == null) {
	    if (other.investingActivities != null)
		return false;
	} else if (!investingActivities.equals(other.investingActivities))
	    return false;
	if (Double.doubleToLongBits(netIncome) != Double
		.doubleToLongBits(other.netIncome))
	    return false;
	if (operatingActivities == null) {
	    if (other.operatingActivities != null)
		return false;
	} else if (!operatingActivities.equals(other.operatingActivities))
	    return false;
	return true;
    }

}
