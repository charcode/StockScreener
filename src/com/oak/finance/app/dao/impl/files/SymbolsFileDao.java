package com.oak.finance.app.dao.impl.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.external.utils.input.api.StreamProvider;
import com.oak.finance.app.dao.SymbolsDao;

public class SymbolsFileDao implements SymbolsDao {

    private final String stocksFileName;
    private final String interestingCompaniesSymbolsFileName;
    private final String stocksWithNoPriceFileName;
    private final String goodValueStocksFileName;
    private final Logger logger;
    private final StreamProvider streamProvider;

    public SymbolsFileDao(String stocksFilename,
	    String stocksWithNoPriceFileName, String goodValueStocksFileName,
	    String interestingCompaniesSymbolsFileName, StreamProvider streamProvider, Logger logger) {
	this.stocksWithNoPriceFileName = stocksWithNoPriceFileName;
	this.stocksFileName = stocksFilename;
	this.goodValueStocksFileName = goodValueStocksFileName;
	this.interestingCompaniesSymbolsFileName = interestingCompaniesSymbolsFileName;
	this.streamProvider = streamProvider;
	this.logger = logger;
    }

    @Override
    public Set<String> getSavedSymbolsWithoutPrices() {
	return readLines(stocksWithNoPriceFileName, "#");
    }
    
    @Override
    public Set<String> getInterestingSymbols() {
	return readLines(interestingCompaniesSymbolsFileName, "#");
    }

    /**
     * lines starting with # are not read
     * @param filename
     * @param commentMarker TODO
     * @return
     */
    private Set<String> readLines(String filename, String commentMarker) {
	Set<String> symbols = new HashSet<String>();
	    Set<String> ret = new HashSet<String>();
	    // This will reference one line at a time
	    String line = null;

	    InputStream inputStream = null;
	    Scanner sc = null;
	    try {
		logger.debug("reading symbols lists from file: "
			+ filename);
		inputStream = streamProvider
			.getStream(filename);
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
	    logger.debug("finished reading  " + symbols.size()
		    + " symbols from: "
		    + filename);
	
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
	logger.debug("finished reading  " + symbols.size() + " symbols from: "
		+ stocksFileName);
	return symbols;
    }

    @Override
    public void saveSymbolsWithoutPrice(
	    Map<String, Date> existingStocksWithoutPrices) {
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
		String line = new StringBuilder(s).append(",") .append(dateString).toString();
		bufferedWriter.write(line);
		bufferedWriter.newLine();
	    }

	    // Always close files.
	    bufferedWriter.close();
	} catch (IOException ex) {
	    logger.error("Error writing to file '" + stocksWithNoPriceFileName
		    + "'", ex);
	}
    }
    @Override
    public void saveGoodValueStock(Stock stock, Map<Date, Economic> economics) {
	try {
	    // Assume default encoding.
	    FileWriter fileWriter = new FileWriter(goodValueStocksFileName,true);
	    
	    // Always wrap FileWriter in BufferedWriter.
	    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	    // Note that write() does not automatically
	    // append a newline character.
	    DateFormat df = new SimpleDateFormat();
	    for(Date date: economics.keySet()){
		Economic e = economics.get(date);
		StringBuilder lineBuilder = new StringBuilder(removeCommas(stock.getSymbol())).append(",") 
			.append(removeCommas(stock.getName())).append(",")
			.append(removeCommas(stock.getStockExchange())).append(",")
			.append(removeCommas(stock.getCurrency())).append(",")
			.append(df.format(date)).append(",")
			.append(e.getBid()).append(",")
			.append(e.getBookValuePerShare()).append(",")
			.append(e.getPerCalculated()).append(",")
			.append(e.getEps()).append(",")
			.append(e.getPe()).append(",")
			.append(e.getPeg()).append(",")
			.append(e.getEpsEstimateCurrentYear()).append(",")
			.append(e.getEpsEstimateNextQuarter()).append(",")
			.append(e.getEpsEstimateNextYear()).append(",")
			.append(e.getMarketCap()).append(",")
			.append(e.getAnnualDividendYieldPercent()).append(",")
			.append(e.getOneYearTargetPrice()).append(",")
			
			;
		
		String line = lineBuilder.toString();
		bufferedWriter.write(line);
		bufferedWriter.newLine();
	    }
	    
	    // Always close files.
	    bufferedWriter.close();
	} catch (IOException ex) {
	    logger.error("Error writing to file '" + goodValueStocksFileName
		    + "'", ex);
	}
    }
    private String removeCommas(String s){
	String ret = s;
	if(s!=null && s.indexOf(',') > 0){
	    ret = s.replace(',', '-');
	}
	return ret;
    }
}
