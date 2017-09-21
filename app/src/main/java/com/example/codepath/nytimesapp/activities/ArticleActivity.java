package com.example.codepath.nytimesapp.activities;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.codepath.nytimesapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Doc;

import static android.os.Build.VERSION_CODES.O;

public class ArticleActivity extends AppCompatActivity {


    Doc article;

    @Bind(R.id.wv_article)
    WebView webViewArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

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
}
