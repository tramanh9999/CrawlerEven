package crawler.XemPhimPlus;

import checker.XmlSyntaxChecker;
import constant.StringContant;
import crawler.BaseCrawler;

import javax.servlet.ServletContext;
import javax.xml.bind.ValidationException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimPlusCategoriesCrawler extends BaseCrawler {
    public XemPhimPlusCategoriesCrawler(ServletContext context) {
        super(context);
    }

    public XemPhimPlusCategoriesCrawler() {
    }

    public static void main(String[] args) throws IOException, ValidationException {
        XemPhimPlusCategoriesCrawler x = new XemPhimPlusCategoriesCrawler();
        String xempURL = "https://xemphimplus.net/";
        Map<String, String> categories = x.getCategories(xempURL);

    }

    public Map<String, String> getCategories(String url) throws IOException, ValidationException {
        BufferedReader reader = getBufferedReaderForURL(url);
        try {
            String line = "";
            StringBuilder documentBd = new StringBuilder();
            boolean isStart = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(StringContant.XPPLUS_START_TAG_UL))
                    isStart = true;
                if (isStart) {
                    documentBd.append(line.trim());
                }
                if (isStart && line.contains(StringContant.XPPLUS_STOP_TAG_UL))
                    break;
            }
             staxParserForCategories(documentBd.toString());
        } catch (XMLStreamException e) {
            Logger.getLogger(XemPhimPlusCategoriesCrawler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException ex) {
                Logger.getLogger(XemPhimPlusCategoriesCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return null;
    }

    public Map<String, String> staxParserForCategories(String src) throws UnsupportedEncodingException, XMLStreamException {
        XmlSyntaxChecker xmlSyntaxChecker = new XmlSyntaxChecker();
//        String refinedDoc = xmlSyntaxChecker.wellformingToXML(src);
        XMLEventReader eventReader = parseStringToXMLEventReader(src);
        Map<String, String> categories = new HashMap<>();
        String link = "";
        while (eventReader.hasNext()) {
            Object next = eventReader.next();
            XMLEvent event = (XMLEvent) next;
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                if (StringContant.XPPLUS_START_TAG_CTE.equals(tagName)) {
                    Attribute linkAttr = startElement.getAttributeByName(
                            new QName("href"));
                    link = linkAttr.getValue();
//next to get data
                    event = (XMLEvent) eventReader.next();
                    Characters character = event.asCharacters();
                    categories.put(link, character.getData());
                }
            }
        }
        return categories;
    }


    public void categoryCrawler(){
        //cal func


    }

}
