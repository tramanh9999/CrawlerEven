package entities;

public class TblMovie {
    String imgUrl;

    public TblMovie(String imgUrl, String title1, String title2, String quality, String audio, String imdb, String duration, String country, int publishYear, int vote) {
        this.imgUrl = imgUrl;
        this.title1 = title1;
        this.title2 = title2;
        this.quality = quality;
        this.audio = audio;
        this.imdb = imdb;
        this.duration = duration;
        this.country = country;
        this.publishYear = publishYear;
        this.vote = vote;
    }

    String title1;
    String title2;
    String quality;
    String audio;
    String imdb;
    int duration;
    String country;
    //category director, actor in anouther table
    int publishYear;
    int vote;

    public TblMovie() {
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


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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
}
