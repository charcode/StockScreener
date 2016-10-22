package com.oak.finance.app.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.oak.external.spring.config.ApplicationConfig;
import com.oak.finance.app.main.server.ApplicationServer;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.oak.api.finance.model.dto"}
//				, basePackageClasses = { YahooFinancialJsonDataModel.class }
			)
@ComponentScan (basePackageClasses = ApplicationConfig.class)
@EnableJpaRepositories(basePackages = {"com.oak.api.finance.repository"})
public class EntryPoint implements CommandLineRunner{
	private static Logger logger = LogManager.getLogger(EntryPoint.class);
	public static void main(String[] args) {
		logger.info("Starting application");
		
		SpringApplication.run(EntryPoint.class, args);
//		ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//		ctx.getBean(ApplicationServer.class).start();
		
	}
	@Autowired
	ApplicationServer server;

	@Override
	public void run(String... arg0) throws Exception {
		logger.info("Starting the app");
		server.start();
	}
}
