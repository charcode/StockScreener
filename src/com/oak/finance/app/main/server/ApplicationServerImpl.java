package com.oak.finance.app.main.server;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.StreamUtils;

import com.oak.api.finance.model.dto.BalanceSheetDto;
import com.oak.api.finance.model.dto.StatementPeriod;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.api.finance.repository.Screen0ResultsRepository;
import com.oak.finance.app.monitor.MarketDataMonitorsController;
import com.oak.finance.interest.SymbolsController;

public class ApplicationServerImpl implements ApplicationServer {
	private Logger log;
	private final SymbolsController symbolsProvider;
	private final MarketDataMonitorsController marketDataMonitorsController;
	private final Screen0ResultsRepository screeningResultsRepository;
	
	@Autowired
	private BalanceSheetRepository bsrep;
	
	public ApplicationServerImpl(SymbolsController symbolsProvider, MarketDataMonitorsController marketDataMonitorsController, Screen0ResultsRepository screeningResultsRepository, Logger log) {
		this.symbolsProvider = symbolsProvider;
		this.marketDataMonitorsController = marketDataMonitorsController;
		this.screeningResultsRepository = screeningResultsRepository;
		this.log = log;
		log.info("ApplicationServerImpl starting ....");
	}

	@Override
	public void start() {
		log.info(ApplicationServerImpl.class.getCanonicalName()+" starting up ");

// THIS CODE FINDS AND DELETES DUPLICATE ENTRIES IN balance_sheet TABLE IN THE DB
//		--------------- START ----------------------
//		Set<String> syms = symbolsProvider.getSymbols();
//		Set<BalanceSheetDto>duplicates = new HashSet<>();
//		for(String k:syms) {
//			Iterable<BalanceSheetDto> bsByTicker = bsrep.findByTicker(k);
//			Map<StatementPeriod, List<BalanceSheetDto>> bsPerPeriod = StreamUtils.createStreamFromIterator( bsByTicker.iterator()).collect(Collectors.groupingBy(BalanceSheetDto::getStatementPeriod));
//			for(StatementPeriod p:bsPerPeriod.keySet()) {
//				Map<Date, List<BalanceSheetDto>> byDate = bsPerPeriod.get(p).stream().collect(Collectors.groupingBy(BalanceSheetDto::getEndDate));
//				Set<Date> datesWithDuplicates = byDate.keySet().stream().filter( d -> byDate.get(d).size() > 1 ).collect(Collectors.toSet());
//				for(Date d:datesWithDuplicates) {
//					List<BalanceSheetDto> listWithDups = byDate.get(d);
//					listWithDups.remove(0);
//					duplicates.addAll(listWithDups);
//				}
//			}
//		}
//		Set<Long>ids = duplicates.stream().map(d->d.getId()).collect(Collectors.toSet());
//		bsrep.delete(duplicates);
//		
//		--------------- END ----------------------
		
		
		Set<String> symbols = symbolsProvider.getSymbols();
		Set<String> interestingSymbols = symbolsProvider.getInterestingSymbols();
		log.debug("found "+symbols.size()+" symbol.");
		Set<String> excludedSymbols = symbolsProvider.getExcludedSymbols();
		log.debug(excludedSymbols.size()+" symbols are excluded.");
		symbols.removeAll(excludedSymbols);
		log.debug("getting prices for "+symbols.size()+" symbol"+(symbols.size()>0?"s":""));
		marketDataMonitorsController.startStocksAnalysis(symbols, interestingSymbols);
	}

}
