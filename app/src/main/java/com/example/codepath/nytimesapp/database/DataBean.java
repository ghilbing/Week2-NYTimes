package com.example.codepath.nytimesapp.database;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gretel on 9/21/17.
 */

public class DataBean {
    protected int id;
    protected String title;
    protected String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DataBean(int id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    Cursor cursor;

    public DataBean getData(String title){

        DataBean bean = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry._ID);
            int titleIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.ARTICLE_TITLE);
            int urlIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.ARTICLE_URL);

            int id = cursor.getInt(idIndex);
            String titleString = cursor.getString(titleIndex);
            String urlString = cursor.getString(urlIndex);

            bean = new DataBean(id, titleString, urlString);

        }
        return bean;
    }

    public List<DataBean> getAllData() {
        List<DataBean> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry._ID);
            int titleIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.ARTICLE_TITLE);
            int urlIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.ARTICLE_URL);

            int cid = cursor.getInt(idIndex);
            String titleString = cursor.getString(titleIndex);
            String urlString = cursor.getString(urlIndex);

            DataBean bean = new DataBean(cid, titleString, urlString);
            list.add(bean);
        }
        return list;
    }

}
