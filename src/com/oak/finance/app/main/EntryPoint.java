package com.oak.finance.app.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.oak.external.spring.config.ApplicationConfig;
import com.oak.finance.app.main.server.ApplicationServer;


@SpringBootApplication
@EntityScan(basePackages = {"com.oak.api.finance.model.dto"}
//				, basePackageClasses = { YahooFinancialJsonDataModel.class }
			)
@EnableJpaRepositories(basePackages = {"com.oak.api.finance.repository"})
public class EntryPoint {
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(EntryPoint.class);
		logger.info("Starting application");
		
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		SpringApplication.run(EntryPoint.class, args);
		// ctx.getBean(ApplicationServer.class).start();
	}
}
