package com.example.codepath.nytimesapp.fragments;

import android.app.VoiceInteractor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codepath.nytimesapp.R;
import com.example.codepath.nytimesapp.adapters.ArticleCursorAdapter;
import com.example.codepath.nytimesapp.database.ArticleContract;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gretel on 9/22/17.
 */

public class ListArticlesSavedFragment extends Fragment
            implements LoaderManager.LoaderCallbacks<Cursor> {

        @Bind(R.id.recycler_view)
        RecyclerView mRecyclerView;

       // private RecyclerView mRecyclerView;
        private ArticleCursorAdapter mAdapter;
        private LinearLayoutManager mLayoutManager;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_saved_articles, container, false);
            ButterKnife.bind(this, view);


            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            configuraSwipe();

            mLayoutManager = new LinearLayoutManager(getActivity());

            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new ArticleCursorAdapter(new ArticleCursorAdapter.OnArticleClickListener() {
                @Override
                public void itemClicked(Cursor cursor) {
                    //launch webview again
                }
            });

            mAdapter.setHasStableIds(true);
            mRecyclerView.setAdapter(mAdapter);

            getLoaderManager().initLoader(0, null, this);

            return view;
        }

        private void configuraSwipe() {
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                    0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    final int x = viewHolder.getLayoutPosition();
                    Cursor cursor = mAdapter.getCursor();
                    cursor.moveToPosition(x);
                    final long id = cursor.getLong(cursor.getColumnIndex(ArticleContract.ArticleEntry._ID));
                    getActivity().getContentResolver().delete(
                            Uri.withAppendedPath(ArticleContract.BASE_CONTENT_URI, String.valueOf(id)),
                            null, null);
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), ArticleContract.BASE_CONTENT_URI, null, null, null, ArticleContract.ArticleEntry.ARTICLE_TITLE);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.setCursor(data);
        }

        @Override
        public void onLoaderReset(Loader loader) {
            mAdapter.setCursor(null);
        }
    }


