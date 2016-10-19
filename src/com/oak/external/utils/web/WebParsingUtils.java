package com.oak.external.utils.web;

public class WebParsingUtils {
	private static String spaceBreak = String.valueOf((char) 160);
	
	public String advanceTrim(String text) {
			String replace = text.replace(spaceBreak, " ");
			replace = replace.replace(",", "");
			String ret = replace.trim();
			return ret;
	}

}
