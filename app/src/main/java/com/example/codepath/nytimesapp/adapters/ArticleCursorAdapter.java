package com.example.codepath.nytimesapp.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.codepath.nytimesapp.R;
import com.example.codepath.nytimesapp.database.ArticleContract;
import com.example.codepath.nytimesapp.database.ArticleDBHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gretel on 9/22/17.
 */

public class ArticleCursorAdapter extends
        RecyclerView.Adapter<ArticleCursorAdapter.VH> {

    private Cursor mCursor;
    private OnArticleClickListener mListener;

    public ArticleCursorAdapter(OnArticleClickListener listener) {
        mListener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved, parent, false);

        final VH vh = new VH(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vh.getAdapterPosition();
                mCursor.moveToPosition(position);
                if (mListener != null) mListener.itemClicked(mCursor);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        mCursor.moveToPosition(position);
        int idx_title = mCursor.getColumnIndex(ArticleContract.ArticleEntry.ARTICLE_TITLE);
        int idx_url = mCursor.getColumnIndex(ArticleContract.ArticleEntry.ARTICLE_URL);

        String title = mCursor.getString(idx_title);
        String url = mCursor.getString(idx_url);

        holder.tv_title_saved.setText(title);
        holder.tv_url.setText(url);
    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                int idx_id = mCursor.getColumnIndex(ArticleContract.ArticleEntry._ID);
                return mCursor.getLong(idx_id);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void setCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface OnArticleClickListener {
        void itemClicked(Cursor cursor);
    }

    public static class VH extends RecyclerView.ViewHolder {


        @Bind(R.id.tv_title_saved)
        TextView tv_title_saved;

        @Bind(R.id.tv_url)
        TextView tv_url;

        public VH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
