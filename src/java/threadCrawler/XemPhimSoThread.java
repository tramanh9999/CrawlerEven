package threadCrawler;

import constant.AppConstant;
import crawler.*;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimSoThread extends BaseThread {
    private ServletContext context;

    @Override
    public void run() {
        while (true) {
            try {
                XemPhimSoCategoriesCrawler categoriesCrawler = new XemPhimSoCategoriesCrawler(context);
                Map<String, String> categories = categoriesCrawler.getCategories(AppConstant.XEMPHIMSO_URL);

                for (Map.Entry<String, String> entry : categories.entrySet()) {
                    Thread crawlingThread = new Thread(new XemPhimSoCrawler(context, entry.getKey(), entry.getValue()));
                    crawlingThread.start();
                    synchronized (BaseThread.getInstance()) {
                        while (BaseThread.isSuspended()) {
                            BaseThread.getInstance().wait();
                        }
                    }
                }

                XemPhimSoThread.sleep(TimeUnit.DAYS.toMillis(AppConstant.breakTimeCrawling));
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(XemPhimSoThread.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }

    public XemPhimSoThread(ServletContext context) {
        this.context = context;
    }
}
