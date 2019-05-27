import java.io.IOException;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Node{
	
	private String title;
	private Document mydoc;
	private Element mymodal;
	public ArrayList<Node> out;
	public boolean old;
	public boolean trav;
	public ArrayList<Object[]> edges;
	
	Node(String title, Element modal){
		out = new ArrayList<Node>();
		edges = new ArrayList<Object[]>();
		this.title = title;
		this.mydoc = null;
		this.mymodal = modal;
		this.old = false;
		this.trav = false;
	}
	Node(String title, Document doc){
		out = new ArrayList<Node>();
		edges = new ArrayList<Object[]>();
		this.title = title;
		this.mydoc = doc;
		this.mymodal = null;
		this.old = false;
		this.trav = false;
		
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
	
	void addToEdge(Object[] e) {
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
		for(Object[] edge : node.edges) {
			if(edge != null) {
				if(edge[1].equals("b")) {
					HtmlButton button = (HtmlButton) edge[0];
					HtmlPage htmlPage = (HtmlPage) button.click();
					String title = htmlPage.getTitleText();
					System.out.println(title);
				}else if(edge[1].equals("e")) {
					HtmlElement href = (HtmlElement) edge[0];
                    HtmlPage htmlPage = (HtmlPage)href.click();
                    String title = htmlPage.getTitleText();
					System.out.println(title);
				}
			}
			
		}
		for(Node n : node.out) {
			graphtraverse(n);
		}
	}
}