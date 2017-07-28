package com.oak.api;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.Screen0Result;
import com.oak.api.finance.model.dto.Status;

import lombok.Data;

/**
 * This is the main controller that external clients call
 * @author charb
 *
 */
public interface MainController {
	Status launchAnalysis(AnalysisResultsListener listener);
	List<Control>getStatuses();
	List<Screen0Result>getResults();
	void fixDuplicatedCashflows(DuplicatCashflowListener listener);
	Optional<String[]> getActiveProfiles();
	
	@Data
	public class TickersData{
		Set<String>allTickers;
		Set<String>excluded;
		Set<String>remaining;		
	}
	public interface AnalysisResultsListener {
		void onTickersLoaded(TickersData data);
		void onAnalysingTicker(String ticker);
		void onFinished(Control control);
	}
	public interface DuplicatCashflowListener {
		void onTickersFound(Map<String,List<Date>>tickers);
		void onProgress(String ticker, Date date);
		void onDone(Status status, String ticker);
	}
	void loadHistoricalQuotes();
	
}
