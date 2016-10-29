package com.oak.api.finance.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Sector implements Comparable<Sector>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	private String description;
	private String industry;
	private Long parentSectorId; // null when top level sector
	private String parentSectorDescription;  // null when top level sector
	
	private double oneDayPriceChangePercent;
	private double marketCap;
	/**
	 * Price to Earning Ratio
	 */
	private double peRatio; 
	/**
	 * Return on equity in percentage
	 */
	private double roePercent;
	private double dividendYield;
	private double longTermDebtToEquity;
	private double priceToBookValue;
	private double netProfitMarginPercent;
	private double priceToFreeCashFlow;
	
	@Override
	public int compareTo(Sector o) {
		int compareTo;
		if(description != null) {
			compareTo = description.compareTo(o.description);
		}else {
			if(o.description == null) {
				compareTo = 0;
			}else {
				compareTo = -1;
			}
		}
		return compareTo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sector other = (Sector) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (Double.doubleToLongBits(dividendYield) != Double.doubleToLongBits(other.dividendYield))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (industry == null) {
			if (other.industry != null)
				return false;
		} else if (!industry.equals(other.industry))
			return false;
		if (Double.doubleToLongBits(longTermDebtToEquity) != Double.doubleToLongBits(other.longTermDebtToEquity))
			return false;
		if (Double.doubleToLongBits(marketCap) != Double.doubleToLongBits(other.marketCap))
			return false;
		if (Double.doubleToLongBits(netProfitMarginPercent) != Double.doubleToLongBits(other.netProfitMarginPercent))
			return false;
		if (Double.doubleToLongBits(oneDayPriceChangePercent) != Double
				.doubleToLongBits(other.oneDayPriceChangePercent))
			return false;
		if (parentSectorDescription == null) {
			if (other.parentSectorDescription != null)
				return false;
		} else if (!parentSectorDescription.equals(other.parentSectorDescription))
			return false;
		if (parentSectorId == null) {
			if (other.parentSectorId != null)
				return false;
		} else if (!parentSectorId.equals(other.parentSectorId))
			return false;
		if (Double.doubleToLongBits(peRatio) != Double.doubleToLongBits(other.peRatio))
			return false;
		if (Double.doubleToLongBits(priceToBookValue) != Double.doubleToLongBits(other.priceToBookValue))
			return false;
		if (Double.doubleToLongBits(priceToFreeCashFlow) != Double.doubleToLongBits(other.priceToFreeCashFlow))
			return false;
		if (Double.doubleToLongBits(roePercent) != Double.doubleToLongBits(other.roePercent))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		long temp;
		temp = Double.doubleToLongBits(dividendYield);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((industry == null) ? 0 : industry.hashCode());
		temp = Double.doubleToLongBits(longTermDebtToEquity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(marketCap);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(netProfitMarginPercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(oneDayPriceChangePercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((parentSectorDescription == null) ? 0 : parentSectorDescription.hashCode());
		result = prime * result + ((parentSectorId == null) ? 0 : parentSectorId.hashCode());
		temp = Double.doubleToLongBits(peRatio);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(priceToBookValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(priceToFreeCashFlow);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(roePercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	

}
