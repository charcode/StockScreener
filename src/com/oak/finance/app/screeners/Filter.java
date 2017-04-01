package com.oak.finance.app.screeners;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude = {"id","asOf"})
public class Filter implements Comparable<Filter>{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Long id;
	
	private Date asOf;
	private Double maxPerCalculated;
	private Double maxPe;
	private Double maxFwdPe;
	private Double maxPeg;
	private Double minEpsTtm;
	private Double minEpsCurrentYr;
	private Double minEpsNextYr;
	private Double minEpsNextQtr;
	private Double maxBookValueMultiple;//*pe, or *per, per > 0 and pe>0 and < 25, <27

	@Override
	public int compareTo(Filter o) {
		int i = maxPerCalculated.compareTo(o.maxPerCalculated);
		if (i == 0)
			i = maxPe.compareTo(o.maxPe);
		if (i == 0)
			i = maxFwdPe.compareTo(o.maxFwdPe);
		if (i == 0)
			i = maxPeg.compareTo(o.maxPeg);
		if (i == 0)
			i = minEpsTtm.compareTo(o.minEpsTtm);
		if (i == 0)
			i = minEpsCurrentYr.compareTo(o.minEpsCurrentYr);
		if (i == 0)
			i = minEpsNextYr.compareTo(o.minEpsNextYr);
		if (i == 0)
			i = minEpsNextQtr.compareTo(o.minEpsNextQtr);
		if (i == 0)
			i = maxBookValueMultiple.compareTo(o.maxBookValueMultiple);

		return i;
	}
}
