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
import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.FinancialAnalysis;
import com.oak.api.finance.model.FinancialAnalysis.Acceptance;
import com.oak.external.finance.app.marketdata.api.BalanceSheetDao;
import com.oak.external.finance.app.marketdata.api.FinancialDataDao;
import com.oak.api.finance.model.FinancialComment;
import com.oak.api.finance.model.FinancialComment.CommentType;
import com.oak.api.finance.model.FinancialData;
import com.oak.api.finance.model.Stock;

public class FinanceFundamentalAnalysisControllerImpl implements
		FinanceAnalysisController {
	private final Logger log;
	private final FinancialDataDao financialDataDao;
	private final double targetMinCurrentRatio;
	private final double targetMinQuickRatio;
	private final double targetMinAssetToDebtRatio;

	public FinanceFundamentalAnalysisControllerImpl(
			FinancialDataDao financialDataDao,
			double targetMinCurrentRatio, double targetMinQuickRatio,
			double targetMinAssetToDebtRatio, Logger log) {
		this.financialDataDao = financialDataDao;
		this.log = log;
		this.targetMinCurrentRatio = targetMinCurrentRatio;
		this.targetMinQuickRatio = targetMinQuickRatio;
		this.targetMinAssetToDebtRatio = targetMinAssetToDebtRatio;
	}

	@Override
	public void onEconomicsUpdate(FinanceAnalysisCallback callback,
			Stock stock, Map<Date, Economic> economics, Set<String> alwaysWatch) {

		for (Date d : economics.keySet()) {
			Economic e = economics.get(d);
			Double eps = e.getEps();
			Double ask = e.getAsk();
			Double bid = e.getBid();
			Double per = 0d;
			per = eps > 0 ? safeDivide(ask, eps) : 0;

			if (bid == null || ask == null) {
				log.debug("No price for: " + stock);
				callback.onPriceMissing(stock, economics);
			} else {
				FinancialAnalysis stockAnalysis = getStockAnalysis(e, stock);
				if (stockAnalysis.getAcceptance().equals(Acceptance.STRONG_BUY)) {
					
					SortedMap<Date, Double> assetToDebtRatios = stockAnalysis.getAssetToDebtRatiosAnnual();
					if(!assetToDebtRatios.isEmpty()) {
						Date latestDate = assetToDebtRatios.lastKey();
						Double assetToDebt = assetToDebtRatios.get(latestDate);
						log.debug("Stock to watch, Strong Buy!! " + stock.getSymbol()
								+ ", per = " + per + ", Debt ratio on "
								+ latestDate + ": " + assetToDebt);

						callback.onBuy(stock, economics, stockAnalysis);
					}else {
						log.debug("Stock is strong buy " + stock.getSymbol()
								+ ", per = " + per + ", but no debt ratios available");
					}
				}else if (stockAnalysis.getAcceptance().equals(Acceptance.BUY)) {
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
		boolean isAttractiveRatios = false;
		if (isGoodCurrency) {
			boolean isPegAttractive = false;
			boolean isEstimateEpsAttractive = false;
			boolean isBookToValueAttractive = false;
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
			double epsPercentCurrentYear = safeDivide(epsEstimateCurrentYear,
					ask);
			double epsPercentNextQuarter = safeDivide(epsEstimateNextQuarter,
					ask);
			double epsPercentNextYear = safeDivide(epsEstimateNextYear, ask);
			Double bookValuePerShare = e.getBookValuePerShare();
			double bookValueMultiple = safeDivide(ask, bookValuePerShare);

			isEstimateEpsAttractive = checkEps(comments,
					isEstimateEpsAttractive, epsPercentCurrentYear,
					epsPercentNextQuarter, epsPercentNextYear);
			isBookToValueAttractive = checkPe(comments,
					isBookToValueAttractive, pe, per, bookValuePerShare,
					bookValueMultiple);
			isPegAttractive = checkPeg(comments, isPegAttractive, peg);

			isAttractiveRatios = isPeAttractive && isPegAttractive
					&& isEstimateEpsAttractive && isBookToValueAttractive;
		} else {
			String msg = "Currency is not interesting: " + stock.getCurrency()
					+ ", " + stock.getName() + ", " + stock.getDescription();
			comments.add(new FinancialComment(msg,
					FinancialComment.CommentType.Currency));
		}

		ret = advancedAnalysis(stock, e, comments, isAttractiveRatios);
		return ret;
	}

	private FinancialAnalysis advancedAnalysis(Stock stock, Economic e,
			List<FinancialComment> comments, boolean isAttractiveRatios) {
		FinancialAnalysis ret;

		if (isAttractiveRatios) {
			log.debug("getting balance sheet for "+stock.getSymbol());
			String exchange = stock.getStockExchange();
//			SortedMap<Date, BalanceSheet> annualBalanceSheetForSymbol = balanceSheetDao
//					.getBalanceSheetForSymbol(stock.getSymbol(), exchange, true);
//			SortedMap<Date, BalanceSheet> quarterlyBalanceSheetForSymbol = balanceSheetDao
//					.getBalanceSheetForSymbol(stock.getSymbol(), exchange, false);

			String symbol = stock.getSymbol();
			FinancialData financialData = financialDataDao.getFinancialDataForSymbol(symbol, exchange);
			if(financialData != null) {
			SortedMap<Date, BalanceSheet> annualBalanceSheet = financialData.getAnnualBalanceSheet();
			SortedMap<Date, BalanceSheet> quarterlyBalanceSheet = financialData.getQuarterlyBalanceSheet();
			SortedMap<Date, Double> currentRatiosQutr = new TreeMap<Date, Double>();
			SortedMap<Date, Double> quickRatiosQutr = new TreeMap<Date, Double>();
			SortedMap<Date, Double> assetToDebtRatiosQutr = new TreeMap<Date, Double>();

			calculateRatiosFromBalanceSheets(quarterlyBalanceSheet,
					currentRatiosQutr, quickRatiosQutr, assetToDebtRatiosQutr);

			SortedMap<Date, Double> currentRatiosAnnual = new TreeMap<Date, Double>();
			SortedMap<Date, Double> quickRatiosAnnual = new TreeMap<Date, Double>();
			SortedMap<Date, Double> assetToDebtRatiosAnnual = new TreeMap<Date, Double>();
			calculateRatiosFromBalanceSheets(annualBalanceSheet,
					currentRatiosAnnual, quickRatiosAnnual,
					assetToDebtRatiosAnnual);
			Acceptance acceptanceResult = analyseRatios(currentRatiosQutr,
					quickRatiosQutr, assetToDebtRatiosQutr,
					currentRatiosAnnual, quickRatiosAnnual,
					assetToDebtRatiosAnnual, stock);
			ret = new FinancialAnalysis(stock, e, comments, acceptanceResult,
					currentRatiosQutr, quickRatiosQutr, assetToDebtRatiosQutr,
					currentRatiosAnnual, quickRatiosAnnual,
					assetToDebtRatiosAnnual);
			}else {
				comments.add(new FinancialComment("No financial data available", CommentType.MissingData));
				ret = new FinancialAnalysis(stock, e, comments, Acceptance.HOLD,
						null, null, null, null, null, null);
			}
		} else {
			ret = new FinancialAnalysis(stock, e, comments, Acceptance.HOLD,
					null, null, null, null, null, null);
		}

		return ret;
	}

	private class Results{
		final boolean good;
		final boolean excellent;
		final boolean onlyLast;
		public Results(boolean good, boolean excellent, boolean onlyLast) {
			super();
			this.good = good;
			this.excellent = excellent;
			this.onlyLast = onlyLast;
		}
	}
	
	private Results computeResult(SortedMap<Date, Double> dataPoints, double min) {
		boolean all = true;
		double average = 0;
		int count = 0;
		double last = 0;
		for (Date d : dataPoints.keySet()) {
			count++;
			double r = dataPoints.get(d);
			if (r < min) {
				all = false;
			}
			average += r;
			last = r;
		}
		average /= count;
		boolean good= average> min && last  > min;
		boolean onlyLast = last>min;
		Results	res = new Results(good,all,onlyLast);
		return res;
	}

	private Acceptance analyseRatios(SortedMap<Date, Double> currentRatiosQutr,
			SortedMap<Date, Double> quickRatiosQutr,
			SortedMap<Date, Double> assetToDebtRatiosQutrData,
			SortedMap<Date, Double> currentRatiosAnnual,
			SortedMap<Date, Double> quickRatiosAnnual,
			SortedMap<Date, Double> assetToDebtRatiosAnnualData, Stock stock) {
	
		// targetMinCurrentRatio = 2.0;
		Results currentRatiosQutrRes = computeResult(currentRatiosQutr,targetMinCurrentRatio);	
		Results currentRatiosAnnualRes = computeResult(currentRatiosAnnual,targetMinCurrentRatio);
		// quickCash > 1.0 is good
		Results quickRatiosQutrRes = computeResult(quickRatiosQutr,targetMinQuickRatio);
		Results quickRatiosAnnualRes = computeResult(quickRatiosAnnual,targetMinQuickRatio);
		// assetToDebt > 2.9 is good
		Results assetToDebtRatiosQutr = computeResult(assetToDebtRatiosQutrData, targetMinAssetToDebtRatio);
		Results assetToDebtRatiosAnnual = computeResult(assetToDebtRatiosAnnualData, targetMinAssetToDebtRatio);
		
		Acceptance ret;
		if (currentRatiosAnnualRes.excellent && currentRatiosQutrRes.excellent
				&& quickRatiosAnnualRes.excellent && quickRatiosQutrRes.excellent
				&& assetToDebtRatiosQutr.excellent
				&& assetToDebtRatiosAnnual.excellent) {
			ret = Acceptance.STRONG_BUY;
		} else if (currentRatiosQutrRes.good && quickRatiosAnnualRes.good
				&& quickRatiosQutrRes.good && currentRatiosAnnualRes.good
				&& assetToDebtRatiosQutr.good && assetToDebtRatiosAnnual.good) {
			ret = Acceptance.BUY;
		} else if(                                  
			currentRatiosQutrRes.good  && quickRatiosAnnualRes.onlyLast  &&    
			quickRatiosQutrRes.good    && currentRatiosAnnualRes.onlyLast  &&    
			assetToDebtRatiosQutr.good && assetToDebtRatiosAnnual.onlyLast	){
			ret = Acceptance.BUY;
		} else {
			ret = Acceptance.HOLD;
		}
		return ret;
	}

	
	private void calculateRatiosFromBalanceSheets(
			SortedMap<Date, BalanceSheet> quarterlyBalanceSheetForSymbol,
			SortedMap<Date, Double> currentRatios,
			SortedMap<Date, Double> quickRatios,
			SortedMap<Date, Double> assetToDebtRatios) {
		for (Date d : quarterlyBalanceSheetForSymbol.keySet()) {
			BalanceSheet balanceSheet = quarterlyBalanceSheetForSymbol.get(d);
			double totalCurrentAssets = balanceSheet
					.getTotalCurrentAssetsCalculated();
			double totalCurrentLiabilities = balanceSheet
					.getTotalCurrentLiabilitiesCalculated();

			if (totalCurrentAssets != 0 && totalCurrentLiabilities != 0) {
				// double debtToAssets = totalCurrentLiabilities /
				// totalCurrentAssets;
				double currentRatio = totalCurrentAssets
						/ totalCurrentLiabilities;
				currentRatios.put(d, currentRatio);// currentRatio > 2.0 is good
			}
			double cashAndEquivalent = balanceSheet.getCashAndEquivalent();
			double netReceivables = balanceSheet.getNetReceivables();
			double quickCash = cashAndEquivalent + netReceivables;

			// quickCash > 1.0 is good
			double quickRatio = quickCash / totalCurrentLiabilities;
			quickRatios.put(d, quickCash);

			double assetToDebtRatio = safeDivide(balanceSheet.getTotalAssets(),
					balanceSheet.getTotalLiabilities());
			quickRatios.put(d, quickRatio);
			assetToDebtRatios.put(d, assetToDebtRatio);
		}
	}

	private boolean peCheck(List<FinancialComment> comments, Double pe,
			Double per) {
		boolean isPeAttractive = false;
		if (pe != null && pe > 0 && pe < 15) {
			String msg = "pe=" + pe + ", pe calculated by yahoo";
			comments.add(new FinancialComment(msg,
					FinancialComment.CommentType.PastEarnings));
			isPeAttractive = true;
		} else if (per != null && per > 0 && per < 15) {
			String msg = "per=" + per
					+ ", per calculated from eps and ask price";
			comments.add(new FinancialComment(msg,
					FinancialComment.CommentType.PastEarnings));
			isPeAttractive = true;
		}
		return isPeAttractive;
	}

	private boolean checkPeg(List<FinancialComment> comments,
			boolean isPegAttractive, Double peg) {
		if (peg != null && peg < 1) {
			String msg = "Peg = " + peg;
			comments.add(new FinancialComment(msg,
					FinancialComment.CommentType.Peg));
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
				&& bookValueMultiple * pe < 27) {
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

		if (epsPercentCurrentYear > 0.05 && epsPercentNextQuarter > 0.024
				&& epsPercentNextYear > 0.01) {
			String msg = "earnings to price, current year="
					+ epsPercentCurrentYear
					+ ", per calculated from epsEstimateCurrentYear and ask price";
			comments.add(new FinancialComment(msg,
					FinancialComment.CommentType.FutureEarnings));
			String msg1 = "earnings to price, next quarter="
					+ epsPercentNextQuarter
					+ ", per calculated from epsEstimateNextQuarter and ask price";
			comments.add(new FinancialComment(msg1,
					FinancialComment.CommentType.FutureEarnings));
			String msg2 = "earnings to price, next year="
					+ epsPercentNextQuarter
					+ ", per calculated from epsEstimateNextQuarter and ask price";
			comments.add(new FinancialComment(msg2,
					FinancialComment.CommentType.FutureEarnings));
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
