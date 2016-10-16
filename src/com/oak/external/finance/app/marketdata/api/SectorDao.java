package com.oak.external.finance.app.marketdata.api;

import java.util.Collection;

import com.oak.api.finance.model.dto.Sector;

public interface SectorDao {
	Collection<Sector>getSectors();
}
