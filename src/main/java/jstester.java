import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class jstester {

	 public static void main(String[] args)  {
    	
    	File file = new File("C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/test_base.html");

       
	 }
	 
	 public static ArrayList<ArrayList> preprocessdoc(Document doc) throws IOException, InterruptedException{
	 //Document doc = Jsoup.parse(file, "utf-8", "www.example.com/");
	     ArrayList<Element> tags = doc.getElementsByTag("script");
	     ArrayList<ArrayList> functionelements = new ArrayList<ArrayList>();
	     for(Element tag : tags) {
	     	String test = tag.toString();
	     	String fileName ="C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/output.txt";
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	         writer.write(test);
	          
	         writer.close();
	         readJSTester text = new readJSTester();
	         List<String> lines = text.readSmallTextFile(fileName); 
	         boolean func = false;
	     	for(String line : lines) {
	     		if(line.contains("function")) {
	     			func = true;
	     			String[] temp = line.split(" ");
	     			String temp1 = temp[temp.length- 1];
	     			if(temp1.contains("(")) {
	     				int paren = temp1.indexOf("(");
	     				String funcname = temp1.substring(0, paren);
	         			ArrayList<String> funct = new ArrayList<String>();
	         			funct.add(funcname);
	         			functionelements.add(funct);
	     			}
	     		}
	     		
	     	}
	         int i = 0;
	         int ind = 0;
	     	for(String line : lines) {
	     		System.out.println(line);
	     		if(ind == 0) {
	     			func = false;
	     		}
	     		if(line.contains("function")) {
	     			String[] temp = line.split(" ");
	     			String temp1 = temp[temp.length- 1];
	     			if(temp1.contains("(")) {
	     				func = true;
	         			ind = 1;
	     			}else {
	     				func = false;
	     				ind = 0;
	     			}
	     		}
	     		if(func) {  
	   			  if(line.contains("{")) {
	   				   
	   				  int count = ( line.split("\\{", -1).length ) - 1;
	   				  ind += count;
	   			  }
	   			  if(line.contains("}")) {
	   				  int count = ( line.split("\\}", -1).length ) - 1;
	   				  ind -= count;
	   				  if(ind == 0) {
	   	      			  func = false;
	   	      			  i++;
	   	      		  }
	  				   
	   			  }
	   			  if(line.contains("onclick")) {
	   				  line = line.trim();
	   				  if(line.contains("hide")) {
		   					int bind = line.indexOf(".hide");
		   					String modal = "hide_" + line.substring(0, bind);
		   					functionelements.get(i).add(modal);
		   					continue;
		   					
	   				  }else if(line.contains("show")) {
	   					  	int bind = line.indexOf(".show");
	   					  	String modal = "show_" + line.substring(0, bind);
	   					  	functionelements.get(i).add(modal);
	   					  	continue;
	   				  }
	   				  if(line.substring(0, 13).equals("location.href") && ind == 1) {
	   					  if(line.contains("=")) {
	   						  int eq = line.indexOf('=');
	   						  int semi = line.indexOf(';', eq);
	   						 if(eq > 0) {
		      						  String var; 
		      						  if(semi > eq) {
		      							  var = line.substring(eq + 1, semi);
		      						  }else {
		      							  var = line.substring(eq + 1);
	   						  }
	   						  
	   							  functionelements.get(i).add(var);
	   					  }
	   					  }
	   				  }else if(line.contains("setAttribute")) {
	   					  
	   					  int bind = line.indexOf(".setAttribute");
	   					  String button = line.substring(0, bind);
	   					  int param = line.indexOf("(");
	   					  int endparam = line.indexOf(")", param);
	   					  String params = line.substring(param + 1, endparam);
	   					  String[] values = params.split(",");
	   					  if(values[0].contains("onclick")) {
	   						  if(button.contains("document.getElementBy")) {
	   							  if(button.contains("document.getElementByName") || button.contains("document.getElementById")) {
	   								  int startname = button.indexOf("(");
	   								  int endname = button.indexOf(")", startname);
	   								  String name = button.substring(startname + 1, endname);
	   								  String woeq = "but_" + name;
	   								  functionelements.get(i).add(woeq);
	   							  }
	   							  
	   						  }else {
	   							  String result = find(button, lines);
	   							  if(!result.contentEquals("")) {
	   								  functionelements.get(i).add(result);
	   							  }
	   						  }
	   						  if(values[1].contains("location.href")) {
	   							 if(values[1].contains("=")) {
	   	      						  int eq = values[1].indexOf('=');
	   	      						  int semi = values[1].indexOf('\"', eq);
	   	      						if(eq > 0) {
		   	   						  String var; 
		   	   						  if(semi > eq) {
		   	   							  var = values[1].substring(eq + 1, semi);
		   	   						  }else {
		   	   							  var = values[1].substring(eq + 1);
		   	   						  }
		   	   						  String result = "href_" + var;
		   	   						  functionelements.get(i).add(result);
	   	      					  }
	   							 }
	   						  }else {
	   							  
	   							  for(ArrayList<String> arr : functionelements) {
	   								  if(values[1].contains(arr.get(0))) {
	   									  String woeq = "func_" + values[1];
	   									  functionelements.get(i).add(woeq);
	   								  }
	   							  }
	   						  }
	   					  }
	   				  }
	   			  }		  
	       		}
	     	}
	     	
	     }
	     for(ArrayList<String> arr : functionelements) {
	     	for(String s : arr) {
	     		System.out.print(s);
	     		System.out.println("\t");
	     	}
	     	System.out.println("");
	     	
	     }
	     return functionelements;
	}
     	
  
  
	 static String find(String button, List<String> lines) {
		 for(String line : lines) {
			 if(line.contains(button)) {
				 line = line.trim();
				 if(line.contains("=")) {
					
					 int eqind = line.indexOf("=");
					 int semi = line.indexOf(";");
					 if(eqind > 0) {
						  String var; 
						  if(semi > eqind) {
							  var = line.substring(eqind + 1, semi);
						  }else {
							  var = line.substring(eqind + 1);
						  }
						  if(var.contains("document.getElementBy")) {
							  if(var.contains("document.getElementByName") || var.contains("document.getElementById")) {
								  int startname = var.indexOf("(");
								  int endname = var.indexOf(")", startname);
								  String name = var.substring(startname + 1, endname);
								  String result = "but_" + name;
								  return result;
							  }
							  
						  }  
					  }
				 }
			 }
		 }
		 
		 return "";
	 }
	 
	 static ArrayList<ArrayList> addEdgesHTML(ArrayList<String[]> temp, List<ArrayList> functions2) {
		 ArrayList<String> buttons = new ArrayList<String>();
		 ArrayList<ArrayList> functions = new ArrayList<ArrayList>();
		 for(String[] b : temp) {
			 ArrayList<String[]> edges = new ArrayList<String[]>();
			 String func = b[1];
			 buttons.add(b[0]);
			 for(ArrayList<String> arr : functions2) {

				 if(func.equals(arr.get(0))) {				 
					 for(String s : arr) {
						String[] result = processfunctions(s);
						if(result[0] != null) {
							edges.add(result);
						}
					 }
					 functions.add(edges);
				 }
			 }
		 }
		 for(ArrayList<String> arr : functions2) {
			 String func = arr.get(0);
			 int i = 0;
			 for(ArrayList<String[]> eds : functions) {
				 for(int ind = 0; ind < eds.size(); ind++) {
					 String[] edge = eds.get(ind);
					 if(edge[1].contentEquals("func")) {
						 if(edge[0].contentEquals(func)) {
							 eds.remove(ind);
							 eds.addAll(functions.get(i));
						 }
					 }
				 }
				 i++;
			 }
		 }
		 int i = 0;
		 for(ArrayList<String[]> edge : functions) {
			 System.out.println(buttons.get(i));
			 for(String[] e : edge) {
				 System.out.println(e[0]);
				 System.out.println(e[1]);
			 }
			 i++;
		 }
		 
		 ArrayList<ArrayList> result = new ArrayList<ArrayList>();
		 result.add(buttons);
		 result.add(functions);
		 return result;
		 
	 }
	 
	 static String[] processfunctions(String s) {
		 String[] result = new String[2];
		 if(s.contains("show_")) {
			 String target = s.substring(6);
			 result[0] = target;
			 result[1] = "show";
		 }else if(s.contains("href_")) {
			 String target = s.substring(6);
			 result[0] = target;
			 result[1] = "href";
		 }else if(s.contains("hide_")) {
			 String target = s.substring(6);
			 result[0] = target;
			 result[1] = "hide";
		 }else if(s.contains("func_"))
		 {
			 String target = s.substring(6);
			 result[0] = target;
			 result[1] = "func";
		 }
		 return result;
		 
	 }
}
