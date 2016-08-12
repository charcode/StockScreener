package com.oak.api.finance.model;

public class FinancialComment {

	public enum CommentType {
		PastEarnings, FutureEarnings, Peg, BookValue, Currency, MissingData
	}

	private final String comment;
	private final CommentType type;

	public FinancialComment(String comment, CommentType type) {
		this.comment = comment;
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public CommentType getType() {
		return type;
	}

}
