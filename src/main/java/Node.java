import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.URL;

public class Node{
	//static String root = "https://anoboard.netlify.com/";
	private String title;
	private String  mydoc;
	public boolean mymodal;
	public File myfile;
	public ArrayList<Node> out;
	public boolean old;
	public boolean trav;
	public ArrayList<String[]> edges;
	
	public static ArrayList<String> printGraphList;
	
	Node(String title, String doc, boolean b){
		out = new ArrayList<Node>();
		edges = new ArrayList<String[]>();
		printGraphList = new ArrayList<String>();
		
		this.title = title;
		this.mydoc = doc;
		this.mymodal = true;
		this.myfile = null;
		this.old = false;
		this.trav = false;
	}
	Node(String title, String doc){
		out = new ArrayList<Node>();
		edges = new ArrayList<String[]>();
		this.title = title;
		this.mydoc = doc;
		this.mymodal = false;
		this.myfile = null;
		this.old = false;
		this.trav = false;
		
	}
	
	void setFile(File file) {
		this.myfile = file;
	}
	boolean getModal() {
		return mymodal;
	}
	
	String getTitle() {
		return title;
	}
	
	String getDoc() {
		return mydoc;
	}
	void addToOut(Node node) {
		out.add(node);
	}
	
	void addToEdge(String[] e) {
		edges.add(e);
	}
	
	Node getOutNode(int i) {
		return out.get(i);
	}
	
	void print() { 
		
		int i = 0;
		
		System.out.println("Title: " + this.title);
		System.out.println("Outgoing Nodes: ");
		
		for(Node n : out) {
			i++;
			System.out.print(i + ") ");
			System.out.println(n.getTitle());
		}
	    
	}
	
	static void printgraph(Node node) {
		
		if(node.trav) {
			return;
		}
		System.out.print("\n");
		node.trav = true;
		System.out.println("from node: " + node.getTitle());
		int i = 0;
		for(Node n : node.out) {
			System.out.println("edge: " + node.edges.get(i)[0]);
			System.out.println("to node: " + n.getTitle());
			i++;
		}
		for(Node n : node.out) {
			printgraph(n);
		}
	}
	
	static void listPrintGraph(Node node) {
		if(node.trav) {
			return;
		}
		System.out.print("\n");
		node.trav = true;
		//System.out.println("from node: " + node.getTitle());
		printGraphList.add("fn: " + node.getTitle());
		int i = 0;
		for(Node n : node.out) {
			//System.out.println("edge: " + node.edges.get(i)[0]);
			printGraphList.add("ed: " + node.edges.get(i)[0]);
			//System.out.println("to node: " + n.getTitle());
			printGraphList.add("tn: " + n.getTitle());
			i++;
		}
		for(Node n : node.out) {
			listPrintGraph(n);
		}
	}
	
	static void graphreset(Node node) {
		if(!node.old && !node.trav) { //|| (node.getDoc().contentEquals(root.getDoc())) || (node.getTitle().contentEquals(root.getTitle()))) {
			return;
		}
		node.old = false;
		node.trav = false;
		for(Node n : node.out) {
			graphreset(n);
		}
	}
	
	static void graphreset(Node node, Node root) {
		if(!node.old && !node.trav || (node.getDoc().contentEquals(root.getDoc())) || (node.getTitle().contentEquals(root.getTitle()))) {
			return;
		}
		node.old = false;
		node.trav = false;
		for(Node n : node.out) {
			graphreset(n, root);
		}
	}
	
	static boolean checkhref(String href, String roothref) {
    	boolean result = true;
    	int slash = href.indexOf("/");
    	if(!(href.contains("/") || href.contains("html"))) {
    		return false;
    	}
    	
    	if(href.contains("http") || href.contains("css") || href.contains("www.") || href.contains(".org") || href.contains(".com") || href.contains(".net") || href.contains(".jpg") || href.contains(".png")){
    		if(href.contains(roothref)) {
    			return result;
    		}
    		return false;
    	}
    	return true;
    }
	
	/*static void graphtraverse(Node root) {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
      	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        Document doc = Jsoup.parse(root.myfile, "utf-8", "www.example.com/");
        String url = root.getDoc();
	}*/
	
	static void graphtraverse(Node root, Node node, HtmlPage p) throws IOException, InterruptedException{
		
		node.trav = true;
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		/*WebClient webClient = new WebClient(BrowserVersion.CHROME);
      	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);*/
        Document doc = Jsoup.parse(node.myfile, "utf-8", "www.example.com/");
        String url = node.getDoc();
        System.out.println(url);
		//System.out.println("for node: " + node.getTitle());
		
		for(int i = 0; i < node.edges.size(); i++) {
			System.out.println(node.getDoc());
			System.out.println(i);
			boolean found = false;
			String[] edge = node.edges.get(i);
			Node n = node.out.get(i);
			/*if(node.equals(root) && !n.getDoc().contentEquals(root.getDoc()) && !n.getTitle().contentEquals(root.getTitle())) {
				graphreset(n, root);
			}*/
			System.out.println(edge[0]);
			System.out.println(edge[1]);
			System.out.println(n.getTitle());
			//HtmlPage page = webClient.getPage(url);
			String title = ""; 
			if(edge[0] != null && edge[0].contains("team6")) {
				if(edge[1].contains("b")) {
					List<DomElement> buttons = p.getElementsByTagName("button");
					String buttonid = edge[0];
					Element button1 = doc.getElementById(buttonid);
					String href = button1.attr("href");
					String ind = edge[0].substring(6);
					int index = Integer.parseInt(ind);
					int compare = -1;
					for(DomElement button : buttons) {
						if(button.hasAttribute("onclick")) {
							if(!edge[1].contentEquals("bh")) {
								continue;
							}else {
								compare++;
								/*if(checkhref("href") && button.getId().contentEquals("")) {
									compare++;
								}*/
							}
						}
						String comp = button.getAttribute("href");
						if(checkhref(comp, root.getDoc()) && button.getId().contentEquals("")) {
							compare++;
						}
						if(compare == index) {
							HtmlPage htmlpage = button.click();
							title = htmlpage.getTitleText();
							if(n.getModal()) {
								if(checkModal(htmlpage, n)) {
									System.out.println("it works!");
									System.out.println(edge[0]);
									System.out.println(n.getTitle());
									graphtraverse(root, n, htmlpage);
									found = true;
									break;
								}
							}else if(node.getModal()) {
								if(checkReturn(htmlpage, n)) {
									System.out.println("it works!");
									System.out.println(edge[0]);
									System.out.println(n.getTitle());
									graphtraverse(root, n, htmlpage);
									found = true;
									break;
								}
							}
							if(title.equals(n.getTitle())) {
								System.out.println("it works!");
								System.out.println(edge[0]);
								System.out.println(n.getTitle());
								graphtraverse(root, n, htmlpage);
								found = true;
								continue;
							}
						}
					}
				}else if(edge[1].equals("e")) {
					try {
						Element edge1 = doc.getElementById(edge[0]);
						String tagname = edge1.tag().toString();
						List<DomElement> buttons = p.getElementsByTagName(tagname);
						String href = edge1.attr("href");
						String ind  = edge[0].substring(6);
						int index = Integer.parseInt(ind);
						int compare = -1;
						for(DomElement button : buttons) {
							String comp = button.getAttribute("href");
							if(button.hasAttribute("onclick")) {
								continue;
							}
							if(checkhref(comp, root.getDoc()) && button.getId().contentEquals("")) {
								compare++;
							}
							if(compare == index) {
								System.out.println(comp);
								System.out.println(href);
								HtmlPage htmlpage = button.click();
								title = htmlpage.getTitleText();
								if(title.equals(n.getTitle())) {
									System.out.println("it works!");
									System.out.println(edge[0]);
									System.out.println(n.getTitle());
									graphtraverse(root, n, htmlpage);
									found = true;
									break;
								}
							}
						}
						
					}catch (Exception e) {
						System.out.println(e);
						title = "error";
					}
				}
			}else {
				if(edge[1].equals("dud")) {
					System.out.println("dud");
					Node out = node.out.get(i);
					System.out.println(edge[0]);
					System.out.println(out.getTitle());
					continue;
				}else if(edge[1].contains("b")) {
					String buttonid = edge[0];
					try {
						HtmlElement button = (HtmlElement) p.getElementById(buttonid);
						HtmlPage htmlPage = (HtmlPage) button.click();
						title = htmlPage.getTitleText();
						if(n.getModal()) {

							if(checkModal(htmlPage, n)) {
								System.out.println("it works!");
								System.out.println(edge[0]);
								System.out.println(n.getTitle());
								graphtraverse(root, n, htmlPage);
								found = true;
								continue;
							}
								
						}else if(node.getModal()) {
							if(checkReturn(htmlPage, n)) {
								System.out.println("it works!");
								System.out.println(edge[0]);
								System.out.println(n.getTitle());
								graphtraverse(root, n, htmlPage);
								found = true;
								continue;
							}
						
						}else {
							System.out.println("it works!");
							System.out.println(edge[0]);
							System.out.println(n.getTitle());
							graphtraverse(root, n, htmlPage);
							found = true;
							continue;
						}
						}catch (Exception e) {
						System.out.println(e);
						title = "error";
					}
				}else if(edge[1].equals("e")) {
					try {
						String eid = edge[0];
						HtmlElement element = (HtmlElement) p.getElementById(eid);
						HtmlPage htmlPage = (HtmlPage) element.click();
						title = htmlPage.getTitleText();
						if(title.equals(n.getTitle())) {
							System.out.println("it works!");
							System.out.println(edge[0]);
							System.out.println(n.getTitle());
							graphtraverse(root, n, htmlPage);
							found = true;
							continue;
						}
					}catch (Exception e) {
						title = "error";
					}
				
				}
				
				
			}
			if(!found) {
				boolean c = false;
				System.out.println("what?");
				if(i < node.edges.size() - 1) {
					String temp = node.edges.get(i+1)[1];
					if(edge[1].contentEquals("bc")) { 
						c = true;
						node.edges.get(i)[1] = "cdud";
					}else {
						node.edges.get(i)[1] = "edud";
					}
					node.edges.get(i+1)[1] = temp;
				}else {
					node.edges.get(i)[1] = "edud";
				}
				if(c) {
					System.out.println("cdud");
					System.out.println("for edge: " + edge[0]);
					System.out.println("may go to: "+ n.getTitle());
				}else {
					System.out.println("edud");
					System.out.println("for edge: " + edge[0]);
					System.out.println("should go to: "+ n.getTitle());
				}
				System.out.println("goes to " + title);
				System.out.println("");
			}
			System.out.println("--------------------------------------------------------------------");	
		}
		
		if(node.trav) {
			return;
		}
		//System.out.println("--------------------------------------------------------------------");	
	}
	
	/*static void graphtraverse(Node node) throws IOException, InterruptedException{
		if(node.trav) {
			return;
		}
		System.out.println();
		node.trav = true;
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
      	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        Document doc = Jsoup.parse(node.myfile, "utf-8", "www.example.com/");
        String url = node.getDoc();
        System.out.println(url);
		System.out.println("for node: " + node.getTitle());
		for(int i = 0; i < node.edges.size(); i++) {
			String[] edge = node.edges.get(i);
			Node n = node.out.get(i);
			HtmlPage page = webClient.getPage(url);
			String title = ""; 
			if(edge[0] != null && edge[0].contains("team6")) {
				if(edge[1].contains("b")) {
					List<DomElement> buttons = page.getElementsByTagName("button");
					String buttonid = edge[0];
					Element button1 = doc.getElementById(buttonid);
					String href = button1.attr("href");
					for(DomElement button : buttons) {
						String comp = button.getAttribute("href");
						if(href.equals(comp)) {
							HtmlPage htmlpage = button.click();
							title = htmlpage.getTitleText();
						}
					}
				}else if(edge[1].equals("e")) {
					try {
						Element edge1 = doc.getElementById(edge[0]);
						String tagname = edge1.tag().toString();
						List<DomElement> buttons = page.getElementsByTagName(tagname);
						String href = edge1.attr("href");
							
						for(DomElement button : buttons) {
							String comp = button.getAttribute("href");
							if(href.equals(comp)) {
								System.out.println(comp);
								HtmlPage htmlpage = button.click();
								title = htmlpage.getTitleText();
								break;
							}
						}
						
					}catch (Exception e) {
						System.out.println(e);
						title = "error";
					}
				}
				if(edge[1].contentEquals("dud")) {
					System.out.println("dud");
					Node out = node.out.get(i);
					System.out.println(edge[0]);
					System.out.println(title);
					System.out.println(out.getTitle());
				}
				else if(!title.equals(n.getTitle())) {
					boolean c = false;
					if(i < node.edges.size() - 1) {
						String temp = node.edges.get(i+1)[1];
						if(edge[1].contentEquals("bc")) { 
							c = true;
							node.edges.get(i)[1] = "cdud";
						}else {
							node.edges.get(i)[1] = "edud";
						}
						node.edges.get(i+1)[1] = temp;

					}else {
						node.edges.get(i)[1] = "edud";
					}
					if(c) {
						System.out.println("cdud");
						System.out.println("for edge: " + edge[0]);
						System.out.println("may go to: "+ n.getTitle());
					}else {
						System.out.println("edud");
						System.out.println("for edge: " + edge[0]);
						System.out.println("should go to: "+ n.getTitle());
					}
					System.out.println("goes to " + title);
					System.out.println("");
				}else {
					System.out.println("it works!");
					System.out.println(edge[0]);
					System.out.println(title);
					System.out.println("");
				}
			}else {
				if(edge[1].equals("dud")) {
					System.out.println("dud");
					Node out = node.out.get(i);
					System.out.println(edge[0]);
					System.out.println(out.getTitle());
					continue;
				}else if(edge[1].contains("b")) {
					String buttonid = edge[0];
					try {
						HtmlElement button = (HtmlElement) page.getElementById(buttonid);
						HtmlPage htmlPage = (HtmlPage) button.click();
						title = htmlPage.getTitleText();
						if(n.getModal()) {
							if(checkModal(htmlPage, n)) {
								System.out.println("it works!");
								System.out.println(edge[0]);
								System.out.println(n.getTitle());
								System.out.println("");
							}else {
								boolean c = false;
								title = htmlPage.getTitleText();
								if(i < node.edges.size() - 1) {
									String temp = node.edges.get(i+1)[1];
									if(edge[1].contentEquals("bc")) { 
										c = true;
										node.edges.get(i)[1] = "cdud";
									}else {
										node.edges.get(i)[1] = "edud";
									}
									node.edges.get(i+1)[1] = temp;
									//System.out.println(edge[1]);
								}else {
									node.edges.get(i)[1] = "edud";
								}
								if(c) {
									System.out.println("cdud");
									System.out.println("for edge: " + edge[0]);
									System.out.println("may go to: "+ n.getTitle());
								}else {
									System.out.println("edud");
									System.out.println("for edge: " + edge[0]);
									System.out.println("should go to: "+ n.getTitle());
								}
								System.out.println("goes to " + title);
								System.out.println("");
							}
							continue;
						}
						
					}catch (Exception e) {
						System.out.println(e);
						title = "error";
					}
				}else if(edge[1].equals("e")) {
					try {
						String eid = edge[0];
						HtmlElement element = (HtmlElement) page.getElementById(eid);
						HtmlPage htmlPage = (HtmlPage) element.click();
						title = htmlPage.getTitleText();
					}catch (Exception e) {
						title = "error";
					}
				
				}
				
				if(!title.equals(n.getTitle())) {
					boolean c = false;
					if(i < node.edges.size() - 1) {
						String temp = node.edges.get(i+1)[1];
						if(edge[1].contentEquals("bc")) { 
							c = true;
							node.edges.get(i)[1] = "cdud";
						}else {
							node.edges.get(i)[1] = "edud";
						}
						node.edges.get(i+1)[1] = temp;
						//System.out.println(edge[1]);
					}else {
						node.edges.get(i)[1] = "edud";
					}
					if(c) {
						System.out.println("cdud");
						System.out.println("for edge: " + edge[0]);
						System.out.println("may go to: "+ n.getTitle());
					}else {
						System.out.println("edud");
						System.out.println("for edge: " + edge[0]);
						System.out.println("should go to: "+ n.getTitle());
					}
					System.out.println("goes to " + title);
					System.out.println("");
				}else {
					System.out.println("it works!");
					System.out.println(edge[0]);
					System.out.println(title);
					System.out.println("");
				}
			}
		}
		webClient.close();

		System.out.println("--------------------------------------------------------------------");		
		for(int j = 0; j < node.out.size(); j++) {
          	Node n = node.out.get(j);
          	if(n.mymodal) {
          		graphtraversem(n, node.edges.get(j)[0]);
          	}else {	
	          	graphtraverse(n);
          	}
		}

	}
	static void graphtraversem(Node node, String ed) throws IOException, InterruptedException{
		if(node.trav) {
			return;
		}
		System.out.println();
		node.trav = true; 
		System.out.println("for node : " + node.getTitle());
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		String doc = node.getDoc();
		System.out.println(node.getDoc());
		File file = node.myfile;
		Document doc2 = Jsoup.parse(file, "UTF-8", "http://example.com/");
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
      	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        for(Element e : doc2.getAllElements()) {
    		if(e.hasClass("modal") && e.id().contentEquals(node.getTitle())) {
    			for(int i = 0; i < node.edges.size(); i++) {
    				String[] edge = node.edges.get(i);
    				Node n = node.out.get(i);
    				String title = ""; 
    				HtmlPage page2 = webClient.getPage(doc);
			        HtmlElement element = (HtmlElement) page2.getElementById(ed);
			        HtmlPage page = (HtmlPage) element.click();
    				if(edge[0] != null && edge[0].contains("team6")) {
    					if(edge[1].contains("b")) {
    						List<DomElement> buttons = page2.getElementsByTagName("button");
    						String buttonid = edge[0];
    						Element button1 = doc2.getElementById(buttonid);
    						String href = button1.attr("href");
    						for(DomElement button : buttons) {
    							String comp = button.getAttribute("href");
    							if(href.equals(comp)) {
    								page = element.click();
    								HtmlPage htmlpage = button.click();
    								title = htmlpage.getTitleText();
    								if(n.getDoc().equals(node.getDoc())) {
    									if(checkReturn(htmlpage, node)) { 
    									System.out.println("it works!");
    									System.out.println(edge[0]);
    									System.out.println(n.getTitle());
    									System.out.println("");
    								}else {
    									boolean c = false;
    									if(i < node.edges.size() - 1) {
    										String temp = node.edges.get(i+1)[1];
    										if(edge[1].contentEquals("bc")) { 
    											c = true;
    											node.edges.get(i)[1] = "cdud";
    										}else {
    											node.edges.get(i)[1] = "edud";
    										}
    										node.edges.get(i+1)[1] = temp;
    										//System.out.println(edge[1]);
    									}else {
    										node.edges.get(i)[1] = "edud";
    									}
    									if(c) {
    										System.out.println("cdud");
    										System.out.println("for edge: " + edge[0]);
    										System.out.println("may go to: "+ n.getTitle());
    									}else {
    										System.out.println("edud");
    										System.out.println("for edge: " + edge[0]);
    										System.out.println("should go to: "+ n.getTitle());
    									}
    									System.out.println("goes to " + title);
    									System.out.println("");
    								}
    									continue;
    								}
    								
    							}
    						}
    					}else if(edge[1].equals("e")) {
    						try {
    							String edgeid = edge[0];
    							Element edge1 = doc2.getElementById(edgeid);
    							String tagname = edge1.tag().toString();
    							List<DomElement> buttons = page2.getElementsByTagName(tagname);
    							String href = edge1.attr("href");
        						for(DomElement button : buttons) {
        							String comp = button.getAttribute("href");
        							if(href.equals(comp)) {
        								page = element.click();
        								HtmlPage htmlpage = button.click();
        								title = htmlpage.getTitleText();
        							}
        						}
    						}catch (Exception x) {
    							System.out.println(x);
    							title = "error";
    						}
    					}
    					if(edge[1].contentEquals("dud")) {
    						System.out.println("dud");
    						Node out = node.out.get(i);
    						System.out.println(edge[0]);
    						System.out.println(title);
    						System.out.println(out.getTitle());
    					}
    					else if(!title.equals(n.getTitle())) {
    						boolean c = false;
    						if(i < node.edges.size() - 1) {
    							String temp = node.edges.get(i+1)[1];
    							if(edge[1].contentEquals("bc")) { 
    								c = true;
    								node.edges.get(i)[1] = "cdud";
    							}else {
    								node.edges.get(i)[1] = "edud";
    							}
    							node.edges.get(i+1)[1] = temp;
    							//System.out.println(edge[1]);
    						}else {
    							node.edges.get(i)[1] = "edud";
    						}
    						if(c) {
    							System.out.println("cdud");
    							System.out.println("for edge: " + edge[0]);
    							System.out.println("may go to: "+ n.getTitle());
    						}else {
    							System.out.println("edud");
    							System.out.println("for edge: " + edge[0]);
    							System.out.println("should go to: "+ n.getTitle());
    						}
    						System.out.println("goes to " + title);
    						System.out.println("");
    					}else {
    						System.out.println("it works!");
    						System.out.println(edge[0]);
    						System.out.println(title);
    						System.out.println("");
    					}
    				}else {
    			        
    					if(edge[1].equals("dud")) {
    						System.out.println("dud");
    						Node out = node.out.get(i);
    						System.out.println(edge[0]);
    						System.out.println(title);
    						System.out.println(out.getTitle());
    						continue;
    					}else if(edge[1].contains("b")) {
    						String buttonid = edge[0];
    						try {
    							HtmlElement button = (HtmlElement) page2.getElementById(buttonid);
    							HtmlPage htmlPage = (HtmlPage) button.click();
    							title = htmlPage.getTitleText();
    							if(n.getDoc().equals(node.getDoc())) {
									if(checkReturn(htmlPage, node)) { 
									System.out.println("it works!");
									System.out.println(edge[0]);
									System.out.println(n.getTitle());
									System.out.println("");
								}else {
									boolean c = false;
									if(i < node.edges.size() - 1) {
										String temp = node.edges.get(i+1)[1];
										if(edge[1].contentEquals("bc")) { 
											c = true;
											node.edges.get(i)[1] = "cdud";
										}else {
											node.edges.get(i)[1] = "edud";
										}
										node.edges.get(i+1)[1] = temp;
										//System.out.println(edge[1]);
									}else {
										node.edges.get(i)[1] = "edud";
									}
									if(c) {
										System.out.println("cdud");
										System.out.println("for edge: " + edge[0]);
										System.out.println("may go to: "+ n.getTitle());
									}else {
										System.out.println("edud");
										System.out.println("for edge: " + edge[0]);
										System.out.println("should go to: "+ n.getTitle());
									}
									System.out.println("goes to " + title);
									System.out.println("");
								}
									continue;
								}
    							
    						}catch (Exception x) {
    							title = "error";
    						}
    					}else if(edge[1].equals("e")) {
    						try {
    							String eid = edge[0];
    							HtmlElement ed1 = (HtmlElement) page2.getElementById(eid);
    							HtmlPage htmlPage = (HtmlPage) ed1.click();
    							title = htmlPage.getTitleText();
    						}catch (Exception x) {
    							title = "error";
    						}
    					
    					}
    					
    					if(!title.equals(n.getTitle())) {
    						boolean c = false;
    						if(i < node.edges.size() - 1) {
    							String temp = node.edges.get(i+1)[1];
    							if(edge[1].contentEquals("bc")) { 
    								c = true;
    								node.edges.get(i)[1] = "cdud";
    							}else {
    								node.edges.get(i)[1] = "edud";
    							}
    							node.edges.get(i+1)[1] = temp;
    							//System.out.println(edge[1]);
    						}else {
    							node.edges.get(i)[1] = "edud";
    						}
    						if(c) {
    							System.out.println("cdud");
    							System.out.println("for edge: " + edge[0]);
    							System.out.println("may go to: "+ n.getTitle());
    						}else {
    							System.out.println("edud");
    							System.out.println("for edge: " + edge[0]);
    							System.out.println("should go to: "+ n.getTitle());
    						}
    						System.out.println("goes to " + title);
    						System.out.println("");
    					}else {
    						System.out.println("it works!");
    						System.out.println(edge[0]);
    						System.out.println(title);
    						System.out.println("");
    					}
    				}
    			}
    			break;
    		}
    	}
        webClient.close();
        System.out.println("--------------------------------------------------------------------");		
		for(int j = 0; j < node.out.size(); j++) {
          	Node n = node.out.get(j);
          	if(n.mymodal) {
          		graphtraversem(n, node.edges.get(j)[0]);
          	}else {	
	          	graphtraverse(n);
          	}
		}
        
	}*/
	
	static boolean checkModal(HtmlPage page, Node n) throws IOException {
		Document doc = Jsoup.parse(n.myfile, "utf-8", "www.example.com");
		 for(Element e : doc.getAllElements()) {
	    		if(e.hasClass("modal") && e.id().contentEquals(n.getTitle())) {
	    			for(int i = 0; i < n.edges.size(); i++) {
	    				String[] edge = n.edges.get(i);
	    				String edgeid = edge[0];
	    				boolean found = false;
	    				try {
	    					if(!edgeid.contains("team6")) {
	    						DomElement e1 = page.getElementById(edgeid);
	    						if(e1 == null) {
	    							return false;
	    						}
	    						continue;
	    					}
	    					Element edge1 = doc.getElementById(edgeid);
							String tagname = edge1.tag().toString();
							List<DomElement> buttons = page.getElementsByTagName(tagname);
							
							String href = edge1.attr("href");
							for(DomElement button : buttons) {
								String comp = button.getAttribute("href");
								if(href.equals(comp)) {
									found = true;
									break;
								}
							}
	    					if(!found) {
	    						return false;
	    					}
	    				}catch(Exception x) {
	    					System.out.println(x);
	    					return false;
	    				}
	    			}
	    		}
		 }
		
		return true;
	}
	
	static boolean checkReturn(HtmlPage page, Node n) throws IOException {
		boolean result = false;
		boolean allfound = true;
		Document doc = Jsoup.parse(n.myfile, "utf-8", "www.example.com");
		for(int i = 0; i < n.edges.size(); i++) {
			String[] edge = n.edges.get(i);
			String edgeid = edge[0];
			boolean found = false;
			try {
				if(!edgeid.contains("team6")) {
					DomElement e1 = page.getElementById(edgeid);
					if(e1 != null) {
						found = true;
					}
					return true;
				}
				Element edge1 = doc.getElementById(edgeid);
				
				String tagname = edge1.tag().toString();
				List<DomElement> buttons = page.getElementsByTagName(tagname);
				String href = edge1.attr("href");
				for(DomElement button : buttons) {
					String comp = button.getAttribute("href");
					if(href.equals(comp)) {
						found = true;
						break;
					}
					if(!found) {
						return true;
					}
				}
			}catch(Exception x) {
				System.out.println(x);
				return false;
			}
		}
		return false;
	}
	
	static void getFile(String root, List<Node> fsm) throws IOException {
		List<String[]> jses = getJS(fsm);
		for(String[] s : jses) {
			if(!s[0].contains("http")) {
				String FILE_URL = root + s[0];
				String FILE_NAME = "C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/" + s[1];
				readJSTester text = new readJSTester();
				text.downloadFileFromURL(FILE_URL, FILE_NAME);
			}else {
				String FILE_URL = s[0];
				String FILE_NAME = "C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/" + s[1];
				readJSTester text = new readJSTester();
				text.downloadFileFromURL(FILE_URL, FILE_NAME);
			}
		}
		
	}
	
	
	static List<String[]> getJS(List<Node> fsm) throws IOException {
		List<String[]> jses = new ArrayList<String[]>();
		for(Node node : fsm){
	        Document docFile = Jsoup.parse(node.myfile, "UTF-8", "http://example.com/");
			for(Element e : docFile.getAllElements()) {
				if(e.tagName().toLowerCase().equals("script")) {
					for(Attribute att : e.attributes().asList()) {
						if(att.getKey().contentEquals("src") && att.getValue().contains(".js")) {
							String[] dir = att.getValue().split("/");
							String js = dir[dir.length-1];
							e.attr("src", js);
							String[] temp = {att.getValue(), js};
							jses.add(temp);
						}
					}
				}
			}
			String test = docFile.toString();
			String filename = node.myfile.toPath().toString();
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		    writer.write(test);
			writer.close();
		}
		return jses;
	}
}