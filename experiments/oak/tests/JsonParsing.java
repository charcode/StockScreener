package oak.tests;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oak.external.finance.app.marketdata.api.impl.yahoo.YahooFinancialJsonDataModel;

public class JsonParsing {
	private Logger log = LogManager.getFormatterLogger(JsonParsing.class);
	private ObjectMapper mapper = new ObjectMapper();
	
	private String json;
	
	
	@Test
	public void TestJsonParsing() {
		json = value;
		try {
			YahooVal financial = mapper.readValue(json, YahooVal.class);
			System.out.println(financial);
		} catch (JsonParseException e) {
			log.error("",e);
		} catch (JsonMappingException e) {
			log.error("",e);
		} catch (IOException e) {
			log.error("",e);
		}
		
	}
	
	static class YahooVal{
		Long raw;
		String fmt;
		public YahooVal() {}
		public Long getRaw() {
			return raw;
		}
		public void setRaw(Long raw) {
			this.raw = raw;
		}
		public String getFmt() {
			return fmt;
		}
		public void setFmt(String fmt) {
			this.fmt = fmt;
		}
		
	}
	String value = " {\r\n" + 
			"\"raw\": 1435622400,\r\n" + 
			"\"fmt\": \"2015-06-30\"\r\n" + 
			"}";
	
	
}
