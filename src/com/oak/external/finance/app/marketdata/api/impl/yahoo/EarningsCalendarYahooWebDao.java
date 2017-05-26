package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
	// private final String url2 =
	// "http://finance.yahoo.com/calendar/earnings?from=2017-03-19&to=2017-03-25&day=2017-03-20";
	private final WebParsingUtils webParsingUtils;
	// private final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
	private final CompletionService<List<EarningsCalendar>> executor;

	public EarningsCalendarYahooWebDao(Logger log, String url, WebParsingUtils webParsingUtils,
			CompletionService<List<EarningsCalendar>> executor) {
		this.log = log;
		this.url = url;
		this.webParsingUtils = webParsingUtils;
		this.executor = executor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.oak.external.finance.app.marketdata.api.impl.yahoo.
	 * EarningsCalendarDao#getEarningsCalendarByDate(java.util.Date)
	 */
	@Override
	public List<EarningsCalendar> getEarningsCalendarByDate(Date lastLoadedEarningsCalendarDate) {
		LocalDate lastLoadedEarningsCalendarLocalDate = LocalDateTime
				.ofInstant(lastLoadedEarningsCalendarDate.toInstant(), ZoneOffset.UTC).toLocalDate();
		String link = url + "?day=" + lastLoadedEarningsCalendarLocalDate;
		List<EarningsCalendar> ret = parseYahoo(link, lastLoadedEarningsCalendarDate);
		return ret;
	}

	private List<EarningsCalendar> parseYahoo(String link, Date lastLoadedEarningsCalendarDate) {

		List<EarningsCalendar> ret = new ArrayList<EarningsCalendar>();

		Document document;
		try {
			document = downladPage(link);
			ret = newParser(lastLoadedEarningsCalendarDate, document);

		} catch (MalformedURLException e) {
			log.error("Unexpected IO error while getting list of earnings calendars " + link, e);
		} catch (IOException e1) {
			if (e1 instanceof HttpStatusException) {
				HttpStatusException se = (HttpStatusException) e1;
				if (se.getStatusCode() == 404) {
					throw new RuntimeException(se);
				}
			}
			log.error("Unexpected IO error while getting list of earnings calendars " + link, e1);
		} catch (Throwable t) {
			log.error("Unexpected error while getting list of earnings calendars " + link, t);
		}

		return ret;
	}

	private Document downladPage(String link) throws IOException {
		log.info("Downloading earning data from: "+link);
		return Jsoup.connect(link).header("Accept-Encoding", "gzip, deflate")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0").maxBodySize(0)
				.timeout(600000).get();
	}

	private List<EarningsCalendar> newParser(Date lastLoadedEarningsCalendarDate, Document document)
			throws ParseException, IOException {
		List<EarningsCalendar> ret = new ArrayList<>();
		// parse the date first:
		Elements dateElements = document.getElementsContainingText("Events Calendar for");
		LocalDate sd = null;
		LocalDate ed = null;

		for (int i = dateElements.size() - 1; i >= 0; i--) {
			Element e = dateElements.get(i);
			Elements spans = e.getElementsByTag("span");
			if (spans.size() > 5) {
				Element start = spans.get(4);
				Element end = spans.get(5);

				String startText = start.text();
				String endText = end.text();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);
				sd = LocalDate.parse(startText, formatter);
				ed = LocalDate.parse(endText, formatter);
				break;
			}
		}
		LocalDate lastLoadedEarningsCalendarLocalDate = LocalDateTime
				.ofInstant(lastLoadedEarningsCalendarDate.toInstant(), ZoneOffset.UTC).toLocalDate();
		// if(lastLoadedEarningsCalendarLocalDate.isBefore(sd)) {
		List<Future<List<EarningsCalendar>>> tasks = new ArrayList<>();
		final LocalDate startDate = sd;
		final LocalDate endDate = ed;
		for (LocalDate date = sd; date.isBefore(ed); date = date.plusDays(1)) {
			final LocalDate d = date;
			String link = computeLinkFromDates(startDate, endDate, d);
			Future<List<EarningsCalendar>> future = executor.submit(() -> {
				Document dom = downladPage(link);

				Elements tablesForDate = dom.select("table");
				int rounds = parsePageAndComputeRounds(dom);
				for (int i = 1; i <= rounds; i++) {
					String batchLink = computeLinkFromOffsets(link, i);
					final int ii = i;
					Future<List<EarningsCalendar>> bachfuture = executor.submit(() -> {
						Document batchDom = downladPage(batchLink);
						Elements tableForBatch = batchDom.select("table");
						List<EarningsCalendar> parseForDate = parseForDate(tableForBatch, d);
						if (LocalDate.of(2017, 3, 21).compareTo(d) == 0) {
							System.out.println(batchLink + " " + d + " " + rounds + " " + ii);
							System.out.println(d);
						}
						return parseForDate;
					});
					tasks.add(bachfuture);
				}

				List<EarningsCalendar> parseForDate = parseForDate(tablesForDate, d);
				return parseForDate;
			});
			tasks.add(future);
		}
		int finished = 0;
		while (finished < tasks.size()) {
			try {
				Future<List<EarningsCalendar>> take = executor.take();
				finished++;
				List<EarningsCalendar> parseResultForDate;
				parseResultForDate = take.get();
				ret.addAll(parseResultForDate);
			} catch (InterruptedException e) {
				log.error("earning calendar collection interrupted", e);
				e.printStackTrace();
			} catch (ExecutionException e) {
				log.error("earning calendar collection encountered error", e);
				e.printStackTrace();
			}
		}
		// }

		return ret;
	}

	private int parsePageAndComputeRounds(Document dom) {
		String[] numbers = dom.getElementsContainingText("Earnings Announcement for").last().parent().children().get(1)
				.child(0).text().split(" of ");// 1-100 of 446 results --> [
												// "1-100", "446 results" ]
		String nRangeStr = numbers[1];// 446 results
		String[] startEndStr = nRangeStr.split(" "); // [ "446", "results" ]
		String endStr = startEndStr[0];// "446"
		int expectedNum = Integer.parseInt(endStr);// 446
		int rounds = expectedNum / 100;
		return rounds;
	}

	/**
	 * &offset=100&size=100
	 * 
	 * @param initial
	 *            looks like:
	 *            http://finance.yahoo.com/calendar/earnings?from=2017-03-26&to=2017-04-01&day=2017-03-27
	 * @param offset
	 *            is a multiplier of 100 (starting with 0)
	 * @return if offset is 0:
	 *         http://finance.yahoo.com/calendar/earnings?from=2017-03-26&to=2017-04-01&day=2017-03-27&offset=0&size=100
	 */
	private String computeLinkFromOffsets(String initial, int offset) {
		String ret = initial;
		ret = ret + "&offset=" + offset * 100 + "&size=100";
		return ret;
	}

	private String computeLinkFromDates(LocalDate sd, LocalDate ed, LocalDate d) {
		String ret = url;
		ret = ret + "?from=" + sd + "&to=" + ed + "&day=" + d;
		return ret;
	}

	private List<EarningsCalendar> parseForDate(Elements tables, LocalDate date) {
		List<EarningsCalendar> ret = new ArrayList<>();
		Date d = java.sql.Date.valueOf(date);
		Date now = new Date();
		Element dataTbl = tables.get(1);

		Elements tRows = dataTbl.select("tr");
		for (Element r : tRows) {
			try {
				Elements ds = r.select("td");
				if (!ds.isEmpty()) {
					EarningsCalendar e = new EarningsCalendar();
					may2017Parse(e, ds);
					e.setAsOfCalendar(d);
					e.setAnnouncementDate(now);
					ret.add(e);
				}
			} catch (Throwable t) {
				log.error("can't parse", t);
			}
		}
		return ret;
	}

	private void may2017Parse(EarningsCalendar e, Elements t) {
		String ticker = t.select(".data-col0").text();
		String companyName = t.select(".data-col1").text();
		String announcementTimeStr = t.select(".data-col2").text();
		AnnouncementTime announcementTime =AnnouncementTime.fromText(announcementTimeStr);
		e.setCompanyName(companyName);
		e.setTicker(ticker);
		e.setTime(announcementTime);
	}
	private void preMay2017Parse(EarningsCalendar e, Elements ds) {
		Element idLink = ds.get(0).select("a").get(0);
		String symbol = idLink.attr("data-symbol");
		String companyName = idLink.attr("title");

//					Double epsEstimate = webParsingUtils.parseDouble(ds.get(2).text());
//					Double epsAnnounced = webParsingUtils.parseDouble(ds.get(3).text());
//					Double surprisePercent = webParsingUtils.parseDouble(ds.get(4).text());
		AnnouncementTime announcementTime = AnnouncementTime.fromText(ds.get(2).text());

		e.setCompanyName(companyName);
		e.setTicker(symbol);
		e.setTime(announcementTime);
	}

	private List<EarningsCalendar> oldParser(Date announcementDate, Document document) throws ParseException {
		List<EarningsCalendar> ret = new ArrayList<>();
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
					if (d.size() > 3) {
						e.setCompanyName(d.get(0).text());
						e.setTicker(d.get(1).text());
						e.setAsOfCalendar(dt);
						e.setAnnouncementDate(announcementDate);
						e.setTime(AnnouncementTime.fromText(d.get(2).text()));
						if (!"".equals(e.getTicker())) {
							ret.add(e);
						}
					}
				} catch (Throwable t) {
					log.error("error occured parsing [" + r.text() + "] filling for: " + e, t);
				}
			}
			i++;
		}
		return ret;
	}

}
