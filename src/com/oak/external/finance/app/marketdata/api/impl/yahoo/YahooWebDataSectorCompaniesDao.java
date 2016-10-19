package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Sets;
import com.oak.api.finance.model.SectorsIndustriesCompanies;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.Sector;
import com.oak.external.finance.app.marketdata.api.SectorsCompaniesYahooWebDao;
import com.oak.external.utils.web.WebParsingUtils;

public class YahooWebDataSectorCompaniesDao implements SectorsCompaniesYahooWebDao{

	private static final String SECTOR = "Sector";
	private static final String DESCRIPTION = "Description";
	private static final String MORE_INFO = "More Info";
	private Logger log;
	private String url = "https://biz.yahoo.com/p/s_conameu.html";
	private WebParsingUtils webParsingUtils;
	
	private List<String> headersExpected = Arrays.asList( SECTOR, "1 Day Price Change %", "Market Cap", "P/E", "ROE %", "Div. Yield %",
			"Long-Term Debt to Equity", "Price to Book Value", "Net Profit Margin % (mrq)",
			"Price to Free Cash Flow (mrq)" );
	
	public YahooWebDataSectorCompaniesDao(Logger log, String url, WebParsingUtils  webParsingUtils) {
		this.log = log;
		this.url = url;
		this.webParsingUtils = webParsingUtils;
	}
	
	@Override
	public SectorsIndustriesCompanies getSectorsAndCompanies() {
		List<String>errors = new ArrayList<>();
		
		SectorsIndustriesCompanies sectorsAndCompanies = parseUrlRecursive(url,errors);
		return sectorsAndCompanies;
	}

	private SectorsIndustriesCompanies parseUrlRecursive(String url, List<String> errors) {
		SectorsIndustriesCompanies ret = new SectorsIndustriesCompanies();
		Map<Sector,String> urls = parseSectorPage(url, errors,SECTOR);
		int lastSlash = url.lastIndexOf("/");
		String root = url.substring(0,lastSlash+1);
		for(Sector s:urls.keySet()) {
			String sectorUrl = root + urls.get(s);
			Map<Sector,String> industriesUrls = parseSectorPage(sectorUrl, errors,DESCRIPTION);
			ret.addIndustriesToSector(s,industriesUrls.keySet());
			for(Sector industry:industriesUrls.keySet()) {
				String industryUrl = root + industriesUrls.get(industry);
				Map<Sector,String> companiesUrl = parseSectorPage(industryUrl, errors,DESCRIPTION);
				
				for(Sector cmpnyAsSector:companiesUrl.keySet()) {
					String companyUrl = companiesUrl.get(cmpnyAsSector);
					Company company = new Company();
					String[] tokens = cmpnyAsSector.getDescription().split("(");
					String name = tokens[0];
					String [] another = tokens[1].split(")");
					String ticker = another[0];
					company.setName(name);
					company.setTicker(ticker);
					ret.addCompanyToIndustry(industry, company);
				}
			}
		}
		return ret;
	}


	private Map<Sector,String> parseSectorPage(String url, List<String> errors,String sectorOrDescription){
		Map<Sector,String> urls = new ConcurrentSkipListMap<>();
		Document document;
		try {
			document = Jsoup.connect(url).get();
		
			Element e = document.select("table").get(3);
			Set<String> headersFound = new HashSet<String>();
			Elements rows = e.select("tr");
			for(Element row : rows) {
				Elements hdrs = e.select("th");
				int i = 0;
				boolean foundTable = false;
				for(Element h:hdrs) {
					String hd = webParsingUtils.advanceTrim(h.text());
					if(!MORE_INFO.equals(hd)) {
						headersFound.add(hd);
						foundTable = checkExpectedHeader(i++, hd, sectorOrDescription);
					}
				}
				
				if (foundTable) {
					Elements sectorElements = row.select("tr");
					for(Element sctElemt:sectorElements ) {
						Elements tr = sctElemt.select("td");
						if(!tr.isEmpty()) {
							Sector s = new Sector();
							if(tr.size() == 1) {
								continue;
							}
							Element sector = tr.get(0);
							s.setDescription(sector.text()); //"Sector"
							s.setOneDayPriceChangePercent(parseDouble(tr.get(1))); //"1 Day Price Change %"
							s.setMarketCap(parseDouble(tr.get(2))); //"Market Cap"
							s.setPeRatio(parseDouble(tr.get(3))); //"P/E"
							s.setRoePercent( parseDouble(tr.get(4))); //"ROE %"
							s.setDividendYield( parseDouble(tr.get(5))); //"Div. Yield %"
							s.setLongTermDebtToEquity( parseDouble(tr.get(6))); //"Long-Term Debt to Equity"
							s.setPriceToBookValue( parseDouble(tr.get(7))); //"Price to Book Value"
							s.setNetProfitMarginPercent( parseDouble(tr.get(8))); //"Net Profit Margin % (mrq)"
							s.setPriceToFreeCashFlow( parseDouble(tr.get(9))); //"Price to Free Cash Flow (mrq)"
							
							Elements sectorUrl = sector.select("a");
							if(! sectorUrl.isEmpty()) {
								String childUrl = sectorUrl.get(0).attr("href");
								urls.put(s,childUrl);
							}
						}
					}
				}
			}
			//validate headers: 
			for(String h:headersExpected) {
				if(!headersFound.contains(h)) {
					errors.add("Cannot find expected entry: "+h+", found entries:"+headersFound);
				}					
			}
		} catch (MalformedURLException e) {
			log.error("Unexpected IO error while getting list of companies and sectors ", e);
		} catch (IOException e1) {
			log.error("Unexpected IO error while getting list of companies and sectors ", e1);
		} catch (Throwable t) {
			log.error("Unexpected error while getting list of companies and sectors ", t);
		}
		return urls;
	}

	private boolean checkExpectedHeader(int i, String hd,String sectorOrDescription) {
		boolean foundTable;
		// first column label is different between the sectors page (Sector) and industries page (Description) 
		String expected = i==0 ? sectorOrDescription : headersExpected.get(i);
		foundTable = hd.equals(expected);
		log.debug("found the right table:"+foundTable+", found: "+hd+", expected:"+expected);
		if(!foundTable) {
			log.error("couldn't find the right table");
		}
		return foundTable;
	}

	private double parseDouble(Element cell) {
		String text = cell.text();
		String t = webParsingUtils.advanceTrim(text).toUpperCase();
		double ret = 0;
		double multiplier = 1.0;
		boolean doNotParse = false;
		if(t.endsWith("B")) {
			t = webParsingUtils.advanceTrim(t.substring(0, t.length()-1));
			multiplier = 1e9;
		}else if(t.endsWith("M")) {
			t = webParsingUtils.advanceTrim(t.substring(0, t.length()-1));
			multiplier = 1e6;
		}else if(t.equals("-") ||"NA".equals(t)) {
			ret = 0;
			doNotParse = true;
		}
		if(!doNotParse) {
			ret = Double.parseDouble(t);
			ret *= multiplier;
		}
		return ret;
	}

}
