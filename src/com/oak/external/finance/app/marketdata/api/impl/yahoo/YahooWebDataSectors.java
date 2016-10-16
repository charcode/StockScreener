package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.util.Collection;

import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import com.oak.api.finance.model.dto.Sector;
import com.oak.external.finance.app.marketdata.api.SectorDao;

public class YahooWebDataSectors implements SectorDao{

	private  Logger log;
	private String url = "https://biz.yahoo.com/p/210conameu.html";
	
	public YahooWebDataSectors(Logger log, String url) {
		this.log = log;
		this.url = url;
	}
	
	@Override
	public Collection<Sector> getSectors() {
		try {
			Jsoup.connect(url).ignoreContentType(true).execute().body();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
