package com.example.codepath.nytimesapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by gretel on 9/21/17.
 */

public class ArticleDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ArticleDBHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "articles.db";

    public static final int DATABASE_VERSION = 1;

    public static String DB_PATH = "";

    private SQLiteDatabase myDatabase;

    private final Context mContext;


    public ArticleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
         if (Build.VERSION.SDK_INT >= 15){
             DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
         } else {
             DB_PATH = Environment.getDataDirectory() + "/data" + context.getPackageName() + "/databases/";
         }

         this.mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create a string that contains the SQL statement to create the task table
        String CREATE_TABLE = "CREATE TABLE " + ArticleContract.ArticleEntry.TABLE_NAME + " ("
                + ArticleContract.ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ArticleContract.ArticleEntry.ARTICLE_TITLE + " TEXT NOT NULL, "
                + ArticleContract.ArticleEntry.ARTICLE_URL + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ArticleContract.ArticleEntry.TABLE_NAME);
        }catch (SQLiteException e){

        }
        onCreate(sqLiteDatabase);


    }

    public void checkAndCopyDatabase() {
        boolean dbExist = checkDatabase();
        if(dbExist) {
            Log.d("TAG", "database already exists");
        } else {
            this.getReadableDatabase();
        }

    }

    public boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DATABASE_NAME;
        checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        if (checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }



    public synchronized void close(){
        if (myDatabase != null) {
            myDatabase.close();
        }
        super.close();
    }

    public Cursor queryData(String query){
        return myDatabase.rawQuery(query, null);
    }



}
