package com.oak.finance.app.dao.impl.files;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oak.external.spring.config.ApplicationConfig;

public class SymbolsFileDaoTests {

//	private String filename = "/stocks/yahoo.csv";
	private SymbolsFileDao undertest;
	
	@Before
	public void setUp() throws Exception {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		undertest = ctx.getBean(SymbolsFileDao.class);
		}

	@Test
	public void testGetStockList() {
		
		Set<String> stockList = undertest.getSymbols();
		System.out.println(stockList);
	}
	@Test
	public void testGetSavedSymbolsWithoutPrices() {
		Set<String> symbolsWithoutPrice = undertest.getSavedSymbolsWithoutPrices();
		System.out.println(symbolsWithoutPrice);
	}
		
		

}
