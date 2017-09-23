package com.example.codepath.nytimesapp.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.codepath.nytimesapp.R;
import com.example.codepath.nytimesapp.adapters.ArticleAdapter;
import com.example.codepath.nytimesapp.adapters.DataBeanAdapter;
import com.example.codepath.nytimesapp.database.ArticleContract;
import com.example.codepath.nytimesapp.database.ArticleDBHelper;
import com.example.codepath.nytimesapp.database.ArticleProvider;
import com.example.codepath.nytimesapp.database.ArticlesSaved;
import com.example.codepath.nytimesapp.database.DataBean;
import com.example.codepath.nytimesapp.models.Doc;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SavedArticlesActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);


    }




}


