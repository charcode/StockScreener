package com.oak.external.utils.input.api.impl;

import java.io.InputStream;

import org.apache.logging.log4j.Logger;

public class ResourceFileStreamProvider extends AbstractFileStreamProviderData {

	public ResourceFileStreamProvider(Logger logger) {
		super(logger);
	}

	@Override
	public InputStream getStream(String filename) {
		InputStream stream = this.getClass().getResourceAsStream(filename);
		return stream;
	}

}
