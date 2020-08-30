package crawler;

import javax.servlet.ServletContext;
import javax.xml.bind.ValidationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class BaseCrawler {
    private ServletContext context;

    public BaseCrawler(ServletContext context) {
        this.context = context;
    }

    public BaseCrawler() {
    }

    public ServletContext getContext() {
        return context;
    }

    protected BufferedReader getBufferedReaderForURL(String urlString) throws IOException, ValidationException {
        if ('/' != urlString.charAt(urlString.length() - 1)) {
            urlString += "/";
        }
        URL url = new URL(urlString);

        URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0 ; Win64x; x64)");
        InputStream is = connection.getInputStream();
        return new BufferedReader(new InputStreamReader(is, "UTF-8"));
    }

    protected XMLEventReader parseStringToXMLEventReader(String xmlSection) throws UnsupportedEncodingException, XMLStreamException {
        byte[] byteArray = xmlSection.getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        return inputFactory.createXMLEventReader(inputStream);
    }
}