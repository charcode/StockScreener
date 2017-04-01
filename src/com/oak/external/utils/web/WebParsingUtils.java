package com.oak.external.utils.web;

public class WebParsingUtils {
	private static String spaceBreak = String.valueOf((char) 160);
	
	
	public WebParsingUtils(){
		
	}
	public String advanceTrim(String text) {
			String replace = text.replace(spaceBreak, " ");
			replace = replace.replace(",", "");
			String ret = replace.trim();
			return ret;
	}
	
	public Double parseDouble(String t) {
		Double ret;
		t = advanceTrim(t).toLowerCase();
		if(t == null || t.equals("null") || "-".equals(t)) {
			ret = null;
		}else {			
			ret = Double.parseDouble(t);
		}
		return ret; 
	}

}
