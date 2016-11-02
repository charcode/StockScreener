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
import com.oak.api.finance.model.FinancialData;
import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.StatementPeriod;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.external.finance.app.marketdata.api.FinancialDataDao;
import com.oak.external.finance.app.marketdata.api.FinancialStatementsProvider;

public class FinancialStatementsProviderImpl implements FinancialStatementsProvider {

	private final FinancialDataDao financialDataDao;
	private final BalanceSheetRepository balanceSheetRepository;
	private final Logger logger;
	private final FinancialStatementsConverter statementsConverter;

	public FinancialStatementsProviderImpl(FinancialDataDao financialDataDao,
			BalanceSheetRepository balanceSheetRepository, FinancialStatementsConverter statementsConverter,
			Logger logger) {
		this.financialDataDao = financialDataDao;
		this.balanceSheetRepository = balanceSheetRepository;
		this.statementsConverter = statementsConverter;
		this.logger = logger;
	}

	@Override
	public FinancialData getFinancialStatements(String ticker) {
		logger.info("Getting financial statements for " + ticker);

		List<BalanceSheetDto> balanceSheetsInDb = balanceSheetRepository.findByTicker(ticker);

		FinancialData dbFinancials = statementsConverter.getFinancialData(ticker, balanceSheetsInDb);
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
			Set<BalanceSheetDto> balanceSheets = new HashSet<>();
			extractBalanceSheets(annualBalanceSheet, balanceSheets, StatementPeriod.ANNUAL);
			extractBalanceSheets(quarterlyBalanceSheet, balanceSheets, StatementPeriod.QUARTERLY);

			// extract new balance sheets that need to be written to the DB
			Set<BalanceSheetDto> toBeSaved = balanceSheets.stream()
					.filter(b -> balanceSheetNotSaved(b, balanceSheetsInDb))
					.collect(Collectors.toSet());
			balanceSheetRepository.save(toBeSaved);
		} else {
			financialData = dbFinancials;
		}

		return financialData;
	}

	private boolean balanceSheetNotSaved(BalanceSheetDto newB, List<BalanceSheetDto> balanceSheetsInDb) {
		boolean ret = true;
		for (BalanceSheetDto saved : balanceSheetsInDb) {
			if (saved.getStatementPeriod().equals(newB.getStatementPeriod())
					&& saved.getEndDate().compareTo(newB.getEndDate()) == 0) {
				ret = false;
				logger.info(newB.getTicker()+ " nothing to save, latest downloaded match old: " + newB.getEndDate());
				break;
			}
		}
		return ret;
	}

	private void extractBalanceSheets(SortedMap<Date, BalanceSheet> annualBalanceSheet,
			Set<BalanceSheetDto> balanceSheets, StatementPeriod p) {
		annualBalanceSheet.keySet().stream().forEach(d -> {
			BalanceSheet balanceSheet = annualBalanceSheet.get(d);
			BalanceSheetDto dto = statementsConverter.convertYahooToDbBalanceSheet(balanceSheet, d, p);
			balanceSheets.add(dto);
		});
	}

}
