package oak.tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitTest {
	private static Logger log = Logger.getLogger(HtmlUnitTest.class.getName());
	private static String path = "C:\\Users\\charb\\Documents\\tests\\google.txt";
	private static String pathAppend = "C:\\Users\\charb\\Documents\\tests\\google_append.txt";
	
	@BeforeClass 
	public  static void clearFiles() {
		truncateFile(path);
		truncateFile(pathAppend);
	}
	private static void truncateFile(String file) {
		try {
			Files.newBufferedWriter(Paths.get(file),StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			log.log(Level.SEVERE, "can't truncate "+file, e);
		}
	}
	@Test
	public void TestParseYahoo() {
		String link = "http://finance.yahoo.com/q/bs?s=AAPL&annual";
		String datesText = "Period Ending";
		try (final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER)) {
			HtmlPage page = webClient.getPage(link);
			page.getBody();
			page.getElementByName(datesText);
		} catch (IOException e) {
			fail("can't read page: " + link + " exception: " + e.getMessage());
		} catch (Throwable t) {
			log.log(Level.SEVERE, "Can't parse page", t);
		}
	}

	@Test
	public void TestParseGoogle() {
		String incomeStatement = "Income Statement";
		String link = "https://www.google.com/finance?q=NYSE%3ALCI&fstype=ii";
		String lnk = "https://www.google.com/finance?q=LCI";
		String linkToUse = link;
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			HtmlPage page = webClient.getPage(linkToUse);
			HtmlAnchor incomeStatementAnchor = page.getAnchorByText(incomeStatement);
			appendText(incomeStatementAnchor.toString());
			List<HtmlAnchor> anchors = page.getAnchors();
			Set<DomNode> allNodes = getAllDomNodes(page);
			Path file = Paths.get ( path );
			List<String>lines = new ArrayList<>(4000);
			for(DomNode n:allNodes) {
				String xPath = n.getCanonicalXPath();
				String text = n.getTextContent();
				String string = xPath + "  -->  "+text;
				appendText(string);
				lines.add(string);
			}
			Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);

			for (HtmlAnchor a : anchors) {
				Iterable<DomElement> childElements = a.getChildElements();
				if (childElements.iterator().hasNext()) {
					appendText("Dom Elements of: " + a);
					for (DomElement e : childElements) {
						appendText(e.toString());
					}
				}
				Iterable<DomNode> children = a.getChildren();
				if (children.iterator().hasNext()) {
					appendText("Dom Nodes of: " + a);
					for (DomNode e : children) {
						appendText(e.toString());
					}
				}
			}
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}

	private void appendText(String string) {
		try {
		    Files.write(Paths.get(pathAppend), string.getBytes(), 
		    		StandardOpenOption.CREATE );
		}catch (IOException e) {
			System.out.println(e.toString() + " " +e.getMessage());
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private Set<DomNode> getAllDomNodes(HtmlPage page) {
		Set<DomNode> ret = new HashSet<>();
		Iterable<DomNode> topLevelChildren = page.getBody().getChildren();
		for (DomNode n : topLevelChildren) {
			ret.addAll(recursiveGetChildren(n));
		}
		return ret;
	}

	private Collection<? extends DomNode> recursiveGetChildren(DomNode n) {
		Set<DomNode> ret = new HashSet<>();
		if (n != null) {
			ret.add(n);
			Iterable<DomNode> children = n.getChildren();
			for (DomNode c : children) {
				ret.addAll(recursiveGetChildren(c));
			}
		}
		return ret;
	}

}
