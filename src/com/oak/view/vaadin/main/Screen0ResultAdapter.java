package com.oak.view.vaadin.main;

import java.util.Date;

import com.oak.api.finance.model.dto.Screen0Result;

public class Screen0ResultAdapter {
	private final Screen0Result res;

	public Screen0ResultAdapter(Screen0Result res){
		this.res = res;
	}

	public String getMarketCap() {
		String ret =  withSuffix(res.getMarketCap());
		return ret;
	}

	public String getPriceBid() {
		return withSuffix(res.getPriceBid());
	}

	public Date getRunDate() {
		return res.getRunDate();
	}
	public String getTicker() {
		return res.getTicker();
	}
	public String getCompanyName() {
		return res.getCompanyName();
	}
	public String getCurrency() {
		return res.getCurrency();
	}
	
	public String getTargetPrice() {
		return withSuffix(res.getTargetPrice());
	}
	public String getPer() {
		return withSuffix(res.getPerCalculated());
	}
	public String getPerCalculated() {
		return withSuffix(res.getPerCalculated());
	}
	public String getPeg() {
		return withSuffix(res.getPeg());
	}
	public String getEps() {
		return withSuffix(res.getEps());
	}
	public String getBookValuePerShare() {
		return withSuffix(res.getBookValuePerShare());
	}

	private String withSuffix(Double count) {
		String ret = "";
		if (count != null) {
			if (count < 1000)
				ret = String.format("%.02f", count);
			else {
				int exp = (int) (Math.log(count) / Math.log(1000));
				ret = String.format("%.02f %c", count / Math.pow(1000, exp), "kMBTPE".charAt(exp - 1));
			}
		}
		return ret;
	}
	
}
