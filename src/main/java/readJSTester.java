import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class readJSTester {
   
   public static void main(String... args) throws IOException{
      
      //ArrayList<String> l = getJSInfo(FILE_NAME, OUTPUT_FILE_NAME);
        
   }
   
   final static String FILE_NAME = "https://melodize.github.io/scripts/home.js";
   final static String OUTPUT_FILE_NAME = "C:/Users/chaec/Documents/GitHub/CS453_Automated_Testing_FSM/src/main/resources/ouput.txt";
   final static Charset ENCODING = StandardCharsets.UTF_8;
   static List<String> lines = new ArrayList<String>();          

   public static ArrayList<ArrayList> getJSInfo(String webFileName, String outputFileName, List<String> modals) throws IOException{
	  
	   //System.out.println(webFileName);
	   
	   ArrayList<ArrayList> buttonelements = new ArrayList<ArrayList>();
	   ArrayList<String> buttons = new ArrayList<String>();
	   readJSTester text = new readJSTester();
       
         downloadFileFromURL(webFileName, outputFileName);
         lines = text.readSmallTextFile(outputFileName);  
         for(int i = 0; i < lines.size(); i++) {
        	
        	String line = lines.get(i);
        	//System.out.println(line);
            if(line.contains(".on('click'")) {
            	String temp = "";
            	int ind = line.indexOf(".on('click");
            	if(line.substring(0, ind).contains("document")) {
            		ind = line.indexOf(",", ind);
            		int ind2 = line.indexOf(",", ind+1);
            		if(ind < 0 || ind2 < 0) {
            			continue;
            		}
            		temp = line.substring(ind+1, ind2);
            		if(temp.contains("#")) {
            			int sharp = temp.indexOf("#");
            			int end = temp.indexOf("\"", sharp);
                		if(end < 0) {
                			end = temp.indexOf("\'", sharp);
                		}
                		if(end < 0) {
                			continue;
                		}
                		temp = temp.substring(sharp+1, end);
            			continue;
            		}
            	}
            	if(line.contains("#")) {
            		int sharp = line.indexOf("#");
            		int end = line.indexOf("\"", sharp);
            		if(end < 0) {
            			end = line.indexOf("\'", sharp);
            		}
            		if(end < 0 || sharp < 0) {
            			continue;
            		}

            		temp = line.substring(sharp+1, end);            		
            	}
            	buttons.add(temp);
        		buttonelements.add(getButtonElements(lines, modals, i));
            }
         }
         
         List<String[]> funcnames = new ArrayList<String[]>();
         List<String[]> hrefs = new ArrayList<String[]>();
         int buttonind = 0;
         int arrind = 0;
         

         for(ArrayList<String[]> arr : buttonelements) {
        	 arrind = 0;
        	 while(arrind < arr.size()) {
        		 String[] arr1 = arr.get(arrind);
        		 if(arr1[1].contentEquals("func")) {
        			 int ind = arr1[0].indexOf('(');
        			 if(ind < 0) {
        				 arrind++;
        				 continue;
        			 }
        			 String funcname = arr1[0].substring(0, ind);
        			 String[] buttonfunc = {funcname, "" + buttonind};
        			 arr.remove(arrind);
        			 arrind--;
        			 funcnames.add(buttonfunc);
        		 }
        		 if(arr1[1].contentEquals("hrefvar")) {
        			 String[] href = {arr1[0], "" + buttonind, "" + arrind};
        			 hrefs.add(href);
        		 }
        		 arrind++;
        	 }
        	 buttonind++;
         }

         for(int i = 0; i < lines.size(); i++) {
        	 String line = lines.get(i);
        	 String def = line.trim();
        	 if(def.length() > 8 && def.substring(0, 8).equals("function")) {
             	for(String[] funcname : funcnames) {
             		if(line.contains(funcname[0])) {
             			List<String[]> funcelements = new ArrayList<String[]>();
             			funcelements = getFunctionElements(lines, modals, i);
             			int buttonind1 = Integer.parseInt(funcname[1]);
             			buttonelements.get(buttonind1).addAll(funcelements);
             		}
             	}
             }
          }
         buttonind = 0;
         for(ArrayList<String[]> arr : buttonelements) {
        	 arrind = 0;
        	 while(arrind < arr.size()) {
        		 String[] arr1 = arr.get(arrind);
        		 if(arr1[1].contentEquals("hrefvar")) {
        			 String[] href = {arr1[0], "" + buttonind, "" + arrind};
        			 hrefs.add(href);
        		 }
        		 arrind++;
        	 }
        	 buttonind++;
         }
         

         for(int i = 0; i < lines.size(); i++) {
        	 String line = lines.get(i);
	         for(String[] href : hrefs) {
	    		 if(line.contains("var " + href[0])) {
	    			 int var = line.indexOf("=");
	    			 if(var < 0) {
						  continue;
	    			 }else if(line.charAt(var+1) == '=' || line.charAt(var-1) == '!') {
	    				 continue;
	    			 }
	    			 int end = line.indexOf(";", var);
					  if(end < 0) {
						  continue;
					  }
					 String temp = line.substring(var + 1, end).trim();
					 if(!(temp.contains("\"") || temp.contains("\'"))) {
						 continue;
					 }else if(temp.equals("\"\"")) {
						 temp = "index.html";
					 }
	    			 int buttonind1 = Integer.parseInt(href[1]);
	    			 int arrind1 = Integer.parseInt(href[2]);
	    			 String[] result = {temp, "href"};
	    			 buttonelements.get(buttonind1).set(arrind1, result); 
	    		 }
	         }
         }
       ArrayList<ArrayList> result = new ArrayList<ArrayList>();
       result.add(buttons);
       result.add(buttonelements);
       return result;
   }
   
   public static ArrayList<String[]> getButtonElements(List<String> lines, List<String> modals, int num){
	   ArrayList<String[]> elements = new ArrayList<String[]>();
	   ArrayList<String[]> hrefs = new ArrayList<String[]>();
	   int i = num;
	   int cond = 0;
	   boolean condon = false;
	   String line = lines.get(i);
	   while(!line.contains("})")) {
		   line = lines.get(i);
		   i++;
		   if(line.contains("if")) {
			  cond++;
			  condon = true;
		   }
		   if(condon) {
			   if(cond == 0) {
				   condon = false;
				   
			   }
			   else{
				   if(line.contains("{")) {
			   
					   int count = ( line.split("\\{", -1).length ) - 1;
					   cond += count;
				   }
				   if(line.contains("}")) {
					   int count = ( line.split("\\}", -1).length ) - 1;
					   cond -= count;
				   }
			   }
			   
		   }
		   if(line.contains("show") || line.contains("hide")) {
			   String temp = line.trim();
			   int ind = temp.indexOf('#');
			   if(ind < 0) {
				   continue;
			   }
			   int end = temp.indexOf('\'', ind);
			   if(end < 0) {
				   end = temp.indexOf('\"', ind);
				   if(end < 0) {
					   continue;
				   }
			   }
			   String targ = temp.substring(ind+1, end);
			   for(String s : modals) {
				   if(s.equals(targ)) {
					   if(condon) {
						   if(line.contains("show")) {
							   String[] show = {targ, "showc"};
							   elements.add(show);
						   }else {
							   String[] hide = {targ, "hidec"};
							   elements.add(hide);
						   } 
					   }else {
						   if(line.contains("show")) {
							   String[] show = {targ, "show"};
							   elements.add(show);
						   }else {
							   String[] hide = {targ, "hide"};
							   elements.add(hide);
						   }
					   }
				   }
				   
			   }
		   }else if(!line.contains(".") && line.contains(");")){
			   String[] funct = {line.trim(), "func"};
			   elements.add(funct);
		   }
		   	else if(line.contains("href")) {
				  String type = "href";
				  int ind = line.indexOf("href");
				  int var = line.indexOf("=", ind);
				  if(var < 0) {
					  continue;
				  }
				  int end = line.indexOf(";", var);
				  String temp = line.substring(var + 1, end);
				  temp = temp.trim();
				  if(condon) {
					  if(!(temp.contains("\"") || temp.contains("\'"))) {
						  type = "hrefvarc";
					  }else if(temp.equals("\"\"")) {
						  type = "hrefc";
						  temp = "index.html";
					  }
				   }else {
					  if(!(temp.contains("\"") || temp.contains("\'"))) {
						  type = "hrefvar";
					  }else if(temp.equals("\"\"")) {
						  temp = "index.html";
					  }
				   }
				  
				  String[] href = {temp, type};
				  elements.add(href);
				  if(type.equals("hrefvar") || type.contentEquals("hrefvarc")) {
					  String[] temp1 = {temp, "" + (elements.size()-1)};
					  hrefs.add(temp1);
				  }
			   }
	   }
	   i = num;
	   if(hrefs.size() > 0) {
		   line = lines.get(i);
		   while(!line.contains("})")) {
			   line = lines.get(i);
			   i++;
			   for(String[] href : hrefs) {
				   boolean found = false;
				   String link = "";
				   if(line.contains(href[0])) {
					   int ind1 = line.indexOf(href[0]);
					   String temp = line.substring(ind1);
					   int var = line.indexOf("=", ind1);
					   if(var < 0) {
							  continue;
					   }else if(line.charAt(var+1) == '=' || line.charAt(var-1) == '!') {
						   continue;
					   }
					  int end = line.indexOf(";", var);
					  if(end < 0) {
						  continue;
					  }
					  found = true;
					  link = line.substring(var + 1, end);
					  link = temp.trim();
				   }
				   if(found) {
					   if(link.equals("\"\"")) {
						   link = "index.html";
					   }
					   String[] foundtrue = {link, "href"};
					   if(href[1].contentEquals("hrefvarc")) {
						   foundtrue[1] = "hrefc";
					   }
					   
					   elements.set(Integer.parseInt(href[1]), foundtrue);
				   }
			   }
		   }
	   }
	   return elements;
   }
   
   public static ArrayList<String[]> getFunctionElements(List<String> lines, List<String> modals, int num){
	   ArrayList<String[]> elements = new ArrayList<String[]>();
	   ArrayList<String[]> hrefs = new ArrayList<String[]>();
	   int hier = 1;
	   int i = num + 1;
	   int cond = 0;
	   boolean condon = false;
	   while(hier != 0) {
		   String line = lines.get(i);
		   i++;
		   if(line.contains("if")) {
			   	  cond++;
				  condon = true;
			   }
		   if(condon) {
			   if(cond == 0) {
				   condon = false;
				   
			   }
			   else{
				   if(line.contains("{")) {
			   
					   int count = ( line.split("\\{", -1).length ) - 1;
					   cond += count;
				   }
				   if(line.contains("}")) {
					   int count = ( line.split("\\}", -1).length ) - 1;
					   cond -= count;
				   }
			   }
				   
			   }
		   if(line.contains("{")) {
			   int count = ( line.split("\\{", -1).length ) - 1;
			   hier += count;
		   }
		   if(line.contains("}")){
			   int count = ( line.split("\\}", -1).length ) - 1;
			   hier -= count;
		   }
		   if(line.contains("show") || line.contains("hide")) {
			   String temp = line.trim();
			   int ind = temp.indexOf('#');
			   if(ind < 0) {
				   continue;
			   }
			   int end = temp.indexOf('\'', ind);
			   if(end < 0) {
				   end = temp.indexOf('\"', ind);
				   if(end < 0) {
					   continue;
				   }
			   }
			   String targ = temp.substring(ind+1, end);
			   for(String s : modals) {
				   if(condon) {
					   if(line.contains("show")) {
						   String[] show = {targ, "showc"};
						   elements.add(show);
					   }else {
						   String[] hide = {targ, "hidec"};
						   elements.add(hide);
					   } 
				   }else {
					   if(line.contains("show")) {
						   String[] show = {targ, "show"};
						   elements.add(show);
					   }else {
						   String[] hide = {targ, "hide"};
						   elements.add(hide);
					   }
				   }
				   
			   }
		   }else if(!line.contains(".") && line.contains(");") && !line.contains("});")){
			   String[] funct = {line.trim(), "func"};
			   elements.add(funct);
		   }
		   else if(line.contains("href")) {
				  String type = "href";
				  int ind = line.indexOf("href");
				  int var = line.indexOf("=", ind);
				  if(var < 0) {
					  continue;
				  }
				  int end = line.indexOf(";", var);
				  String temp = line.substring(var + 1, end);
				  temp = temp.trim();
				  if(condon) {
					  if(!(temp.contains("\"") || temp.contains("\'"))) {
						  type = "hrefvarc";
					  }else if(temp.equals("\"\"")) {
						  type = "hrefc";
						  temp = "index.html";
					  }
				   }else {
					  if(!(temp.contains("\"") || temp.contains("\'"))) {
						  type = "hrefvar";
					  }else if(temp.equals("\"\"")) {
						  temp = "index.html";
					  }
				   }
				  String[] href = {temp, type};
				  elements.add(href);
				  if(type.equals("hrefvar")) {
					  String[] temp1 = {temp, "" + (elements.size()-1)};
					  hrefs.add(temp1);
				  }
			   }
	   }
	   i = num;
	   hier = 1;
	   if(hrefs.size() > 0) {
		   String line = lines.get(i);
		   while(hier != 0) {
			   if(line.contains("{")) {
				   int count = ( line.split("{", -1).length ) - 1;
				   hier += count;
			   }
			   if(line.contains("}")){
				   int count = ( line.split("}", -1).length ) - 1;
				   hier -= count;
			   }
			   line = lines.get(i);
			   i++;
			   for(String[] href : hrefs) {
				   boolean found = false;
				   String link = "";
				   if(line.contains(href[0])) {
					   int ind1 = line.indexOf(href[0]);
					   String temp = line.substring(ind1);
					   int var = line.indexOf("=", ind1);
					   if(var < 0) {
							  continue;
					   }else if(line.charAt(var+1) == '=' || line.charAt(var-1) == '!') {
						   continue;
					   }
					  int end = line.indexOf(";", var);
					  if(end < 0) {
						  continue;
					  }
					  found = true;
					  link = line.substring(var + 1, end);
					  link = temp.trim();
				   }
				   if(found) {
					   if(link.equals("\"\"")) {
						   link = "index.html";
					   }
					   String[] foundtrue = {link, "href"};
					   if(href[1].contentEquals("hrefvarc")) {
						   foundtrue[1] = "hrefc";
					   }
					   
					   elements.set(Integer.parseInt(href[1]), foundtrue);
				   }
			   }
		   }
	   }
	   return elements;
   }
      
 //////////////////////////////////////////// 
   
   public static void downloadFileFromURL(String urlString, String destination){    
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
   List<String> readSmallTextFile(String fileName) throws IOException {
      Path path = Paths.get(fileName);
      return Files.readAllLines(path, ENCODING);
   }
   
   void writeSmallTextFile(List<String> lines, String fileName) throws IOException {
      Path path = Paths.get(fileName);
      Files.write(path, lines, ENCODING);
   }
   
   //For larger files
   
   void readLargerTextFile(String fileName) throws IOException {
      Path path = Paths.get(fileName);
      try (Scanner scanner =  new Scanner(path, ENCODING.name())){
         while (scanner.hasNextLine()){
            //process each line in some way
            log(scanner.nextLine());
         }
      }
   }
   
   void readLargerTextFileAlternate(String fileName) throws IOException {
      Path path = Paths.get(fileName);
      try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)){
         String line = null;
         while ((line = reader.readLine()) != null) {
            //process each line in some way
            log(line);
         }
      }
   }
   
   void writeLargerTextFile(String fileName, List<String> lines) throws IOException {
      Path path = Paths.get(fileName);
      try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
         for(String line : lines){
            writer.write(line);
            writer.newLine();
         }
      }
   }
   
   private static void log(Object msg){
      System.out.println(String.valueOf(msg));
   }
} 