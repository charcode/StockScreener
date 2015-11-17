package com.oak.finance.app.main.server;

import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.oak.finance.app.monitor.MarketDataMonitorsController;
import com.oak.finance.interest.SymbolsProvider;

public class ApplicationServerImpl implements ApplicationServer {
	private Logger log;
	private final SymbolsProvider symbolsProvider;
	private final MarketDataMonitorsController marketDataMonitorsController;
	
	public ApplicationServerImpl(SymbolsProvider symbolsProvider, MarketDataMonitorsController marketDataMonitorsController, Logger log) {
		this.symbolsProvider = symbolsProvider;
		this.marketDataMonitorsController = marketDataMonitorsController;
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
