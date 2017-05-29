package com.oak.finance.app.monitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.FinancialAnalysis;
import com.oak.api.finance.model.Stock;
import com.oak.external.finance.app.marketdata.api.MarketDataProvider;
import com.oak.finance.app.monitor.analysis.FinanceAnalysisController;
import com.oak.finance.app.monitor.analysis.FinanceAnalysisController.FinanceAnalysisCallback;
import com.oak.finance.interest.SymbolsController;

public class MarketDataMonitorsControllerImpl implements MarketDataMonitorsController {

	/**
	 * When market data readers report blank prices, the stocks get added to
	 * this queue the purpose is to make a list of persistently empty stocks to
	 * remove them from the list
	 */
	private final LinkedBlockingQueue<String> newStocksWithoutPrices;
	private final LinkedBlockingQueue<Map<Stock, Map<Date, Economic>>> marketDataForAnalysisQueue;
	private final LinkedBlockingQueue<Map<Stock, Map<Date, Economic>>> marketDataForPersistingQueue;
	private boolean stop = false;
	private final Logger logger;
	private final FinanceAnalysisController analysisController;
	private final SymbolsController symbolsProvider;
	private final MarketDataProvider marketDataProvider;
	private final Executor executor;
	private final StocksCallback callback;
	private final MarketDataPersistenceController marketDataPersistenceController;
	private final Map<Stock, Map<Date, Economic>> endMarker;

	public MarketDataMonitorsControllerImpl(SymbolsController symbolsController,
			FinanceAnalysisController analysisController, MarketDataProvider marketDataProvider,
			MarketDataPersistenceController marketDataPersistenceController, Logger logger) {
		logger.debug("creating MarketDataMonitorsControllerImpl");
		this.newStocksWithoutPrices = new LinkedBlockingQueue<String>();
		this.marketDataForAnalysisQueue = new LinkedBlockingQueue<Map<Stock, Map<Date, Economic>>>();
		this.marketDataForPersistingQueue = new LinkedBlockingQueue<Map<Stock, Map<Date, Economic>>>();
		this.logger = logger;
		this.executor = Executors.newCachedThreadPool();
		this.analysisController = analysisController;
		this.symbolsProvider = symbolsController;
		this.marketDataProvider = marketDataProvider;
		this.marketDataPersistenceController = marketDataPersistenceController;
		this.callback = new StocksCallback();
		logger.debug("starting the wait loop");
		endMarker = createEndMarker();
	}

	public void saveGoodValueStock(Stock stock, Map<Date, Economic> economics) {
		symbolsProvider.saveGoodValueStock(stock, economics);
	}

	private void saveSymbolsWithoutPrice(Map<String, Date> existingStocksWithoutPrices) {
		symbolsProvider.saveSymbolsWithoutPrice(existingStocksWithoutPrices);
	}

	@Override
	public void startStocksAnalysis(final Set<String> symbolList, final Set<String> interestingSymbols) {
		Runnable marketDataCollectionRunnable = new Runnable() {
			@Override
			public void run() {
				marketDataCollectionThread(symbolList);
			}
		};
		Thread marketDataCollectionThread = new Thread(marketDataCollectionRunnable, "DataCollectionThread");
		Thread stocksAnalysisThread = new Thread("StocksAnalysisThread") {
			public void run() {
				stockProcessorThread(interestingSymbols, new EconomicAnalyzer(),marketDataForAnalysisQueue);
			}
		};
		Thread economicsPersistenceThread = new Thread("EconomicsPersistenceThread") {
			@Override
			public void run() {
				stockProcessorThread(interestingSymbols, new EconomicPersistence(),marketDataForPersistingQueue);
			}
		};
		executor.execute(marketDataCollectionThread);
		executor.execute(stocksAnalysisThread);
		executor.execute(economicsPersistenceThread);
		try {
			economicsPersistenceThread.join();
		} catch (InterruptedException e) {
			logger.error("Interrupted "+e.getMessage(),e);
		}
	}

	private void stockProcessorThread(Set<String> alwaysWatch, EconomicProcessor processor, LinkedBlockingQueue<Map<Stock, Map<Date, Economic>>> marketDataQueue) {
		try {
			boolean stop = false;
			while (!stop) {
				Map<Stock, Map<Date, Economic>> marketData = marketDataQueue.take();
				if (endMarker.equals(marketData)) {
					stop = true;
					logger.info(processor.getEndOfProcessingMessage());
				} else {
					processor.onEconomicData(alwaysWatch, marketData);
				}
			}
		} catch (InterruptedException e) {
			logger.error("Stock Analysis thread was interrupted", e);
		}
	}
	interface EconomicProcessor{
		void onEconomicData(Set<String> alwaysWatch, Map<Stock, Map<Date, Economic>> marketData);
		String getEndOfProcessingMessage();
	}
	class EconomicAnalyzer implements EconomicProcessor{
		@Override
		public void onEconomicData(Set<String> alwaysWatch, Map<Stock, Map<Date, Economic>> marketData) {
			analyzeEconomics(alwaysWatch,marketData);
		}
		@Override
		public String getEndOfProcessingMessage() {
			return "Finished collecting and analyzing market data, exiting loop.";
		}
	}
	class EconomicPersistence implements EconomicProcessor{
		@Override
		public void onEconomicData(Set<String> alwaysWatch, Map<Stock, Map<Date, Economic>> marketData) {
			marketDataPersistenceController.persist(marketData);
		}
		@Override
		public String getEndOfProcessingMessage() {
			return "Finished saving market data to db, exiting loop.";
		}
	}

	private void analyzeEconomics(Set<String> alwaysWatch, Map<Stock, Map<Date, Economic>> marketData) {
		for (Stock s : marketData.keySet()) {
			Map<Date, Economic> economics = marketData.get(s);
			analysisController.onEconomicsUpdate(callback, s, economics, alwaysWatch);
		}
	}

	private void marketDataCollectionThread(Set<String> symbolList) {
		Collection<Set<String>> batches = batch(symbolList, 10);
		for (Set<String> batch : batches) {
			Map<Stock, Map<Date, Economic>> batchResult = marketDataProvider.retrieveMarketData(batch);
			marketDataForAnalysisQueue.add(batchResult);
			marketDataForPersistingQueue.add(batchResult);
		}
		marketDataForAnalysisQueue.add(endMarker);
		marketDataForPersistingQueue.add(endMarker);
	}

	private Map<Stock, Map<Date, Economic>> createEndMarker() {
		Map<Stock, Map<Date, Economic>> endMarker = new HashMap<Stock, Map<Date, Economic>>();
		Stock stopStock = new Stock("STOP", "STOP", "STOP", "STOP", "STOP", "STOP");
		HashMap<Date, Economic> stopMap = new HashMap<Date, Economic>();
		Date beginning = Calendar.getInstance().getTime();
		beginning.setTime(0);
		Economic stopEconomic = new Economic(beginning, 0.0, 0L, 0.0, 0L, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, "STOP", 0L, "STOP", 0.0, 0.0, 0.0, 0.0, "STOP", TimeZone.getDefault(), 0L, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0L, 0L, 0L, 0.0, 0.0,
				Calendar.getInstance(), Calendar.getInstance());
		stopMap.put(beginning, stopEconomic);
		endMarker.put(stopStock, stopMap);
		return endMarker;
	}

	private <T> Collection<Set<T>> batch(Set<T> set, int size) {
		Collection<Set<T>> ret = null;

		if (set != null) {
			ret = new ArrayList<Set<T>>();
			int i = 0;
			Set<T> batch = new HashSet<T>();
			for (T e : set) {
				batch.add(e);
				if (i >= size - 1) {
					ret.add(batch);
					batch = new HashSet<T>();
					i = 0;
				} else {
					i++;
				}
			}
			if (!batch.isEmpty()) {
				ret.add(batch);
			}
		}
		return ret;
	}

	private class StocksCallback implements FinanceAnalysisCallback {

		@Override
		public void onPriceMissing(Stock stock, Map<Date, Economic> economics) {
			SortedSet<Date> dates = new TreeSet<Date>(economics.keySet());
			HashMap<String, Date> s = new HashMap<String, Date>();
			s.put(stock.getSymbol(), dates.last());
			saveSymbolsWithoutPrice(s);
		}

		@Override
		public void onBuy(Stock stock, Map<Date, Economic> economics, FinancialAnalysis financialAnalysis) {
			saveGoodValueStock(stock, economics);
		}

		@Override
		public void onSell(Stock stock, Map<Date, Economic> economics) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onWatchList(Stock stock, Map<Date, Economic> economics, FinancialAnalysis stockAnalysis) {

			saveGoodValueStock(stock, economics);
		}

	}

}
