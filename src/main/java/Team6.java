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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// TODO: create new file/class for each function to organize the whole project
public class Team6 {
	 static List<String> tags = new ArrayList<String>(); //record tags
     static List<String> attrs = new ArrayList<String>(); //record attributes
     static List<Element> elements = new ArrayList<Element>();
     static List<Node> fsm = new ArrayList<Node>();
     static String root = "https://melodize.github.io/";
     static String baseid = "team6_";
     static int countid = 0; 
     static int nodeid = 0;
     /*public static void addHTML(Element e1, Node n) throws IOException{
    	 if(n.old) {
      		return;
      	}
      	n.old = true;
      	int mod = 0;
      	WebClient webClient = new WebClient(BrowserVersion.CHROME);
      	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        String url;
        HtmlPage page;
        if(n.getDoc() != null) {
        	url = n.getDoc().location();
        	page = webClient.getPage(url);
        }else {
        	url = root;
        	page = webClient.getPage(url);
        }
        String outputname = "";
        if(url.contentEquals(root)) {
        	outputname = "index";
        }else {
        	String[] temp = url.split("/");
        	for(String s : temp) {
        		if(s.contains(".html")) {
        			outputname = s.substring(0, s.length() - 5);
        		}
        	}
        }
        String fileName = "C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/" + outputname + ".html";
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
        webClient.close();
        
          for(Element e : e1.getAllElements()){      // all elements in html
          	boolean dont = false;
          	if(mod > 0) {
          		mod--;
         		continue;
          	}
              tags.add(e.tagName().toLowerCase());    // add each tag in tags List
              if(e.tagName().equals("button")) {
            	  	webClient = new WebClient(BrowserVersion.CHROME);
                	webClient.getOptions().setThrowExceptionOnScriptError(false);
                	webClient.getOptions().setJavaScriptEnabled(true);
			        HtmlButton htmlButton;
			        HtmlPage htmlPage;
			        page = webClient.getPage(url);
			        if(e.id().contentEquals("")) {
            			e.attr("id", baseid + "" + countid);
            			countid++;
            		}else if(e.id().contains("team6_")) {
            			continue;
            		}
		          	htmlButton = (HtmlButton) page.getElementById(e.id());
		          	try {
		          		htmlPage = (HtmlPage) htmlButton.click();
		          		webClient.close();
		          	}catch(Exception x) {
		          		System.out.print(x);
		          		continue;
		          	}
		          	boolean add = true;
		              String url1 = htmlPage.getUrl().toString();
		              String[] temp = url1.split("/");
		              Document doc2;
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
			       	for(String[] edge : n.edges) {
          		    	if(edge[0] != null && edge[0].equals(e.id())){
          		    		dont = true;
          		    		break;
          		    	}
          		    }
		      		if(!dont) {
		      			System.out.println("adding now to " + n.getTitle());
		      			System.out.println(out.getTitle());
		      			String[] edge = {htmlButton.getId(), "b"};
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
              	Document doc = Jsoup.connect(root).get();
              	Node out = new Node(title, e, doc);
              	
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
              		String[] edge = {null, "dud"};
              		n.addToEdge(edge);
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
                  		int first = linkHref.indexOf("/");
                  		String temp = linkHref.substring(first + 1);
                  		Document doc2;
                  		if(e.id().contentEquals("")) {
                			e.attr("id", baseid + "" + countid);
                			countid++;
                		}else if(e.id().contains("team6_")) {
                			continue;
                		}
                  		if(linkHref.contains("http")) {
                  			try{
                  				doc2 = Jsoup.connect(linkHref).get();
                  			}catch (Exception x){
                  				continue;
                  			}
                  		}else if(temp.length() > 1) {
                  			try{
                  				doc2 = Jsoup.connect(root + temp).get();
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
              		    for(String[] edge : n.edges) {
              		    	if(edge[0] != null && edge[0].equals(e.id())){
              		    		dont = true;
              		    		break;
              		    	}
              		    }
                  		if(!dont) {
                  			n.addToOut(out);
                  			String[] edge;
                  			try {
                				if(e.id().contentEquals("")) {
                					System.out.println("how did I know");
                					String[] temp1 = {null, "dud"};
                					edge = temp1;
                				}else {
                					String[] temp1 = {e.id(), "e"};
                					edge = temp1;
                				}
                			}catch(Exception x) {
                				String[] temp1 = {null, "dud"};
            					edge = temp1;
                			}
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
          String str = n.getDoc().toString();
          writer.write(str);

          writer.close();
          File input = new File(fileName);
          n.setFile(input);
          nodeid++;
          webClient.close();
          for(int i = 0; i < n.out.size(); i ++) {
          	Node node = n.out.get(i);
          	if(node.getModal() != null) {
          		Element m = node.getModal();
          		addHTML(m, node);
          	}else {
          		Document doc2 = node.getDoc();
          		addHTML(doc2, node);
          	}
  		}
          
     }*/
     
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
        Node home = new Node(doc.title(), doc.location());
        fsm.add(home);
        ArrayList<ArrayList> result = preprocess(doc);
        ArrayList<String> elements = result.get(0);
        ArrayList<ArrayList> edgeelements = result.get(1);
          
        
        addHTML(home);
        
        Node.printgraph(home);
        Node.graphreset(home);
        //Node.getFile(root, fsm);
        //Node.graphtraverse(home);
        java.lang.System.exit(0);
    }
    static ArrayList<ArrayList> preprocess(Document doc) throws IOException {
    	List<String> modals = new ArrayList<String>();
    	List<String> jses = new ArrayList<String>();
    	ArrayList<ArrayList> elements = new ArrayList<ArrayList>();
    	ArrayList<String[]> buttons = new ArrayList<String[]>();
    	for(Element e : doc.getAllElements()){
    		if(e.hasClass("modal")) {
    			String title = e.id();
    			modals.add(title);
    		}
    		for(Attribute att : e.attributes().asList()) {
    			if(att.getKey().contentEquals("src") && att.getValue().contains(".js")) {
    				if(att.getValue().contains("http")) {
    					jses.add(att.getValue());
    					continue;
    				}
    				jses.add(root + att.getValue());
    			}
    		}
    	}
    	
    	for(String js : jses) {
    		ArrayList<ArrayList> result = readJSTester.getJSInfo(js, "C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/output.txt", modals);
    		buttons.addAll(result.get(0));
    		elements.addAll(result.get(1));
    	}
    	ArrayList<ArrayList> result = new ArrayList<ArrayList>();
    	result.add(buttons);
    	result.add(elements);
    	return result;
    }
    
    static String processhref(String href) {
    	String result = href;
    	int slash = href.indexOf("/");
    	if(!href.contains("html")) {
    		return "-1";
    	}
    	if(slash >= 0 && slash < 2) {
    		result = root + href.substring(slash + 1);
    	}else if(href.contains("html")) {
    		result = root + href;
    	}else {
    		
    		return "-1";
    	}
    	
    	return result;
    }
    
    static void getJSElements(Document doc, ArrayList<String> elements, ArrayList<String> compare, ArrayList<ArrayList> edgeelements, Node home) throws IOException {
    	for(int i = 0; i < elements.size(); i++) {
        	String button = elements.get(i);
        	boolean there = false;
        	for(String c : compare) {
        		if(button.contentEquals(c)) {
        			there = true;
        		}
        	}
        	if(!there) {
        		continue;
        	}
        	String[] edge = {button, "b"};
        	ArrayList<String[]> edgeelement = edgeelements.get(i);
        	for(String[] arr : edgeelement) {
        		boolean add = true;
        		if(arr[1].contentEquals("href")) {
        			String link;
        			if(arr[0].contains("\"")) {
        				link = arr[0].substring(1, arr[0].length()-1);
        				
        			}else {
        				link = arr[0];
        			}
        			link = processhref(link);
    				Document doc2 = Jsoup.connect(link).get();
    				Node out = new Node(doc2.title(), doc2.location());
    				System.out.println("adding now to: " + doc.title() + " edge: " + edge[0] + " to node : " + out.getTitle());    				
    				for(Node node : fsm) {
            			if(node.getTitle().contentEquals(out.getTitle())) {
            				out = node;
            				add = false;
            				break;
            			}
        			}
    				home.addToOut(out);
    				home.addToEdge(edge);
    				
        			if(add) {
        				fsm.add(out);
        			}
        		}else if(arr[1].contentEquals("show")){
        			Node out = new Node(arr[0], doc.location(), true);
    				System.out.println("adding now to: " + doc.title() + " edge: " + edge[0] + " to node : " + out.getTitle());
    				for(Node node : fsm) {
            			if(node.getTitle().contentEquals(out.getTitle())) {
            				out = node;
            				add = false;
            				break;
            			}
        			}
    				home.addToOut(out);
        			home.addToEdge(edge);
        			
        			if(add) {
        				fsm.add(out);
        			}
        		}
        	}
        }
    }
    static void getModalElements(Document doc, ArrayList<String> elements, ArrayList<String> compare, ArrayList<ArrayList> edgeelements, Node home) throws IOException {
    	for(int i = 0; i < elements.size(); i++) {
        	String button = elements.get(i);
        	String[] edge = {button, "b"};
        	ArrayList<String[]> edgeelement = edgeelements.get(i);
        	boolean there = false;
        	for(String c : compare) {
        		if(button.contentEquals(c)) {
        			there = true;
        		}
        	}
        	if(!there) {
        		continue;
        	}
        	for(String[] arr : edgeelement) {
        		boolean add = true;
        		if(arr[1].contentEquals("href")) {
        			String link;
        			if(arr[0].contains("\"")) {
        				link = arr[0].substring(1, arr[0].length()-1);
        				
        			}else {
        				link = arr[0];
        			}
        			link = processhref(link);
    				Document doc2 = Jsoup.connect(link).get();
    				Node out = new Node(doc2.title(), doc2.location());
    				System.out.println("adding now to: " + home.getTitle() + " edge: " + edge[0] + " to node : " + out.getTitle());    				
    				for(Node node : fsm) {
            			if(node.getTitle().contentEquals(out.getTitle())) {
            				out = node;
            				add = false;
            				break;
            			}
        			}
    				home.addToOut(out);
    				home.addToEdge(edge);
    				
        			if(add) {
        				fsm.add(out);
        			}
        		}else if(arr[1].contentEquals("hide")){
        			Node out = new Node(doc.title(), doc.location(), true);
    				System.out.println("adding now to: " + home.getTitle() + " edge: " + edge[0] + " to node : " + out.getTitle());
    				for(Node node : fsm) {
            			if(node.getTitle().contentEquals(out.getTitle())) {
            				out = node;
            				add = false;
            				break;
            			}
        			}
    				home.addToOut(out);
        			home.addToEdge(edge);
        			
        			if(add) {
        				fsm.add(out);
        			}
        		}
        	}
        }
    }
    
    static void addHTML(Node node) throws IOException {
    	
    	if(node.old) {
      		return;
      	}
    	System.out.println("");
      	node.old = true;
    	String root = node.getDoc();
    	Document doc = Jsoup.connect(root).get();
        ArrayList<ArrayList> result = preprocess(doc);
        ArrayList<String> elements = result.get(0);
        ArrayList<ArrayList> edgeelements = result.get(1);
        ArrayList<String> compare = new ArrayList<String>();
        for(Element e : doc.getAllElements()) {
        	if(!e.id().equals("")) {
        		compare.add(e.id());
        	}
        }
        getJSElements(doc, elements, compare, edgeelements, node);
        String url = node.getDoc();
        String outputname = "";
        if(url.contentEquals(root)) {
        	outputname = "index";
        }else {
        	String[] temp = url.split("/");
        	for(String s : temp) {
        		if(s.contains(".html")) {
        			outputname = s.substring(0, s.length() - 5);
        		}
        	}
        }
        String fileName = "C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/" + outputname + ".html";
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
        
        int mod = 0;
        for(Element e : doc.getAllElements()){
        	if(mod > 0) {
        		mod--;
        		continue;
        	}
        	boolean dont = false;
        	
            String tag = e.tagName().toLowerCase();

            if(e.hasClass("modal")) {
            	Elements modale = e.getAllElements();
            	mod = modale.size();
            	continue;
            }
            
            for(Attribute att : e.attributes().asList()){
                String attrKey = att.getKey();
                String attrVal = att.getValue();
                if(attrKey.contentEquals("href")) {
                	
                	String link = processhref(e.attr("href"));
                	if(link.contentEquals("-1")) {
                		continue;
                	}
                	if(e.id().contentEquals("")) {
            			e.attr("id", baseid + "" + countid);
            			countid++;
            		}else if(e.id().contains("team6_")) {
            			continue;
            		}
                	Document doc2 = Jsoup.connect(link).get();
                	boolean add = true;
    		       	Node out = new Node(doc2.title(), doc2.location());
    		       	for(Node neighbors : fsm) {
            			if(neighbors.getTitle().contentEquals(out.getTitle())) {
            				out = neighbors;
            				add = false;
            			}
            		}
            		for(String[] e1 : node.edges) {
            			if(e1[1].equals(e.id())){
            				dont = true;
            				break;
            			}
            		}
            		if(!dont) {
            			System.out.println("adding now to " + node.getTitle());
            			System.out.println(out.getTitle());
            			node.addToOut(out);
            			String[] edge;
            			try {
            				if(e.id().contentEquals("")) {
            					String[] temp1 = {null, "dud"};
            					edge = temp1;
            				}else {
            					String[] temp1 = {e.id(), "e"};
            					edge = temp1;
            				}
            			}catch(Exception x) {
            				String[] temp1 = {null, "e"};
        					edge = temp1;
            			}
            			node.addToEdge(edge);
            			if(add) {
            				fsm.add(out);
            			}
            		}
            		dont = false;
                	}
                }
            }
        String str = doc.toString();
        writer.write(str);

        writer.close();
        File input = new File(fileName);
        node.setFile(input);
        nodeid++;
        
        for(int i = 0; i < node.out.size(); i ++) {
        	Node e1 = node.out.get(i);
        	if(e1.getModal()) {
        		addModal(e1);
        	}else {
        		addHTML(e1);
        	}
        	
		}
    }
    static void addModal(Node node) throws IOException {
    	
    	if(node.old) {
    		return;
    	}
    	System.out.println("");
    	node.old = true;
    	String root = node.getDoc();
    	Document doc = Jsoup.connect(root).get();
        ArrayList<ArrayList> result = preprocess(doc);
        ArrayList<String> elements = result.get(0);
        ArrayList<ArrayList> edgeelements = result.get(1);
        ArrayList<String> compare = new ArrayList<String>();
        Element modale = null;
        for(Element e : doc.getAllElements()) {
        	if(e.hasClass("modal")) {
            	modale = e;
            }
        }
        if(modale == null) {
        	return;
        }
        for(Element e : modale.getAllElements()) {
        	if(!e.id().equals("")) {
        		compare.add(e.id());
        	}
        }
        getModalElements(doc, elements, compare, edgeelements, node);
        String fileName = "C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/test" + nodeid + ".html";
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
        for(Element e : modale.getAllElements()){
        	boolean dont = false;
        	
            String tag = e.tagName().toLowerCase();
            
            for(Attribute att : e.attributes().asList()){
                String attrKey = att.getKey();
                String attrVal = att.getValue();
                if(attrKey.contentEquals("href")) {
                	
                	String link = processhref(e.attr("href"));
                	if(link.contentEquals("-1")) {
                		continue;
                	}
                	if(e.id().contentEquals("")) {
            			e.attr("id", baseid + "" + countid);
            			countid++;
            		}else if(e.id().contains("team6_")) {
            			continue;
            		}
                	Document doc2 = Jsoup.connect(link).get();
                	boolean add = true;
    		       	Node out = new Node(doc2.title(), doc2.location());
    		       	for(Node neighbors : fsm) {
            			if(neighbors.getTitle().contentEquals(out.getTitle())) {
            				out = neighbors;
            				add = false;
            			}
            		}
            		for(String[] e1 : node.edges) {
            			if(e1[1].equals(e.id())){
            				dont = true;
            				break;
            			}
            		}
            		if(!dont) {
            			System.out.println("adding now to " + node.getTitle());
            			System.out.println(out.getTitle());
            			node.addToOut(out);
            			String[] edge;
            			try {
            				if(e.id().contentEquals("")) {
            					String[] temp1 = {null, "dud"};
            					edge = temp1;
            				}else {
            					String[] temp1 = {e.id(), "e"};
            					edge = temp1;
            				}
            			}catch(Exception x) {
            				String[] temp1 = {null, "e"};
        					edge = temp1;
            			}
            			node.addToEdge(edge);
            			if(add) {
            				fsm.add(out);
            			}
            		}
            		dont = false;
                	}
                }
            }
	    String str = doc.toString();
	    writer.write(str);
	
	    writer.close();
	    File input = new File(fileName);
	    node.setFile(input);
	    nodeid++;
	    for(int i = 0; i < node.out.size(); i ++) {
	    	Node e1 = node.out.get(i);
	    	addHTML(e1);
		}
    }
    
}