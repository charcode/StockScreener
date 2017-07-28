package com.oak.api;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.data.util.StreamUtils;

import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.Screen0Result;
import com.oak.api.finance.model.dto.Status;
import com.oak.api.finance.repository.ControlRepository;
import com.oak.api.finance.repository.Screen0ResultsRepository;
import com.oak.finance.app.main.controllers.ApplicationController;

public class MainControllerImpl implements MainController {
	private final Screen0ResultsRepository resRepo;
	private final ApplicationController appController;
	private final ControlRepository ctrlRepo;
	private final Environment env;
	
	public MainControllerImpl(ApplicationController appController, 
			Screen0ResultsRepository resRepo,ControlRepository ctrlRepo, Environment env){
		this.appController = appController;
		this.resRepo = resRepo;
		this.ctrlRepo = ctrlRepo;
		this.env = env;
	}

	@Override
	public Status launchAnalysis(AnalysisResultsListener listener) {
		appController.launchAnalysis();
		return Status.IN_PROGRESS;
	}
	@Override
	public void loadHistoricalQuotes() {
		appController.loadHistoricalQuotes();
	}
	@Override
	public List<Control> getStatuses() {
		List<Control> sts = fromIterable(ctrlRepo.findAll(), 
							(a,b) -> reverseCompare(a.getTimeStamp(), 
									b.getTimeStamp()) // show latest first
						);
		return sts;
	}
	private <T>List<T>fromIterable(Iterable<T> i, Comparator<T>c){
		return StreamUtils.createStreamFromIterator(i.iterator())
				.sorted(c)
				.collect(Collectors.toList());
	}
	private <T> int reverseCompare(T a, Comparable<T> b) {
		int ret = 1;
		if(a != null && b != null) {
			ret = b.compareTo(a); 
		}
		return ret;
	}
	
	@Override
	public List<Screen0Result>getResults(){
		List<Screen0Result> res = fromIterable(resRepo.findAll(), (a,b) ->
									reverseCompare (a.getRunDate(),// show latest first
												b.getRunDate())
									);
		return res;
	}

	@Override
	public void fixDuplicatedCashflows(DuplicatCashflowListener listener) {
		appController.fixDuplicatedCashflows(listener);
	}
	
	@Override
	public Optional<String[]> getActiveProfiles() {
		return Optional.ofNullable(env==null?null:env.getActiveProfiles());
	}
}
