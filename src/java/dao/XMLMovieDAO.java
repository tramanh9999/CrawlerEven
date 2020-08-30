package dao;

import constant.StringContant;
import entities.Movie;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLMovieDAO extends BaseDao<Movie, Long> {
    public XMLMovieDAO() {
    }

    private static final Object LOCK = new Object();

    private static XMLMovieDAO instance;

    public static XMLMovieDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new XMLMovieDAO();

            }
            return instance;
        }
    }

    public synchronized void writeXMLMoviesToFile(Movie movie) {
        try {
            create(movie);
            System.out.printf("Validate ok %s%n", movie.getTitle1());


        } catch (Exception validateFailedException) {
            System.out.printf("Validate failed %s%n", movie.getTitle1());
//            Logger.getLogger(XMLMovieDAO.class.getName()).log(Level.SEVERE, "", validateFailedException);
        }
    }
    @Override
    public Movie create(Movie movie) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory;
        DocumentBuilder dBuilder;
        Document doc = null;
        Element movieElement = null;


        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        try {
            doc = dBuilder.parse(StringContant.MOVIE_XML_PATH);
        } catch (IllegalArgumentException | FileNotFoundException e) {
            doc = dBuilder.newDocument();
            movieElement = doc.createElement("movie");
            doc.appendChild(movieElement);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        short type = doc.getElementsByTagName("movie").item(0).getNodeType();
        if (type == Node.ELEMENT_NODE) {
            movieElement = (Element) doc.getElementsByTagName("movie").item(0);
        }


        Element title1 = doc.createElement("title1");
        title1.appendChild(doc.createTextNode(movie.getTitle1()));
        movieElement.appendChild(title1);

        Element title2 = doc.createElement("title2");
        title2.appendChild(doc.createTextNode(movie.getTitle2()));
        movieElement.appendChild(title2);
        Element subTitle = doc.createElement("subTitle");
        subTitle.appendChild(doc.createTextNode(movie.getSubTitle()));
        movieElement.appendChild(subTitle);
        Element audio = doc.createElement("audio");
        audio.appendChild(doc.createTextNode(movie.getAudio()));
        movieElement.appendChild(audio);
        Element imdb = doc.createElement("imdb");
        imdb.appendChild(doc.createTextNode(movie.getImdb()));
        movieElement.appendChild(imdb);
        Element vote = doc.createElement("vote");
        vote.appendChild(doc.createTextNode(movie.getVote() + ""));
        movieElement.appendChild(vote);
        Element publishYear = doc.createElement("publishYear");
        publishYear.appendChild(doc.createTextNode(movie.getCountry()));
        movieElement.appendChild(publishYear);
        Element country = doc.createElement("country");
        country.appendChild(doc.createTextNode(movie.getCountry()));
        movieElement.appendChild(country);
//        Element isActive = doc.createElement("isActive");
//        isActive.appendChild(doc.createTextNode(movie.isActive() + ""));
//        movieElement.appendChild(isActive);
        Element introduction = doc.createElement("introduction");
        introduction.appendChild(doc.createTextNode(movie.getIntroduction()));
        movieElement.appendChild(introduction);
        Element duration = doc.createElement("duration");
        duration.appendChild(doc.createTextNode(movie.getDuration()));
        movieElement.appendChild(duration);


        //document prolog
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, StringContant.MOVIE_DTD_PATH);

        DOMSource source = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(StringContant.MOVIE_XML_PATH));
        transformer.transform(source, streamResult);
        return movie;
    }
}
