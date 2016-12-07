package com.oak.finance.app.monitor;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.Economic;
import com.oak.api.finance.model.Stock;
import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.EconomicDto;
import com.oak.api.finance.repository.CompanyRepository;
import com.oak.api.finance.repository.EconomicRepository;

public class MarketDataPersistenceControllerImpl implements MarketDataPersistenceController{

	private final Logger logger;
	private final EconomicRepository economicRepository;
	private final CompanyRepository companyRepository;
	
	public MarketDataPersistenceControllerImpl(Logger logger, EconomicRepository economicRepository, CompanyRepository companyRepository) {
		this.logger = logger;
		this.economicRepository = economicRepository;
		this.companyRepository = companyRepository;
	}

	@Override
	public void persist(Map<Stock, Map<Date, Economic>> marketData) {
		Set<EconomicDto> economics = marketData.entrySet().stream()
			.map(e -> convertToSet(e.getKey(),e.getValue()))
			.flatMap(e -> e.stream())
			.collect(Collectors.toSet());
		
		// we don't want to save the economy data that's already there.. 
		Set<EconomicDto> economyToSave = economics.stream()
				.filter(e -> economicRepository.findByPriceDateAndTicker(e.getPriceDate(), e.getTicker()).isEmpty())
				.collect(Collectors.toSet());
		Set<EconomicDto> economyExisting = economics.stream()
				.filter(e -> !economicRepository.findByPriceDateAndTicker(e.getPriceDate(), e.getTicker()).isEmpty())
				.collect(Collectors.toSet());
		
		if(!economyExisting.isEmpty()) {
			logger.debug("Skipping already saved economics "+economyExisting.size());
		}
		if(!economyToSave.isEmpty()) {
			logger.debug("Saving economics: "+economyToSave.size());
			Set<String> tickers = economyToSave.stream().map(c -> c.getTicker()).collect(Collectors.toSet());
			Set<Company> companies = companyRepository.findByTickerIn(tickers);
			Map<String,Long>idsByTicker = companies.stream().collect(Collectors.toMap(Company::getTicker, Company::getId));
			for (EconomicDto e : economyToSave) {
				Long id = idsByTicker.get(e.getTicker());
				e.setCompanyId(id);
			}
			try {
				economicRepository.save(economyToSave);
			}catch(Throwable t) {
				logger.error("error occured saving economy, "+t.getMessage(),t);
			}
		}
	}

	private Set<EconomicDto> convertToSet(Stock key, Map<Date, Economic> values) {
		Set<EconomicDto> econs = 
		values.entrySet().stream().map(
				e -> convertToEconomy(key,e.getKey(),e.getValue())
				).collect(Collectors.toSet());
		
		return econs;
	}

	private EconomicDto convertToEconomy(Stock stock, Date date, Economic econ) {
		EconomicDto ret = new EconomicDto();
		ret.setTicker(stock.getSymbol());
		ret.setPriceDate(date);
		ret.setBid(econ.getBid());
		ret.setAsk(econ.getAsk());
		ret.setEps(econ.getEps());
		ret.setDayHigh(econ.getDayHigh());
		ret.setDayLow(econ.getDayLow());
		ret.setLastTradeDateStr(econ.getLastTradeDateStr());
		ret.setLastTradeSize(econ.getLastTradeSize());
		ret.setLastTradeTimeStr(econ.getLastTradeTimeStr());
		ret.setOpen(econ.getOpen());
		ret.setPreviousClose(econ.getPreviousClose());
		ret.setPriceAvg200(econ.getPriceAvg200());
		ret.setPriceAvg50(econ.getPriceAvg50());
		ret.setTimeZone(econ.getTimeZone());
		ret.setVolume(econ.getVolume());
		ret.setYearHigh(econ.getYearHigh());
		ret.setYearLow(econ.getYearLow());
		ret.setBookValuePerShare(econ.getBookValuePerShare());
		ret.setEbitda(econ.getEbitda());
		ret.setEpsEstimateCurrentYear(econ.getEpsEstimateCurrentYear());
		ret.setEpsEstimateNextQuarter(econ.getEpsEstimateNextQuarter());
		ret.setEpsEstimateNextYear(econ.getEpsEstimateNextYear());
		ret.setMarketCap(econ.getMarketCap());
		ret.setOneYearTargetPrice(econ.getOneYearTargetPrice());
		ret.setPe(econ.getPe());
		ret.setPeg(econ.getPeg());
		ret.setPriceBook(econ.getPriceBook());
		ret.setPriceSales(econ.getPriceSales());
		ret.setRevenue(econ.getRevenue());
		ret.setRoe(econ.getRoe());
		ret.setSharesFloat(econ.getSharesFloat());
		ret.setSharesOutstanding(econ.getSharesOutstanding());
		ret.setSharesOwned(econ.getSharesOwned());
		ret.setAnnualDividendYield(econ.getAnnualDividendYield());
		ret.setAnnualDividendYieldPercent(econ.getAnnualDividendYieldPercent());
		ret.setDividendExDate(econ.getDividendExDate());
		ret.setDividendPayDate(econ.getDividendPayDate());
		return ret;
	}
	
}
