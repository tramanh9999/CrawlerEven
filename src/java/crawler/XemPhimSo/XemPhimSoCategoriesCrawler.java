package crawler.XemPhimSo;

import checker.XmlSyntaxChecker;
import constant.StringContant;
import crawler.BaseCrawler;

import javax.servlet.ServletContext;
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

public class XemPhimSoCategoriesCrawler extends BaseCrawler {
    public XemPhimSoCategoriesCrawler(ServletContext context) {
        super(context);
    }

    public XemPhimSoCategoriesCrawler() {
    }

    public static void main(String[] args) throws IOException {
        XemPhimSoCategoriesCrawler x = new XemPhimSoCategoriesCrawler();
        String xempURL = "https://xemphimsoz.com/";
        Map<String, String> categories = x.getCategories(xempURL);

    }

    public Map<String, String> getCategories(String url) throws IOException {
        BufferedReader reader = getBufferedReaderForURL(url);
        try {
            String line = "";
            StringBuilder documentBd = new StringBuilder();
            boolean isStart = false;
            boolean isFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(StringContant.XEMPHIMSO_LI))
                    isFound = true;
                if (isFound && line.contains(StringContant.XEMPHIMSO_LI_ITEM))
                    isStart = true;
                if (isStart) {
                    documentBd.append(line.trim());
                }
                if (isStart && line.contains(StringContant.XEMPHIMSO_END_TAG_OF_CTGORY))
                    break;
            }
            return staxParserForCategories(documentBd.toString());
        } catch (XMLStreamException e) {
            Logger.getLogger(XemPhimSoCategoriesCrawler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException ex) {
                Logger.getLogger(XemPhimSoCategoriesCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return null;
    }

    public Map<String, String> staxParserForCategories(String src) throws UnsupportedEncodingException, XMLStreamException {
        XmlSyntaxChecker xmlSyntaxChecker= new XmlSyntaxChecker();
        String refinedDoc = xmlSyntaxChecker.wellformingToXML(src);
        refinedDoc="<ul>"+refinedDoc+"</ul>";
        XMLEventReader eventReader = parseStringToXMLEventReader(refinedDoc);
        Map<String, String> categories = new HashMap<>();
        String link = "";
        while (eventReader.hasNext()) {
            Object next = eventReader.next();
            XMLEvent event = null;
            try{
                event = (XMLEvent) next;
            }catch (Exception e){
                e.printStackTrace();
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                if (StringContant.XEMPHIMSO_START_TAG_OF_CTE.equals(tagName)) {
                    Attribute attrHref = startElement.getAttributeByName(
                            new QName("href"));
                    link = attrHref.getValue();
                    event = (XMLEvent) eventReader.next();
                    Characters character = event.asCharacters();
                    categories.put(link, character.getData());
                }
            }
        }
        return categories;
    }
}
