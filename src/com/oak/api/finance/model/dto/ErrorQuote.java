package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name="historic_quote_error")
@EqualsAndHashCode(callSuper=true)
public class ErrorQuote extends AbstractQuote {

	private final boolean isError;
	private final Exception exception;
	private final String message;
	private final QuoteErrorType errorType;
	private final Date timeStamp;

	private ErrorQuote(String ticker, Exception e, String message, QuoteErrorType errorType) {
		super(null, ticker);
		this.exception = e;
		this.message = message;
		this.errorType = errorType;
		this.isError = true;
		this.timeStamp = new Date();
	}
	
	public static ErrorQuote newQuoteInError(String ticker, Exception e, String message, QuoteErrorType errorType) {
		return new ErrorQuote(ticker, e, message, errorType);
	}
}
