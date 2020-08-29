package crawler.XemPhimSo;

import checker.XmlSyntaxChecker;
import constant.StringContant;
import crawler.BaseCrawler;
import threadCrawler.BaseThread;
import entities.TblCategory;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimSoEachPageCrawler extends BaseCrawler implements Runnable {
    private String url;
    private TblCategory category;

    public XemPhimSoEachPageCrawler(ServletContext context, String url, TblCategory category) {
        super(context);
        this.url = url;
        this.category = category;
    }

    public static void main(String[] args) {
        new XemPhimSoEachPageCrawler(null, "https://xemphimsoz.com/the-loai/phim-hanh-dong/trang-1/", null).run();
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            StringBuilder documentBd = new StringBuilder();
            boolean isStart = false;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains(StringContant.XEMPHIMSO_START_TAG_PAGE_CONTENT))
                    isStart = true;
                if (isStart) {
                    documentBd.append(line);
                }
                if (line.contains(StringContant.XEMPHIMSO_END_TAG_PAGE_CONTENT))
                    break;
            }
            try {
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(AzaudioCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            ;
            crawlDetailItems(stAXparserForEachPage(documentBd.toString()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException e) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void crawlDetailItems(Map<String, String> detailLinkMap) {

    }

    private Map<String, String> stAXparserForEachPage(String document) throws XMLStreamException, UnsupportedEncodingException, XMLStreamException {
        String refinedDocument = new XmlSyntaxChecker().wellformingToXML(document);
        refinedDocument = refinedDocument.trim();
        XMLEventReader eventReader = parseStringToXMLEventReader(refinedDocument);
        Map<String, String> detailLinkMap = new HashMap<>();
        while (eventReader.hasNext()) {
            String tagName = "";
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                tagName = startElement.getName().getLocalPart();
                Attribute attrSrc = startElement.getAttributeByName(new QName(("class")));

                if ("a".equals(tagName) && StringContant.XEMPHIMSO_ITEM_CLASS_VALUE.equals(attrSrc.getValue())) {
                    Attribute linkAtr = startElement.getAttributeByName(new QName("href"));
                    Attribute titleAttr = startElement.getAttributeByName(new QName("title"));
                    String detailLink = linkAtr.getValue();
                    detailLinkMap.put(detailLink, titleAttr.getValue());
                }
            }
        }
        return detailLinkMap;
    }
}



