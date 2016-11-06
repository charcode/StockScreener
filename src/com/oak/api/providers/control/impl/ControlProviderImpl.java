package com.oak.api.providers.control.impl;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.Status;
import com.oak.api.finance.repository.ControlRepository;
import com.oak.api.providers.control.ControlProvider;
import com.oak.api.providers.control.ControlType;

public class ControlProviderImpl implements ControlProvider{

	private final ControlRepository rep;
	private final Logger log;
	public ControlProviderImpl(ControlRepository controlRepository, Logger log){
		this.rep = controlRepository;
		this.log = log;
	}

	@Override
	public Control getLatestControlByType(ControlType type) {
		Collection<Control> controls = rep.findByTypeAndStatusIn(type,Status.successStatuses());
		Optional<Control> control = controls.stream().collect(Collectors.maxBy(Control::compareTo));
		Control ret;
		if(control.isPresent()) {
			ret = control.get();
		}else {
			ret = Control.nonExistant();
			log.warn("no control found with type "+type+", returning: "+ret);
		}
		return ret;
	}
	
	@Override
	public void save(Control c) {
		rep.save(c);
	}
}
