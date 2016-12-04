package com.oak.view.vaadin.main;

import java.util.List;
import java.util.stream.Collectors;

import com.oak.api.finance.model.dto.Screen0Result;

public class ScreenResultsAdapter {
	List<Screen0ResultAdapter>adapt(List<Screen0Result> r){
		return r.stream().map(i -> new Screen0ResultAdapter(i)).collect(Collectors.toList());
	}
}
