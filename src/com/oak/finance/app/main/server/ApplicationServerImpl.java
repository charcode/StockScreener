package com.oak.finance.app.main.server;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.data.util.StreamUtils;

import com.oak.api.finance.model.dto.Screen0Result;
import com.oak.api.finance.repository.Screen0ResultsRepository;
import com.oak.finance.app.monitor.MarketDataMonitorsController;
import com.oak.finance.interest.SymbolsController;

public class ApplicationServerImpl implements ApplicationServer {
	private Logger log;
	private final SymbolsController symbolsProvider;
	private final MarketDataMonitorsController marketDataMonitorsController;
	private final Screen0ResultsRepository screeningResultsRepository;
	
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
