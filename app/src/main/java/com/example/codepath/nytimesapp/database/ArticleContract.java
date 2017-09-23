package com.example.codepath.nytimesapp.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gretel on 9/21/17.
 */

public class ArticleContract {

    private ArticleContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.codepath.nytimesapp.database";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ARTICLES = "articles";



    //Define the Version table

    public static final class ArticleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARTICLES);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;

        //Define table name

        public static final String TABLE_NAME = "articles";

        //Define table columns

        public final static String _ID = BaseColumns._ID;
        public final static String ARTICLE_TITLE = "title";
        public final static String ARTICLE_URL = "url";

    }

}
