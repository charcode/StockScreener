package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name="income_statement")
public class IncomeStatementDto extends AbstractFinancialStatement  
{

	private Double	researchDevelopment;
	private Double	sellingGeneralAndAdministrative;
	private Double	nonRecurring;
	private Double	otherOperatingExpenses;
	private Double	totalOperatingExpenses;
	private Double	totalOtherIncomeExpensesNet;
	private Double	earningsBeforeInterestAndTaxes;
	private Double	interestExpense;
	private Double	incomeBeforeTax;
	private Double	incomeTaxExpense;
	private Double	minorityInterest;
	private Double	netIncomeFromContinuingOps;
	private Double	discontinuedOperations;
	private Double	extraordinaryItems;
	private Double	effectOfAccountingChanges;
	private Double	otherItems;
	private Double	netIncome;
	private Double	preferredStockAndOtherAdjustments;
	private Double	netIncomeApplicableToCommonShares;

}
