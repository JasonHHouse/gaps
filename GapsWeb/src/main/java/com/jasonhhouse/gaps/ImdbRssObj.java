package com.jasonhhouse.gaps;

public class ImdbRssObj {


    private String imdb_id;

    private String title;

    private String poster_path;

    public String getImdb_id() {
        return imdb_id;
    }

    public String getTitle() {
        return title;
    }


    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPoster_url() {
        return poster_path;
    }

    public void setPoster_url(String poster_path) {
        this.poster_path = poster_path;
    }

}