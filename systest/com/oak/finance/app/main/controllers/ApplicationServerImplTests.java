package com.oak.finance.app.main.controllers;

import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oak.api.finance.dao.DuplicateCashflowsDao;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.api.finance.repository.CashFlowStatementRepository;
import com.oak.api.finance.repository.CompanyRepository;
import com.oak.api.finance.repository.EarningsCalendarRepository;
import com.oak.api.finance.repository.IncomeStatementRepository;
import com.oak.api.finance.repository.Screen0ResultsRepository;
import com.oak.external.finance.app.marketdata.api.FinancialDataDao;
import com.oak.external.finance.app.marketdata.api.FinancialStatementsProvider;
import com.oak.external.finance.app.marketdata.api.MarketDataProvider;
import com.oak.finance.app.monitor.MarketDataMonitorsController;
import com.oak.finance.interest.SymbolsController;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class ApplicationServerImplTests {

	private ApplicationController undertest;
	@Mock
	MarketDataProvider marketDataProvider ;
	@Mock
	SymbolsController stockListProvider;
	@Mock
	MarketDataMonitorsController marketDataMonitorsFactory;
	@Mock
	Logger log;
	
	@Mock
	Screen0ResultsRepository screeningResultsRepository;
	@Mock
	EarningsCalendarRepository earningsCalendarRepository;
	@Mock
	BalanceSheetRepository balanceSheetRepository;
	@Mock
	CashFlowStatementRepository cashFlowStatementRepository;
	@Mock
	IncomeStatementRepository incomeStatementRepository;
	@Mock
	CompanyRepository companyRepository;
	@Mock
	DuplicateCashflowsDao duplicateCashlfowDao;
	@Mock
	FinancialStatementsProvider financialStatementsProvider;
	
	@Before
	public void setUp() throws Exception {		
	}

	@Test
	public void testStart() {
		
		Set<String> symbols = Sets.newSet("AAPL");
		Mockito.when(stockListProvider.getSymbols()).thenReturn(symbols);

		
		undertest = new ApplicationMainControllerImpl(stockListProvider, marketDataMonitorsFactory, 
				screeningResultsRepository, earningsCalendarRepository, balanceSheetRepository, 
				cashFlowStatementRepository, incomeStatementRepository, companyRepository, duplicateCashlfowDao,
				financialStatementsProvider, log);
	}
}
