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
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimSoCrawler extends BaseCrawler implements Runnable {


    String url;
    private String categoryName;
    protected TblCategory category = null;


    public XemPhimSoCrawler(ServletContext context, String url, String categoryName) {
        super(context);
        this.url = url;
        this.categoryName = categoryName;
    }

    public XemPhimSoCrawler() {

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

    TblCategory createCategory(String categoryName) {
        return null;
    }

    public static void main(String[] args) {
        XemPhimSoCrawler x = new XemPhimSoCrawler(null, "https://xemphimsoz.com/the-loai/phim-hanh-dong/", "Phim hanh dong");
        x.run();
    }

    @Override
    public void run() {
        category = createCategory(categoryName);
        if (category == null) {
            Logger.getLogger(XemPhimSoCrawler.class.getName()).log(Level.SEVERE, null, new Exception("Error: category null"));
            return;
        }

        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            String document = "";
            boolean isStart = false;
            boolean isEading = false;
            int divCounter = 0;
            int divOpen = 0, divClose = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains(StringContant.XEMPHIMSO_START_TAG_OF_PAGE_UL)) {
                    isStart = true;
                }
                if (isStart) {
                    document += line;
                    if (line.contains("</div>")) {
                        break;
                    }
                }
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
            String refinedDocument = new XmlSyntaxChecker().wellformingToXML(document);
            int lastPage = getLastPage(refinedDocument);
            for (int i = 0; i < lastPage; i++) {
                String pageUrl = url + StringContant.XEMPHIMSO_PAGE_PREFIX_URL + (i + 1);
                Thread pageCrawlingThread = new Thread(new XemPhimSoEachPageCrawler(this.getContext(), pageUrl, category));
                pageCrawlingThread.start();
                try {
                    synchronized (BaseThread.getInstance()) {
                        while (BaseThread.isSuspended()) {
                            BaseThread.getInstance().wait();
                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(AzaudioCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(XemPhimSoCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(XemPhimSoCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
