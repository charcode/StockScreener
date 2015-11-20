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
import com.oak.api.finance.model.FinancialComment;
import com.oak.api.finance.model.Stock;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.BalanceSheetDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.CashFlowStatementDao;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.IncomeStatementDao;

public class FinanceFundamentalAnalysisControllerImpl implements
		FinanceAnalysisController {
	private final Logger log;
	private final BalanceSheetDao balanceSheetDao;
	private final IncomeStatementDao incomeStatementDao;
	private final CashFlowStatementDao cashFlowStatementDao;
	private final double targetMinCurrentRatio;
	private final double targetMinQuickRatio;
	private final double targetMinAssetToDebtRatio;

	public FinanceFundamentalAnalysisControllerImpl(
			BalanceSheetDao balanceSheetDao,
			IncomeStatementDao incomeStatementDao,
			CashFlowStatementDao cashFlowStatementDao,
			double targetMinCurrentRatio, double targetMinQuickRatio,
			double targetMinAssetToDebtRatio, Logger log) {
		this.balanceSheetDao = balanceSheetDao;
		this.log = log;
		this.incomeStatementDao = incomeStatementDao;
		this.cashFlowStatementDao = cashFlowStatementDao;
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
				if (stockAnalysis.getAcceptance().equals(Acceptance.STRONG_BUY)) {
					
					SortedMap<Date, Double> assetToDebtRatios = stockAnalysis.getAssetToDebtRatiosAnnual();
					Date latestDate = assetToDebtRatios.lastKey();
					Double assetToDebt = assetToDebtRatios.get(latestDate);
					log.debug("Stock to watch, Strong buy!! " + stock + ", per = " + per+", Debt ratio on "+latestDate+": "+assetToDebt);
					
					callback.onBuy(stock, economics, stockAnalysis);
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
			SortedMap<Date, BalanceSheet> annualBalanceSheetForSymbol = balanceSheetDao
					.getBalanceSheetForSymbol(stock.getSymbol(), true);
			SortedMap<Date, BalanceSheet> quarterlyBalanceSheetForSymbol = balanceSheetDao
					.getBalanceSheetForSymbol(stock.getSymbol(), false);

			SortedMap<Date, Double> currentRatiosQutr = new TreeMap<Date, Double>();
			SortedMap<Date, Double> quickRatiosQutr = new TreeMap<Date, Double>();
			SortedMap<Date, Double> assetToDebtRatiosQutr = new TreeMap<Date, Double>();

			calculateRatiosFromBalanceSheets(quarterlyBalanceSheetForSymbol,
					currentRatiosQutr, quickRatiosQutr, assetToDebtRatiosQutr);

			SortedMap<Date, Double> currentRatiosAnnual = new TreeMap<Date, Double>();
			SortedMap<Date, Double> quickRatiosAnnual = new TreeMap<Date, Double>();
			SortedMap<Date, Double> assetToDebtRatiosAnnual = new TreeMap<Date, Double>();
			calculateRatiosFromBalanceSheets(annualBalanceSheetForSymbol,
					currentRatiosAnnual, quickRatiosAnnual,
					assetToDebtRatiosAnnual);
			Acceptance acceptanceResult = analyseRatios(currentRatiosQutr,
					quickRatiosQutr, assetToDebtRatiosQutr,
					currentRatiosAnnual, quickRatiosAnnual,
					assetToDebtRatiosAnnual);
			ret = new FinancialAnalysis(stock, e, comments, acceptanceResult,
					currentRatiosQutr, quickRatiosQutr, assetToDebtRatiosQutr,
					currentRatiosAnnual, quickRatiosAnnual,
					assetToDebtRatiosAnnual);
		} else {
			ret = new FinancialAnalysis(stock, e, comments, Acceptance.HOLD,
					null, null, null, null, null, null);
		}

		return ret;
	}

	private Acceptance analyseRatios(SortedMap<Date, Double> currentRatiosQutr,
			SortedMap<Date, Double> quickRatiosQutr,
			SortedMap<Date, Double> assetToDebtRatiosQutr,
			SortedMap<Date, Double> currentRatiosAnnual,
			SortedMap<Date, Double> quickRatiosAnnual,
			SortedMap<Date, Double> assetToDebtRatiosAnnual) {

		// targetMinCurrentRatio = 2.0;
		boolean all = true;
		double average = 0;
		int count = 0;
		double last = 0;
		for (Date d : currentRatiosQutr.keySet()) {
			count++;
			double r = currentRatiosQutr.get(d);
			if (r < targetMinCurrentRatio) {
				all = false;
			}
			average += r;
			last = r;
		}
		average /= count;
		boolean currentRatiosQutrGood = average > targetMinCurrentRatio;
		boolean currentRatiosQutrExcellent = all
				&& last > targetMinCurrentRatio;

		count = 0;
		average = 0;
		all = true;
		for (Date d : currentRatiosAnnual.keySet()) {
			count++;
			double r = currentRatiosAnnual.get(d);
			if (r < targetMinCurrentRatio) {
				all = false;
			}
			average += r;
			last = r;
		}
		average /= count;
		boolean currentRatiosAnnualGood = average > targetMinCurrentRatio;
		boolean currentRatiosAnnualExcellent = all
				&& last > targetMinCurrentRatio;

		// quickCash > 1.0 is good

		count = 0;
		average = 0;
		all = true;
		for (Date d : quickRatiosAnnual.keySet()) {
			count++;
			double r = quickRatiosAnnual.get(d);
			if (r < targetMinCurrentRatio) {
				all = false;
			}
			average += r;
			last = r;
		}
		average /= count;
		boolean quickRatiosAnnualGood = average > targetMinQuickRatio;
		boolean quickRatiosAnnualExcellent = all && last > targetMinQuickRatio;

		count = 0;
		average = 0;
		all = true;
		for (Date d : quickRatiosQutr.keySet()) {
			count++;
			double r = quickRatiosQutr.get(d);
			if (r < targetMinCurrentRatio) {
				all = false;
			}
			average += r;
			last = r;
		}
		average /= count;
		boolean quickRatiosQutrGood = average > targetMinCurrentRatio;
		boolean quickRatiosQutrExcellent = all && last > targetMinCurrentRatio;

		// assetToDebt > 2.9 is good

		count = 0;
		average = 0;
		all = true;
		for (Date d : assetToDebtRatiosAnnual.keySet()) {
			count++;
			double r = assetToDebtRatiosAnnual.get(d);
			if (r < targetMinCurrentRatio) {
				all = false;
			}
			average += r;
			last = r;
		}
		average /= count;
		boolean assetToDebtRatiosAnnualGood = average > targetMinAssetToDebtRatio
				&& last > targetMinAssetToDebtRatio;
		boolean assetToDebtRatiosAnnualxcellent = all
				&& last > targetMinAssetToDebtRatio;

		count = 0;
		average = 0;
		all = true;
		for (Date d : assetToDebtRatiosQutr.keySet()) {
			count++;
			double r = assetToDebtRatiosQutr.get(d);
			if (r < targetMinCurrentRatio) {
				all = false;
			}
			average += r;
			last = r;
		}
		average /= count;
		boolean assetToDebtRatiosQutrGood = average > targetMinAssetToDebtRatio
				&& last > targetMinAssetToDebtRatio;
		boolean assetToDebtRatiosQutrExcellent = all
				&& last > targetMinAssetToDebtRatio;

		Acceptance ret;
		if (currentRatiosAnnualExcellent && currentRatiosQutrExcellent
				&& quickRatiosAnnualExcellent && quickRatiosQutrExcellent
				&& assetToDebtRatiosQutrExcellent
				&& assetToDebtRatiosAnnualxcellent) {
			ret = Acceptance.STRONG_BUY;
		} else if (currentRatiosQutrGood && quickRatiosAnnualGood
				&& quickRatiosQutrGood && currentRatiosAnnualGood
				&& assetToDebtRatiosQutrGood && assetToDebtRatiosAnnualGood) {
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
