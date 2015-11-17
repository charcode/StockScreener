package com.oak.finance.app.monitor.analysis;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.mockito.internal.util.collections.Sets;

import com.oak.api.finance.model.BalanceSheet;
import com.oak.api.finance.model.FinancialAnalysis;
import com.oak.api.finance.model.FinancialAnalysis.Acceptance;
import com.oak.api.finance.model.FinancialComment;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.economy.Economic;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.BalanceSheetDao;

public class FinanceFundamentalAnalysisControllerImpl implements
	FinanceAnalysisController {
    private final Logger log;
    private final BalanceSheetDao balanceSheetDao;

    public FinanceFundamentalAnalysisControllerImpl(
	    BalanceSheetDao balanceSheetDao, Logger log) {
	this.balanceSheetDao = balanceSheetDao;
	this.log = log;
    }

    @Override
    public void onEconomicsUpdate(FinanceAnalysisCallback callback,
	    Stock stock, Map<Date, Economic> economics, Set<String> alwaysWatch) {

	for (Date d : economics.keySet()) {
	    Economic e = economics.get(d);
	    Double eps = e.getEps();
	    Double pe = e.getPe();
	    Double peg = e.getPeg();
	    Double ask = e.getAsk();
	    Double bid = e.getBid();
	    Double per = 0d;
	    per = eps > 0 ? safeDivide(ask, eps) : 0;

	    if (bid == null || ask == null) {
		log.debug("No price for: " + stock);
		callback.onPriceMissing(stock, economics);
	    } else {
		FinancialAnalysis stockAnalysis = getStockAnalysis(e, stock);
		if (stockAnalysis.getAcceptance().equals(Acceptance.BUY)) {
		    log.debug("Stock to watch " + stock + ", per = " + per);

		    callback.onBuy(stock, economics, stockAnalysis);
		} else if (per != null && per > 60) {
		    e.setPerCalculated(per);
		    callback.onSell(stock, economics);
		} else if (alwaysWatch.contains(stock.getSymbol())) {
		    callback.onWatchList(stock, economics, stockAnalysis);
		}
	    }
	    // log.debug("finished processing "+symbol+", bid: "+bid+", eps:"+eps+", per:"+per);
	}

    }

    private FinancialAnalysis getStockAnalysis(Economic e, Stock stock) {
	// log.debug("analysing "+stock);
	FinancialAnalysis ret = null;
	List<FinancialComment> comments = new LinkedList<FinancialComment>();
	Set<String> currencies = Sets.newSet("USD", "EUR", "GBp");
	boolean isGoodCurrency = currencies.contains(stock.getCurrency());
	boolean isBuy = false;
	if (isGoodCurrency) {
	    boolean isPegAttractive = false;
	    boolean isEstimateEpsAttractive = false;
	    boolean isBookToValueAttractive = false;
	    boolean isBalanceSheetAttractive = false;
	    Double pe = e.getPe();
	    Double eps = e.getEps();
	    Double ask = e.getAsk();
	    Double peg = e.getPeg();
	    Double per = 0d;
	    per = eps > 0 ? safeDivide(ask, eps) : 0;
	    boolean isPeAttractive = peCheck(comments, pe, per);
	    Double epsEstimateCurrentYear = e.getEpsEstimateCurrentYear();
	    Double epsEstimateNextQuarter = e.getEpsEstimateNextQuarter();
	    Double epsEstimateNextYear = e.getEpsEstimateNextYear();
	    double epsPercentCurrentYear = safeDivide(epsEstimateCurrentYear, ask);
	    double epsPercentNextQuarter = safeDivide(epsEstimateNextQuarter, ask);
	    double epsPercentNextYear = safeDivide(epsEstimateNextYear, ask);
	    Double bookValuePerShare = e.getBookValuePerShare();
	    double bookValueMultiple = safeDivide(ask, bookValuePerShare);

	    isEstimateEpsAttractive = checkEps(comments, isEstimateEpsAttractive, epsPercentCurrentYear, epsPercentNextQuarter, epsPercentNextYear);
	    isBookToValueAttractive = checkPe(comments, isBookToValueAttractive, pe, per, bookValuePerShare, bookValueMultiple);
	    isPegAttractive = checkPeg(comments, isPegAttractive, peg);
	    
	    SortedMap<Date, BalanceSheet> annualBalanceSheetForSymbol = balanceSheetDao.getBalanceSheetForSymbol(stock.getSymbol(), true);
	    SortedMap<Date, BalanceSheet> quarterlyBalanceSheetForSymbol = balanceSheetDao.getBalanceSheetForSymbol(stock.getSymbol(), false);
	    isBalanceSheetAttractive = checkBalanceSheet(annualBalanceSheetForSymbol,quarterlyBalanceSheetForSymbol);
	    isBuy = isPeAttractive && isPegAttractive && isEstimateEpsAttractive && isBookToValueAttractive;
	} else {
	    String msg = "Currency is not interesting: " + stock.getCurrency()
		    + ", " + stock.getName() + ", " + stock.getDescription();
	    comments.add(new FinancialComment(msg, FinancialComment.CommentType.Currency));
	}
	ret = isBuy ? new FinancialAnalysis(stock, e, comments, Acceptance.BUY)
		: new FinancialAnalysis(stock, e, comments, Acceptance.HOLD);
	return ret;
    }
    
    private boolean checkBalanceSheet( SortedMap<Date, BalanceSheet> annualBalanceSheetForSymbol,SortedMap<Date, BalanceSheet> quarterlyBalanceSheetForSymbol) {
	boolean isBalanceSheetAttractive = false;

	SortedMap<Date,Double>currentRatios = new TreeMap<Date,Double>();
	SortedMap<Date,Double>quickRatios = new TreeMap<Date,Double>();
	
	for (Date d : quarterlyBalanceSheetForSymbol.keySet()) {
	    BalanceSheet balanceSheet = quarterlyBalanceSheetForSymbol.get(d);
	    double totalCurrentAssets = balanceSheet .getTotalCurrentAssetsCalculated();
	    double totalCurrentLiabilities = balanceSheet .getTotalCurrentLiabilitiesCalculated();

	    if (totalCurrentAssets != 0 && totalCurrentLiabilities !=0) {
//		double debtToAssets = totalCurrentLiabilities / totalCurrentAssets;
		double currentRatio = totalCurrentAssets/totalCurrentLiabilities;
		currentRatios.put(d, currentRatio);// currentRatio > 2.0 is good
		double cashAndEquivalent = balanceSheet.getCashAndEquivalent();
		double netReceivables = balanceSheet.getNetReceivables();
		double quickCash = cashAndEquivalent + netReceivables;
		double quickRatio = quickCash / totalCurrentLiabilities;// currentRatio > 1.0 is good
	    }
	}
	
	return isBalanceSheetAttractive;
    }


    private boolean peCheck(List<FinancialComment> comments,
	    Double pe, Double per) {
	boolean isPeAttractive = false;
	if (pe != null && pe > 0 && pe < 15) {
	String msg = "pe=" + pe + ", pe calculated by yahoo";
	comments.add(new FinancialComment(msg, FinancialComment.CommentType.PastEarnings));
	isPeAttractive = true;
	} else if (per != null && per > 0 && per < 15) {
	String msg = "per=" + per
		+ ", per calculated from eps and ask price";
	comments.add(new FinancialComment(msg, FinancialComment.CommentType.PastEarnings));
	isPeAttractive = true;
	}
	return isPeAttractive;
    }

    private boolean checkPeg(List<FinancialComment> comments,
	    boolean isPegAttractive, Double peg) {
	if (peg != null && peg < 1) {
	    String msg = "Peg = " + peg;
	    comments.add(new FinancialComment(msg, FinancialComment.CommentType.Peg));
	    isPegAttractive = true;
	}
	return isPegAttractive;
    }

    private boolean checkPe(List<FinancialComment> comments,
	    boolean isBookToValueAttractive, Double pe, Double per,
	    Double bookValuePerShare, double bookValueMultiple) {
	if (pe > 0 && bookValueMultiple > 0 && bookValueMultiple * pe < 25) {
	String msg = "book value/share= " + bookValuePerShare
		+ ", gives a multiple of = " + bookValueMultiple
		+ ", pe*bv=" + bookValueMultiple * pe;
	comments.add(new FinancialComment(msg,
		FinancialComment.CommentType.BookValue));
	isBookToValueAttractive = true;
	} else if (per > 0 && bookValueMultiple > 0
	    && bookValueMultiple * pe < 25) {
	String msg = "book value/share= " + bookValuePerShare
		+ ", gives a multiple of = " + bookValueMultiple
		+ ", per*bv=" + bookValueMultiple * pe;
	comments.add(new FinancialComment(msg,
		FinancialComment.CommentType.BookValue));
	isBookToValueAttractive = true;
	}
	return isBookToValueAttractive;
    }

    private boolean checkEps(List<FinancialComment> comments,
	    boolean isEstimateEpsAttractive, double epsPercentCurrentYear,
	    double epsPercentNextQuarter, double epsPercentNextYear) {
	
	if (epsPercentCurrentYear > 0.05 && epsPercentNextQuarter > 0.025
	    && epsPercentNextYear > 0.01) {
	String msg = "earnings to price, current year="
		+ epsPercentCurrentYear
		+ ", per calculated from epsEstimateCurrentYear and ask price";
	comments.add(new FinancialComment(msg, FinancialComment.CommentType.FutureEarnings));
	String msg1 = "earnings to price, next quarter="
		+ epsPercentNextQuarter
		+ ", per calculated from epsEstimateNextQuarter and ask price";
	comments.add(new FinancialComment(msg1, FinancialComment.CommentType.FutureEarnings));
	String msg2 = "earnings to price, next year="
		+ epsPercentNextQuarter
		+ ", per calculated from epsEstimateNextQuarter and ask price";
	comments.add(new FinancialComment(msg2, FinancialComment.CommentType.FutureEarnings));
	isEstimateEpsAttractive = true;
	}
	return isEstimateEpsAttractive;
    }

    private double safeDouble(Double b) {
	double ret;
	if (b == null) {
	    ret = 0;
	} else {
	    ret = b;
	}
	return ret;
    }

    private double safeDivide(Double a, Double b) {
	Double ret = null;
	if (a != null && b != null && b.compareTo(0.0) != 0) {
	    ret = a / b;
	}
	return safeDouble(ret);
    }

}
