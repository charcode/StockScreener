package com.oak.external.finance.app.marketdata.api.impl.yahoo;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class AbstractYahooFinanceWebParser<T> {
	private static String datesText = "Period Ending";
	private static String spaceBreak = String.valueOf((char) 160);

	protected final Set<String> values;
	protected final Logger log;
	private final SimpleDateFormat f;
	private final String dateTag;

	public AbstractYahooFinanceWebParser(Logger log, String dateTag) {
		super();
		this.values = new HashSet<String>();
		this.log = log;
		this.dateTag = dateTag;
		f = new SimpleDateFormat("MMM dd, yyyy");
		initializeInterestValues(this.values);
	}

	protected abstract void initializeInterestValues(Set<String> values);

	protected abstract String getRootWeb();

	protected abstract T buildSingleStatementFromDoubleMap(String symbol, Map<String, List<Double>> valueMap, int i);

	private String createLink(String symbol, boolean annual) {
		String link = getRootWeb() + symbol + (annual ? "&annual" : "");
		return link;
	}

	protected Map<String, List<Double>> parseDoubleValues(SortedSet<Date> dates, String link) {
		dates.clear();
		Map<String, List<Double>> valueMap = new HashMap<String, List<Double>>();
		for (String v : values) {
			valueMap.put(v, new ArrayList<Double>(4));
		}
		try (final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11)) {
			HtmlPage page = webClient.getPage(link);
			HtmlElement body = page.getBody();
			page.getElementByName(datesText);
		} catch (IOException e) {
			log.error("can't read page: " + link, e);
		}
		try {
			oldExtractor(dates, link, valueMap);
		} catch (SocketTimeoutException e) {
			log.error("time out while reading page: " + link);
		} catch (IOException e) {
			log.error("can't read page: " + link, e);
		} catch (Exception e) {
			log.error("other error can't read page: " + link, e);
		}
		return valueMap;
	}

	private void oldExtractor(SortedSet<Date> dates, String link, Map<String, List<Double>> valueMap)
			throws IOException {
		Document d = Jsoup.connect(link).get();
		Element body = d.body();
		Elements datesRefElts = body.getElementsMatchingOwnText(datesText);
		if (datesRefElts.size() > 0) {
			Element dateRootElt = datesRefElts.get(0).parent().parent().parent();
			Elements datesTag = dateRootElt.getElementsByTag(getDateTag());

			for (Element dateTag : datesTag) {
				String dateText = dateTag.text();
				if (!"Period Ending".equals(dateText)) {
					try {
						dates.add(f.parse(dateText));
					} catch (ParseException e) {
						log.error("can't get date from: " + dateText, e);
						e.printStackTrace();
					}
				}
			}
			Elements dataElts = dateRootElt.siblingElements();
			for (Element e : dataElts) {

				String value = e.text();
				String key = whatValue(value, e);
				if (key.length() > 0) {
					exctractDoubleValues(key, value, valueMap);
				}
			}
		}
	}

	protected String getDateTag() {
		return dateTag;
	}

	private void exctractDoubleValues(String key, String value, Map<String, List<Double>> valueMap) {
		String[] vals = value.split(key);
		if (vals.length > 1) {
			String[] valuess = vals[1].split(" ");
			int i = 0;
			for (String v : valuess) {
				if (v.length() > 0 && !spaceBreak.equals(v)) {
					try {
						double d = parseToDouble(v);
						i++;
						valueMap.get(key).add(d);
					} catch (Exception e) {
						log.error("cannot parse [" + v + "] from " + value, e);
					}
				}
			}
		}
	}

	private double parseToDouble(String v) {
		double parsed = 0;
		String trimmed = advancedTrim(v);
		if (!trimmed.equals("-")) {
			int multiplier = 1;
			if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
				multiplier = -1;
				trimmed = trimmed.substring(1, trimmed.lastIndexOf(")"));
			}
			parsed = multiplier * Double.parseDouble(trimmed);
		}
		return parsed;
	}

	private String whatValue(String value, Element e) {
		String v = "";
		if (!"".equals(value)) {
			if (e.childNodeSize() > 0) {
				Element child = e.child(0);
				v = child.text();
				if (e.childNodeSize() > 1 && ("".equals(v) || !values.contains(v))) {
					child = e.child(1);
					v = child.text();
				}
			}
			if ("".equals(v) || !values.contains(v)) {
				v = "";
				if (values.contains(value)) {
					v = value;
				} else {
					for (String val : values) {
						if (value.startsWith(val)) {
							v = val;
						}
					}
				}
			}
		}

		return v;
	}

	private String advancedTrim(String v) {
		String replace = v.replace(spaceBreak, " ");
		replace = replace.replace(",", "");
		String ret = replace.trim();
		return ret;
	}

	protected SortedMap<Date, T> getStatementsForSymbol(String symbol, boolean annual) {
		SortedSet<Date> dates = new TreeSet<Date>();
		SortedMap<Date, T> incomeStatementsOnPage = parseStatementsPage(dates, symbol, true);
		return incomeStatementsOnPage;
	}

	private SortedMap<Date, T> parseStatementsPage(SortedSet<Date> dates, String symbol, boolean annual) {
		String link = createLink(symbol, annual);
		Map<String, List<Double>> valueMap = parseDoubleValues(dates, link);
		SortedMap<Date, T> ret = createStatementsFromDoubeMap(symbol, dates, valueMap);
		return ret;
	}

	private SortedMap<Date, T> createStatementsFromDoubeMap(String symbol, SortedSet<Date> dates,
			Map<String, List<Double>> valueMap) {
		SortedMap<Date, T> ret = null;
		if (dates != null && values != null) {
			ret = new TreeMap<Date, T>();
			int i = 1;
			int total = dates.size();
			for (Date date : dates) {
				T s = buildSingleStatementFromDoubleMap(symbol, valueMap, total - i);
				i++;
				ret.put(date, s);
			}

		}
		return ret;
	}

}