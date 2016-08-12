package com.oak.external.finance.model.economy;

public class IncomeStatement {
    
    class OperatingExpenses{
	private final double researchDevelopment;
	private final double sellingGeneralAndAdministrative;
	private final double nonRecurring;
	private final double otherOperatingExpenses;
	private final double totalOperatingExpenses;
	public OperatingExpenses(double researchDevelopment,
		double sellingGeneralAndAdministrative, double nonRecurring,
		double otherOperatingExpenses, double totalOperatingExpenses) {
	    super();
	    this.researchDevelopment = researchDevelopment;
	    this.sellingGeneralAndAdministrative = sellingGeneralAndAdministrative;
	    this.nonRecurring = nonRecurring;
	    this.otherOperatingExpenses = otherOperatingExpenses;
	    this.totalOperatingExpenses = totalOperatingExpenses;
	}
	public double getResearchDevelopment() {
	    return researchDevelopment;
	}
	public double getSellingGeneralAndAdministrative() {
	    return sellingGeneralAndAdministrative;
	}
	public double getNonRecurring() {
	    return nonRecurring;
	}
	public double getOtherOperatingExpenses() {
	    return otherOperatingExpenses;
	}
	public double getTotalOperatingExpenses() {
	    return totalOperatingExpenses;
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOuterType().hashCode();
	    long temp;
	    temp = Double.doubleToLongBits(nonRecurring);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(otherOperatingExpenses);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(researchDevelopment);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(sellingGeneralAndAdministrative);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(totalOperatingExpenses);
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
	    OperatingExpenses other = (OperatingExpenses) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (Double.doubleToLongBits(nonRecurring) != Double
		    .doubleToLongBits(other.nonRecurring))
		return false;
	    if (Double.doubleToLongBits(otherOperatingExpenses) != Double
		    .doubleToLongBits(other.otherOperatingExpenses))
		return false;
	    if (Double.doubleToLongBits(researchDevelopment) != Double
		    .doubleToLongBits(other.researchDevelopment))
		return false;
	    if (Double.doubleToLongBits(sellingGeneralAndAdministrative) != Double
		    .doubleToLongBits(other.sellingGeneralAndAdministrative))
		return false;
	    if (Double.doubleToLongBits(totalOperatingExpenses) != Double
		    .doubleToLongBits(other.totalOperatingExpenses))
		return false;
	    return true;
	}
	private IncomeStatement getOuterType() {
	    return IncomeStatement.this;
	}
	
    }

    class IncomeFromContinuingOperations{
	private final double totalOtherIncomeExpensesNet   ;
	private final double earningsBeforeInterestAndTaxes;
	private final double interestExpense ;
	private final double incomeBeforeTax ;
	private final double incomeTaxExpense;
	private final double minorityInterest;
	private final double netIncomeFromContinuingOps;
	

	private IncomeFromContinuingOperations(
		double totalOtherIncomeExpensesNet,
		double earningsBeforeInterestAndTaxes, 
		double interestExpense,
		double incomeBeforeTax, 
		double incomeTaxExpense,
		double minorityInterest, 
		double netIncomeFromContinuingOps) {
	    this.totalOtherIncomeExpensesNet = totalOtherIncomeExpensesNet;
	    this.earningsBeforeInterestAndTaxes = earningsBeforeInterestAndTaxes;
	    this.interestExpense = interestExpense;
	    this.incomeBeforeTax = incomeBeforeTax;
	    this.incomeTaxExpense = incomeTaxExpense;
	    this.minorityInterest = minorityInterest;
	    this.netIncomeFromContinuingOps = netIncomeFromContinuingOps;
	}

	public double getTotalOtherIncomeExpensesNet() {
	    return totalOtherIncomeExpensesNet;
	}

	public double getEarningsBeforeInterestAndTaxes() {
	    return earningsBeforeInterestAndTaxes;
	}

	public double getInterestExpense() {
	    return interestExpense;
	}

	public double getIncomeBeforeTax() {
	    return incomeBeforeTax;
	}

	public double getIncomeTaxExpense() {
	    return incomeTaxExpense;
	}

	public double getMinorityInterest() {
	    return minorityInterest;
	}
	public double getNetIncomeFromContinuingOps(){
	    return netIncomeFromContinuingOps;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOuterType().hashCode();
	    long temp;
	    temp = Double.doubleToLongBits(earningsBeforeInterestAndTaxes);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(incomeBeforeTax);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(incomeTaxExpense);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(interestExpense);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(minorityInterest);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(netIncomeFromContinuingOps);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(totalOtherIncomeExpensesNet);
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
	    IncomeFromContinuingOperations other = (IncomeFromContinuingOperations) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (Double.doubleToLongBits(earningsBeforeInterestAndTaxes) != Double
		    .doubleToLongBits(other.earningsBeforeInterestAndTaxes))
		return false;
	    if (Double.doubleToLongBits(incomeBeforeTax) != Double
		    .doubleToLongBits(other.incomeBeforeTax))
		return false;
	    if (Double.doubleToLongBits(incomeTaxExpense) != Double
		    .doubleToLongBits(other.incomeTaxExpense))
		return false;
	    if (Double.doubleToLongBits(interestExpense) != Double
		    .doubleToLongBits(other.interestExpense))
		return false;
	    if (Double.doubleToLongBits(minorityInterest) != Double
		    .doubleToLongBits(other.minorityInterest))
		return false;
	    if (Double.doubleToLongBits(netIncomeFromContinuingOps) != Double
		    .doubleToLongBits(other.netIncomeFromContinuingOps))
		return false;
	    if (Double.doubleToLongBits(totalOtherIncomeExpensesNet) != Double
		    .doubleToLongBits(other.totalOtherIncomeExpensesNet))
		return false;
	    return true;
	}

	private IncomeStatement getOuterType() {
	    return IncomeStatement.this;
	}
	
    }
    class NonRecurrentEvents{
	private final double discontinuedOperations;
	private final double extraordinaryItems    ;
	private final double effectOfAccountingChanges;
	private final double otherItems               ;
	public NonRecurrentEvents(double discontinuedOperations,
		double extraordinaryItems, double effectOfAccountingChanges,
		double otherItems) {
	    super();
	    this.discontinuedOperations = discontinuedOperations;
	    this.extraordinaryItems = extraordinaryItems;
	    this.effectOfAccountingChanges = effectOfAccountingChanges;
	    this.otherItems = otherItems;
	}
	public double getDiscontinuedOperations() {
	    return discontinuedOperations;
	}
	public double getExtraordinaryItems() {
	    return extraordinaryItems;
	}
	public double getEffectOfAccountingChanges() {
	    return effectOfAccountingChanges;
	}
	public double getOtherItems() {
	    return otherItems;
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOuterType().hashCode();
	    long temp;
	    temp = Double.doubleToLongBits(discontinuedOperations);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(effectOfAccountingChanges);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(extraordinaryItems);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(otherItems);
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
	    NonRecurrentEvents other = (NonRecurrentEvents) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (Double.doubleToLongBits(discontinuedOperations) != Double
		    .doubleToLongBits(other.discontinuedOperations))
		return false;
	    if (Double.doubleToLongBits(effectOfAccountingChanges) != Double
		    .doubleToLongBits(other.effectOfAccountingChanges))
		return false;
	    if (Double.doubleToLongBits(extraordinaryItems) != Double
		    .doubleToLongBits(other.extraordinaryItems))
		return false;
	    if (Double.doubleToLongBits(otherItems) != Double
		    .doubleToLongBits(other.otherItems))
		return false;
	    return true;
	}
	private IncomeStatement getOuterType() {
	    return IncomeStatement.this;
	}
	
    }
    
    private final OperatingExpenses operatingExpenses;
    private final IncomeFromContinuingOperations incomeFromContinuingOperations;
    private final NonRecurrentEvents nonRecurrentEvents;
    private final double netIncome                        ;
    private final double preferredStockAndOtherAdjustments;
    private final double netIncomeApplicableToCommonShares;


    public IncomeStatement(
	    	String symbol, 
	    	double researchDevelopment,
	    	double sellingGeneralAndAdministrative, 
	    	double nonRecurring,
		double otherOperatingExpenses, 
		double totalOperatingExpenses,
		double totalOtherIncomeExpensesNet,
		double earningsBeforeInterestAndTaxes, 
		double interestExpense,
		double incomeBeforeTax, 
		double incomeTaxExpense,
		double minorityInterest,
		double netIncomeFromContinuingOps,
		double discontinuedOperations,
		double extraordinaryItems, 
		double effectOfAccountingChanges,
		double otherItems, 
		double netIncome                        
		,double preferredStockAndOtherAdjustments
		,double netIncomeApplicableToCommonShares
	    ) {
	
	this.operatingExpenses = new OperatingExpenses(researchDevelopment, sellingGeneralAndAdministrative, nonRecurring, otherOperatingExpenses, totalOperatingExpenses);
	this.incomeFromContinuingOperations = new IncomeFromContinuingOperations(totalOtherIncomeExpensesNet, earningsBeforeInterestAndTaxes, interestExpense, incomeBeforeTax, incomeTaxExpense, minorityInterest, netIncomeFromContinuingOps);
	this.nonRecurrentEvents = new NonRecurrentEvents(discontinuedOperations, extraordinaryItems, effectOfAccountingChanges, otherItems);
	this.netIncome                        =netIncome                        ;
	this.preferredStockAndOtherAdjustments=preferredStockAndOtherAdjustments;
	this.netIncomeApplicableToCommonShares=netIncomeApplicableToCommonShares;
    }


	public double getResearchDevelopment() {
	    return operatingExpenses.getResearchDevelopment();
	}
	public double getSellingGeneralAndAdministrative() {
	    return operatingExpenses.getSellingGeneralAndAdministrative();
	}
	public double getNonRecurring() {
	    return operatingExpenses.getNonRecurring();
	}
	public double getOtherOperatingExpenses() {
	    return operatingExpenses.getOtherOperatingExpenses();
	}
	public double getTotalOperatingExpenses() {
	    return operatingExpenses.getTotalOperatingExpenses();
	}
	public double getTotalOtherIncomeExpensesNet() {
	    return incomeFromContinuingOperations.getTotalOtherIncomeExpensesNet();
	}

	public double getEarningsBeforeInterestAndTaxes() {
	    return incomeFromContinuingOperations.getEarningsBeforeInterestAndTaxes();
	}

	public double getInterestExpense() {
	    return incomeFromContinuingOperations.getInterestExpense();
	}

	public double getIncomeBeforeTax() {
	    return incomeFromContinuingOperations.getIncomeBeforeTax();
	}

	public double getIncomeTaxExpense() {
	    return incomeFromContinuingOperations.getIncomeTaxExpense();
	}

	public double getMinorityInterest() {
	    return incomeFromContinuingOperations.getMinorityInterest();
	}
	public double getNetIncomeFromContinuingOps(){
	    return incomeFromContinuingOperations.getNetIncomeFromContinuingOps();
	}
	public double getDiscontinuedOperations() {
	    return nonRecurrentEvents.getDiscontinuedOperations();
	}
	public double getExtraordinaryItems() {
	    return nonRecurrentEvents.getExtraordinaryItems();
	}
	public double getEffectOfAccountingChanges() {
	    return nonRecurrentEvents.getEffectOfAccountingChanges();
	}
	public double getOtherItems() {
	    return nonRecurrentEvents.getOtherItems();
	}


	public double getNetIncome() {
	    return netIncome;
	}


	public double getPreferredStockAndOtherAdjustments() {
	    return preferredStockAndOtherAdjustments;
	}


	public double getNetIncomeApplicableToCommonShares() {
	    return netIncomeApplicableToCommonShares;
	}


	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime
		    * result
		    + ((incomeFromContinuingOperations == null) ? 0
			    : incomeFromContinuingOperations.hashCode());
	    long temp;
	    temp = Double.doubleToLongBits(netIncome);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(netIncomeApplicableToCommonShares);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    result = prime
		    * result
		    + ((nonRecurrentEvents == null) ? 0 : nonRecurrentEvents
			    .hashCode());
	    result = prime
		    * result
		    + ((operatingExpenses == null) ? 0 : operatingExpenses
			    .hashCode());
	    temp = Double.doubleToLongBits(preferredStockAndOtherAdjustments);
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
	    IncomeStatement other = (IncomeStatement) obj;
	    if (incomeFromContinuingOperations == null) {
		if (other.incomeFromContinuingOperations != null)
		    return false;
	    } else if (!incomeFromContinuingOperations
		    .equals(other.incomeFromContinuingOperations))
		return false;
	    if (Double.doubleToLongBits(netIncome) != Double
		    .doubleToLongBits(other.netIncome))
		return false;
	    if (Double.doubleToLongBits(netIncomeApplicableToCommonShares) != Double
		    .doubleToLongBits(other.netIncomeApplicableToCommonShares))
		return false;
	    if (nonRecurrentEvents == null) {
		if (other.nonRecurrentEvents != null)
		    return false;
	    } else if (!nonRecurrentEvents.equals(other.nonRecurrentEvents))
		return false;
	    if (operatingExpenses == null) {
		if (other.operatingExpenses != null)
		    return false;
	    } else if (!operatingExpenses.equals(other.operatingExpenses))
		return false;
	    if (Double.doubleToLongBits(preferredStockAndOtherAdjustments) != Double
		    .doubleToLongBits(other.preferredStockAndOtherAdjustments))
		return false;
	    return true;
	}
	

}
