package com.example.codepath.nytimesapp.database;

/**
 * Created by gretel on 9/21/17.
 */

public class ArticlesSaved {

    long id;
    String webUrl;
    String headline;



    public ArticlesSaved(long id, String webUrl, String headline) {
        this.id = id;
        this.webUrl = webUrl;
        this.headline = headline;

    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWebUrl() {
        return this.webUrl;
    }

    public String getHeadline() {
        return this.headline;
    }



}
