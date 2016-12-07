package com.oak.external.spring.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.oak.api.MainController;
import com.oak.api.MainControllerImpl;
import com.oak.api.finance.dao.DuplicateCashflowDaoImpl;
import com.oak.api.finance.dao.DuplicateCashflowsDao;
import com.oak.api.finance.repository.BalanceSheetRepository;
import com.oak.api.finance.repository.CashFlowStatementRepository;
import com.oak.api.finance.repository.CompanyRepository;
import com.oak.api.finance.repository.CompanyWithProblemsRepository;
import com.oak.api.finance.repository.ControlRepository;
import com.oak.api.finance.repository.EarningsCalendarRepository;
import com.oak.api.finance.repository.EconomicRepository;
import com.oak.api.finance.repository.IncomeStatementRepository;
import com.oak.api.finance.repository.Screen0ResultsRepository;
import com.oak.api.finance.repository.SectorRepository;
import com.oak.api.providers.control.ControlProvider;
import com.oak.api.providers.control.impl.ControlProviderImpl;
import com.oak.external.finance.app.marketdata.api.BalanceSheetDao;
import com.oak.external.finance.app.marketdata.api.CashFlowStatementDao;
import com.oak.external.finance.app.marketdata.api.DataConnector;
import com.oak.external.finance.app.marketdata.api.EarningsCalendarDao;
import com.oak.external.finance.app.marketdata.api.FinancialDataDao;
import com.oak.external.finance.app.marketdata.api.FinancialStatementsProvider;
import com.oak.external.finance.app.marketdata.api.IncomeStatementDao;
import com.oak.external.finance.app.marketdata.api.MarketDataProvider;
import com.oak.external.finance.app.marketdata.api.SectorsCompaniesYahooWebDao;
import com.oak.external.finance.app.marketdata.api.impl.FinancialStatementsConverter;
import com.oak.external.finance.app.marketdata.api.impl.FinancialStatementsProviderImpl;
import com.oak.external.finance.app.marketdata.api.impl.MarketDataPollingProviderImpl;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.EarningsCalendarYahooWebDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooDataConnector;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooDataConverter;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooJsonFinancialDataDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooWebDataBalanceSheetDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooWebDataCashFlowStatementDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooWebDataIncomeStatementDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooWebDataSectorCompaniesDao;
import com.oak.external.finance.app.marketdata.api.yahoo.YahooDataConverterImpl;
import com.oak.external.utils.input.api.StreamProvider;
import com.oak.external.utils.input.api.impl.FileStreamProvider;
import com.oak.external.utils.web.WebParsingUtils;
import com.oak.finance.app.dao.SymbolsDao;
import com.oak.finance.app.dao.impl.files.SymbolsFileDao;
import com.oak.finance.app.main.controllers.ApplicationController;
import com.oak.finance.app.main.controllers.ApplicationMainControllerImpl;
import com.oak.finance.app.main.server.ApplicationServer;
import com.oak.finance.app.main.server.ApplicationServerImpl;
import com.oak.finance.app.monitor.MarketDataMonitorsController;
import com.oak.finance.app.monitor.MarketDataMonitorsControllerImpl;
import com.oak.finance.app.monitor.MarketDataPersistenceController;
import com.oak.finance.app.monitor.MarketDataPersistenceControllerImpl;
import com.oak.finance.app.monitor.analysis.FinanceAnalysisController;
import com.oak.finance.app.monitor.analysis.FinanceFundamentalAnalysisControllerImpl;
import com.oak.finance.interest.SymbolsController;
import com.oak.finance.interest.SymbolsControllerImpl;
import com.oak.view.vaadin.main.StockScreenerControlUI;

@Configuration
public class ApplicationConfig {

	@Value("sector.datasource")
	private String sectorsUrl;
	
	/**
	 * When market data provider is instantiated, it will try to check the corporate earning calendar
	 * it does this using a sliding window back, and loads all the financial statements that were issued the 
	 * last few days. 
	 * This controls how many days to look back
	 */
	private int earningsCalendarWindowBackInDays = 15;
	private int earningsCalendarWindowForwardInDays = 90;
	private long historyBackInMilliSeconds = 7 * 24 * 60 * 60 * 1000;
	// private String stocksFilename = "/stocks/yahoo.csv";
	private static String ROOT_PATH = "C:\\Users\\charb\\Dropbox\\invest\\";
	private static String TEST_STOCKS = "yahoo_1.csv";
	private static String STOCKS = "yahoo.csv";
	
	private String stocksFilename = ROOT_PATH + STOCKS;
	private String interestingCompaniesSymbolsFileName = ROOT_PATH + "interestingCompaniesSymbolsFileName.csv";
	// private String stocksFilename =
	// "C:\\Users\\charb_000\\Documents\\invest\\data\\yahoo.us.csv";
	// private String stocksFilename =
	// "C:\\Users\\charb_000\\Documents\\invest\\data\\test_yahoo.csv";
	// private String stocksStocksWithBadPrices =
	// "/stocks/yahooStocksWithoutPrices.csv";
	// private String stocksWithNoPriceFileName=
	// "/stocks/stocksWithNoPrice.txt";
	private String stocksWithNoPriceFileName = ROOT_PATH + "stocksWithNoPrice.txt";
//	private String stocksToWatch = ROOT_PATH + "watchList_persist.csv";
	private String stocksToWatch = ROOT_PATH + "watchList.csv";
	// private String stocksToWatch =
	// "C:\\Users\\charb_000\\Documents\\invest\\data\\test_watchList.csv";
	private Double targetMinCurrentRatio = 2.0;
	private Double targetMinQuickRatio = 1.0;
	private Double targetMinAssetToDebtRatio = 3.0;
	
	private Logger log = LogManager.getLogger(ApplicationConfig.class);

	@Autowired
	private ControlRepository controlRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CompanyWithProblemsRepository companyWithProblemsRepository;
	@Autowired
	private SectorRepository sectorRepository;
	@Autowired
	private Screen0ResultsRepository screeningResultsRepository;
	@Autowired
	private BalanceSheetRepository balanceSheetRepository;
	@Autowired
	private IncomeStatementRepository incomeStatementRepository;
	@Autowired
	private CashFlowStatementRepository cashFlowStatementRepository;
	@Autowired
	private EarningsCalendarRepository earningsCalendarRepository;
	@Autowired
	private EconomicRepository economicRepository;
	
	@Bean
	ApplicationServer appServer() {
		log.debug("Creating appServer ...");
		Logger appLog = LogManager.getFormatterLogger(ApplicationServerImpl.class);
		ApplicationServer ret = new ApplicationServerImpl(appController(), appLog);
		log.debug("Creating appServer ... Done");
		return ret;
	}
	@Bean
	DuplicateCashflowsDao duplicateCashflowsDao() {
		return new DuplicateCashflowDaoImpl();
	}
	
	@Bean
	ApplicationController appController() {
		log.debug("creating app...");
		ApplicationMainControllerImpl applicationServer = new ApplicationMainControllerImpl(
				symbolController(), marketDataMonitorsController(),
				screeningResultsRepository, earningsCalendarRepository, 
				balanceSheetRepository, cashFlowStatementRepository, 
				incomeStatementRepository, companyRepository, duplicateCashflowsDao(), 
				financialStatementsProvider(), LogManager.getFormatterLogger(ApplicationMainControllerImpl.class));
		log.debug("creating app...done");
		return applicationServer;
	}

	@Bean
	SymbolsController symbolController() {
		log.debug("creating stockListProvider...");
		SymbolsController symbolController = new SymbolsControllerImpl(
				symbolsDao(), sectorsCompaniesDao(), 
				controlProvider(), companyRepository, 
				companyWithProblemsRepository, sectorRepository, 
				screeningResultsRepository, yahooConnector(), LogManager.getFormatterLogger(SymbolsControllerImpl.class));
		log.debug("creating stockListProvider...done");
		return symbolController;
	}

	@Bean
	StreamProvider streamProvider() {
		log.debug("creating streamProvider...");
		// ResourceFileStreamProvider resourceFileStreamProvider = new
		// ResourceFileStreamProvider(stocksFilename,
		// LogManager.getFormattesrLogger(ResourceFileStreamProvider.class));
		StreamProvider streamProvider = new FileStreamProvider(stocksFilename,
				LogManager.getFormatterLogger(FileStreamProvider.class));
		log.debug("creating streamProvider...done");
		return streamProvider;
	}

	@Bean
	SymbolsDao symbolsDao() {
		log.debug("creating symbolsDao...");
		SymbolsFileDao symbolsFileDao = new SymbolsFileDao(stocksFilename,
				stocksWithNoPriceFileName, stocksToWatch,
				interestingCompaniesSymbolsFileName, streamProvider(),
				LogManager.getFormatterLogger(SymbolsFileDao.class));
		log.debug("creating symbolsDao...done");
		return symbolsFileDao;
	}

	@Bean
	MarketDataProvider marketDataProvider() {
		log.debug("creating marketDataProvider...");
		MarketDataProvider marketDataPollingProvider = new MarketDataPollingProviderImpl(yahooConnector(),
				earningsCalendarDao(), earningsCalendarRepository, balanceSheetRepository,
				financialStatementsProvider(), controlProvider(),
				earningsCalendarWindowBackInDays, earningsCalendarWindowForwardInDays, LogManager.getFormatterLogger(MarketDataPollingProviderImpl.class));
		log.debug("creating marketDataProvider...done");
		return marketDataPollingProvider;
	}

	@Bean
	DataConnector yahooConnector() {
		log.debug("creating yahooConnector...");
		Logger logger = LogManager.getFormatterLogger(YahooDataConnector.class);
		YahooDataConnector yahooDataConnector = new YahooDataConnector(logger,
				yahooDataConverter());
		log.debug("creating yahooConnector...done");
		return yahooDataConnector;
	}

	@Bean
	YahooDataConverter yahooDataConverter() {
		log.debug("creating yahooDataConverter...");
		YahooDataConverterImpl yahooDataConverter = new YahooDataConverterImpl(
				LogManager.getFormatterLogger(YahooDataConverterImpl.class));
		log.debug("creating yahooDataConverter... Done");
		return yahooDataConverter;
	}

	@Bean
	MarketDataMonitorsController marketDataMonitorsController() {
		log.debug("creating marketDataMonitorsController...");
		MarketDataMonitorsController marketDataMonitorsController = new MarketDataMonitorsControllerImpl(
				symbolController(),
				financeAnalysisController(),
				marketDataProvider(),
				marketDataPersistenceController(), 
				LogManager.getFormatterLogger(MarketDataMonitorsControllerImpl.class));
		log.debug("creating marketDataMonitorsController...done");
		return marketDataMonitorsController;
	}
	@Bean
	MarketDataPersistenceController marketDataPersistenceController() {
		log.debug("creating marketDataPersistenceController...");
		MarketDataPersistenceController marketDataPersistanceController = 
				new MarketDataPersistenceControllerImpl(
						LogManager.getFormatterLogger(MarketDataPersistenceControllerImpl.class), 
						economicRepository, companyRepository);
		log.debug("creating marketDataPersistenceController...done");
		return marketDataPersistanceController;
	}

	@Bean 
	FinancialDataDao financialDataDao() {
		log.debug("creating yahooJsonFinancialDataDao...");
		YahooJsonFinancialDataDao yahooJsonFinancialDataDao = new YahooJsonFinancialDataDao(LogManager.getFormatterLogger(YahooJsonFinancialDataDao.class));
		log.debug("creating yahooJsonFinancialDataDao...done");
		return yahooJsonFinancialDataDao;
	}
	@Deprecated
	@Bean
	BalanceSheetDao balanceSheetDao() {
		log.debug("creating balanceSheetDao...");
		BalanceSheetDao yahooWebDataBalanceSheet = new YahooWebDataBalanceSheetDao(
				LogManager
						.getFormatterLogger(YahooWebDataBalanceSheetDao.class));
		log.debug("creating balanceSheetDao... done");
		return yahooWebDataBalanceSheet;
	}
	@Bean // TODO IncomeStatementDao Not used - needs cleaning up
	IncomeStatementDao incomeStatementDao(){
		log.debug("creating incomeStatementDao... instance of YahooWebDataIncomeStatementDao");
		YahooWebDataIncomeStatementDao yahooWebDataIncomeStatementDao = new YahooWebDataIncomeStatementDao(LogManager.getFormatterLogger(YahooWebDataIncomeStatementDao.class));
		log.debug("creating incomeStatementDao...done ");
		return yahooWebDataIncomeStatementDao; 
	}
	@Bean // TODO CashFlowStatementDao Not used - needs cleaning up
	CashFlowStatementDao cashFlowStatementDao(){
		log.debug("creating incomeStatementDao... instance of YahooWebDataIncomeStatementDao");
		YahooWebDataCashFlowStatementDao yahooWebDataCashFlowStatementDao = new YahooWebDataCashFlowStatementDao(LogManager.getFormatterLogger(YahooWebDataIncomeStatementDao.class));
		log.debug("creating incomeStatementDao...done ");
		return yahooWebDataCashFlowStatementDao; 
	}
	
	@Bean
	FinancialStatementsProvider financialStatementsProvider() {
		log.debug("creating financialStatementsProvider... instance of FinancialStatementsProviderImpl");
		Logger logger = LogManager.getFormatterLogger(FinancialStatementsProviderImpl.class);
		FinancialStatementsProviderImpl financialStatementsProvider = new FinancialStatementsProviderImpl(
				financialDataDao(), balanceSheetRepository, statementsConverter(), incomeStatementRepository, cashFlowStatementRepository, logger);
		log.debug("creating financialStatementsProvider...done ");
		return financialStatementsProvider;
	}
	@Bean
	FinancialStatementsConverter statementsConverter() {
		Logger logger = LogManager.getFormatterLogger(FinancialStatementsProviderImpl.class);
		FinancialStatementsConverter statementsConverter = new FinancialStatementsConverter(logger);
		return statementsConverter;
	}
	
	@Bean
	FinanceAnalysisController financeAnalysisController() {
		log.debug("creating financeAnalysisController...");
		Logger logger = LogManager .getFormatterLogger(FinanceFundamentalAnalysisControllerImpl.class);
		FinanceFundamentalAnalysisControllerImpl financeFundamentalAnalysisController = new FinanceFundamentalAnalysisControllerImpl(
				financialStatementsProvider(), 
				targetMinCurrentRatio, targetMinQuickRatio, targetMinAssetToDebtRatio, logger);
		log.debug("creating financeAnalysisController...done");
		return financeFundamentalAnalysisController;
	}
	
	@Bean
	SectorsCompaniesYahooWebDao sectorsCompaniesDao() {
		log.debug("creating sectorDao...");
		Logger logger = LogManager.getFormatterLogger(YahooWebDataSectorCompaniesDao.class);
		SectorsCompaniesYahooWebDao dao = new YahooWebDataSectorCompaniesDao(logger,sectorsUrl, webParsingUtils());
		log.debug("creating sectorDao...done");
		return dao;
	}

	@Bean
	 WebParsingUtils webParsingUtils() {
		log.debug("creating webParsingUtils...");
		WebParsingUtils webParsingUtils = new WebParsingUtils();
		log.debug("creating webParsingUtils...Done");
		return webParsingUtils;
	}
	@Bean
	EarningsCalendarDao earningsCalendarDao() {
		log.debug("creating earningsCalendarDao...");
		Logger logger = LogManager.getFormatterLogger(EarningsCalendarYahooWebDao.class);
		String url = "https://biz.yahoo.com/research/earncal/";
		log.debug("creating earningsCalendarDao...Done");
		EarningsCalendarDao ret = new EarningsCalendarYahooWebDao(logger, url, webParsingUtils());
		return ret;
	}
	
	@Bean
	ControlProvider controlProvider() {
		log.debug("creating controlProvider....");
		Logger contrlLogger = LogManager.getFormatterLogger(ControlProviderImpl.class);
		ControlProvider controlProvider = new ControlProviderImpl(controlRepository,contrlLogger);
		log.debug("creating controlProvider...Done!");
		return controlProvider;
	}
	@Bean 
	MainController mainController() {
		log.debug("creating mainController....");
		MainControllerImpl controller = new MainControllerImpl(appController(),screeningResultsRepository,
				controlRepository);
		log.debug("creating mainController...Done!");
		return controller;
	}
	@Bean
	@Scope("prototype") 
	StockScreenerControlUI ui() {
		log.debug("creating ui....");
		StockScreenerControlUI ui = new StockScreenerControlUI(mainController());
		log.debug("creating ui...Done!");
		return ui;
	}
	
}
