package com.oak.finance.app.dao.impl.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Screen0Result;
import com.oak.external.utils.input.api.StreamProvider;
import com.oak.external.utils.web.WebParsingUtils;
import com.oak.finance.app.dao.SymbolsDao;

public class SymbolsFileDao implements SymbolsDao {

	private final String stocksFileName;
	private final String interestingCompaniesSymbolsFileName;
	private final String stocksWithNoPriceFileName;
	private final String goodValueStocksFileName;
	private final Logger logger;
	private final StreamProvider streamProvider;
	private final WebParsingUtils webParsingUtils;

	public SymbolsFileDao(String stocksFilename, String stocksWithNoPriceFileName, String goodValueStocksFileName,
			String interestingCompaniesSymbolsFileName, StreamProvider streamProvider, WebParsingUtils webParsingUtils, Logger logger) {
		this.stocksWithNoPriceFileName = stocksWithNoPriceFileName;
		this.stocksFileName = stocksFilename;
		this.goodValueStocksFileName = goodValueStocksFileName;
		this.interestingCompaniesSymbolsFileName = interestingCompaniesSymbolsFileName;
		this.streamProvider = streamProvider;
		this.webParsingUtils = webParsingUtils;
		this.logger = logger;
	}

	@Override
	public Set<String> getSavedSymbolsWithoutPrices() {
		return readLinesAndReturnFirstColumn(stocksWithNoPriceFileName, "#");
	}

	@Override
	public Set<String> getInterestingSymbols() {
		return readLinesAndReturnFirstColumn(interestingCompaniesSymbolsFileName, "#");
	}

	@Override
	public Collection<Screen0Result> readPreviousResults() {
		List<String> lines = readLines(goodValueStocksFileName);
		List<Screen0Result> ret = lines.stream().filter(l -> !l.startsWith("symbol,company")).map(l -> lineToResult(l))
				.collect(Collectors.toList());

		return ret;
	}

	private Screen0Result lineToResult(String line) {
		Screen0Result ret = new Screen0Result();
		String[] t = line.split(",");
		try {
			ret.setTicker(t[0]);
			ret.setCompanyName(t[1]);
			ret.setExchangeCode(t[2]);
			ret.setCurrency(t[3]);
			ret.setRunDate(parseDate(t[4]));
			ret.setPriceBid(parseDouble(t[5]));
			ret.setBookValuePerShare(parseDouble(t[6]));
			ret.setPerCalculated(parseDouble(t[7]));
			ret.setEps(parseDouble(t[8]));
			ret.setPer(parseDouble(t[9]));
			ret.setEpsEstThisYear(parseDouble(t[10]));
			ret.setEpsEstNextQuarter(parseDouble(t[11]));
			ret.setEpsEstNextYear(parseDouble(t[12]));
			ret.setMarketCap(parseDouble(t[13]));
			ret.setDividendYield(parseDouble(t[14]));
			ret.setTargetPrice(parseDouble(t[15]));
		} catch (Exception e) {
			logger.error("error", e);
		}
		return ret;
	}
	
	private Double parseDouble(String s) {
		Double d = webParsingUtils.parseDouble(s);
		return d;
	}

	//03/06/2016 21:03
	private SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm"); 
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
	private java.sql.Date parseDate(String t) {
		java.sql.Date valueOf; 
	
			try {
				Date parse = f.parse(t);
				if(parse.getYear() < 1) {
					parse.setYear(2000+parse.getYear());
				}
				valueOf = new java.sql.Date(parse.getTime());
			} catch (Exception e1) {
				String message = "Cannot parse date from "+t;
				logger.error(message,e1);
				throw new RuntimeException(message,e1);
			}
		
		return valueOf;
	}

	private List<String> readLines(String filename) {
		List<String> lines = new ArrayList<>();
		InputStream inputStream = null;
		Scanner sc = null;
		String line = null;
		try {
			logger.debug("reading symbols lists from file: " + filename);
			inputStream = streamProvider.getStream(filename);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				String trimmed = line.trim();
				lines.add(line);

			}

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("error closing stream", e);
				}
			}
			if (sc != null) {
				sc.close();
			}
		}
		return lines;
	}

	/**
	 * lines starting with # are not read
	 * 
	 * @param filename
	 * @param commentMarker
	 *            TODO
	 * @return
	 */
	private Set<String> readLinesAndReturnFirstColumn(String filename, String commentMarker) {
		Set<String> symbols = new HashSet<String>();
		Set<String> ret = new HashSet<String>();
		// This will reference one line at a time
		String line = null;

		InputStream inputStream = null;
		Scanner sc = null;
		try {
			logger.debug("reading symbols lists from file: " + filename);
			inputStream = streamProvider.getStream(filename);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				String trimmed = line.trim();
				if (!trimmed.startsWith(commentMarker)) {
					String[] tockens = trimmed.split(",");
					ret.add(tockens[0]);
				}
			}

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("error closing stream", e);
				}
			}
			if (sc != null) {
				sc.close();
			}
		}
		symbols.addAll(ret);
		logger.debug("finished reading  " + symbols.size() + " symbols from: " + filename);

		return symbols;
	}

	@Override
	public Set<String> getSymbols() {
		Set<String> symbols = new TreeSet<String>();
		InputStream inputStream = null;
		Scanner sc = null;
		try {
			logger.debug("reading symbols lists from file: " + stocksFileName);
			inputStream = streamProvider.getStream(stocksFileName);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String beforeHash;
				String trim = line.trim();
				logger.debug("reading line: " + line);
				if (trim.contains("#")) {
					beforeHash = trim.split("#")[0].trim();
				} else {
					beforeHash = trim;
				}
				if (beforeHash.length() > 0) {
					String symbol = beforeHash.split(";")[0].trim();
					symbols.add(symbol);
				}
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				logger.error("error in scanner", sc.ioException());
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("error closing stream", e);
				}
			}
			if (sc != null) {
				sc.close();
			}
		}
		logger.debug("finished reading  " + symbols.size() + " symbols from: " + stocksFileName);
		return symbols;
	}

	@Override
	public void saveSymbolsWithoutPrice(Map<String, Date> existingStocksWithoutPrices) {
		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(stocksWithNoPriceFileName);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			DateFormat df = new SimpleDateFormat();
			for (String s : existingStocksWithoutPrices.keySet()) {
				Date date = existingStocksWithoutPrices.get(s);
				String dateString = df.format(date);
				String line = new StringBuilder(s).append(",").append(dateString).toString();
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}

			// Always close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			logger.error("Error writing to file '" + stocksWithNoPriceFileName + "'", ex);
		}
	}

	@Override
	public void saveGoodValueStock(Stock stock, Map<Date, Economic> economics) {
		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(goodValueStocksFileName, true);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			DateFormat df = new SimpleDateFormat();
			for (Date date : economics.keySet()) {
				Economic e = economics.get(date);
				StringBuilder lineBuilder = new StringBuilder(removeCommas(stock.getSymbol())).append(",")
						.append(removeCommas(stock.getName())).append(",")
						.append(removeCommas(stock.getStockExchange())).append(",")
						.append(removeCommas(stock.getCurrency())).append(",").append(df.format(date)).append(",")
						.append(e.getBid()).append(",").append(e.getBookValuePerShare()).append(",")
						.append(e.getPerCalculated()).append(",").append(e.getEps()).append(",").append(e.getPe())
						.append(",").append(e.getPeg()).append(",").append(e.getEpsEstimateCurrentYear()).append(",")
						.append(e.getEpsEstimateNextQuarter()).append(",").append(e.getEpsEstimateNextYear())
						.append(",").append(e.getMarketCap()).append(",").append(e.getAnnualDividendYieldPercent())
						.append(",").append(e.getOneYearTargetPrice()).append(",")

				;

				String line = lineBuilder.toString();
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}

			// Always close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			logger.error("Error writing to file '" + goodValueStocksFileName + "'", ex);
		}
	}

	private String removeCommas(String s) {
		String ret = s;
		if (s != null && s.indexOf(',') > 0) {
			ret = s.replace(',', '-');
		}
		return ret;
	}
}
