package com.example.codepath.nytimesapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.codepath.nytimesapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.codepath.nytimesapp.database.ArticleContract;
import com.example.codepath.nytimesapp.database.ArticleDBHelper;
//import com.example.codepath.nytimesapp.database.ArticleProvider;
import com.example.codepath.nytimesapp.database.ArticlesSaved;

public class ArticleActivity extends AppCompatActivity {

    public static final String EXTRA = "article";

    @Bind(R.id.wv_article)
    WebView webViewArticle;

    Menu menu;

    ArticlesSaved articlesSaved;
    ArticleDBHelper articleDBHelper;

    MenuItem mShare;
    MenuItem mAdd;

    private Uri mCurrentArticleUri;


    //@Bind(R.id.toolbar_detail)
    //Toolbar toolbar;

     //String url = getIntent().getStringExtra("url");
     //String title = getIntent().getStringExtra("title");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
       // getSupportActionBar().setTitle(article.getHeadline().toString());

        ButterKnife.bind(this);



        webViewArticle.getSettings().setLoadsImagesAutomatically(true);
        webViewArticle.getSettings().setJavaScriptEnabled(true);
        webViewArticle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //Responsive Layout

        webViewArticle.getSettings().setUseWideViewPort(true);
        webViewArticle.getSettings().setLoadWithOverviewMode(true);
        webViewArticle.getSettings().setSupportZoom(true);
        webViewArticle.getSettings().setBuiltInZoomControls(true);
        webViewArticle.getSettings().setDisplayZoomControls(false);

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        Intent intent = getIntent();

        mCurrentArticleUri = intent.getData();



        //this.articlesSaved = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA));


        webViewArticle.setWebViewClient(new MyBrowser());



        webViewArticle.loadUrl(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.share) {
            shareArticle();

            return true;
        }

        if (id == R.id.add) {
            saveArticle();
        }

        if (id == R.id.action_saved_articles) {

            Intent intent = new Intent(this, SavedArticlesActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }



    private void saveArticle() {


            //Read from extra

            String url = getIntent().getStringExtra("url").toString().trim();
            String title = getIntent().getStringExtra("title").toString().trim();

            //Create ContentValues objects where columns are the keys and the extras are the values


            ContentValues values = new ContentValues();
            values.put(ArticleContract.ArticleEntry.ARTICLE_TITLE, title);
            values.put(ArticleContract.ArticleEntry.ARTICLE_URL, url);

            Uri newUri = getContentResolver().insert(ArticleContract.ArticleEntry.CONTENT_URI, values);

            if (newUri == null) {

                Toast.makeText(this, getString(R.string.insert_row_failed), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, getString(R.string.insert_row_successfull), Toast.LENGTH_LONG).show();
            }


    }

    private void deleteArticle(){

        if(mCurrentArticleUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentArticleUri, null, null);

            if(rowsDeleted == 0){
                Toast.makeText(this, getString(R.string.delete_failed), Toast.LENGTH_LONG).show();
            }

            else{
                Toast.makeText(this, getString(R.string.delete_successfull), Toast.LENGTH_LONG).show();
            }

        }

        finish();

    }

    private void shareArticle() {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        if (url != null) {
            Intent shareIntent = new Intent();
            shareIntent.setType("text/plain");
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject_prefix_share_action) + title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.title_share_action)));
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_trailer_share_actiton) + title, Toast.LENGTH_LONG).show();
        }
    }
}
