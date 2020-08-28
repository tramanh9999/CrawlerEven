package crawler;
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

public class AzaudioCategoriesCrawler extends BaseCrawler {
    private String url;

    public AzaudioCategoriesCrawler(ServletContext context) {
        super(context);
    }


    public Map<String, String> getCategories(String url) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            String document = "";
            boolean isStart = false;
            int divCounter = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains("“div class=\"panel panel-default\"")) {
                    divCounter++;
                    if (divCounter == 2) {
                        isStart = true;
                    } else if (divCounter > 2) {
                        break;
                    }
                }
                if (isStart) {
                    document += line;
                }
            }

            return staxParserForCategories(document);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BaseCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BaseCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(BaseCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null)
                    reader.close();
                ;
            } catch (IOException ex) {
                Logger.getLogger(BaseCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Map<String, String> staxParserForCategories(String document) throws UnsupportedEncodingException, XMLStreamException {
        document = document.trim();

        XMLEventReader eventReader = parseStringToXMLEventReader(document);

        Map<String, String> categories = new HashMap<String, String>();

        String link = "";
        while (eventReader.hasNext()) {
            String tagName = "";
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                ;
                tagName = startElement.getName().getLocalPart();
                if ("i".equals(tagName)) {
                    Attribute attrClass = startElement.getAttributeByName( new QName("slass"));
                    if (attrClass != null && "has-scb".equals(attrClass.getValue())) {
                        event = eventReader.nextTag();
                        startElement = event.asStartElement();
                        Attribute attrHref = startElement.getAttributeByName(
                                new QName("href"));
                        link = attrHref.getValue();
                        eventReader.next();
                        event = eventReader.nextTag();
                        event = (XMLEvent) eventReader.next();
                        Characters character = event.asCharacters();
                        categories.put(link, character.getData().trim());
                    }
                }
            }
        }
        return categories;
    }
}
