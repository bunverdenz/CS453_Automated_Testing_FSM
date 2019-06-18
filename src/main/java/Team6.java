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

import java.io.*;
import java.net.URL;
import java.util.*;

// TODO: create new file/class for each function to organize the whole project
public class Team6 {
    static List<String> tags = new ArrayList<String>(); //record tags
    static List<String> attrs = new ArrayList<String>(); //record attributes
    static List<Element> elements = new ArrayList<Element>();
    static List<Node> fsm = new ArrayList<Node>();
    
    static ArrayList<String> drawStringList;
    
    public static String resource_folder_path = "C:/Users/aerol/OneDrive/Desktop/CS453_Automated_Testing_FSM/src/main/resources/";
    
    static String root = "";
    static String baseid = "team6_";
    static int nodeid = 0;
    
    public static boolean done = false;
    
    public static void main(String[] args) throws IOException, InterruptedException {
    	mainFunc("", "", "", "", "use");
    }
    public static void mainFunc(String rootin, String idin, String pwin, String loginPagein, String console) throws IOException, InterruptedException{
    	String use_console = console;
    	
    	root = rootin;
    	String id = idin;
    	String pwd = pwin;
    	String loginpage = loginPagein;
    	
    	boolean auth = false;
    	if(console.contentEquals("use")) {
    		System.out.println("Please enter the address of your homepage.");
            Scanner scanner = new Scanner(System.in);
            root = scanner.nextLine();

            System.out.println("Do you have any log-in information?");
            String Y = scanner.nextLine();
            
            id = "";
            pwd = "";
            loginpage = "";
            if(Y.contentEquals("Y")) {
               System.out.println("Please enter the address of the login page.");
               loginpage = scanner.nextLine();
               System.out.println("Please enter your username.");
               id = scanner.nextLine();
               System.out.println("Please enter your password.");
               pwd = scanner.nextLine();
               auth = true;
            }
    	}
    	
        
         // Reference for events: https://www.w3schools.com/tags/ref_eventattributes.asp

         final Collection<String> EVENTS = Arrays.asList(
                 "onchange","onfocus","onselect","onsubmit",
                 "onclick", "onmouseover", "ondbclick", "onwheel",
                 "onkeydown", "onkeypress", "onkeyup",
                 "ondrag","ondragend","ondrop","onscroll","ondragstart",
                 "onload","alert");
         
         Document doc = (Document) Jsoup.connect(root).get();
         Node home = new Node(doc.title(), doc.location());
         fsm.add(home);
         ArrayList<ArrayList> result = preprocess(doc);
         ArrayList<String> elements = result.get(0);
         ArrayList<ArrayList> edgeelements = result.get(1);
         System.out.println(home.old);
         java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
       
         
         addHTML(home);
         WebClient webClient = new WebClient(BrowserVersion.CHROME);
          webClient.getOptions().setThrowExceptionOnScriptError(false);
         webClient.getOptions().setJavaScriptEnabled(true);
         String url = home.getDoc(); 
         
         
         
         Node.printgraphstore(home);
         
         Node.listPrintGraph(home);
         
         Node.graphreset(home);
         HtmlPage page = webClient.getPage(root);
         if(auth) {
            page = webClient.getPage(loginpage);
            page = loginTest.processLogin(id, pwd, page);
         }
         ArrayList<String> path = new ArrayList<String>();
         Node.graphtraverse(home, home, page, path);
         webClient.close();
         
         Node.prereset(home);
         Node.graphreset(home);
         Node.printgraphafterstore(home);
         
         Node.prereset(home);
         Node.graphreset(home);
         
         
         PrintWriter pw2 = new PrintWriter(resource_folder_path + "fsmPathDrawText.txt");
  	     pw2.close();
  	     pw2 = new PrintWriter(resource_folder_path + "fsmPathDrawText.txt");
  	     
  	     
  	     
  	     
         int everyother = 0;
         ArrayList<ArrayList<ArrayList<String>>> population = GA_Search.ga_search(home);
         for(ArrayList<ArrayList<String>> a : population) {
            for(ArrayList<String> b : a) {
         	   everyother++;
         	   if(everyother % 2 == 0) {
         		   System.out.println("fedges");
             	   pw2.print("\n");
             	   pw2.print("fedges\n");
         	   }else {
         		   System.out.println("sedges");
             	   pw2.print("\n");
             	   pw2.print("sedges\n");
         	   }
         	   
                for(String s : b) {
             	   System.out.println(s);
                    pw2.print(s + "\n");
                }
            }
         }
         //Greedy.greedy(home, fsm);
         pw2.close();
         
         done = true;
         
         //java.lang.System.exit(0);
    }
    
    static <E> ArrayList<ArrayList> preprocess(Document doc) throws IOException, InterruptedException {
       List<String> modals = new ArrayList<String>();
       List<String> jses = new ArrayList<String>();
       List<String[]> htmlactions = new ArrayList<String[]>();
       List<ArrayList> functions = new ArrayList<ArrayList>();
       ArrayList<ArrayList> elements = new ArrayList<ArrayList>();
       ArrayList<String[]> buttons1 = new ArrayList<String[]>();
       ArrayList<String[]> buttons = new ArrayList<String[]>();
       int num = 0;
       //functions = jstester.preprocessdoc(doc);
       for(Element e : doc.getAllElements()){
          
          if(e.hasClass("modal")) {
             String title = e.id();
             modals.add(title);
          }
          if(e.hasAttr("onclick")) {
             String func = e.attr("onclick");
             if(func.contains("(")) {
                if(func.contains("(")) {
                   int paren = func.indexOf("(");
                   if(paren > 0) {
                      func = func.substring(0, paren);          
                   }
                }
             }else if(func.contains("location.href")) {
                if(func.contains("=")) {
                     int eq = func.indexOf('=');
                     int semi = func.indexOf(';', eq);
                    if(eq > 0) {
                             if(semi > eq) {
                                func = func.substring(eq + 1, semi);
                             }else {
                                func = func.substring(eq + 1);
                             }
                     }
                }
             }
             if(e.id() != "") {
                String[] temp = {e.id(),func};
                buttons1.add(temp);
             }else {
                String[] temp = {"" + num,func};
                buttons1.add(temp);
                num++;
             }
             
          }
          
          
          for(Attribute att : e.attributes().asList()) {
             if(att.getKey().contentEquals("src") && att.getValue().contains(".js")) {
                if(att.getValue().contains("http")) {
                   jses.add(att.getValue());
                   continue;
                }
                String temp = att.getValue();
                if(temp.length() > 1 && temp.charAt(0) == '/') {
                   temp = temp.substring(1);
                   if(temp.length() > 1 && temp.charAt(0) == '/') {
                      temp = temp.substring(1);
                   }
                }
                jses.add(root + temp);
             }
          }
       }
       
       
       for(String js : jses) {
          ArrayList<ArrayList> result = readJSTester.getJSInfo(js, resource_folder_path + "output.txt", modals);
          
          //System.out.println(js);
          buttons.addAll(result.get(0));
          elements.addAll(result.get(1));
       }
       ArrayList<ArrayList> result1 = new ArrayList<ArrayList>();
       result1.add(buttons);
       result1.add(elements);
       ArrayList<ArrayList> result2 = jstester.addEdgesHTML(buttons1, functions);
       
       ArrayList<ArrayList> result3 = new ArrayList<ArrayList>();
       result3.add(result1);
       
       result3.add(result2);
       return result3;
    }
    
    static String processhref(String href) {
       String result = href;
       int slash = href.indexOf("/");
       if(!(href.contains("/") || href.contains("html"))) {
          return "-1";
       }
       
       if(href.contains("http") || href.contains("css") || href.contains("www.") || href.contains(".org") || href.contains(".com") || href.contains(".net") || href.contains(".jpg") || href.contains(".png")){
          if(href.contains(root)) {
             return result;
          }
          return "-1";
       }else if(slash >= 0 && slash < 2) {
          result = root + href.substring(slash + 1);
       }else if(href.contains("html")) {
          result = root + href;
       }else if(href.contentEquals("/")) {
          result = root;
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
              boolean found = false;
              if(arr[1].contains("href")) {
                 if(arr[1].contentEquals("hrefc")){
                    edge[1] = "bc";
                 }
                 String alink = arr[0].substring(1, arr[0].length()-1);
                 String blink = alink;
                 if(alink.length() > 1 && alink.charAt(0) == '/') {
                    alink = alink.substring(1);
                    if(alink.length() > 1 && alink.charAt(0) == '/') {
                        alink = alink.substring(1);
                    }
                 }
                 
                 
                 String link = processhref(blink);
                 if(link.contentEquals("-1")) {
                    if(alink.contentEquals("#") || alink.contentEquals("")) {
                       link = home.getDoc();
                    }else {
                       continue;
                    }
                    
                 }
                 Document doc2; 
                 Node out;
                 try {
                   System.out.println(link);
                    doc2 = Jsoup.connect(link).get();
                    out = new Node(doc2.title(), link);
                 }catch(Exception x) {
                   try {
                    int dir = 0;
                     if(alink.length() > 1 && alink.charAt(0) == '.') {
                        while(alink.length() > 1 && alink.charAt(0) == '.') {
                           alink = alink.substring(1);
                           dir++;
                        }
                     }
                     if(alink.length() > 1 && alink.charAt(0) == '/') {
                        alink = alink.substring(1);
                     }
                     String docurl = home.getDoc();
                     String[] dirs = docurl.split("/");
                     int ind = 0;
                     String trylink = "";
                     for(String s : dirs) {
                        trylink += s + "/";
                        ind++;
                        if(ind == dirs.length - dir + 1) {
                           break;
                        }
                        
                     }
                     if(trylink.contains(alink)) {
                        continue;
                     }
                     trylink = trylink + alink;
                     System.out.println("try: " + trylink);
                     doc2 = Jsoup.connect(trylink).get();
                     out = new Node(doc2.title(), trylink);
                   }catch(Exception y) {
                      continue;
                   }    
                 }
                for(Node node : fsm) {
                     if(node.getDoc().contentEquals(out.getDoc())) {
                        out = node;
                        node.numin++;
                        add = false;
                        break;
                     }
                 }
                int n = 0;
                ArrayList<String[]> edges = new ArrayList<String[]>();
                if(home.edges.size() > 0) {
                   edges = home.edges.get(0);
                }
                for(int j = 0; j < edges.size(); j++) {
                     String[] edge1 = edges.get(j);

                      Node node = home.out.get(n);
                      if(edge1[0].equals(button) &&node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         found = true;
                      }
                      n++;
                   }
                  if(found) {
                      continue;
                   }
                home.addToOut(out);
                home.addToEdge(edge);
                System.out.println("adding now to: " + doc.title() + " edge: " + edge[0] + " to node : " + out.getDoc());
                 if(add) {
                    fsm.add(out);
                 }
              }else if(arr[1].contains("show")){
                 if(arr[1].contentEquals("showc")){
                    edge[1] = "bc";
                 }
                 System.out.println(arr[0]);
                 Node out = new Node(arr[0], doc.location(), true);
                
                for(Node node : fsm) {
                     if(node.getTitle().contentEquals(out.getTitle())) {
                        out = node;
                        node.numin++;
                        add = false;
                        break;
                     }
                 }
                int n = 0;
                ArrayList<String[]> edges = new ArrayList<String[]>();
                if(home.edges.size() > 0) {
                   edges = home.edges.get(0);
                }
                for(int j = 0; j < edges.size(); j++) {
                     String[] edge1 = edges.get(j);
                      Node node = home.out.get(n);
                      if(edge1[0].equals(button) && node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         found = true;
                      }
                      n++;
                   }
                  if(found) {
                      continue;
                   }
                 home.addToOut(out);
                 home.addToEdge(edge);
                 System.out.println("adding now to: " + doc.title() + " edge: " + edge[0] + " to node : " + out.getTitle());
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
           
           
           for(int j = 0; j < edgeelement.size(); j++) {
              boolean add = true;
              String[] arr = edgeelement.get(j);
              boolean found = false;
              if(arr[1].contains("href")) {
                 if(arr[1].contentEquals("hrefc")){
                    edge[1] = "bc";
                 }
                 String alink = arr[0].substring(1, arr[0].length()-1);
                 String blink = alink;
                 if(alink.length() > 1 && alink.charAt(0) == '/') {
                    alink = alink.substring(1);
                    if(alink.length() > 1 && alink.charAt(0) == '/') {
                        alink = alink.substring(1);
                    }
                 }
                 
                 
                 String link = processhref(blink);
                 if(link.contentEquals("-1")) {
                    if(alink.contentEquals("#") || alink.contentEquals("")) {
                       link = home.getDoc();
                    }else {
                       continue;
                    }
                    
                 }
                 Document doc2; 
                 Node out;
                 try {
                    doc2 = Jsoup.connect(link).get();
                    out = new Node(doc2.title(), link);
                 }catch(Exception x) {
                   try {
                      String trylink = home.getDoc() + alink;
                      System.out.println(trylink);
                      doc2 = Jsoup.connect(trylink).get();
                      out = new Node(doc2.title(), trylink);
                   }catch(Exception y) {
                      continue;
                   }    
                 }
                            
                for(Node node : fsm) {
                     if(node.getDoc().contentEquals(out.getDoc())) {
                        out = node;
                        node.numin++;
                        add = false;
                        break;
                     }
                 }
                int n = 0;
                ArrayList<String[]> edges = new ArrayList<String[]>();
                if(home.edges.size() > 0) {
                   edges = home.edges.get(0);
                }
                for(int k = 0; k < edges.size(); k++) {
                     String[] edge1 = edges.get(k);
                      Node node = home.out.get(n);
                      if(edge1[0].equals(button) && node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         found = true;
                      }
                      n++;
                   }
                  if(found) {
                      continue;
                   }
                home.addToOut(out);
                home.addToEdge(edge);
                System.out.println("adding now to: " + home.getTitle() + " edge: " + edge[0] + " to node : " + out.getDoc());    
                 if(add) {
                    fsm.add(out);
                 }
              }else if(arr[1].contains("hide")){
                 if(arr[1].contentEquals("hidec")){
                    edge[1] = "bc";
                 }
                 Node out = new Node(doc.title(), doc.location(), true);
                
                for(Node node : fsm) {
                     if(node.getDoc().contentEquals(out.getDoc())) {
                        out = node;
                        node.numin++;
                        add = false;
                        break;
                     }
                 }
                int n = 0;
                ArrayList<String[]> edges = new ArrayList<String[]>();
                if(home.edges.size() > 0) {
                   edges = home.edges.get(0);
                }
                for(int k = 0; k < edges.size(); k++) {
                     String[] edge1 = edges.get(k);
                      Node node = home.out.get(n);
                      if(edge1[0].equals(button) && node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         found = true;
                      }
                      n++;
                   }
                  if(found) {
                      continue;
                   }
                  System.out.println("adding now to: " + home.getTitle() + " edge: " + edge[0] + " to node : " + out.getDoc());
                home.addToOut(out);
                 home.addToEdge(edge);
                 
                 if(add) {
                    fsm.add(out);
                 }
              }
           }
        }
    }
    
    static void getHTMLElements(Document doc, ArrayList<String> elements, ArrayList<String> compare, ArrayList<ArrayList> edgeelements, Node home) throws IOException {
       for(int i = 0; i < elements.size(); i++) {
           String button = elements.get(i);
           String[] edge = {button, "bh"};
           if(edgeelements.size() == 0) {
              return;
           }
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
           
           
           for(int j = 0; j < edgeelement.size(); j++) {
              boolean add = true;
              String[] arr = edgeelement.get(j);
              boolean found = false;
              if(arr[1].contains("href")) {
                    String alink = arr[0].substring(1, arr[0].length()-1);
                    String blink = alink;
                    if(alink.length() > 1 && alink.charAt(0) == '/') {
                       alink = alink.substring(1);
                       if(alink.length() > 1 && alink.charAt(0) == '/') {
                           alink = alink.substring(1);
                       }
                    }
                    
                    
                    String link = processhref(blink);
                    if(link.contentEquals("-1")) {
                       if(alink.contentEquals("#") || alink.contentEquals("")) {
                          link = home.getDoc();
                       }else {
                          continue;
                       }
                       
                    }
                    Document doc2; 
                    Node out;
                    try {
                       doc2 = Jsoup.connect(link).get();
                       out = new Node(doc2.title(), link);
                    }catch(Exception x) {
                      try {
                        int dir = 0;
                          if(alink.length() > 1 && alink.charAt(0) == '.') {
                             while(alink.length() > 1 && alink.charAt(0) == '.') {
                                alink = alink.substring(1);
                                dir++;
                             }
                          }
                          if(alink.length() > 1 && alink.charAt(0) == '/') {
                             alink = alink.substring(1);
                          }
                          String docurl = home.getDoc();
                          String[] dirs = docurl.split("/");
                          int ind = 0;
                          String trylink = "";
                          for(String s : dirs) {
                             trylink += s + "/";
                             ind++;
                             if(ind == dirs.length - dir + 1) {
                                break;
                             }
                             
                          }
                          if(trylink.contains(alink)) {
                             continue;
                          }
                          trylink = trylink + alink;
                          System.out.println("try: " + trylink);
                          doc2 = Jsoup.connect(trylink).get();
                          out = new Node(doc2.title(), trylink);
                      }catch(Exception y) {
                         continue;
                      }    
                    }
                           
                for(Node node : fsm) {
                     if(node.getDoc().contentEquals(out.getDoc())) {
                        out = node;
                        node.numin++;
                        add = false;
                        break;
                     }
                 }
                int n = 0;
                ArrayList<String[]> edges = new ArrayList<String[]>();
                if(home.edges.size() > 0) {
                   edges = home.edges.get(0);
                }
                for(int k = 0; k < edges.size(); k++) {
                     String[] edge1 = edges.get(k);
                      Node node = home.out.get(n);
                      if(edge1[0].equals(button) &&node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         found = true;
                      }
                      n++;
                   }
                  if(found) {
                      continue;
                   }
                home.addToOut(out);
                home.addToEdge(edge);
                System.out.println("adding now to: " + home.getTitle() + " edge: " + edge[0] + " to node : " + out.getDoc());    
                 if(add) {
                    fsm.add(out);
                 }
              }else if(home.getModal() && arr[1].contains("show")){
                 
                 Node out = new Node(doc.title(), doc.location(), true);
                
                for(Node node : fsm) {
                     if(node.getDoc().contentEquals(out.getDoc())) {
                        out = node;
                        node.numin++;
                        add = false;
                        break;
                     }
                 }
                int n = 0;
                ArrayList<String[]> edges = new ArrayList<String[]>();
                if(home.edges.size() > 0) {
                   edges = home.edges.get(0);
                }
                for(int k = 0; k < edges.size(); k++) {
                     String[] edge1 = edges.get(k);
                      
                      Node node = home.out.get(n);
                      if(edge1[0].equals(button) && node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         found = true;
                      }
                      n++;
                   }
                  if(found) {
                      continue;
                   }
                  System.out.println("adding now to: " + home.getTitle() + " edge: " + edge[0] + " to node : " + out.getDoc());
                home.addToOut(out);
                 home.addToEdge(edge);
                 
                 if(add) {
                    fsm.add(out);
                 }
              }else if(!home.getModal() && arr[1].contains("show")){
                 Node out = new Node(arr[0], doc.location(), true);
                
                for(Node node : fsm) {
                     if(node.getDoc().contentEquals(out.getDoc())) {
                        out = node;
                        node.numin++;
                        add = false;
                        break;
                     }
                 }
                int n = 0;
                ArrayList<String[]> edges = new ArrayList<String[]>();
                if(home.edges.size() > 0) {
                   edges = home.edges.get(0);
                }
                for(int k = 0; k < edges.size(); k++) {
                     String[] edge1 = edges.get(k);
                      Node node = home.out.get(n);
                      if(edge1[0].equals(button) && node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         found = true;
                      }
                      n++;
                   }
                  if(found) {
                      continue;
                   }
                home.addToOut(out);
                 home.addToEdge(edge);
                 System.out.println("adding now to: " + doc.title() + " edge: " + edge[0] + " to node : " + out.getDoc());
                 if(add) {
                    fsm.add(out);
                 }
              }
           }
        }
    }
    
    static void addHTML(Node node) throws IOException, InterruptedException {
       if(node.old) {
            return;
         }
       node.old = true;
       int countid = 0;
       System.out.println("");
       System.out.println(node.getDoc());
         String url = node.getDoc();
       Document doc = Jsoup.connect(url).get();
        ArrayList<ArrayList> result3 = preprocess(doc);
        ArrayList<ArrayList> result = result3.get(0);
        ArrayList<String> elements = result.get(0);
        ArrayList<ArrayList> edgeelements = result.get(1);
        ArrayList<ArrayList> result2 = result3.get(1);
        ArrayList<String> elements1 = result2.get(0);
        ArrayList<ArrayList> edgeelements1 = result2.get(1);
        ArrayList<String> compare = new ArrayList<String>();
        
        int premod = 0;
        for(Element e : doc.getAllElements()) {
           if(premod > 0) {
              premod--;
              continue;
           }
           if(e.hasClass("modal")) {
               Elements modale = e.getAllElements();
               premod = modale.size();
               continue;
            }
           if(!e.id().equals("")) {
              compare.add(e.id());
           }
        }
        getJSElements(doc, elements, compare, edgeelements, node);
        getHTMLElements(doc, elements1, compare, edgeelements1, node);
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
           if(outputname.equals("")) {
               outputname = temp[temp.length-1];
            }
        }
        if(outputname.contains("?")) {
           int ind = outputname.indexOf('?');
           outputname = outputname.substring(0, ind) + nodeid;
        }
        
        String fileName = resource_folder_path + "test" + nodeid + ".html";
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
                   String alink = e.attr("href");
                   String blink = e.attr("href");
                   if(alink.length() > 1 && alink.charAt(0) == '\"') {
                      alink = alink.substring(1, alink.length()-1);
                      blink = alink;
                   }
                   if(alink.length() > 1 && alink.charAt(0) == '/') {
                      alink = alink.substring(1);
                      if(alink.length() > 1 && alink.charAt(0) == '/') {
                          alink = alink.substring(1);
                      }
                   }
                   
                   
                   String link = processhref(blink);
                   if(link.contentEquals("-1")) {
                      if(alink.contentEquals("#") || alink.contentEquals("")) {
                         link = node.getDoc();
                      }else {
                         continue;
                      }
                      
                   }
                   if(e.id().contentEquals("")) {
                      e.attr("id",baseid + "" + nodeid + "_" + countid);
                      countid++;
                  }
                   
                   Document doc2; 
                   Node out;
                   try {
                     System.out.println(link);
                      doc2 = Jsoup.connect(link).get();
                      out = new Node(doc2.title(), link);
                   }catch(Exception x) {
                     try {
                        int dir = 0;
                        if(alink.length() > 1 && alink.charAt(0) == '.') {
                           while(alink.length() > 1 && alink.charAt(0) == '.') {
                              alink = alink.substring(1);
                              dir++;
                           }
                        }
                        if(alink.length() > 1 && alink.charAt(0) == '/') {
                           alink = alink.substring(1);
                        }
                        String docurl = node.getDoc();
                        String[] dirs = docurl.split("/");
                        int ind = 0;
                        String trylink = "";
                        for(String s : dirs) {
                           trylink += s + "/";
                           ind++;
                           if(ind == dirs.length - dir + 1) {
                              break;
                           }
                           
                        }
                        if(trylink.contains(alink)) {
                           continue;
                        }
                        trylink = trylink + alink;
                        System.out.println("try: " + trylink);
                        doc2 = Jsoup.connect(trylink).get();
                        out = new Node(doc2.title(), trylink);
                     }catch(Exception y) {
                        countid--;
                        continue;
                     }    
                   }
                   boolean add = true;
                    
                    for(Node neighbors : fsm) {
                     if(neighbors.getDoc().contentEquals(out.getDoc()) || neighbors.getDoc().substring(0, neighbors.getDoc().length()-1).contentEquals(out.getDoc()) || out.getDoc().substring(0, out.getDoc().length()-1).contentEquals(neighbors.getDoc())) {
                        out = neighbors;
                        add = false;
                     }
                  }
                    int n = 0;
                    ArrayList<String[]> edges = new ArrayList<String[]>();
                    if(node.edges.size() > 0) {
                       edges = node.edges.get(0);
                    }
                    for(int j = 0; j < edges.size(); j++) {
                         String[] edge1 = edges.get(j);
                      Node node1 = node.out.get(n);
                      if(edge1[0].equals(e.id())) {
                         dont = true;
                      }
                      n++;
                   }
                  if(!dont) {
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
                     node.addToOut(out);
                     node.addToEdge(edge);
                     System.out.println("adding now to " + node.getTitle() + " edge: " + edge[0] + " to node: " + out.getDoc());
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
    static void addModal(Node node) throws IOException, InterruptedException {
       if(node.old) {
          return;
       }
       int countid = 0;
       System.out.println("");
       node.old = true;
       String url = node.getDoc();
       Document doc = Jsoup.connect(url).get();
        ArrayList<ArrayList> result3 = preprocess(doc);
         ArrayList<ArrayList> result = result3.get(0);
         ArrayList<String> elements = result.get(0);
         ArrayList<ArrayList> edgeelements = result.get(1);
         ArrayList<ArrayList> result2 = result3.get(1);
         ArrayList<String> elements1 = result2.get(0);
         ArrayList<ArrayList> edgeelements1 = result2.get(1);
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
        for(Element e : modale.getAllElements()) {
           if(!e.id().equals("")) {
              compare.add(e.id());
           }
        }
        getModalElements(doc, elements, compare, edgeelements, node);
        getHTMLElements(doc, elements1, compare, edgeelements1, node);
        String fileName = resource_folder_path + "test" + nodeid + ".html";
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
        for(Element e : modale.getAllElements()){
           boolean dont = false;
           
            String tag = e.tagName().toLowerCase();
            
            for(Attribute att : e.attributes().asList()){
               dont = false;
                String attrKey = att.getKey();
                String attrVal = att.getValue();
                if(attrKey.contentEquals("href")) {
                   
                   String alink = e.attr("href");
                   String blink = e.attr("href");
                    if(alink.length() > 1 && alink.charAt(0) == '\"') {
                       alink = alink.substring(1, alink.length()-1);
                       blink = alink;
                    }
                    if(alink.length() > 1 && alink.charAt(0) == '/') {
                       alink = alink.substring(1);
                       if(alink.length() > 1 && alink.charAt(0) == '/') {
                           alink = alink.substring(1);
                       }
                    }
                    
                    
                    String link = processhref(blink);
                    if(link.contentEquals("-1")) {
                       if(alink.contentEquals("#") || alink.contentEquals("")) {
                          link = node.getDoc();
                       }else {
                          continue;
                       }
                       
                    }
                    if(e.id().contentEquals("")) {
                       e.attr("id",baseid + "" + nodeid + "_" + countid);
                       countid++;
                   }
                    
                    Document doc2; 
                    Node out;
                    try {
                      System.out.println(link);
                       doc2 = Jsoup.connect(link).get();
                       out = new Node(doc2.title(), link);
                    }catch(Exception x) {
                      try {
                        int dir = 0;
                          if(alink.length() > 1 && alink.charAt(0) == '.') {
                             while(alink.length() > 1 && alink.charAt(0) == '.') {
                                alink = alink.substring(1);
                                dir++;
                             }
                          }
                          if(alink.length() > 1 && alink.charAt(0) == '/') {
                             alink = alink.substring(1);
                          }
                          String docurl = node.getDoc();
                          String[] dirs = docurl.split("/");
                          int ind = 0;
                          String trylink = "";
                          for(String s : dirs) {
                             trylink += s + "/";
                             ind++;
                             if(ind == dirs.length - dir + 1) {
                                break;
                             }
                             
                          }
                          if(trylink.contains(alink)) {
                             continue;
                          }
                          trylink = trylink + alink;
                          System.out.println("try: " + trylink);
                          doc2 = Jsoup.connect(trylink).get();
                          out = new Node(doc2.title(), trylink);
                      }catch(Exception y) {
                         countid--;
                         continue;
                      }    
                    }
                    boolean add = true;
                    for(Node neighbors : fsm) {
                     if(neighbors.getDoc().contentEquals(out.getDoc())) {
                        out = neighbors;
                        add = false;
                     }
                  }
                 int n = 0;
                 ArrayList<String[]> edges = new ArrayList<String[]>();
                 if(node.edges.size() > 0) {
                    edges = node.edges.get(0);
                 }
                 for(int j = 0; j < edges.size(); j++) {
                      String[] edge1 = edges.get(j);
                      Node node1 = node.out.get(n);
                      if(edge1[0].equals(e.id()) && node.getDoc().equals(out.getDoc()) || node.getTitle().contentEquals(out.getTitle())) {
                         dont = true;
                      }
                      n++;
                   }
                  if(!dont) {
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
                     System.out.println("adding now to " + node.getTitle() + " edge: " + edge[0] + " to node: " + out.getDoc());
                     if(add) {
                        fsm.add(out);
                     }
                  }
                  //dont = false;
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