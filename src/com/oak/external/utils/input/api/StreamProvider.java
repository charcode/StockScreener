package com.oak.external.utils.input.api;

import java.io.InputStream;

public interface StreamProvider {
	InputStream getStream(String filename);
}
