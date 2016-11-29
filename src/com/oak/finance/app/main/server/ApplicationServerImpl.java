package com.oak.finance.app.main.server;


import org.apache.logging.log4j.Logger;

import com.oak.finance.app.main.controllers.ApplicationController;

public class ApplicationServerImpl implements ApplicationServer {
	
	private final ApplicationController mainController;
	private final Logger log; 
	public ApplicationServerImpl(ApplicationController mainController,Logger log ) {
		this.log = log;
		this.mainController = mainController;
	}

	@Override
	public void start() {
		log.debug("Starting the main app");
		mainController.startUp();
//		mainController.fixWrongQuarterlyStatements();
//		mainController.launchAnalysis();
//		mainController.fixDuplicatedCashflows(new DuplicatCashflowListener() {
//			
//			@Override
//			public void onTickersFound(Map<String, List<Date>> tickers) {
//				System.out.println("found tickers with duplicates: "+tickers.size());
//			}
//			
//			@Override
//			public void onProgress(String ticker, Date date) {
//				System.out.println(ticker+ " .. fixing ticker for date: "+ date);
//			}
//			
//			@Override
//			public void onDone(Status status, String ticker) {
//				
//				System.out.println(ticker+ " fixing ticker: "+ status);
//			}
//		});
//
	}

}
