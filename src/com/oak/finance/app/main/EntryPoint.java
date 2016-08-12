package com.oak.finance.app.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oak.external.spring.config.ApplicationConfig;
import com.oak.finance.app.main.server.ApplicationServer;

public class EntryPoint {
    private final Logger logger ;
    private final ApplicationContext ctx;
    EntryPoint(ApplicationContext ctx,Logger logger){
	this.ctx = ctx;
	this.logger = logger;
    }
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(EntryPoint.class);
		logger.info("Starting application");
		
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		ctx.getBean(ApplicationServer.class).start();
	}
}
