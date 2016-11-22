package com.oak.finance.app.main.controllers;

import com.oak.api.MainController.DuplicatCashflowListener;

public interface ApplicationController {
	void launchAnalysis();

	void fixDuplicatedCashflows(DuplicatCashflowListener listener);

	void fixWrongQuarterlyStatements();
}
