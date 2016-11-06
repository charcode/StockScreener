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
		log.debug("Starting the main loop");
		mainController.onStartUp();
	}

}
