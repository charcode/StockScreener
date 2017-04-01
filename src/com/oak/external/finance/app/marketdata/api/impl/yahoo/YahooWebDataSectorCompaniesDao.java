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

import com.oak.api.finance.model.SectorsIndustriesCompanies;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.Sector;
import com.oak.external.finance.app.marketdata.api.SectorsCompaniesYahooWebDao;
import com.oak.external.utils.web.WebParsingUtils;

public class YahooWebDataSectorCompaniesDao implements SectorsCompaniesYahooWebDao{

	private static final String SECTOR = "Sector";
	private static final String DESCRIPTION = "Description";
	private static final String MORE_INFO = "More Info";
	private final Logger log;
	private final String url; //= "https://biz.yahoo.com/p/s_conameu.html";
	private final WebParsingUtils webParsingUtils;

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
		
		log.info("Getting Sectors/Descriptions info and companies");
		SectorsIndustriesCompanies sectorsAndCompanies = parseUrlRecursive(url,errors);
		log.info("Getting Sectors/Descriptions info and companies .... done!");
		return sectorsAndCompanies;
	}

	private SectorsIndustriesCompanies parseUrlRecursive(String url, List<String> errors) {
		SectorsIndustriesCompanies ret = new SectorsIndustriesCompanies();
		Map<Sector,String> urls = parseSectorPage(url, errors,SECTOR); // sectors page has a "Sector" on the header
		int lastSlash = url.lastIndexOf("/");
		String root = url.substring(0,lastSlash+1);
		for(Sector s:urls.keySet()) {
			String sectorUrl = root + urls.get(s);
			log.info("Getting industries and companies for sector: " + s.getDescription());
			Map<Sector,String> industriesUrls = parseSectorPage(sectorUrl, errors,DESCRIPTION); // industries pages have a "Description" instead of "Sector"
			ret.addIndustriesToSector(s,industriesUrls.keySet());
			for(Sector industry:industriesUrls.keySet()) {
				if (industry != null) {
					String industryUrl = root + industriesUrls.get(industry);
					Map<Sector, String> companiesUrl = parseSectorPage(industryUrl, errors, DESCRIPTION); // companies pages have a "Description" instead of "Sector"

					for (Sector cmpnyAsSector : companiesUrl.keySet()) {
						String desc = cmpnyAsSector.getDescription();
						if (!desc.startsWith("Sector:") && !desc.startsWith("Industry:")) {
							try {
//								String companyUrl = companiesUrl.get(cmpnyAsSector);
								Company company = new Company();
								int st = desc.lastIndexOf("(");
								int et = 0;
								if(st > 0) {
									et = st - 1;
								}
								String name = desc.substring(0, et);
								String ticker = desc.substring(st+1, desc.length() - 1);
//								String[] tokens = desc.split("\\(");
//								String name = tokens[0];
//								String[] another = tokens[1].split("\\)");
//								String ticker = another[0];
								company.setName(name);
								company.setTicker(ticker);
								ret.addCompanyToIndustry(industry, company);
							} catch (Throwable t) {
								log.error(industryUrl + " : cannot split this: " + desc, t);
							}
						}
					}
				}
			}
		}
		return ret;
	}


	private Map<Sector,String> parseSectorPage(String url, List<String> errors,String sectorOrDescription){
		Map<Sector,String> urls = new ConcurrentSkipListMap<>();
		Document document;
		try {
			document = Jsoup.connect(url)
					 	.header("Accept-Encoding", "gzip, deflate")
					    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					    .maxBodySize(0)
					    .timeout(600000)
					    .get();
		
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
							try {
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
							}catch(NumberFormatException nfe) {
								log.error("Cannot parse "+url+ " , row: "+tr,nfe);
							}
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
			log.error("Unexpected IO error MalformedURLException while getting list of companies and sectors "+url, e);
		} catch (IOException e1) {
			log.error("Unexpected IO error IOException while getting list of companies and sectors "+url, e1);
		} catch (Throwable t) {
			log.error("Unexpected error while getting list of companies and sectors "+url, t);
		}
		return urls;
	}

	private boolean checkExpectedHeader(int i, String hd,String sectorOrDescription) {
		boolean foundTable;
		// first column label is different between the sectors page (Sector) and industries page (Description) 
		String expected = i==0 ? sectorOrDescription : headersExpected.get(i);
		foundTable = hd.equals(expected);
//		log.debug("found the right table:"+foundTable+", found: "+hd+", expected:"+expected);
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
			try {
				ret = Double.parseDouble(t);
				ret *= multiplier;
			} catch (Exception e) {
				log.error("cannot parseDouble " + t, e);
			}
		}
		return ret;
	}

}
