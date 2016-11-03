package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name="cashflow_statment")
public class CashFlowStatementDto extends AbstractFinancialStatement {

	private Double depreciation;
	private Double adjustmentsToNetIncome;
	private Double changesInAccountsReceivables;
	private Double changesInLiabilities;
	private Double changesInInventories;
	private Double changesInOtherOperatingActivities;
	private Double totalCashFlowFromOperatingActivities;
	private Double capitalExpenditures;
	private Double investments;
	private Double otherCashFlowsFromInvestingActivities;
	private Double totalCashFlowsFromInvestingActivities;
	private Double dividendsPaid;
	private Double salePurchaseOfStock;
	private Double netBorrowings;
	private Double otherCashFlowsFromFinancingActivities;
	private Double totalCashFlowsFromFinancingActivities;
	private Double netIncome;
	private Double effectOfExchangeRateChanges;
	private Double changeInCashAndCashEquivalents;

}
