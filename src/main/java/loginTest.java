import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

import com.gargoylesoftware.htmlunit.WebConsole.Logger;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

//import org.w3c.dom.html.HTMLElement;
//import com.sun.java.swing.plaf.windows.resources.windows;

public class loginTest {

    public static void main(String[] args) throws IOException {

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        Document doc = Jsoup.connect("https://www.fanfiction.net/login.php?cache=bust").get();
        //Document doc = Jsoup.connect("https://melodize.github.io/").get();        
        WebClient webClient = new WebClient((BrowserVersion.CHROME));
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
//        java.net.URL url = file.toURI().toURL();
        HtmlPage page = webClient.getPage("https://sc2replaystats.com/Account/signin");
        processLogin("aerolane0302@gmail.com", "software123", page);

    }

    static HtmlPage processLogin(String username, String password, HtmlPage page) throws IOException {

        int index = 0;

        boolean found = false;
        HtmlPage pageResult = page;
        Iterable<DomElement> pageDesc= page.getDomElementDescendants();
        List<DomElement> target = new ArrayList<>();
        pageDesc.forEach(target::add);
        index = 0;
        int keep;
        
        DomNodeList<DomElement> loginForms = page.getElementsByTagName("form"); 
        

        DomNodeList<DomElement> allInputs = page.getElementsByTagName("input");
//        List<String> storeNameInputs = new ArrayList<>();
//        List<String> storePwdInputs = new ArrayList<>();
        String storeNameInputs = "";
        String storePwdInputs = "";
        for(DomElement input: allInputs){
           HtmlInput castInput = (HtmlInput) input;
           boolean checkUsername = (castInput.getNameAttribute().toLowerCase().contains("name")&&
                 castInput.getNameAttribute().toLowerCase().contains("u")) //in case they user "uname"
                 ||
                 (castInput.getNameAttribute().toLowerCase().contains("mail") && 
                       castInput.getNameAttribute().length() <= 5)
                 ||(castInput.getNameAttribute().toLowerCase().contains("log")&&
                       castInput.getNameAttribute().toLowerCase().contains("in"))
                 ||(castInput.getNameAttribute().toLowerCase().contains("sign")&&
                       castInput.getNameAttribute().toLowerCase().contains("in"));//just mail or email
           //I found "forgot_email" 
           if (checkUsername) {
              storeNameInputs = castInput.getNameAttribute();
           }
           boolean checkPassword = (castInput.getNameAttribute().contains("p")&&
                 castInput.getNameAttribute().contains("w")&&
                 castInput.getNameAttribute().contains("d"));
           if (checkPassword) {
              storePwdInputs = castInput.getNameAttribute();
           }
           System.out.println("INPUT");
           System.out.println(castInput.getNameAttribute());
        }
        for(DomElement form: loginForms){
           System.out.println("FORM");
           System.out.println(form);
            HtmlForm castForm = (HtmlForm) form;
              
              boolean checkPwd = (castForm.getInputByName("password") != null)
                    || (castForm.getInputByName("pwd") != null)
                    || (castForm.getInputByName("pw") != null);
              if(checkPwd) {
//                 for (String inputName: storeNameInputs) {
                    if(castForm.getInputByName(storeNameInputs)!=null) {
                       castForm.getInputByName(storeNameInputs).setValueAttribute(username);
                       System.out.println("FORMHEY");
                         System.out.println(storeNameInputs);
//                         break;
                    }
//                 }
//                 for (String inputName: storePwdInputs) {
                    if(castForm.getInputByName(storePwdInputs)!=null) {
                       castForm.getInputByName(storePwdInputs).setValueAttribute(password);
                       System.out.println("FORMHEY");
                         System.out.println(storePwdInputs);
//                         break;
                    }
//                 }
//                 castForm.getInputByValue("password").setValueAttribute(password); 
                 try {
                    pageResult = (HtmlPage)((DomElement) castForm.getFirstByXPath("//input[@type='submit']")).click();
                    
                    
                 }catch(Exception e) {
                    System.out.println(e);
                    System.out.println(pageResult);
                    continue;
                 }
//                   
//                                           
//                   System.out.println(pageResult.asXml()); 
                 System.out.println(pageResult.getTitleText());
                   System.out.println(page.getTitleText());
                   break;
              }

        }

        System.out.println(found);
        return pageResult;
    }
}