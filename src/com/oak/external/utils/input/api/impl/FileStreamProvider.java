package com.oak.external.utils.input.api.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.logging.log4j.Logger;

public class FileStreamProvider extends AbstractFileStreamProviderData {

		public FileStreamProvider(String filename, Logger logger) {
			super(logger);
		}

	@Override
	public InputStream getStream(String filename) {
		InputStream filestream = null;;
		try {
			filestream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			logger.error("Error reading file "+filename,e);;
		}
		
		return filestream;
	}
}
