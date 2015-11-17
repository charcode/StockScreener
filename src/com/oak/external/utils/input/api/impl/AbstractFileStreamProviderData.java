package com.oak.external.utils.input.api.impl;

import org.apache.logging.log4j.Logger;

import com.oak.external.utils.input.api.StreamProvider;

public abstract class AbstractFileStreamProviderData implements StreamProvider{
	protected final Logger logger;

	public AbstractFileStreamProviderData(Logger logger) {
		this.logger = logger;
	}
}