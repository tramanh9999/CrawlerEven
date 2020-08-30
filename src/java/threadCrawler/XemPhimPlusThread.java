package threadCrawler;

import constant.AppConstant;
import crawler.XemPhimPlus.XemPhimPlusCategoriesCrawler;
import crawler.XemPhimSo.XemPhimSoCategoryAllPageCrawler;

import javax.servlet.ServletContext;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimPlusThread extends BaseThread {
    private ServletContext context;

    @Override
    public void run() {
        while (true) {
            try {
                XemPhimPlusCategoriesCrawler categoriesCrawler = new XemPhimPlusCategoriesCrawler(context);
                Map<String, String> categories = categoriesCrawler.getCategories(AppConstant.XEMPHIMSO_URL);

                for (Map.Entry<String, String> entry : categories.entrySet()) {
                    Thread crawlingThread = new Thread(new XemPhimSoCategoryAllPageCrawler(context, entry.getKey(), entry.getValue()));
                    crawlingThread.start();
                    synchronized (BaseThread.getInstance()) {
                        while (BaseThread.isSuspended()) {
                            BaseThread.getInstance().wait();
                        }
                    }
                }

                XemPhimPlusThread.sleep(TimeUnit.DAYS.toMillis(AppConstant.breakTimeCrawling));
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException | IOException | ValidationException ex) {
                Logger.getLogger(XemPhimPlusThread.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }

    public XemPhimPlusThread(ServletContext context, String key, String value) {
        this.context = context;
    }
}
