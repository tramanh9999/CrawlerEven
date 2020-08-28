package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@Table(name = "TblMovie", catalog = "OscarJpa", schema = "dbo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Movie", propOrder = {})
@XmlSeeAlso(TblMovie.class)
@XmlRootElement(name = "MovieType", namespace = "www.movie.com")
@NamedQueries({
        @NamedQuery(name = "TblMovie.findAll", query = "select t from TblMovie t"),
        @NamedQuery(name = "TblMovie.findByMovieId", query = "select t from TblMovie t"),
        @NamedQuery(name = "TblMovie.findByMovieName", query = "select t from TblMovie t where t.title1 like " +
                "concat('%', :movieName, '%')")
        ,})

public class TblMovie {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "movieID", nullable = false)
    private Long movieID;
    @XmlElement(name="imgUrl", namespace = "www.movie.vn", required = true)
            @Column(name ="imgUrl", length = 1000)
    String imgUrl;

    String title1;
    String title2;
    String quality;
    String audio;
    String imdb;
    String duration;
    String introduction;
    boolean isActive;
    String country;
    int publishYear;

    //category director, actor in anouther table
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    int vote;
    String subTitle;

    public TblMovie() {
    }


    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "TblMovie{" +
                "imgUrl='" + imgUrl + '\'' +
                ", title1='" + title1 + '\'' +
                ", title2='" + title2 + '\'' +
                ", quality='" + quality + '\'' +
                ", audio='" + audio + '\'' +
                ", imdb='" + imdb + '\'' +
                ", duration='" + duration + '\'' +
                ", country='" + country + '\'' +
                ", publishYear=" + publishYear +
                ", vote=" + vote +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }

    public TblMovie(String imgUrl, String title1, String title2, String quality, String audio, String imdb, String duration, String introduction, String country, int publishYear, int vote, String subTitle) {
        this.imgUrl = imgUrl;
        this.title1 = title1;
        this.title2 = title2;
        this.quality = quality;
        this.audio = audio;
        this.imdb = imdb;
        this.duration = duration;
        this.introduction = introduction;
        this.country = country;
        this.publishYear = publishYear;
        this.vote = vote;
        this.subTitle = subTitle;
    }

    public void setMovieID(Long movieID) {
        this.movieID = movieID;
    }

    @Id
    public Long getMovieID() {
        return movieID;
    }
}
