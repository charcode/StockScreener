package com.oak.external.finance.app.marketdata.api.impl;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.BalanceSheet;
import com.oak.api.finance.model.CashFlowStatement;
import com.oak.api.finance.model.FinancialData;
import com.oak.api.finance.model.dto.AbstractFinancialStatement;
import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.CashFlowStatementDto;
import com.oak.api.finance.model.dto.IncomeStatementDto;
import com.oak.api.finance.model.dto.StatementPeriod;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.api.finance.repository.CashFlowStatementRepository;
import com.oak.api.finance.repository.IncomeStatementRepository;
import com.oak.external.finance.app.marketdata.api.FinancialDataDao;
import com.oak.external.finance.app.marketdata.api.FinancialStatementsProvider;
import com.oak.external.finance.model.economy.IncomeStatement;

public class FinancialStatementsProviderImpl implements FinancialStatementsProvider {

	private final FinancialDataDao financialDataDao;
	private final BalanceSheetRepository balanceSheetRepository;
	private final IncomeStatementRepository incomeStatementRepository;
	private final CashFlowStatementRepository cashFlowStatementRepository;
	private final Logger logger;
	private final FinancialStatementsConverter statementsConverter;
	private BalanceSheetConverter balanceSheetConverter;
	private CashflowStatmentConverter cashflowConverter;
	private IncomeStatementConverter incomeStatmentConverter;

	public FinancialStatementsProviderImpl(FinancialDataDao financialDataDao,
			BalanceSheetRepository balanceSheetRepository, FinancialStatementsConverter statementsConverter,
			IncomeStatementRepository incomeStatementRepository, CashFlowStatementRepository cashFlowStatementRepository, Logger logger) {
		this.financialDataDao = financialDataDao;
		this.balanceSheetRepository = balanceSheetRepository;
		this.cashFlowStatementRepository = cashFlowStatementRepository;
		this.incomeStatementRepository = incomeStatementRepository;
		this.statementsConverter = statementsConverter;
		this.logger = logger;
		this.balanceSheetConverter = new BalanceSheetConverter();
		this.cashflowConverter = new CashflowStatmentConverter();
		this.incomeStatmentConverter = new IncomeStatementConverter();
	}

	@Override
	public FinancialData getFinancialStatements(String ticker) {
		logger.info("Getting financial statements for " + ticker);

		List<BalanceSheetDto> balanceSheetsInDb = balanceSheetRepository.findByTicker(ticker);
		List<CashFlowStatementDto> cashFlowStatementsInDb = cashFlowStatementRepository.findByTicker(ticker);
		List<IncomeStatementDto> incomeStatementsInDb = incomeStatementRepository.findByTicker(ticker);

		FinancialData dbFinancials = statementsConverter.getFinancialData(ticker, balanceSheetsInDb, cashFlowStatementsInDb, incomeStatementsInDb);
		
		boolean oldOrMissingFinancials = true;
		FinancialData financialData;
		if(!dbFinancials.getQuarterlyBalanceSheet().isEmpty()) {
			Date lastPeriod = dbFinancials.getQuarterlyBalanceSheet().lastKey();
			ZonedDateTime ninetyDaysAgo = ZonedDateTime.now().plusDays(-90);
			oldOrMissingFinancials = lastPeriod.toInstant().isBefore(ninetyDaysAgo.toInstant());
			logger.info(ticker+": "+(oldOrMissingFinancials?"N":"No n")+"eed to download balance sheet for ["+ticker+"]. Latest saved is from "+lastPeriod);
		}
		if(oldOrMissingFinancials) {
			logger.info("downloading balance sheet for ["+ticker+"]");

			financialData = financialDataDao.getFinancialDataForSymbol(ticker,
					null /* some provider of financial statements data need an exchange supplied, some don't */);
			SortedMap<Date, BalanceSheet> annualBalanceSheet = financialData.getAnnualBalanceSheet();
			SortedMap<Date, BalanceSheet> quarterlyBalanceSheet = financialData.getQuarterlyBalanceSheet();
			SortedMap<Date, CashFlowStatement> annualCashflowStatement = financialData.getAnnualCashflowStatement();
			SortedMap<Date, CashFlowStatement> quarterlyCashflowStatement = financialData.getQuarterlyCashflowStatement();
			SortedMap<Date, IncomeStatement> annualIncomeStatement = financialData.getAnnualIncomeStatement();
			SortedMap<Date, IncomeStatement> quarterlyIncomeStatement = financialData.getQuarterlyIncomeStatement();
			
			Set<BalanceSheetDto> balanceSheets = new HashSet<>();
			Set<CashFlowStatementDto> cashflowStatements = new HashSet<>();
			Set<IncomeStatementDto> incomeStatements = new HashSet<>();
			
			extractFinancialData(annualBalanceSheet, balanceSheets, StatementPeriod.ANNUAL, balanceSheetConverter);
			extractFinancialData(quarterlyBalanceSheet, balanceSheets, StatementPeriod.QUARTERLY, balanceSheetConverter);
			
			extractFinancialData(annualCashflowStatement, cashflowStatements, StatementPeriod.QUARTERLY, cashflowConverter);
			extractFinancialData(quarterlyCashflowStatement, cashflowStatements, StatementPeriod.QUARTERLY, cashflowConverter);
			
			extractFinancialData(annualIncomeStatement, incomeStatements, StatementPeriod.QUARTERLY, incomeStatmentConverter);
			extractFinancialData(quarterlyIncomeStatement, incomeStatements, StatementPeriod.QUARTERLY, incomeStatmentConverter);

			// extract new balance sheets that need to be written to the DB
			Set<BalanceSheetDto> bsToBeSaved = filterFinancialDataToBeSaved(balanceSheetsInDb, balanceSheets);
			Set<CashFlowStatementDto> cfToBeSaved = filterFinancialDataToBeSaved(cashFlowStatementsInDb,
					cashflowStatements);
			Set<IncomeStatementDto> isToBeSaved = filterFinancialDataToBeSaved(incomeStatementsInDb, incomeStatements);
			balanceSheetRepository.save(bsToBeSaved);
			cashFlowStatementRepository.save(cfToBeSaved);
			incomeStatementRepository.save(isToBeSaved);
		} else {
			financialData = dbFinancials;
		}

		return financialData;
	}

	private <T extends AbstractFinancialStatement> Set<T> filterFinancialDataToBeSaved(List<T> financialDataInDb,
			Set<T> newFinancialData) {
		Set<T> toBeSaved = newFinancialData.stream()
				.filter(b -> financialDataNotSaved(b, financialDataInDb))
				.collect(Collectors.toSet());
		return toBeSaved;
	}

	private <T extends AbstractFinancialStatement> boolean financialDataNotSaved(T newData, List<T> financialDataInDb) {
		boolean ret = true;
		for (T saved : financialDataInDb) {
			if (saved.getStatementPeriod().equals(newData.getStatementPeriod())
					&& saved.getEndDate().compareTo(newData.getEndDate()) == 0) {
				ret = false;
				logger.info(newData.getTicker()+ " nothing to save, latest downloaded match old: " + newData.getEndDate());
				break;
			}
		}
		return ret;
	}

	private <I,O> void extractFinancialData(SortedMap<Date, I> financialDataIn,
			Set<O> financialDataOut, StatementPeriod p, Converter<I,O> c) {
		financialDataIn.keySet().stream().forEach(d -> {
			I balanceSheet = financialDataIn.get(d);
			O dto = c.convert(balanceSheet, d, p);
			financialDataOut.add(dto);
		});
	}
	
	private interface Converter<I,O>{
		O convert (I i, Date d,StatementPeriod period);
	}

	private class BalanceSheetConverter implements Converter<BalanceSheet,BalanceSheetDto>{

		@Override
		public BalanceSheetDto convert(BalanceSheet i, Date d,StatementPeriod period) {
			return statementsConverter.convertYahooToDbBalanceSheet(i, d, period);
		}
		
	}
	private class IncomeStatementConverter implements Converter<IncomeStatement,IncomeStatementDto>{
		
		@Override
		public IncomeStatementDto convert(IncomeStatement i, Date d,StatementPeriod period) {
			return statementsConverter.convertYahooToDbIncomeStatement(i, d, period);
		}
		
	}
	private class CashflowStatmentConverter implements Converter<CashFlowStatement,CashFlowStatementDto>{
		
		@Override
		public CashFlowStatementDto convert(CashFlowStatement i, Date d,StatementPeriod period) {
			return statementsConverter.convertYahooToDbCashFlowStatement(i, d, period);
		}
		
	}
}
