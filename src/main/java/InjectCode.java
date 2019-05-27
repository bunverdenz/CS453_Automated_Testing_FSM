import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InjectCode {

    public static boolean checkOnClick(Element element){

        for(Attribute att : element.attributes().asList()){
            if (att.getKey().equals("onclick")){
                return true;
            }
        }
        return false;
    }
    public static boolean checkId(Element element){

        for(Attribute att : element.attributes().asList()){
            if (att.getKey().equals("id")){
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) throws IOException {

        String root = "https://melodize.github.io/";
        String base_id = "team6_";
        int count_id = 0;

        String fileName = "test.html";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));


        Document doc = Jsoup.connect(root).get();
        for(Element e : doc.getAllElements()){
            if((e.tagName().equals("button") || e.tagName().equals("a")) && !checkId(e)) {
                e.attr("id",base_id + count_id);
                count_id ++;
            }
            else if(checkOnClick(e) && !checkId(e)){
                e.attr("id",base_id + count_id);
                count_id ++;
            }
        }




        String str = doc.toString();
        writer.write(str);

        writer.close();


//        return fileName;
    }

}
