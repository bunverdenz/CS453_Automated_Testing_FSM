import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.logging.Level;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import java.io.File;
import java.io.IOException;
import java.util.*;

// TODO: create new file/class for each function to organize the whole project
public class Team6 {
	 static List<String> tags = new ArrayList<String>(); //record tags
     static List<String> attrs = new ArrayList<String>(); //record attributes
     static List<Element> elements = new ArrayList<Element>();
     static List<Node> fsm = new ArrayList<Node>();
     static String root = "https://melodize.github.io/";
     
     public static void addHTML(Element e1, Node n) throws IOException{
    	 if(n.old) {
      		return;
      	}
      	n.old = true;
      	int mod = 0;
      	final WebClient webClient = new WebClient(BrowserVersion.CHROME);
      	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        HtmlPage page;
        String url;
        if(n.getDoc() != null) {
        	url = ((Document) e1).location();
        	page = webClient.getPage(url);
        }else {
        	page = webClient.getPage(root);
        }
          for(Element e : e1.getAllElements()){      // all elements in html
          	boolean dont = false;
          	if(mod > 0) {
          		mod--;
         		continue;
          	}
              tags.add(e.tagName().toLowerCase());    // add each tag in tags List
              if(e.tagName().equals("button")) {
		          	
			          
			        HtmlButton htmlButton;
			        HtmlPage htmlPage;
		              
		          	htmlButton = (HtmlButton) page.getElementById(e.id());
		          	try {
		          		htmlPage = (HtmlPage) htmlButton.click(); 
		          		System.out.println(htmlPage);
		          	}catch(Exception x) {
		          		System.out.print(x);
		          		continue;
		          	}
		          	boolean add = true;
		              String url1 = htmlPage.getUrl().toString();
		              String[] temp = url1.split("/");
		              Document doc2 = Jsoup.connect(url1).get();
		              if(url1.contains("http")) {
		      			try{
		      				doc2 = Jsoup.connect(url1).get();
		      			}catch (Exception x){
		      				continue;
		      			}
		      		}else if(temp.length > 1) {
		      			try{
		      				doc2 = Jsoup.connect(root + temp[1]).get();
		      			}catch (Exception x){
		      				continue;
		      			}
		      		}else {
		      			try{
		      				doc2 = Jsoup.connect(root + url1).get();
		      			}catch (Exception x){
		      				continue;
		      			}
		      		}    
			       	Node out = new Node(doc2.title(), doc2);
			       	for(Node node : fsm) {
		     			if(node.getTitle().contentEquals(out.getTitle())) {
		     				out = node;
		     				add = false;
		     			}
		     		}
		      		for(Node e2 : n.out) {
		      			if(e2.getTitle().equals(out.getTitle())){
		      				dont = true;
		      				break;
		      			}
		      		}
		      		if(!dont) {
		      			System.out.println("adding now to " + n.getTitle());
		      			System.out.println(out.getTitle());
		      			Object[] edge = {htmlButton, "b"};
		      			n.addToEdge(edge);
		      			n.addToOut(out);
		      			if(add) {
		      				fsm.add(out);
		      			}
		      		}
		      		dont = false;
		      		continue;
          	}
              if(e.hasClass("modal") && !e.equals(e1)) {
              	boolean add = true;
              	Elements modale = e.getAllElements();
             	mod = modale.size();
              	String title = e.id();
              	Node out = new Node(title, e);
              	for(Node node : fsm) {
          			if(node.getTitle().contentEquals(out.getTitle())) {
          				out = node;
          				add = false;
          			}
          		}
              	for(Node neighbor : n.out) {
              		if(neighbor.getModal() != null) {
              			String ntitle = neighbor.getTitle();
              			if(ntitle.equals(title)) {
              				dont = true;
              				break;
              			}
              		}
              	}
              	if(!dont) {
              		System.out.println("adding now to " + n.getTitle());
              		System.out.println(out.getTitle());
              		n.addToOut(out);
              		n.addToEdge(null);
              		if(add) {
              			fsm.add(out);
              		}
              	}
              	dont = false;
              	continue;
              }
              for(Attribute att : e.attributes().asList()){
              	String attrKey = att.getKey();
                  String attrVal = att.getValue();
                  attrs.add(attrKey.toLowerCase());
                  if(attrKey.contentEquals("href")) {
                  	if(attrVal.contains("html")) {
                  		boolean add = true;
                  		String linkHref = e.attr("href");
                  		String[] temp = linkHref.split("/");
                  		Document doc2;
                  		if(linkHref.contains("http")) {
                  			try{
                  				doc2 = Jsoup.connect(linkHref).get();
                  			}catch (Exception x){
                  				continue;
                  			}
                  		}else if(temp.length > 1) {
                  			try{
                  				doc2 = Jsoup.connect(root + temp[1]).get();
                  			}catch (Exception x){
                  				continue;
                  			}
                  		}else {
                  			try{
                  				doc2 = Jsoup.connect(root + linkHref).get();
                  			}catch (Exception x){
                  				continue;
                  			}
                  		}        		       	
              		    Node out = new Node(doc2.title(), doc2);
              		    for(Node node : fsm) {
                  			if(node.getTitle().contentEquals(out.getTitle())) {
                  				out = node;
                  				add = false;
                  			}
                  		}
              		    for(Node e2 : n.out) {
                      		if(e2.getTitle().equals(out.getTitle())) {
  	            				dont = true;
  	            			}
  	            		}
                  		if(!dont) {
                  			n.addToOut(out);
                  			Object[] edge = {e, "e"};
                  			n.addToEdge(edge);
                  			System.out.println("adding now to " + n.getTitle());
                  			System.out.println(out.getTitle());
                  			if(add) {
                  				fsm.add(out);
                  			}
                  		}
                  		dont = false;
                  	}
                  }
                      
              }
          }
          for(Node e2 : n.out) {
         	if(e2.getModal() != null) {
         		Element m = e2.getModal();
         		addHTML(m, e2);
         	}else {
         		Document doc2 = e2.getDoc();
         		addHTML(doc2, e2);
         	}
  		}
     }
     
    public static void main(String[] args) throws IOException {
    	
    	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    	
        // Reference for events: https://www.w3schools.com/tags/ref_eventattributes.asp

        final Collection<String> EVENTS = Arrays.asList(
                "onchange","onfocus","onselect","onsubmit",
                "onclick", "onmouseover", "ondbclick", "onwheel",
                "onkeydown", "onkeypress", "onkeyup",
                "ondrag","ondragend","ondrop","onscroll","ondragstart",
                "onload","alert");

        Document doc = Jsoup.connect(root).get();
        Node home = new Node(doc.title(), doc);
        fsm.add(home);
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        HtmlPage page;
        page = webClient.getPage(root);
        int mod = 0;
        for(Element e : doc.getAllElements()){
        	if(mod > 0) {
        		System.out.println(e.id());
        		mod--;
        		continue;
        	}
        	boolean dont = false;
            tags.add(e.tagName().toLowerCase());    // add each tag in tags List
            //System.out.println("Tag: "+ e.tag()+" attributes = "+e.attributes());  // attributes with values in string
            //System.out.println("Tag: "+ e.tag()+" attributes = "+e.attributes().asList()); //attributes in List<Attribute>
            if(e.tagName().equals("button")) {
            	
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setJavaScriptEnabled(true);
                HtmlButton htmlButton;
                HtmlPage htmlPage;
                
            	htmlButton = (HtmlButton) page.getElementById(e.id());
            	try {
              		htmlPage = (HtmlPage) htmlButton.click();
              		System.out.println(htmlPage);
              	}catch(Exception x) {
              		System.out.print(x);
              		continue;
              	}
              	boolean add = true;
                  String url = htmlPage.getUrl().toString();
                  String[] temp = url.split("/");
                  Document doc2 = Jsoup.connect(url).get();
                  if(url.contains("http")) {
          			try{
          				doc2 = Jsoup.connect(url).get();
          			}catch (Exception x){
          				continue;
          			}
          		}else if(temp.length > 1) {
          			try{
          				doc2 = Jsoup.connect(root + temp[1]).get();
          			}catch (Exception x){
          				continue;
          			}
          		}else {
          			try{
          				doc2 = Jsoup.connect(root + url).get();
          			}catch (Exception x){
          				continue;
          			}
          		}    
  		       	Node out = new Node(doc2.title(), doc2);
  		       	System.out.println(out.getTitle());
  		       	for(Node node : fsm) {
         			if(node.getTitle().contentEquals(out.getTitle())) {
         				out = node;
         				add = false;
         			}
         		}
          		for(Node e2 : home.out) {
          			if(e2.getTitle().equals(out.getTitle())){
          				dont = true;
          				break;
          			}
          		}
          		if(!dont) {
          			System.out.println("adding now to " + home.getTitle());
          			System.out.println(out.getTitle());
          			home.addToOut(out);
          			Object[] edge = {htmlButton, "b"};
          			home.addToEdge(edge);
          			if(add) {
          				fsm.add(out);
          			}
          		}
          		dont = false;
          		continue;
        	}
            
            if(e.hasClass("modal")) {
            	boolean add = true;
            	String title = e.id();
            	Node out = new Node(title, e);
            	Elements modale = e.getAllElements();
            	mod = modale.size();
            	System.out.println(mod);
            	for(Node node : fsm) {
        			if(node.getTitle().contentEquals(out.getTitle())) {
        				out = node;
        				add = false;
        			}
        		}
            	for(Node neighbor : home.out) {
            		if(neighbor.getModal() != null) {
            			String ntitle = neighbor.getTitle();
            			if(ntitle.equals(title)) {
            				dont = true;
            				break;
            			}
            		}
            	}
            	if(!dont) {
            		System.out.println("adding now to " + home.getTitle());
             		System.out.println(out.getTitle());
            		home.addToOut(out);
            		home.addToEdge(null);
            		if(add) {
            			fsm.add(out);
            		}
            	}
            	dont = false;
            	continue;
            }
            
            for(Attribute att : e.attributes().asList()){
            // for each tag get all attributes in one List<Attribute>
                String attrKey = att.getKey();
                String attrVal = att.getValue();
                //System.out.println("Key: " + attrKey + " , Value: "+ attrVal);
                attrs.add(attrKey.toLowerCase());
                if(attrKey.contentEquals("href")) {
                	if(attrVal.contains("html")) {
                		boolean add = true;
                		String linkHref = e.attr("href");
                		String[] temp = linkHref.split("/");
                		Document doc2;
                		if(linkHref.contains("http")) {
                			try{
                				doc2 = Jsoup.connect(linkHref).get();
                			}catch (Exception x){
                				continue;
                			}
                		}else if(temp.length > 1) {
                			try{
                				doc2 = Jsoup.connect(root + temp[1]).get();
                			}catch (Exception x){
                				continue;
                			}
                		}else {
                			try{
                				doc2 = Jsoup.connect(root + linkHref).get();
                			}catch (Exception x){
                				continue;
                			}
                		}    
        		       	Node out = new Node(doc2.title(), doc2);
        		       	System.out.println(out.getTitle());
        		       	for(Node node : fsm) {
                			if(node.getTitle().contentEquals(out.getTitle())) {
                				out = node;
                				add = false;
                			}
                		}
                		for(Node e1 : home.out) {
                			if(e1.getTitle().equals(out.getTitle())){
                				dont = true;
                				break;
                			}
                		}
                		if(!dont) {
                			System.out.println("adding now to home");
                			System.out.println(out.getTitle());
                			home.addToOut(out);
                			Object[] edge = {e, "e"};
                			home.addToEdge(edge);
                			if(add) {
                				fsm.add(out);
                			}
                		}
                		dont = false;
                	}
                }
            }
        }
        
        for(Node e1 : home.out) {
        	if(e1.getModal() != null) {
        		Element m = e1.getModal();
        		addHTML(m, e1);
        	}else {
        		Document doc2 = e1.getDoc();
        		addHTML(doc2, e1);
        	}
		}
        
        //home.print();
        Node.printgraph(home);
        Node.graphreset(home);
        Node.graphtraverse(home);
        java.lang.System.exit(0);
    }
    
    
}