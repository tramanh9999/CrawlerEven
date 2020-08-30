package crawler.XemPhimSo;

import checker.XmlSyntaxChecker;
import constant.StringContant;
import crawler.BaseCrawler;
import threadCrawler.BaseThread;
import entities.Category;

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
import java.util.logging.Level;
import java.util.logging.Logger;

//get page number section of each category
public class XemPhimSoCategoryAllPageCrawler extends BaseCrawler implements Runnable {


    String categoryUrl;
    private String categoryName;
    protected Category category = null;


    public XemPhimSoCategoryAllPageCrawler(ServletContext context, String categoryUrl, String categoryName) {
        super(context);
        this.categoryUrl = categoryUrl;
        this.categoryName = categoryName;
    }



    @Override
    public void run() {
        BufferedReader reader;
        String pageNumberSection;
        try {
            reader = getBufferedReaderForURL(categoryUrl);
            pageNumberSection = getPageNumberSectionFromURLReader(reader);
            setToWaitingThread();
            createEachPageCrawlThreadByPageNumSection(pageNumberSection);
        } catch (IOException | XMLStreamException | ValidationException ex) {
            Logger.getLogger(XemPhimSoCategoryAllPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setToWaitingThread() {
        try {
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    Logger.getLogger(XemPhimSoCategoryAllPageCrawler.class.getName()).log(Level.INFO, BaseThread.getInstance().getName());
                    BaseThread.getInstance().wait();
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(XemPhimSoCategoryAllPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getLastPage(String document) throws UnsupportedEncodingException, XMLStreamException {
        document = document.trim();
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        String tagName = "";
        int lastPageNum = 1;
        int countA = 0;
        while (eventReader.hasNext()) {
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                tagName = startElement.getName().getLocalPart();

                if ("a".equals(tagName)) {
                    countA++;
                    if (countA == StringContant.XEMPHIMSO_INDEX_OF_TAG_LAST_PAGE) {
                        Attribute attrHref = startElement.getAttributeByName(new QName("title"));
                        String lastPageStr = attrHref == null ? "" : attrHref.getValue();
                        String prefix = StringContant.XEMPHIMSO_PAGE_PREFIX_VALUE;
                        lastPageNum = Integer.parseInt(lastPageStr.substring(prefix.length()));
                        break;
                    }
                }
            }
        }
        return lastPageNum;
    }


    Category createCategory(String categoryName) {
        return null;
    }

    public static void main(String[] args) {
        XemPhimSoCategoryAllPageCrawler x = new XemPhimSoCategoryAllPageCrawler(null, "https://xemphimsoz.com/the-loai/phim-hanh-dong/", "Phim hanh dong");
        x.run();
    }
    private void createEachPageCrawlThreadByPageNumSection(String document) throws UnsupportedEncodingException, XMLStreamException {
        String refinedDocument = new XmlSyntaxChecker().wellformingToXML(document);
        int lastPage = getLastPage(refinedDocument);
        System.out.printf("Crawling page %s%n",categoryUrl);
        for (int i = 0; i < lastPage; i++) {
            String pageUrl = categoryUrl + StringContant.XEMPHIMSO_PAGE_PREFIX_URL + (i + 1);
            Thread pageCrawlingThread = new Thread(new XemPhimSoCategoryEachPageCrawler(this.getContext(), pageUrl, category));
            pageCrawlingThread.start();
            try {
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(XemPhimSoCategoryAllPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String getPageNumberSectionFromURLReader(BufferedReader reader) throws IOException {
        String line;
        StringBuilder document = new StringBuilder(StringContant.EMPTY_STRING);
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains(StringContant.XEMPHIMSO_START_TAG_OF_PAGE_UL)) {
                isStart = true;
            }
            if (isStart) {
                document.append(line);
                if (line.contains("</div>")) {
                    break;
                }
            }
        }
        return document.toString();
    }


}
