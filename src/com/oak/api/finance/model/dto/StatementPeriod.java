package com.oak.api.finance.model.dto;

public enum StatementPeriod {
	
	MONTLY(30), 
	QUARTERLY(90), 
	ANNUAL(365);
	
	private int length;

	private StatementPeriod(int length) {
		this.length = length;
	}
	
	public int getLength() {
		return length;
	}

}
