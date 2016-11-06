package com.oak.finance.app.main.controllers;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.assertj.core.util.Sets;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.springframework.data.util.StreamUtils;

import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.api.finance.repository.CashFlowStatementRepository;
import com.oak.api.finance.repository.CompanyRepository;
import com.oak.api.finance.repository.EarningsCalendarRepository;
import com.oak.api.finance.repository.IncomeStatementRepository;
import com.oak.api.finance.repository.Screen0ResultsRepository;
import com.oak.api.finance.utils.repository.ReconcilingRepository;
import com.oak.finance.app.monitor.MarketDataMonitorsController;
import com.oak.finance.interest.SymbolsController;

public class ApplicationMainControllerImpl implements ApplicationController {
	private Logger log;
	private final SymbolsController symbolsController;
	private final MarketDataMonitorsController marketDataMonitorsController;
	private final Screen0ResultsRepository screeningResultsRepository;
	private final BalanceSheetRepository bsRep;
	private final CashFlowStatementRepository cfRep;
	private final IncomeStatementRepository incomeStatementRepository;
	private final EarningsCalendarRepository earningsCalendarRepository;
	private final CompanyRepository companyRepository;
	
	private final Set<String> tickers;
	
	public ApplicationMainControllerImpl(SymbolsController symbolController, 
			MarketDataMonitorsController marketDataMonitorsController, 
			Screen0ResultsRepository screeningResultsRepository, 
			EarningsCalendarRepository earningsCalendarRepository, BalanceSheetRepository bsRep, 
			CashFlowStatementRepository cfRep, IncomeStatementRepository incomeStatementRep, 
			CompanyRepository companyRepository, Logger log) {
		this.symbolsController = symbolController;
		this.marketDataMonitorsController = marketDataMonitorsController;
		this.screeningResultsRepository = screeningResultsRepository;
		this.bsRep = bsRep;
		this.cfRep = cfRep;
		this.incomeStatementRepository = incomeStatementRep;
		this.earningsCalendarRepository = earningsCalendarRepository;
		this.companyRepository = companyRepository;
		tickers = new ConcurrentHashSet<>();
		this.log = log;
		log.info("ApplicationServerImpl starting ....");
	}

	@Override
	public void onStartUp() {
		log.info(ApplicationMainControllerImpl.class.getCanonicalName()+" starting up ");

		tickers.addAll(symbolsController.getSymbols());
		startDifferentReconciliations();
		
		Set<String> interestingSymbols = symbolsController.getInterestingSymbols();
		log.debug("found "+tickers.size()+" symbol.");
		Set<String> excludedSymbols = symbolsController.getExcludedSymbols();
		log.debug(excludedSymbols.size()+" symbols are excluded.");
		tickers.removeAll(excludedSymbols);
		log.debug("getting prices for "+tickers.size()+" symbol"+(tickers.size()>0?"s":""));
		marketDataMonitorsController.startStocksAnalysis(tickers, interestingSymbols);
	}

	private void startDifferentReconciliations() {
		
		Thread t0 = new Thread() {
			public void run() {
				reconcileTickersFromEarningsCalendar();
			}
		};
		Thread t1 = new Thread() {
			public void run() {
				reconcileTickersInFinancialStatements();
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				reconcileTickersInLosersTables();
			}
		};
		Thread t3 = new Thread() {
			public void run() {
				reconcileTickersInWinnersTables();
			}
		};
		
		Set<Thread> threads = startAllThreads(t0, t1, t2, t3);
		for(Thread t:threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				log.error("This should never happen",e);
			}
		}
		clearDuplicates();
	}

	private Set<Thread>startAllThreads(Thread... threads ){
		Set<Thread>ret = new HashSet<>();
		for(Thread t:threads) {
			ret.add(t);
			t.start();
		}
		return ret;
	}
	
	private void clearDuplicates() {
		Iterable<Company> allCompanies = companyRepository.findAll();
		Map<String, List<Company>> companiesByTicker = StreamUtils.createStreamFromIterator(allCompanies.iterator())
		.collect(Collectors.groupingBy(Company::getTicker));
		Set<String> duplicateTickers = companiesByTicker.keySet().stream()
		.filter(t->companiesByTicker.get(t).size()>1)
		.collect(Collectors.toSet());
		Set<Company> toDelete = duplicateTickers.stream()
		.map(k->getCompaniesToDelete(k,companiesByTicker))
		.flatMap(companySet -> companySet.stream())
		.collect(Collectors.toSet());
		companyRepository.delete(toDelete);
	}

	private Set<Company> getCompaniesToDelete(String k, Map<String, List<Company>> companiesByTicker) {
		Set<Company>c2Delete = new HashSet<>();
		List<Company> dups = companiesByTicker.get(k);
		if (!dups.isEmpty()) {
			Set<Company> withName = getCompaniesWithName(dups);
			if(withName.size() > 1) {
				Set<Company> withIndustry = getCompaniesWithIndustry(withName);
				if(withIndustry.size()>1) {
					Set<Company> withSectors = getCompaniesWithSectors(withIndustry);
					if(withSectors.size() > 1) {						
						getAllButTheFirst(c2Delete,withSectors);
					}else {
						getAllButTheFirst(c2Delete,withIndustry);
					}
				}else {
					getAllButTheFirst(c2Delete,withIndustry);
				}
			}else {
				getAllButTheFirst(c2Delete, dups);
			}
		} else {
			// empty -- very unusual, k is a key of companiesByTicker, something is wrong;
			log.warn("unusual situation, "+k+" is not a ticker in "+companiesByTicker.keySet());
			c2Delete = Sets.newHashSet();
		}
		return c2Delete;
	}

	private void getAllButTheFirst(Set<Company> c2Delete, Collection<Company> dups) {
		dups.remove(dups.iterator().next());
		c2Delete.addAll(dups);
	}

	private Set<Company> getCompaniesWithIndustry(Set<Company> withName) {
		return withName.stream().filter(c->c.getIndustryDescription() != null).collect(Collectors.toSet());
	}
	private Set<Company> getCompaniesWithSectors(Set<Company> withName) {
		return withName.stream().filter(c->c.getSectorDescription() != null).collect(Collectors.toSet());
	}

	private Set<Company> getCompaniesWithName(List<Company> dups) {
		return dups.stream()
		.filter(c -> c.getName() != null)
		.collect(Collectors.toSet());
	}

	private void reconcileTickersInWinnersTables() {
		// TODO Auto-generated method stub
		
	}

	private void reconcileTickersInLosersTables() {
		// TODO Auto-generated method stub
		
	}

	private void reconcileTickersInFinancialStatements() {
		checkAndSave(this.bsRep);
		checkAndSave(this.cfRep);
		checkAndSave(this.incomeStatementRepository);
	}

	private void reconcileTickersFromEarningsCalendar() {
		checkAndSave(this.earningsCalendarRepository);
	}

	synchronized private void checkAndSave(ReconcilingRepository rep) {
		
		Set<String> tickersNotSaved = rep.findDistinctTickerNotIn(tickers);
		tickers.addAll(tickersNotSaved);
		Set<Company> companies = tickersNotSaved.stream().map(t -> {
				Company c = new Company();
				c.setTicker(t);
				return c;
		}).collect(Collectors.toSet());
		symbolsController.saveNewCompanies(companies);
	}

}
