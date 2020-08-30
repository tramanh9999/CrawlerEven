package entities;

import javax.xml.bind.annotation.XmlElement;

public class Movie {

    @XmlElement
    Long id;
    String subTitle;
    int vote;
    private String imgUrl;
    private String title1;
    private String title2;
    private String quality;
    private String audio;
    private String imdb;
    private String duration;
    private String introduction;
    private boolean isActive;
    private String country;
    private int publishYear;

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

    public Movie() {
    }

    public Movie(String imgUrl, String title1, String title2, String quality, String audio, String imdb, String duration, String introduction, String country, int publishYear, int vote, String subTitle) {
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
}
