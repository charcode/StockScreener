package com.oak.api.finance.model.dto;

import java.util.Date;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@Entity
public class Quote extends AbstractQuote{

	private Double open;
	private Double close;
	private Double high;
	private Double low;
	private Double adjustmentFactor;
	private Long volume;
	private Date date;
	
	
	public Quote(Long id, String ticker, Double open, Double close, Double high, Double low, Double adjustmentFactor, Long volume, Date date) {
		super(id,ticker);
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.adjustmentFactor = adjustmentFactor;
		this.volume = volume;
		this.date = date;
	}
	
	
}
