package crawler.XemPhimSo;

import checker.XmlSyntaxChecker;
import constant.AppConstant;
import constant.StringContant;
import crawler.BaseCrawler;
import threadCrawler.BaseThread;
import entities.Category;
import threadCrawler.XemPhimSoThread;

import javax.servlet.ServletContext;
import javax.xml.bind.ValidationException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimSoCategoryEachPageCrawler extends BaseCrawler implements Runnable {
    private String categoryEachPageNumberUrl;
    private Category category;

    public XemPhimSoCategoryEachPageCrawler(ServletContext context, String categoryEachPageUrl, Category category) {
        super(context);
        this.categoryEachPageNumberUrl = categoryEachPageUrl;
        this.category = category;
    }

    public static void main(String[] args) {
        new XemPhimSoCategoryEachPageCrawler(null, "https://xemphimsoz.com/the-loai/phim-hanh-dong/trang-1/", null).run();
    }

    @Override
    public void run() {
        BufferedReader reader;
        StringBuilder movieListDocument;
        boolean isStart = false;
        try {
            reader = getBufferedReaderForURL(categoryEachPageNumberUrl);
            String line = "";
            movieListDocument = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains(StringContant.XEMPHIMSO_START_TAG_PAGE_CONTENT))
                    isStart = true;
                if (isStart) {
                    movieListDocument.append(line);
                }
                if (line.contains(StringContant.XEMPHIMSO_END_TAG_PAGE_CONTENT))
                    break;
            }
            setToWaitingThread();
            crawlDetailPages(staxParserForDetailPageMap(movieListDocument.toString()));
        } catch (IOException ex) {
            System.out.printf("Get URL failed: %s%n", categoryEachPageNumberUrl);
        } catch (XMLStreamException ex) {
            Logger.getLogger(XemPhimSoCategoryEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            Logger.getLogger(XemPhimSoCategoryEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setToWaitingThread() {
        try {
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(XemPhimSoCategoryEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void crawlDetailPages(Map<String, String> detailLinkMap) {
        Set<String> keySet = detailLinkMap.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            String keyUrl = keyIterator.next();
            System.out.printf("Crawling detail page: %s%n", keyUrl);
            Thread detailCrawler = new Thread(new XemPhimSoDetailCrawler(this.getContext(), keyUrl, null));
            detailCrawler.start();
            try {
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(XemPhimSoDetailCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Map<String, String> staxParserForDetailPageMap(String document) throws XMLStreamException, UnsupportedEncodingException, XMLStreamException {
        String refinedDocument = new XmlSyntaxChecker().wellformingToXML(document);
        refinedDocument = refinedDocument.trim();
        if(refinedDocument.isBlank()){
            return new HashMap<>();
        }
        XMLEventReader eventReader = parseStringToXMLEventReader(refinedDocument);
        Map<String, String> detailLinkMap = new HashMap<>();
        while (eventReader.hasNext()) {
            String tagName = "";
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                tagName = startElement.getName().getLocalPart();
                Attribute attrSrc = startElement.getAttributeByName(new QName(("class")));

                if ("a".equals(tagName) && Objects.nonNull(attrSrc) && StringContant.XEMPHIMSO_ITEM_CLASS_VALUE.equals(attrSrc.getValue())) {
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



