package crawler;

import checker.XmlSyntaxChecker;
import constant.StringContant;
import entities.TblMovie;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XemPhimSoDetailCrawler extends BaseCrawler implements Runnable {

    private String url;
    private TblMovie movie;

    public XemPhimSoDetailCrawler(ServletContext context, String url, TblMovie movie) {
        super(context);
        this.url = url;
        this.movie = movie;
    }

    public static void main(String[] args) {
        new XemPhimSoDetailCrawler(null, "https://xemphimsoz.com/phim/lac-dan-19797/", null).run();
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
                if (line.contains(StringContant.XEMPHIMSO_START_TAG_OF_MOVIE_INFO)) {
                    isStart = true;
                }
                if (isStart) {
                    documentBd.append(line);
                }
                if (line.contains(StringContant.XEMPHIMSO_END_TAG_OF_MOVIE_INFO)) {
                    break;
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

            stAXparserForDetailMovie(documentBd.toString());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException e) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, e);
        }

    }


    private Map<String, String> stAXparserForDetailMovie(String document) throws XMLStreamException, UnsupportedEncodingException, XMLStreamException {
        String refinedDocument = new XmlSyntaxChecker().wellformingToXML(document);
        refinedDocument = refinedDocument.trim();
        XMLEventReader eventReader = parseStringToXMLEventReader(refinedDocument);
        Map<String, String> detailLinkMap = new HashMap<>();
        TblMovie movie = new TblMovie();
        boolean startGetDuration = false;
        boolean startGetCategory = false;
        boolean startGetCountry;
//        while (eventReader.hasNext()) {
//            XMLEvent event = (XMLEvent) eventReader.next();
//
//            if (event.isStartElement()) {
//                StartElement startElement = event.asStartElement();
//                Attribute classAttribute = startElement.getAttributeByName(new QName("class"));
//
//                String tagName = startElement.getName().getLocalPart();
//                //get image link
//                if ("img".equals(tagName) && "movie-thumb".equals(classAttribute.getValue())) {
//                    movie.setImgUrl(startElement.getAttributeByName(new QName("href")).getValue());
//                    continue;
//                }
//                if ("span".equals(tagName)) {
//                    XMLEvent valueEvent = (XMLEvent) eventReader.next();
//                    String value = valueEvent.asCharacters().getData().trim();
//                    boolean isValidAttr = Objects.nonNull(classAttribute) && classAttribute.getValue() != null
//                    if (isValidAttr) {
//                        switch (classAttribute.getValue()) {
//                            case "quality":
//                                movie.setQuality(value);
//                                continue;
//                                break;
//                            case "audio":
//                                movie.setAudio(value);
//                                continue;
//                                break;
//                            case "imdb":
//                                movie.setImdb(value);
//                                startGetDuration = true;
//                                continue;
//                                break;
//                            default:
//                        }
//                    }
//                    if (startGetDuration) {
//                        eventReader.next();
//                        XMLEvent durationEvent = (XMLEvent) eventReader.next();
//                        Characters durationCharacter = durationEvent.asCharacters();
//                        if (Objects.nonNull(durationCharacter) && durationCharacter.getData() != null) {
//                            String durationStr = durationCharacter.getData();
//                            String[] durationArr = durationStr.split(" ");
//                            int duration;
//                            try {
//                                duration = Integer.parseInt(durationArr[3]);
//                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
//                                duration = 0;
//                            }
//                            movie.setDuration(duration);
//                        }
//                        startGetDuration = false;
//                        startGetCategory = true;
//                    }
//                    if (startGetCategory) {
//                        eventReader.next();
//                        eventReader.next();
//                        XMLEvent categoryEvent = (XMLEvent) eventReader.next();
//                        StartElement categoryElement = categoryEvent.asStartElement();
//                        Attribute categoryLink = categoryElement.getAttributeByName(new QName("href"));
//                        //TODO:find id of cate by link and add to relation
//                        startGetCategory = false;
//                        startGetCountry = true;
//                    }
//                    if(startGetCategory){
//                        eventReader.next();
//                        eventReader.next();
//                        XMLEvent categoryEvent = (XMLEvent) eventReader.next();
//                        StartElement countryElement = categoryEvent.asStartElement();
//                        Attribute countryLink = countryElement.getAttributeByName(new QName("href"));
//                        //TODO:find id of cate by link and add to relation
//                        startGetCountry = false;
//                    }
//                }
//            }
//
//        }


        while()
        return detailLinkMap;
    }
}

/*                XemPhimSoCategoriesCrawler categoriesCrawler = new XemPhimSoCategoriesCrawler(context);
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
*/
