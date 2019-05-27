import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConsole.Logger;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class htmlUnitTester {
	
    public static void main(String[] args) throws IOException {
    	
    	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    	
    	
    	final WebClient webClient = new WebClient();
    	HtmlPage page3;
    	page3 = webClient.getPage("https://melodize.github.io/");
    	HtmlButton b = (HtmlButton) page3.getElementById("workerBtn");
    	HtmlPage page2;
    	page2 = b.click();
    	//System.out.print(page2.getTitleText());
    	
    	
    	/*final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    	final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
    	System.out.println("HtmlUnit - Welcome to HtmlUnit".contentEquals(page.getTitleText()));*/

    }
}
