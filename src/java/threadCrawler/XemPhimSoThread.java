package threadCrawler;

import constant.AppConstant;
import constant.StringContant;
import crawler.XemPhimSo.XemPhimSoCategoryLinkPageCrawler;
import crawler.XemPhimSo.XemPhimSoCategoryAllPageCrawler;
import utilities.XMLUtilities;

import javax.servlet.ServletContext;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimSoThread extends BaseThread {
    private ServletContext context;

    @Override
    public void run() {
//        while (true) {
            Map<String, String> categoryURLsMap;
            XemPhimSoCategoryLinkPageCrawler categoriesCrawler;

            try {
                categoriesCrawler = new XemPhimSoCategoryLinkPageCrawler(context);
                categoryURLsMap = categoriesCrawler.getCategories(AppConstant.XEMPHIMSO_URL);
                XMLUtilities.createXMLwithDOMandDTD(categoryURLsMap, StringContant.OUTPUT_CATEGORY_XML_PATH, StringContant.DTO_CATEGORY_PATH);
                createCategoryPageThreads(categoryURLsMap);
            } catch (InterruptedException | IOException | ValidationException ex) {
                Logger.getLogger(XemPhimSoThread.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
//    }

    private void createCategoryPageThreads(Map<String, String> categories) throws InterruptedException {
        //delete this

        for (Map.Entry<String, String> entry : categories.entrySet()) {
            Thread crawlingThread = new Thread(new XemPhimSoCategoryAllPageCrawler(context, entry.getKey(), entry.getValue()));
            crawlingThread.start();
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
        }

        XemPhimSoThread.sleep(TimeUnit.MINUTES.toMillis(AppConstant.breakTimeCrawling));
        synchronized (BaseThread.getInstance()) {
            while (BaseThread.isSuspended()) {
                BaseThread.getInstance().wait();
            }
        }
    }

    public XemPhimSoThread(ServletContext context) {
        this.context = context;
    }
}
