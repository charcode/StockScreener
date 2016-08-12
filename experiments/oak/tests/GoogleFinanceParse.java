package oak.tests;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class GoogleFinanceParse {

	@Test
	public void testParseGoogle() {
		String link = "https://www.google.com/finance?q=NYSE%3ALCI&fstype=ii";
		String datesText = "In Millions of USD (except for per share items)";
		Document d;
		try (WebClient wc = new WebClient()){
			HtmlPage page = wc.getPage(link);
			List<DomElement> balanceSheet = page.getElementsByIdAndOrName("Balance Sheet");
			HtmlElement b = page.getBody();
			
			d = Jsoup.connect(link).get();
			Element body = d.body();
			Elements datesRefElts = body.getElementsMatchingOwnText(datesText);
			Element elementById = body.getElementById("fs-table");
			
			if (datesRefElts.size() > 0) {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
