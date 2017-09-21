package com.example.codepath.nytimesapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.codepath.nytimesapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Doc;

import static android.os.Build.VERSION_CODES.O;

public class ArticleActivity extends AppCompatActivity {


    Doc article;

    @Bind(R.id.wv_article)
    WebView webViewArticle;

    MenuItem mShare;

    /*@Bind(R.id.toolbar_detail)
    Toolbar toolbar;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

        webViewArticle.setWebViewClient(new MyBrowser());

        webViewArticle.loadUrl(getIntent().getStringExtra("url"));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article, menu);

        mShare = menu.findItem(R.id.share);
        mShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                shareArticle();
                Log.i("DEBUG", "CLICK");
                return true;
            }
        });




        return super.onCreateOptionsMenu(menu);

        //MenuItem searchItem = menu.findItem(R.id.action_search);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }

        if(id == R.id.share){
            shareArticle();
            Log.i("DEBUG", "CLICK2");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareArticle() {
        String url = article.getWebUrl();

        if (url != null) {
            Intent shareIntent = new Intent();
            shareIntent.setType("text/plain");
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject_prefix_share_action) + article.getHeadline());
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.title_share_action)));
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_trailer_share_actiton) + article.getHeadline(), Toast.LENGTH_LONG).show();
        }
    }
}
