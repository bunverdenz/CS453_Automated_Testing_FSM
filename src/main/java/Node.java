import java.io.*;
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


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
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
   public int numin;
   public ArrayList<ArrayList> edges;
   public int startat;
   
   public static ArrayList<String> printGraphList;
   
   Node(String title, String doc, boolean b){
      out = new ArrayList<Node>();
      edges = new ArrayList<ArrayList>();
      printGraphList = new ArrayList<String>();
      
      this.title = title;
      this.mydoc = doc;
      this.mymodal = true;
      this.myfile = null;
      this.old = false;
      this.trav = false;
      this.numin = 0;
      this.startat = 0;
   }
   Node(String title, String doc){
      out = new ArrayList<Node>();
      edges = new ArrayList<ArrayList>();
      this.title = title;
      this.mydoc = doc;
      this.mymodal = false;
      this.myfile = null;
      this.old = false;
      this.trav = false;
      this.numin = 0;
      this.startat = 0;
      
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
      
      ArrayList<String[]> newedge = new ArrayList<String[]>();
      newedge.add(e);
      this.edges.add(newedge);
      
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

   static void printgraphstore(Node node) throws FileNotFoundException {
	   PrintWriter pw = new PrintWriter(Team6.resource_folder_path + "fsmDrawText.txt");
	   pw.close();
	   pw = new PrintWriter(Team6.resource_folder_path + "fsmDrawText.txt");
	   
	   printgraph(node, pw);
	   
	   pw.close();
   }
   
   static void printgraph(Node node, PrintWriter pw) {
      
      if(node.trav) {
         return;
      }
      System.out.print("\n");
      pw.print("\n");
      node.trav = true;
      System.out.println("from node: " + node.getTitle());
      pw.print("fn: " + node.getTitle() + "\n");
      int i = 0;
      for(Node n : node.out) {
         ArrayList<String[]> edges = node.edges.get(i);
         System.out.println("edge: " + edges.get(0)[0]);
         pw.print("ed: " + edges.get(0)[0] + "\n");
         System.out.println("to node: " + n.getTitle());
         pw.print("tn: " + n.getTitle() + "\n");
         i++;
      }
      for(Node n : node.out) {
         printgraph(n, pw);
      }
   }
   
   static void printgraphafterstore(Node node) throws FileNotFoundException {
	   PrintWriter pw2 = new PrintWriter(Team6.resource_folder_path + "fsmAfterText.txt");
	   pw2.close();
	   pw2 = new PrintWriter(Team6.resource_folder_path + "fsmAfterText.txt");
	   
	   printgraphafter(node, pw2);
	   
	   pw2.close();
   }
   
   static void printgraphafter(Node node, PrintWriter pw) {
	   if(node.trav) {
	         
	         return;
	      }
	      System.out.print("\n");
		  pw.print("\n");
	      node.trav = true;
	      System.out.println("from node: " + node.getTitle());
		  pw.print("fn: " + node.getTitle() + "\n");
	      int i = 0;
	      for(Node n : node.out) {
	         ArrayList<String[]> edges = node.edges.get(i);
	         if(edges.size() <= 1) {
	            edges.get(0)[1] = "dud";
	         }else {
	            if(edges.size() > 1 && edges.get(0)[1].contentEquals("dud")) {
	               if(edges.get(0)[0].contains("team6")) {
	                  edges.get(0)[1] = "e";
	               }else {
	                  edges.get(0)[1] = "b";
	               }
	            }
	         }
	         System.out.println("edge: " + edges.get(0)[0]);
			 pw.print("ed: " + edges.get(0)[0] + "\n");
	         System.out.println("edge: " + edges.get(0)[1]);
			 pw.print("edt: " + edges.get(0)[1] + "\n");
	         System.out.println("to node: " + n.getTitle());
			 pw.print("tn: " + n.getTitle() + "\n");
	         if(!edges.get(0)[1].contentEquals("dud") || edges.size() > 1) {
	            System.out.println("path");
				pw.print("new path!\n");
	             String[] edge = edges.get(1);
	               for(int j = 0; j < edge.length; j++) {
	                  System.out.println(edge[j]);
					  pw.print(edge[j] + "\n");
	               }
	         }
	         System.out.println("////////");
			 pw.print("////" + "\n");
	         i++;
	      }
	      for(Node n : node.out) {
	         printgraphafter(n, pw);
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
         ArrayList<String[]> edges = node.edges.get(i);
         printGraphList.add("ed: " + edges.get(0)[0]);
         printGraphList.add("tn: " + n.getTitle());
         i++;
      }
      for(Node n : node.out) {
         listPrintGraph(n);
      }
   }
   
   static void graphreset(Node node) {
      
      if(!node.trav) { //|| (node.getDoc().contentEquals(root.getDoc())) || (node.getTitle().contentEquals(root.getTitle()))) {
         return;
      }
      node.old = false;
      node.trav = false;
      for(Node n : node.out) {
         graphreset(n);
      }
      
   }
   
   static void prereset(Node node) {
      if(node.trav) { //|| (node.getDoc().contentEquals(root.getDoc())) || (node.getTitle().contentEquals(root.getTitle()))) {
         return;
      }
      node.trav = true;
      for(Node n : node.out) {
         prereset(n);
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
   
   static boolean checkhref(String href, String roothref, String nodehref) {
       boolean result = true;
       int slash = href.indexOf("/");
       if(href.equals("#") || href.contentEquals("")) {
          return true;
       }
       
       if(!(href.contains("/") || href.contains("html"))) {
          result = false;
       }
       
       if(href.contains("http") || href.contains("css") || href.contains("www.") || href.contains(".org") || href.contains(".com") || href.contains(".net") || href.contains(".jpg") || href.contains(".png")){
          if(href.contains(roothref)) {
             return true;
          }
          result = false;
          }else if(slash >= 0 && slash < 2) {
           return true;
        }else if(href.contains("html")) {
           return true;
        }else if(href.contentEquals("/")) {
           return true;
        }
        
       try {
           int dir = 0;
           System.out.println(nodehref);
             if(href.length() > 1 && href.charAt(0) == '.') {
                while(href.length() > 1 && href.charAt(0) == '.') {
                   href = href.substring(1);
                   dir++;
                }
             }
             if(href.length() > 1 && href.charAt(0) == '/') {
                href = href.substring(1);
             }
             
             String[] dirs = nodehref.split("/");
             int ind = 0;
             String trylink = "";
             for(String s : dirs) {
                trylink += s + "/";
                ind++;
                if(ind == dirs.length - dir + 1) {
                   System.out.println(dir);
                   System.out.println(trylink);
                   break;
                }
             }
             if(trylink.contains(href)) {
                return false;
             }
             trylink = trylink + href;
             System.out.println("try: " + trylink);
             Document doc2 = Jsoup.connect(trylink).get();
             return true;
       }catch(Exception y) {
             return false;
          }    
    }
   
   static void graphtraverse(Node root, Node node, HtmlPage p, ArrayList<String> path) throws IOException, InterruptedException{
         if(node.trav) {
            return;
         }
         ArrayList<String> temppath = new ArrayList<String>();
         for(String edge : path) {
            temppath.add(edge);
         }
         if(node.equals(root)) {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
               webClient.getOptions().setThrowExceptionOnScriptError(false);
              webClient.getOptions().setJavaScriptEnabled(true);
              p = webClient.getPage(node.getDoc());
         }
         System.out.println(p);
         node.trav = true;
         node.numin--;
         java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

           Document doc = Jsoup.parse(node.myfile, "utf-8", "www.example.com/");
           String url = node.getDoc();
           System.out.println(url);
         
         for(int i = node.startat; i < node.edges.size(); i++) {
            System.out.println(node.getTitle());
            path = new ArrayList<String>();
            for(String edge : temppath) {
               path.add(edge);
            }
            if(node.equals(root) || node.getDoc().contains("index.html")) {
               path = new ArrayList<String>();
            }
            
            boolean found = false;
            ArrayList<String[]> edges = node.edges.get(i);
            
            String[] edge = edges.get(0);
            Node n = node.out.get(i);
            
            path.add(edges.get(0)[0]);
           
            System.out.println(edge[0]);
            System.out.println(edge[1]);
            System.out.println(n.getTitle());
            
            String title = ""; 
            if(edge[0] != null && edge[0].contains("team6")) {
               
               if(edge[1].contains("b")) {
                  List<DomElement> buttons = p.getElementsByTagName("button");
                  Elements tagedges = doc.getElementsByTag("button");
                  String buttonid = edge[0];
                  Element button1 = doc.getElementById(buttonid);
                  String href = button1.attr("href");
                  int ind2 = edge[0].indexOf("_", 7);
                  String ind = edge[0].substring(ind2 + 1);
                  int index = Integer.parseInt(ind);
                  int compare = -1;
                  String compares = "";
                  for(Element tagedge : tagedges) {
                     if(tagedge.attr("id").contains("team6")) {
                        ind2 = tagedge.id().indexOf("_", 7);
                        compares = tagedge.id().substring(ind2 + 1); 
                        break;
                     }
                  }
                  if(!compares.equals("")) {
                     compare = Integer.parseInt(compares) - 1;
                  }
                  for(DomElement button : buttons) {
                     if(button.hasAttribute("onclick")) {
                        if(!edge[1].contentEquals("bh")) {
                           continue;
                        }else {
                           compare++;
                           
                        }
                     }
                     String comp = button.getAttribute("href");
                     if(checkhref(comp, root.getDoc(), node.getDoc()) && button.getId().contentEquals("")) {
                        compare++;
                     }
                     if(compare == index) {
                        HtmlPage htmlpage = button.click();
                        title = htmlpage.getTitleText();
                        if(n.getModal()) {
                           if(checkModal(htmlpage, n)) {
                              System.out.println("it works!");
                              String[] arraypath = convertarray(path);
                              node.edges.get(i).add(arraypath);
                              System.out.println(edge[0]);
                              System.out.println(n.getTitle());
                              System.out.println(n.equals(node));
                              System.out.println("");
                              graphtraverse(root, n, htmlpage, path);
                              found = true;
                              break;
                           }
                        }else if(node.getModal()) {
                           if(checkReturn(htmlpage, n)) {
                              System.out.println("it works!");
                              String[] arraypath = convertarray(path);
                              node.edges.get(i).add(arraypath);
                              System.out.println(edge[0]);
                              System.out.println(n.getTitle());
                              System.out.println("");
                              graphtraverse(root, n, htmlpage, path);
                              found = true;
                              break;
                           }
                        }
                        if(title.equals(n.getTitle())) {
                           System.out.println("it works!");
                           String[] arraypath = convertarray(path);
                           node.edges.get(i).add(arraypath);
                           System.out.println(edge[0]);
                           System.out.println(n.getTitle());
                           System.out.println("");
                           graphtraverse(root, n, htmlpage, path);
                           found = true;
                           continue;
                        }
                     }
                  }
               }else if(edge[1].equals("e")) {
                  //System.out.println(p);
                  //System.out.println("");
                  try {
                     Element edge1 = doc.getElementById(edge[0]);
                     String tagname = edge1.tag().toString();
                     Elements tagedges = doc.getElementsByTag(tagname);
                     String compares = "";
                    
                     List<DomElement> buttons = p.getElementsByTagName(tagname);
                     String href = edge1.attr("href");
                     int ind2 = edge[0].indexOf("_", 7) + 1;
                     String ind = edge[0].substring(ind2);
                     int index = Integer.parseInt(ind);
                     int compare = -1;
                     for(Element tagedge : tagedges) {
                        if(tagedge.attr("id").contains("team6")) {
                            ind2 = tagedge.id().indexOf("_", 7) + 1;
                            compares = tagedge.id().substring(ind2);
                           break;
                        }
                     }
                     if(!compares.equals("")) {
                        
                        compare = Integer.parseInt(compares) - 1;
                     }
                     for(DomElement button : buttons) {
                        String comp = button.getAttribute("href");
                        if(button.hasAttribute("onclick")) {
                           continue;
                        }
                        if(checkhref(comp, root.getDoc(), node.getDoc()) && button.getId().contentEquals("")) {
                           compare++;
                        }
                        if(compare == index) {
                           HtmlPage htmlpage = button.click();
                           title = htmlpage.getTitleText();
                           if(title.equals(n.getTitle())) {
                              System.out.println("it works!");
                              String[] arraypath = convertarray(path);
                              node.edges.get(i).add(arraypath);
                              System.out.println(edge[0]);
                              System.out.println(n.getTitle());
                              System.out.println("");
                              graphtraverse(root, n, htmlpage, path);
                              found = true;
                              break;
                           }else {
                              compare = -1;
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
                           String[] arraypath = convertarray(path);
                           node.edges.get(i).add(arraypath);
                           System.out.println(node.edges.get(i).size());
                           System.out.println(edge[0]);
                           System.out.println(n.getTitle());
                           System.out.println("");
                           graphtraverse(root, n, htmlPage, path);
                           found = true;
                           continue;
                        }
                           
                     }else if(node.getModal()) {
                        if(checkReturn(htmlPage, n)) {
                           System.out.println("it works!");
                           String[] arraypath = convertarray(path);
                           node.edges.get(i).add(arraypath);
                           System.out.println(edge[0]);
                           System.out.println(n.getTitle());
                           System.out.println("");
                           graphtraverse(root, n, htmlPage, path);
                           found = true;
                           continue;
                        }
                     
                     }else if(title.contentEquals(n.getTitle())){
                        System.out.println("it works!");
                        String[] arraypath = convertarray(path);
                        node.edges.get(i).add(arraypath);
                        System.out.println(edge[0]);
                        System.out.println(n.getTitle());
                        System.out.println("");
                        graphtraverse(root, n, htmlPage, path);
                        found = true;
                        continue;
                     }else {
                        WebClient webClient = new WebClient(BrowserVersion.CHROME);
                       webClient.getOptions().setThrowExceptionOnScriptError(false);
                       webClient.getOptions().setJavaScriptEnabled(true);
                       p = webClient.getPage(node.getDoc());
                       button = (HtmlElement) p.getElementById(buttonid);
                        htmlPage = (HtmlPage) button.click();
                        title = htmlPage.getTitleText();
                        if(title.contentEquals(n.getTitle())){
                              System.out.println("it works!");
                              String[] arraypath = convertarray(path);
                              node.edges.get(i).add(arraypath);
                              System.out.println(edge[0]);
                              System.out.println(n.getTitle());
                              System.out.println("");
                              graphtraverse(root, n, htmlPage, path);
                              found = true;
                              continue;
                        }
                     }
                     }catch (Exception e) {
                     //System.out.println(e);
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
                        String[] arraypath = convertarray(path);
                        node.edges.get(i).add(arraypath);
                        System.out.println(edge[0]);
                        System.out.println(n.getTitle());
                        System.out.println("");
                        graphtraverse(root, n, htmlPage, path);
                        found = true;
                        continue;
                     }
                  }catch (Exception e) {
                     title = "error";
                  }
               
               }
               
               
            }
             if(!found && !node.getDoc().contentEquals(n.getDoc())) {
                WebClient webClient = new WebClient(BrowserVersion.CHROME);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setJavaScriptEnabled(true);
                p = webClient.getPage(node.getDoc());
                
                if(node.numin > 0) {
                   System.out.println(node.numin);
                   node.trav = false;
                   node.startat = i;
                   path = new ArrayList<String>();
                  for(String edge1 : temppath) {
                     path.add(edge1);
                  }
                  if(node.equals(root) || node.getDoc().contains("index.html")) {
                     path = new ArrayList<String>();
                  }
                }
               //boolean c = false;
               if(node.numin <= 0 && !found) {
                 ArrayList<Object> newedge = checkloops(root, node, i, path);
                 boolean yesfound = (boolean) newedge.get(0);
                 if(yesfound) {
                    HtmlPage htmlPage = (HtmlPage) newedge.get(1);
                    String newedgefound = (String) newedge.get(2);
                    path.add(path.size()-1, newedgefound);
                    System.out.println("it works!");
                     String[] arraypath = convertarray(path);
                     node.edges.get(i).add(arraypath);
                     System.out.println(newedgefound);
                     System.out.println(n.getTitle());
                     System.out.println("");
                     graphtraverse(root, n, htmlPage, path);
                     found = true;
                     continue;
                 }
                 
                  if(i < node.edges.size() - 1 && node.edges.get(i).size() <= 1) {
                     ArrayList<String[]> edges1 = node.edges.get(i+1);
                     String temp = edges1.get(0)[1];
                     edges.get(0)[1] = "dud";
                     edges1.get(0)[1] = temp;
                  }else {
                     edges.get(0)[1] = "dud";
                  }
               }
               if(edge[1].contentEquals("bc")) {
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
         return;
       
      }
   
   static ArrayList<Object> checkloops(Node root, Node node, int i, ArrayList<String> path) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
      ArrayList<Object> loop = new ArrayList<Object>();
      ArrayList<String[]> testedgedetails = node.edges.get(i);
      String[] testedge = testedgedetails.get(0);
      Node outnode = node.out.get(i);
      for(ArrayList<String[]> edgedetails : node.edges) {
        String[] edge = edgedetails.get(0);
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
          webClient.getOptions().setThrowExceptionOnScriptError(false);
          webClient.getOptions().setJavaScriptEnabled(true);
          HtmlPage p = webClient.getPage(node.getDoc());
          Document doc = Jsoup.parse(node.myfile, "utf-8", "www.example.com/");
          if(edge[0] != null && edge[0].contains("team6")) { 
             if(edge[0].contains("team6") && edge[1].contains("b")) {
                  String buttonid = edge[0];
                  try {
                     HtmlElement button = (HtmlElement) p.getElementById(buttonid);
                     HtmlPage htmlPage = (HtmlPage) button.click();
                     if(htmlPage.getUrl().equals(p.getUrl())) {
                           Element edge1 = doc.getElementById(edge[0]);
                              String tagname = edge1.tag().toString();
                              ArrayList<Object> result = checkEdge(tagname, testedge, doc, htmlPage, root, node, outnode);
                              boolean cont = (boolean) result.get(0);
                              if(cont) {
                                 HtmlPage htmlpage = (HtmlPage) result.get(1);
                                 loop.add(cont);
                                 loop.add(htmlpage);
                                 loop.add(edge[0]);
                                 return loop;
                              }

                     }
                  }catch (Exception e) {
                   loop.add(false);
                    return loop;
                  }
            }  
          
               try {
                  Element edge1 = doc.getElementById(edge[0]);
                  String tagname = edge1.tag().toString();
                  ArrayList<Object> result = checkEdge(tagname, edge, doc, p, root, node, node);
                  boolean cont = (boolean) result.get(0);

                  if(cont) {
                      HtmlPage htmlpage = (HtmlPage) result.get(1);
                      edge1 = doc.getElementById(testedge[0]);
                        tagname = edge1.tag().toString();

                        result = checkEdge(tagname, testedge, doc, htmlpage, root, node, outnode);
                        cont = (boolean) result.get(0);
                        if(cont) {
                           htmlpage = (HtmlPage) result.get(1);
                           loop.add(cont);
                           loop.add(htmlpage);
                           loop.add(edge[0]);
                           return loop;
                        }
                     
                  }
               }catch (Exception e) {
                 loop.add(false);
                  return loop;
               }
              
         
          }
      }
      
      loop.add(false);
      return loop;
   }
   static String[] convertarray(ArrayList<String> path) {
      String[] result = new String[path.size()];
      int i = 0;
      for(String s : path) {
         result[i] = s;
         i++;
      }
      return result;
   }
   
   static ArrayList<Object> checkEdge(String tagname, String[] edge, Document doc, HtmlPage p, Node root, Node node, Node n) throws IOException {
      List<DomElement> buttons = p.getElementsByTagName(tagname);
       Elements tagedges = doc.getElementsByTag(tagname);
       String buttonid = edge[0];
       Element button1 = doc.getElementById(buttonid);
       String href = button1.attr("href");
       int ind2 = edge[0].indexOf("_", 7);
       String ind = edge[0].substring(ind2 + 1);
       int index = Integer.parseInt(ind);
       int compare = -1;
       String compares = "";
       ArrayList<Object> result = new ArrayList<Object>();
       for(Element tagedge : tagedges) {
          if(tagedge.attr("id").contains("team6")) {
             ind2 = tagedge.id().indexOf("_", 7);
             compares = tagedge.id().substring(ind2 + 1); 
             break;
          }
       }
       if(!compares.equals("")) {
          compare = Integer.parseInt(compares) - 1;
       }
       for(DomElement button : buttons) {
          if(button.hasAttribute("onclick")) {
             if(!edge[1].contentEquals("bh")) {
                continue;
             }else {
                compare++;
                
             }
          }
          String comp = button.getAttribute("href");
          if(checkhref(comp, root.getDoc(), node.getDoc()) && button.getId().contentEquals("")) {
             compare++;
          }
          if(compare == index) {
             HtmlPage htmlpage = button.click();
             String title = htmlpage.getTitleText();
             if(n.getModal()) {
                if(checkModal(htmlpage, n)) {
                   result.add(true);
                   result.add(htmlpage);
                   return result;
                }
             }else if(node.getModal()) {
                if(checkReturn(htmlpage, n)) {
                   result.add(true);
                   result.add(htmlpage);
                   return result;
                }
             }
             if(title.equals(n.getTitle())) {
               result.add(true);
                result.add(htmlpage);
                return result;
             }
          }
       }
          result.add(false);
         return result;
   }
   static boolean checkModal(HtmlPage page, Node n) throws IOException {
      Document doc = Jsoup.parse(n.myfile, "utf-8", "www.example.com");
      boolean found = false;
       for(Element e : doc.getAllElements()) {
             if(e.hasClass("modal") && e.id().contentEquals(n.getTitle())) {
                for(int i = 0; i < n.edges.size(); i++) {
                   ArrayList<String[]> edges = n.edges.get(i);
                   String[] edge = edges.get(0);
                   String edgeid = edge[0];
                   found = false;
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
      return found;
   }
   
   static boolean checkReturn(HtmlPage page, Node n) throws IOException {
      boolean result = false;
      boolean allfound = true;
      Document doc = Jsoup.parse(n.myfile, "utf-8", "www.example.com");
      for(int i = 0; i < n.edges.size(); i++) {
         ArrayList<String[]> edges = n.edges.get(i);
         String[] edge = edges.get(0);
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
            String FILE_NAME = Team6.resource_folder_path + s[1];
            readJSTester text = new readJSTester();
            text.downloadFileFromURL(FILE_URL, FILE_NAME);
         }else {
            String FILE_URL = s[0];
            String FILE_NAME = Team6.resource_folder_path + s[1];
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
