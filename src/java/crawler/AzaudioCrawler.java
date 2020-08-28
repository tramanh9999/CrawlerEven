package crawler;
import entities.TblCategory;
import javax.servlet.ServletContext;
public class AzaudioCrawler extends BaseCrawler implements Runnable {
    private String url;
    String categoryName;
    protected TblCategory category;

    public AzaudioCrawler(ServletContext context, String url, String categoryName, TblCategory category) {
        super(context);
        this.url = url;
        this.categoryName = categoryName;
        this.category = category;
    }

    @Override
    public void run() {

    }
}
