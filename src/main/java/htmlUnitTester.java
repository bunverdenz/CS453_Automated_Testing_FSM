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
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.java.swing.plaf.windows.resources.windows;

public class htmlUnitTester {
	
    public static void main(String[] args) throws IOException {
    	
    	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    	File file = new File("C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/index.html");
    	
    	WebClient webClient = new WebClient();
    	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
    	java.net.URL url = file.toURI().toURL();
    	HtmlPage page = webClient.getPage("https://melodize.github.io/");
    
    	Document doc = Jsoup.connect("https://melodize.github.io/").get();
    	for(Element e : doc.getAllElements()) {
    		if(e.hasClass("modal")) {
    			HtmlElement e1 = (HtmlElement) page.getElementById(e.id());
    			System.out.println(e1);
    			HtmlElement e3 = (HtmlElement) e1.getElementsByTagName("a").get(0);
    			System.out.println(e3);
    			HtmlButton b = (HtmlButton) page.getElementById("loginBtn");
    			b.click();
    			HtmlPage page3 = (HtmlPage) e3.click();
    			System.out.println(page3);
    		}
    	}
    }
    
    	
    	//System.out.print(page2.getTitleText());
    	
    	
    	/*final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    	final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
    	System.out.println("HtmlUnit - Welcome to HtmlUnit".contentEquals(page.getTitleText()));*/
}
    
