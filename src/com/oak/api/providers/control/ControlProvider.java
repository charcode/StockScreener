package com.oak.api.providers.control;

import com.oak.api.finance.model.dto.Control;

public interface ControlProvider {

	void save(Control c);
	Control getLatestControlByType(ControlType type);
}
