package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oak.api.finance.model.dto.AnnouncementTime;
import com.oak.api.finance.model.dto.EarningsCalendar;
import com.oak.external.finance.app.marketdata.api.EarningsCalendarDao;
import com.oak.external.utils.web.WebParsingUtils;

public class EarningsCalendarYahooWebDao implements EarningsCalendarDao {
	private final Logger log;
	private final String url; // = "https://biz.yahoo.com/research/earncal/";
	private final WebParsingUtils webParsingUtils;
	private final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");

	public EarningsCalendarYahooWebDao(Logger log, String url, WebParsingUtils webParsingUtils) {
		this.log = log;
		 this.url = url;
		this.webParsingUtils = webParsingUtils;
	}

	/* (non-Javadoc)
	 * @see com.oak.external.finance.app.marketdata.api.impl.yahoo.EarningsCalendarDao#getEarningsCalendarByDate(java.util.Date)
	 */
	@Override
	public List<EarningsCalendar> getEarningsCalendarByDate(Date date) {
		String dateStr = sdf.format(date);
		String link = url + dateStr + ".html";
		List<EarningsCalendar> ret = parseYahoo(link,date);
		return ret;
	}

	private List<EarningsCalendar> parseYahoo(String link,Date announcementDate) {

		List<EarningsCalendar> ret = new ArrayList<EarningsCalendar>();

		Document document;
		try {
			document = Jsoup.connect(link).header("Accept-Encoding", "gzip, deflate")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
			Elements tables = document.select("table");

			Element dateTbl = tables.get(2);
			Elements ds = dateTbl.select("td");
			int i = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			Date dt = null;
			for (Element d : ds) {
				if (i == 2) {
					String text = d.text().substring(6);
					dt = sdf.parse(text);
					break;
				}
				i++;
			}
			i = 0;
			Element table = tables.get(5);
			Element t2 = table.select("table").get(1);
			Elements rows = t2.select("tr");
			for (Element r : rows) {
				if (i >= 2) {
					EarningsCalendar e = new EarningsCalendar();
					try {
						Elements d = r.select("td");
						if(d.size()>3) {
							e.setCompanyName(d.get(0).text());
							e.setTicker(d.get(1).text());
							e.setAsOfCalendar(dt);
							e.setAnnouncementDate(announcementDate);
							e.setTime(AnnouncementTime.fromText(d.get(2).text()));
							if(!"".equals(e.getTicker())){
								ret.add(e);
							}
						}
					} catch (Throwable t) {
						log.error("error occured parsing [" + r.text() + "] filling for: " + e, t);
					}
				}
				i++;
			}

		} catch (MalformedURLException e) {
			log.error("Unexpected IO error while getting list of earnings calendars ", e);
		} catch (IOException e1) {
			if(e1 instanceof HttpStatusException) {
				HttpStatusException se = (HttpStatusException)e1;
				if(se.getStatusCode() ==  404) {
					throw new RuntimeException(se);
				}
			}
			log.error("Unexpected IO error while getting list of earnings calendars ", e1);
		} catch (Throwable t) {
			log.error("Unexpected error while getting list of earnings calendars ", t);
		}

		return ret;
	}

}
