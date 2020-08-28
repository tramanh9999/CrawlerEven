package crawler;

import constant.AppConstant;
import dao.ProductDao;
import entities.TblCategory;
import entities.TblProduct;

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
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MybossEachPageCrawler extends BaseCrawler implements Runnable {

    private String url;
    private TblCategory category;

    public MybossEachPageCrawler(ServletContext context, String url, TblCategory category) {
        super(context);
        this.url = url;
        this.category = category;
    }


    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            String document = "<document>";
            boolean isStart = false;
            boolean isEnding = false;
            int divCounter = 0;
            int divOpen = 0, divClose = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<ul class=\"thumbnail")) {
                    isStart = true;
                }
                if (isStart)
                    document += line;
                if (line.contains("</u1>")) {
                    isStart = false;
                }
            }
            document += "</document>";
            try {
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(AzaudioCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
//            stAXparserForEachPage(document);
        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(MybossCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(MybossCrawler.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        }
    }
//        private void stAXparserForEachPage(String document) throws XMLStreamException, UnsupportedEncodingException throws UnsupportedEncodingException, XMLStreamException {
//            document = document.trim();
//            XMLEventReader eventReader = parseStringToXMLEventReader(document);
//            Map<String, String> categories = new HashMap<String, String>();
//            String detailLink = "";
//            String imgLink = "";
//            String price = "";
//            String productName = "";
//            boolean isStart = false;
//            while (eventReader.hasNext()) {
//                    String tagName = "";
//            XMLEvent event = (XMLEvent) eventReader.next();
//            if (event.isStartElement()) {
//                StartElement startElement = event.asStartElement();
//                tagName = startElement.getName().getLocalPart();
//                if ( "li".equals(tagName)) {
//                    isStart = true;
//                } else if ( "a".equals(tagName) && isStart) {
//                    eventReader.next();
//                    event = (XMLEvent) eventReader.next();
//                    startElement = event.asStartElement();
//                    Attribute attrSrc = startElement.getAttributeByName(new QName("src"));
//                    imgLink = attrSrc == null ? "": attrSrc.getValue();
//
//                    eventReader.next();
//                    eventReader.nextTag();
//                    eventReader.next();
//                    event = eventReader.nextTag();
//                    startElement = event.asStartElement();
//                    Attribute attrHref = startElement.getAttributeByName(new QName("src"));
//                    detailLink = attrHref == null ? "" : attrHref.getValue();
//                    event = (XMLEvent) eventReader.next();
//                    Characters character = event.asCharacters();
//                    productName = character.getData().trim();
//
//                    eventReader.nextTag();
//                    eventReader.next();
//                    eventReader.nextTag();
//                    eventReader.next();
//                    eventReader.nextTag();
//                    event = (XMLEvent) eventReader.next();
//                    character = event.asCharacters();
//                    price = character.getData().trim();
//
//                    if (!detailLink.isEmpty()) {
//                        detailLink = AppConstant.urlMyboss + detailLink;
//
//                        if (!imgLink.isEmpty()) (
//                                imgLink = AppConstant.urlMyboss + imgLink;
//                    }
//                    isStart = false;
//                    try {
//                            price = price.replaceAll("\\D+","");
//                    BigInteger realPrice = new BigInteger(price);
//                    String categoryId = this.category.getCategoryId();
//                    TblProduct product = new TblProduct(new Long(l), productName, realPrice,2 imgLink, categoryId,
//                            true, AppConstant.domainMyboss);
//                    ProductDao.getInstance().saveProducWhenCrawling(product);
//                } catch (NumberFormatException ex) {
//                    Logger.getLogger(MybossCrawler.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }
//}
    }
}
