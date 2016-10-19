package com.oak.api.finance.model;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import com.oak.api.finance.model.dto.Company;
import com.oak.api.finance.model.dto.Sector;

public class SectorsIndustriesCompanies {
	private Map<Sector,Set<Sector>>industriesPerSector;
	private Map<Sector,Set<Company>>companiesPerIndustry;
	public SectorsIndustriesCompanies() {
		this.industriesPerSector = new ConcurrentSkipListMap<>();
		this.companiesPerIndustry = new ConcurrentSkipListMap<>();
	}
	
	public void addIndustriesToSector(Sector sector,Set<Sector> industries) {
		industries.stream()
		.filter(i -> update(i,sector))
		.collect(Collectors.toSet()) ;
		addToList(industriesPerSector,sector,industries);
	}
	private boolean update(Sector i, Sector sector) {
		i.setParentSectorId(sector.getId());
		i.setParentSectorDescription(sector.getDescription());
		return true;
	}
	
	public void addCompanyToIndustry(Sector industry, Company company) {
		if(!companiesPerIndustry.containsKey(industry)) {
			companiesPerIndustry.put(industry, new ConcurrentSkipListSet<>());
		}
		company.setIndustryId(industry.getId());
		company.setIndustryDescription(industry.getDescription());
		company.setSectorId(industry.getParentSectorId());
		company.setSectorDescription(industry.getParentSectorDescription());
		companiesPerIndustry.get(industry).add(company);
	}
	public void addCompaniesToIndustry(Sector industry, Set<Company> company) {
		addToList(companiesPerIndustry,industry,company);
	}

	public Set<Sector> sectors(){
		return industriesPerSector.keySet();
	}
	
	public Set<Sector> industries(Sector sector){
		Set<Sector> industries = industriesPerSector.get(sector);
		return industries;
	}
	public Set<Sector> industries(){
		Set<Sector> ret = industriesPerSector.values().stream()
		.flatMap(Set::stream)
		.collect(Collectors.toSet());
		return ret;
	}
	
	public Set<Company>companies(){
		Set<Company> ret = companiesPerIndustry.values().stream()
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
		return ret;
	}
	
	private  <T> void addToList(Map<Sector,Set<T>>map,Sector key,Set<T> elements){
		if(!map.containsKey(key)) {
			map.put(key, new ConcurrentSkipListSet<>(elements));
		}else {
			map.get(key).addAll(elements);
		}
	}
}
