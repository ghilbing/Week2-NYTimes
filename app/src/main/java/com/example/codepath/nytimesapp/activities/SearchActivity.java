package com.example.codepath.nytimesapp.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.codepath.nytimesapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.example.codepath.nytimesapp.adapters.ArticleAdapter;
import com.example.codepath.nytimesapp.adapters.EndlessRecyclerViewScrollListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.codepath.nytimesapp.fragments.FiltersFragment;
import com.example.codepath.nytimesapp.models.Doc;
import com.example.codepath.nytimesapp.models.Filters;
import com.example.codepath.nytimesapp.models.SearchApiResponse;
import com.example.codepath.nytimesapp.rest.SearchApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Bind(R.id.recycler)
    RecyclerView mRecycler;

    /*@Bind(R.id.et_query)
    EditText query;

    @Bind(R.id.btn_search)
    Button search;*/

    private String mQueryString;


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
        checkConnection();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //Inflate menu
       /* getMenuInflater().inflate(R.menu.menu_item, menu);

        return super.onCreateOptionsMenu(menu);*/

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryString = query;

                // perform query here
                articles.clear();
                mAdapter.notifyDataSetChanged();
                onArticleSearch(0, mQueryString);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               // onArticleSearch(0);

                return false;
            }
        });

        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = searchView.findViewById(searchEditId);
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if (id == R.id.action_filter){
            showEditDialog();
        }

       /* if (id == R.id.action_saved_articles) {

            Intent intent = new Intent(this, SavedArticlesActivity.class);
            startActivity(intent);

        }*/

        return super.onOptionsItemSelected(item);

    }


    private void setupViews() {

     //   ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.recycler);

        //Setting the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search...");
       // toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        mAdapter = new ArticleAdapter(this, articles);
        mRecycler.setAdapter(mAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(), layoutManager.getOrientation());
        DividerItemDecoration verticalItemDecoration = new DividerItemDecoration(mRecycler.getContext(), DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.vertical_divider);
        verticalItemDecoration.setDrawable(verticalDivider);

        mRecycler.addItemDecoration(verticalItemDecoration);
       // mRecycler.addItemDecoration(dividerItemDecoration);
        mRecycler.setLayoutManager(layoutManager);

        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                onArticleSearch(page, mQueryString);
            }
        };

        mRecycler.addOnScrollListener(mScrollListener);


    }

    public void checkConnection(){
        if(!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), "Network connectivity problem", Toast.LENGTH_LONG).show();

        } else if (!isOnline()){
            Toast.makeText(getApplicationContext(), "Your device is not online, check WIFI", Toast.LENGTH_LONG).show();

        } else {
            onArticleSearch(0, mQueryString);
        }


    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }


    public void onArticleSearch(final int page, String mQueryString){

        //filters.setQuery(this.query.getText().toString());

        filters.setQuery(mQueryString);


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
        onArticleSearch(0, mQueryString);
    }

    private void showEditDialog() {
        final FragmentManager fm = getSupportFragmentManager();
        final FiltersFragment frag = FiltersFragment.newInstance(filters);
        frag.setEditorFiltersDialogListener(new FiltersFragment.EditFiltersDialogListener() {
            @Override
            public void onFinishedEditFilters(Filters newFilters) {
                frag.dismiss();
                filters = newFilters;
                clearArticles();
                onArticleSearch(0, mQueryString);
            }
        });
        frag.show(fm, "fragment_edit_filters");
    }

    public void onFilterActionBarItemClick(MenuItem item) {
        showEditDialog();
    }
}
