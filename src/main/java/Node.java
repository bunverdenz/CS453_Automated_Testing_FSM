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
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.URL;

public class Node{
	
	private String title;
	public Document mydoc;
	public Element mymodal;
	public File myfile;
	public ArrayList<Node> out;
	public boolean old;
	public boolean trav;
	public ArrayList<String[]> edges;
	
	Node(String title, Element modal, Document doc){
		out = new ArrayList<Node>();
		edges = new ArrayList<String[]>();
		this.title = title;
		this.mydoc = doc;
		this.mymodal = modal;
		this.myfile = null;
		this.old = false;
		this.trav = false;
	}
	Node(String title, Document doc){
		out = new ArrayList<Node>();
		edges = new ArrayList<String[]>();
		this.title = title;
		this.mydoc = doc;
		this.mymodal = null;
		this.myfile = null;
		this.old = false;
		this.trav = false;
		
	}
	
	void setFile(File file) {
		this.myfile = file;
	}
	Element getModal() {
		return mymodal;
	}
	
	String getTitle() {
		return title;
	}
	
	Document getDoc() {
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
		node.trav = true;
		System.out.println("from node: " + node.getTitle());
		
		for(Node n : node.out) {
			System.out.println("to node: " + n.getTitle());
		}
		for(Node n : node.out) {
			
			printgraph(n);
		}
	}
	
	static void graphreset(Node node) {
		if(!node.old && !node.trav) {
			return;
		}
		node.old = false;
		node.trav = false;
		for(Node n : node.out) {
			graphreset(n);
		}
	}
	
	static void graphtraverse(Node node) throws IOException{
		if(node.trav) {
			return;
		}
		node.trav = true;
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);
      	webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        final WebClient webClient2 = new WebClient(BrowserVersion.CHROME);
        webClient2.getOptions().setThrowExceptionOnScriptError(false);
        webClient2.getOptions().setJavaScriptEnabled(true);
        HtmlPage page, page2;
        java.net.URL url = node.myfile.toURI().toURL();
        String url2 = node.getDoc().location();
    	page = webClient.getPage(url);
    	page2 = webClient2.getPage(url2);
        
		int i = 0;
		System.out.println("for node: " + node.getTitle());
		for(String[] edge : node.edges) {
			Node n = node.out.get(i);
			String title = ""; 
			if(edge[0] != null && edge[0].contains("team6")) {
				page = webClient.getPage(url);
				if(edge[1].equals("b")) {
					String buttonid = edge[0];
					HtmlButton button = (HtmlButton) page.getElementById(buttonid);
					HtmlPage htmlPage = (HtmlPage) button.click();
					title = htmlPage.getTitleText();
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
					edge[1] = "dud";
					System.out.println("edud");
					System.out.println(edge[0]);
					System.out.println(n.getTitle());
					System.out.println(title);
					System.out.println("");
				}else {
					System.out.println(edge[0]);
					System.out.println(title);
					System.out.println("");
				}
			}else {
				if(edge[1].equals("dud")) {
					System.out.println("mdud");
					System.out.println(edge[0]);
					continue;
				}else if(edge[1].equals("b")) {
					String buttonid = edge[0];
					System.out.println(page2);
					try {
						HtmlButton button = (HtmlButton) page2.getElementById(buttonid);
						HtmlPage htmlPage = (HtmlPage) button.click();
						System.out.println(button);
						title = htmlPage.getTitleText();
					}catch (Exception e) {
						title = "error";
					}
				}else if(edge[1].equals("e")) {
					try {
						String eid = edge[0];
						HtmlElement element = (HtmlElement) page2.getElementById(eid);
						HtmlPage htmlPage = (HtmlPage) element.click();
						title = htmlPage.getTitleText();
					}catch (Exception e) {
						title = "error";
					}
				}
				
				if(!title.equals(n.getTitle())) {
					edge[1] = "dud";
					System.out.println("edud");
					System.out.println(edge[0]);
					System.out.println(n.getTitle());
					System.out.println(title);
					System.out.println("");
				}else {
					System.out.println(edge[0]);
					System.out.println(title);
					System.out.println("");
				}
			}
			
			i++;
		}
		System.out.println("");
		System.out.println("");
		webClient.close();
		webClient2.close();
		for(Node n : node.out) {
			graphtraverse(n);
		}
	}
	
	static void getFile(String root, List<Node> fsm) throws IOException {
		List<String[]> jses = getJS(fsm);
		for(String[] s : jses) {
			if(s[0].length() > 7 && s[0].substring(0, 7).contentEquals("scripts")) {
				String FILE_URL = root + s[0];
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