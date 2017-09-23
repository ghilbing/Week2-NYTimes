package com.example.codepath.nytimesapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by gretel on 9/21/17.
 */

public class ArticleProvider extends ContentProvider {

    public static final String LOG_TAG = ArticleProvider.class.getSimpleName();

    //URI matcher code for the



    //URI matcher code for the content URI for the table
    private static final int ARTICLES = 100;
    //URI matcher code for the content URI for a single row in the table
    private static final int ARTICLE_ID = 101;

    //UriMatcher object to match a content URI to a corresponding code
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_ARTICLES, ARTICLES);
        sUriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_ARTICLES + "/#", ARTICLE_ID);

    }

    //Database helper object
    private ArticleDBHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new ArticleDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //Get readable com.example.codepath.nytimesapp.database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        //Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch (match){
            case ARTICLES:
                //For the TASKS code, query the tasks table directly with the given projection, selection, selection arguments
                //and sort order. The cursor could contain multiple rows of the task table

                cursor = database.query(ArticleContract.ArticleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case ARTICLE_ID:
                //For the TASK_ID code, extract out the ID from the URI
                selection = ArticleContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                //This will perform a query on the task table to return a Cursor containing a determined row of the table
                cursor = database.query(ArticleContract.ArticleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }

        //Set notification URI on the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLES:
                return insertArticle(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    //Insert a task into the com.example.codepath.nytimesapp.database with the given content values. Return the new content URI for that specific row in the com.example.codepath.nytimesapp.database.
    private Uri insertArticle(Uri uri, ContentValues values){

        //Check that the name is not null
        String title = values.getAsString(ArticleContract.ArticleEntry.ARTICLE_TITLE);
        if(title == null) {
            throw new IllegalArgumentException("Article requires a title");

        }

        //Check that the date is not null
        String url = values.getAsString(ArticleContract.ArticleEntry.ARTICLE_URL);
        if(url == null){
            throw new IllegalArgumentException("Article requires a url");

        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Insert the new item with the given values
        long id = database.insert(ArticleContract.ArticleEntry.TABLE_NAME, null, values);
        //If the ID is -1, then the insertion failed. Log an error and return null
        if(id == -1){

            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listeners that the data has changed for the task content URI
        getContext().getContentResolver().notifyChange(uri, null);

        //Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLES:
                //Delete all rows
                rowsDeleted = database.delete(ArticleContract.ArticleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ARTICLE_ID:
                //Delete a single row given by the ID in the URI
                selection = ArticleContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ArticleContract.ArticleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        //If 1 or more rows were deleted, then notify all listeners that the data at the given URI has changed
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLES:
                return updateArticle(uri, contentValues, selection, selectionArgs);
            case ARTICLE_ID:
                selection = ArticleContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateArticle(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updateArticle(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){

        if(contentValues.containsKey(ArticleContract.ArticleEntry.ARTICLE_TITLE)){
            String name = contentValues.getAsString(ArticleContract.ArticleEntry.ARTICLE_TITLE);
            if (name == null){
                throw new IllegalArgumentException("Article requires a title");
            }
        }

        if(contentValues.containsKey(ArticleContract.ArticleEntry.ARTICLE_URL)){
            String date = contentValues.getAsString(ArticleContract.ArticleEntry.ARTICLE_URL);
            if (date == null){
                throw new IllegalArgumentException("Article requires a URL");
            }
        }


        //If there are no values to update, then do not try to update the com.example.codepath.nytimesapp.database
        if (contentValues.size() == 0){
            return 0;
        }
        //Otherwise, get writable com.example.codepath.nytimesapp.database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(ArticleContract.ArticleEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLES:
                return ArticleContract.ArticleEntry.CONTENT_LIST_TYPE;
            case ARTICLE_ID:
                return ArticleContract.ArticleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);

        }

    }
}
