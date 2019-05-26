import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConsole.Logger;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

// TODO: create new file/class for each function to organize the whole project
public class Team6 {
	
    public static void main(String[] args) throws IOException {
    	
    	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    	
        // Reference for events: https://www.w3schools.com/tags/ref_eventattributes.asp
    	
    	
        final Collection<String> EVENTS = Arrays.asList(
                "onchange","onfocus","onselect","onsubmit",
                "onclick", "onmouseover", "ondbclick", "onwheel",
                "onkeydown", "onkeypress", "onkeyup",
                "ondrag","ondragend","ondrop","onscroll","ondragstart",
                "onload","alert");

        //Load document from file (change this to your own path)
        File input = new File("C:/Users/aerol/OneDrive/Desktop/CS453_Automated_Testing_FSM/src/main/resources/test1.html");
        Document docFile = Jsoup.parse(input, "UTF-8", "http://example.com/");

        // 1. Find all attributes and tags AND events found
        // (we need just attribute for events, but record tags in case we need)
        List<String> tags = new ArrayList<String>(); //record tags
        List<String> attrs = new ArrayList<String>(); //record attributes
        List<String> attrEvents = new ArrayList<String>(); //record events found

        for(Element e : docFile.getAllElements()){      // all elements in html

            tags.add(e.tagName().toLowerCase());    // add each tag in tags List
            //System.out.println("Tag: "+ e.tag()+" attributes = "+e.attributes());  // attributes with values in string
            //System.out.println("Tag: "+ e.tag()+" attributes = "+e.attributes().asList()); //attributes in List<Attribute>

            for(Attribute att : e.attributes().asList()){ // for each tag get all attributes in one List<Attribute>
                String attrKey = att.getKey();
                String attrVal = att.getValue();
                System.out.println("Key: " + attrKey + " , Value: "+ attrVal);
                attrs.add(attrKey.toLowerCase());
                if (EVENTS.contains(attrKey.toLowerCase())) {
                    System.out.println("We found Events!! : " + attrKey);
                    attrEvents.add(attrKey.toLowerCase());
                }
            }
        }

        System.out.println("*****************");
        System.out.println("All Tags = " + tags);
        System.out.println("All Attributes = "+ attrs);
        System.out.println("All Events Found = "+ attrEvents);
        System.out.println("Distinct Tags = "+ new HashSet<String>(tags));
        System.out.println("Distinct Attributes = "+ new HashSet<String>(attrs));
        System.out.println("-------------------------------");

        
        //  TODO: Perform the event with Value such as
        //        onclick = "alert("HEY")"
        //        We also need value, alert, to do next event (use attrVal line 44)
        

        // Get all buttons element (this works fine, just another way to get attribute)
        Elements buttons = docFile.getElementsByTag("button");
        for (Element button : buttons) {
            String funcName = button.attr("onclick");
            String buttonText = button.text();
            System.out.println("Test onclick in Button");
            System.out.println(funcName);
            System.out.println(buttonText);
        }
        System.out.println("-------------------------------");


        // 2. Load document from URL (not from file)
        Document doc = Jsoup.connect("http://example.com/").get();

        String title = doc.title();
        System.out.println(title);
        String text = doc.body().text();
        System.out.println(text);
        Element link = doc.select("a").first();
        String linkOuterH = link.outerHtml();
        System.out.println(linkOuterH);
        String linkHref = link.attr("href");
        // TODO: connecting again takes some time, do we have any other way?
        Document doc2 = Jsoup.connect(linkHref).get();
        System.out.println(doc2.title());
        
        
        /*
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    	HtmlPage page3;
    	page3 = webClient.getPage("http://example.com/");
    	HtmlForm loginForm = page3.getFormByName("Sign In");
    	HtmlTextInput username = loginForm.getInputByName("NameofUsernameElement");
    	HtmlPasswordInput pass = loginForm.getInputByName("NameofPassowordElement");
    	HtmlSubmitInput b = loginForm.getInputByValue("LoginButtonValue");

    	username.setValueAttribute("Actualy Username");
    	pass.setValueAttribute("Actual Password");
    	HtmlPage page2;
    	page2 = b.click();
    	*/
    	
    	
    	final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    	final HtmlPage page = webClient.getPage("http://example.com/");
    	System.out.println(title.contentEquals(page.getTitleText()));
    	
        
    }
}
