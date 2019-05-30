import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
   
   public static void main(String[] args) throws IOException{
      readJSTester text = new readJSTester();
      
      downloadFileFromURL(FILE_NAME, OUTPUT_FILE_NAME);
      
      List<String> lines = text.readSmallTextFile(OUTPUT_FILE_NAME);
      ArrayList<Integer> lineNum = new ArrayList<Integer>();
      
      for(int i = 0; i < lines.size(); i++) {
         System.out.print("line ");
         System.out.print(i);
         System.out.print(": ");
         System.out.println(lines.get(i));
      }
      
      System.out.println();
      for(int i = 0; i < lines.size(); i++) {
         if(lines.get(i).contains("loginModal")) {
            lineNum.add(i);
            System.out.println(i);
         }
      }
      
      
      for(int i = 0; i < lineNum.size(); i++) {
         for(int j = lineNum.get(i); j > 0; j--) {
            if(lines.get(j).length() > 6) {
               if(lines.get(j).substring(0, 7).equals("function")) {
                  System.out.println(lines.get(j));
               }
            }
         }
      }
   }
   
   final static String FILE_NAME = "https://melodize.github.io/scripts/login.js";
   final static String OUTPUT_FILE_NAME = "C:/Users/aerol/OneDrive/Desktop/CS453_Automated_Testing_FSM/src/main/resources/ouput.js";
   final static Charset ENCODING = StandardCharsets.UTF_8;
   
   public static void downloadFileFromURL(String urlString, String destination) {    
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            java.io.File file = new java.io.File(destination);
            file.createNewFile();
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