package crawler;

import checker.XmlSyntaxChecker;
import constant.StringContant;
import entities.TblMovie;
import utilities.VNCharacterUtils;

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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
@Xml
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
        new XemPhimSoDetailCrawler(null, "https://xemphimsoz.com/phim/tinh-yeu-va-tham-vong-19171/", null).run();
        new XemPhimSoDetailCrawler(null, "https://xemphimsoz.com/phim/quai-kiet-sieu-hang-tvb-19733/", null).run();
        new XemPhimSoDetailCrawler(null, "https://xemphimsoz.com/phim/duyen-tinh-oan-trai-19660/", null).run();
        new XemPhimSoDetailCrawler(null, "https://xemphimsoz.com/phim/quai-kiet-sieu-hang-tvb-19733/", null).run();
        new XemPhimSoDetailCrawler(null, "https://xemphimsoz.com/phim/vu-dong-can-khon-niet-ban-than-thach-19794/", null).run();
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            StringBuilder documentBd = new StringBuilder();
            StringBuilder movieContent = new StringBuilder();
            boolean isStart = false;
            boolean isFoundMovieContent = false;
            boolean isStopGetMovieContent = false;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                if (line.contains(StringContant.XEMPHIMSO_START_TAG_OF_MOVIE_INFO)) {
                    isStart = true;
                }
                if (isStart) {
                    documentBd.append(line);
                }
                if (line.contains(StringContant.XEMPHIMSO_END_TAG_OF_MOVIE_INFO)) {
                    break;
                }
                if (line.contains(StringContant.XEMPHIMSO_END_TAG_OF_MOVIE_CONTENT)) {
                    isStopGetMovieContent = true;
                }
                if (isFoundMovieContent && !isStopGetMovieContent) {
                    movieContent.append(line);
                }
                if (line.contains(StringContant.XEMPHIMSO_START_TAG_MOVIE_CONTENT)) {
                    isFoundMovieContent = true;
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

            TblMovie movie = stAXparserForDetailMovie(documentBd.toString());
            //TODO get content of film
            movie.setIntroduction(movieContent.toString());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException e) {
            Logger.getLogger(XemPhimSoEachPageCrawler.class.getName()).log(Level.SEVERE, null, e);
        }

    }


    private TblMovie stAXparserForDetailMovie(String document) throws XMLStreamException, UnsupportedEncodingException, XMLStreamException {
        String refinedDocument = new XmlSyntaxChecker().wellformingToXML(document);
        refinedDocument = refinedDocument.trim();
        XMLEventReader eventReader = parseStringToXMLEventReader(refinedDocument);
        Map<String, String> detailLinkMap = new HashMap<>();
        TblMovie movie = new TblMovie();
        List<Integer> categoryIds = new ArrayList<>();
        List<Integer> actorIds = new ArrayList<>();

        boolean isFoundLink = false;
        while (eventReader.hasNext()) {
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                //first found image , then set flag = true
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                boolean foundATagImg = "img".equals(tagName);
                if (!isFoundLink) {
                    if (foundATagImg) {
                        Attribute imgClassAttr = startElement.getAttributeByName(new QName("class"));
                        boolean isHasClassValue = Objects.nonNull(imgClassAttr) && "movie-thumb".equals(imgClassAttr.getValue());
                        if (isHasClassValue) {
                            Attribute imgLinkAttr = startElement.getAttributeByName(new QName("src"));
                            if (Objects.nonNull(imgLinkAttr)) {
                                movie.setImgUrl(imgLinkAttr.getValue());
                                isFoundLink = true;
                                continue;
                            }
                        }
                    }
                }
                Attribute classAttr = startElement.getAttributeByName(new QName("class"));

                if (Objects.nonNull(classAttr)) {
                    String valueofClassAttribute = classAttr.getValue();
                    if ("a".equals(tagName) && "title-1".equals(valueofClassAttribute)) {
                        Attribute titleAttr = startElement.getAttributeByName(new QName("title"));
                        movie.setTitle1(titleAttr.getValue());
                        continue;
                    }
                    if ("h2".equals(tagName) && "title-2".equals(valueofClassAttribute)) {
                        Characters title2Character = ((XMLEvent) eventReader.next()).asCharacters();
                        movie.setTitle2(title2Character.getData());
                    }
                }
                boolean isFoundListItem = "li".equals(tagName);
                if (isFoundListItem) {
                    eventReader.next();
                    XMLEvent eventCharacter = (XMLEvent) eventReader.next();
                    if (eventCharacter.isCharacters()) {
                        String movieProperties = VNCharacterUtils.removeAccent(eventCharacter.asCharacters().getData().toLowerCase());
                        switch (movieProperties) {
                            case "phu de":
                                eventReader.next();
                                eventReader.next();
                                XMLEvent subTitle = (XMLEvent) eventReader.next();
                                if (subTitle.isCharacters()) {
                                    movie.setSubTitle(subTitle.asCharacters().getData());
                                    continue;
                                }
                                break;
                            case "chat luong":
                                eventReader.next();
                                eventReader.next();
                                eventReader.next();
                                XMLEvent quality = (XMLEvent) eventReader.next();
                                if (quality.isCharacters()) {
                                    //find and get id of quality
                                    movie.setQuality(quality.asCharacters().getData());
                                    continue;
                                }
                                break;
                            case "diem imdb":
                                eventReader.next();//to end tag span
                                eventReader.next();// to ":   "
                                eventReader.next();//to the <span class="imdb">
                                XMLEvent imdb = (XMLEvent) eventReader.next();
                                if (imdb.isCharacters()) {
                                    movie.setImdb(imdb.asCharacters().getData());
                                    continue;
                                }
                                break;
                            case "ngon ngu":
                                eventReader.next();
                                eventReader.next();
                                eventReader.next();
                                XMLEvent audio = (XMLEvent) eventReader.next();
                                if (audio.isCharacters()) {
                                    movie.setAudio(audio.asCharacters().getData());
                                    continue;
                                }
                                break;
                            case "thoi luong":
                                eventReader.next();
                                XMLEvent duration = (XMLEvent) eventReader.next();
                                if (duration.isCharacters()) {
                                    movie.setDuration(duration.asCharacters().getData());
                                    continue;
                                }
                                break;
                            case "the loai":
                                eventReader.next();//to the end tag </span>
                                eventReader.next();//to the " :  "
                                while (eventReader.hasNext()) {
                                    XMLEvent categoryLinkEle = (XMLEvent) eventReader.next();
                                    if (categoryLinkEle.isEndElement()) {
                                        XMLEvent endElement = categoryLinkEle.asEndElement();
                                        if ("li".equals(endElement.asEndElement().getName().getLocalPart())) {
//                                            isEndGetCate = true;
                                            break;

                                        }
                                    }
                                    if (categoryLinkEle.isStartElement()) {
                                        StartElement ctelinkElement = categoryLinkEle.asStartElement();
                                        boolean isTagCategoryLink = "a".equals(ctelinkElement.getName().getLocalPart());
                                        if (isTagCategoryLink) {
                                            StartElement categoryLinkElement = ctelinkElement.asStartElement();
                                            Attribute linkAttr = categoryLinkElement.getAttributeByName(new QName("href"));
                                            //find by link to get id, set in somewhere to add into relation table
                                            if (Objects.nonNull(linkAttr)) {
                                                String cte = linkAttr.getValue();
                                                System.out.println("cte: " + cte);
                                                //int cteId= tblCategory.findByLink();
                                                //categoryIds.add(cteId);
                                            }
                                        }
                                    }
                                }
                                break;
                            case "quoc gia":
                                eventReader.next();
                                eventReader.next();
                                XMLEvent country = (XMLEvent) eventReader.next();
                                if (country.isStartElement()) {
                                    StartElement countryElement = country.asStartElement();
                                    Attribute countryLink = countryElement.getAttributeByName(new QName("href"));
                                    movie.setCountry(Objects.nonNull(countryLink) ? countryLink.getValue() : StringContant.EMPTY_STRING);
                                    continue;
                                }
                                break;
                            case "dien vien":
                                eventReader.next();
                                eventReader.next();
                                while (eventReader.hasNext()) {
                                    XMLEvent actor = (XMLEvent) eventReader.next();
                                    if (actor.isStartElement()) {
                                        StartElement actorElement = actor.asStartElement();
                                        boolean isTagActorLink = "a".equals(actorElement.getName().getLocalPart());
                                        if (isTagActorLink) {
                                            StartElement actorLink = actorElement.asStartElement();
                                            Attribute linkAttr = actorLink.getAttributeByName(new QName("href"));
                                            //find by link to get id, set in somewhere to add into relation table
                                            if (Objects.nonNull(linkAttr)) {
                                                String link = linkAttr.getValue();
                                                System.out.println("actor:" + link);
                                                //int cteId= tblActor.findByLink(link);
                                                //if cteId== 0 -> insert new dien vien -else- actorIds.add(cteId);
                                            }
                                        }
                                    }

                                    if (actor.isEndElement()) {
                                        XMLEvent endElement = actor.asEndElement();
                                        if ("li".equals(endElement.asEndElement().getName().getLocalPart())) {
                                            break;
                                        }
                                    }

                                }
                                break;
                            case "dao dien":
                                eventReader.next();
                                eventReader.next();
                                while (eventReader.hasNext()) {
                                    XMLEvent director = (XMLEvent) eventReader.next();

                                    if (director.isEndElement()) {
                                        XMLEvent endElement = director.asEndElement();
                                        if ("li".equals(endElement.asEndElement().getName().getLocalPart())) {
//                                            isEndGetCate = true;
                                            break;

                                        }
                                    }

                                    if (director.isStartElement()) {

                                        StartElement directorElement = director.asStartElement();
                                        boolean isTagDirectorLink = "a".equals(directorElement.getName().getLocalPart());
                                        if (isTagDirectorLink) {
                                            StartElement directorLink = directorElement.asStartElement();
                                            Attribute linkAttr = directorLink.getAttributeByName(new QName("href"));
                                            //find by link to get id, set in somewhere to add into relation table
                                            if (Objects.nonNull(linkAttr)) {
                                                String link = linkAttr.getValue();
                                                System.out.println("direct: " + link);
                                                //int cteId= tblDirector.findByLink(link);
                                                //if cteId== 0 -> insert new dien vien -else- directorIds.add(cteId);
                                            }
                                        }
                                    }
                                }
                                break;
                            case "nam phat hanh":
                                eventReader.next();//to end tag </span>
                                eventReader.next();//to ":   "
                                XMLEvent publishYearEvent = (XMLEvent) eventReader.next(); // to the publist year tag
                                StartElement yearElement = publishYearEvent.asStartElement();
                                Attribute yearAttribute = yearElement.getAttributeByName(new QName("title"));
                                String yeatrStr = Objects.nonNull(yearAttribute) ? yearAttribute.getValue() : StringContant.EMPTY_STRING;
                                try {
                                    int year = Integer.parseInt(yeatrStr);
                                    movie.setPublishYear(year);
                                    continue;
                                } catch (NumberFormatException e) {
                                    movie.setPublishYear(StringContant.DEFAULT_YEAR_IF_CANT_PARSE);
                                }
                                break;
                            default:

                                //save raltion cte + movie
                                //save raltion actor + movie
                                //save raltion diẻcor+ movie
                        }
                    }
                }
            } else if (event.isEndElement()) {
                if ("section".equals(event.asEndElement().getName().getLocalPart())) {
                    System.out.println(movie);
                    //TODO save movie
                    //get id , save relation with
                    //save raltion cte + movie
                    //save raltion actor + movie
                    //save raltion diẻcor+ movie
                }
            }
        }
        return movie;
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
