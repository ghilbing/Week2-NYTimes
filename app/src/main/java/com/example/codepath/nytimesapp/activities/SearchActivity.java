package com.example.codepath.nytimesapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.codepath.nytimesapp.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import adapters.ArticleAdapter;
import adapters.EndlessRecyclerViewScrollListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import models.Doc;
import models.Filters;
import models.SearchApiResponse;
import rest.SearchApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.search_view)
    MaterialSearchView searchView;

    @Bind(R.id.recycler)
    RecyclerView mRecycler;

    @Bind(R.id.et_query)
    EditText query;

    @Bind(R.id.btn_search)
    Button search;

    EndlessRecyclerViewScrollListener mScrollListener;

    Filters filters;
    ArrayList<Doc> articles = new ArrayList<>();
    ArticleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Setup filters

        filters = new Filters("newest", new HashSet<String>(), null, "");

        setupViews();



        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                //If close SearchView, RecyclerView will return default

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //perform query here
                articles.clear();
                mAdapter.notifyDataSetChanged();



                return true;
            }



            @Override
            public boolean onQueryTextChange(String newText) {

               return false;
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;

    }

    private void setupViews() {

        ButterKnife.bind(this);

        //Setting the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search...");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        mAdapter = new ArticleAdapter(this, articles);
        mRecycler.setAdapter(mAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);

        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                onArticleSearch(page);
            }
        };

        mRecycler.addOnScrollListener(mScrollListener);


    }

    public void onArticleSearch(final int page){
        filters.setQuery(this.query.getText().toString());


        //Call asynctask to retrieve data from the api
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (page != 0){

                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

                SearchApi.search(filters, page, new Callback<SearchApiResponse>() {
                    @Override
                    public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                        SearchApiResponse body = response.body();
                        if (body == null){
                            return;
                        }

                        int articlesSize = mAdapter.getItemCount();
                        List<Doc> items = body.getResponse().getDocs();
                        articles.addAll(items);
                        mAdapter.notifyItemRangeInserted(articlesSize, items.size());

                    }

                    @Override
                    public void onFailure(Call<SearchApiResponse> call, Throwable t) {
                        t.printStackTrace();
                        Log.d("FAILURE", t.getMessage());

                    }
                });
            }
        });

    }

    private void clearArticles(){
        articles.clear();
        mAdapter.notifyDataSetChanged();
        mScrollListener.resetState();
    }

    public void onArticleSearch(View view){
        clearArticles();
        onArticleSearch(0);
    }

}
