package com.example.codepath.nytimesapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.codepath.nytimesapp.R;
import com.example.codepath.nytimesapp.activities.SavedArticlesActivity;
import com.example.codepath.nytimesapp.database.ArticleContract;
import com.example.codepath.nytimesapp.database.DataBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gretel on 9/21/17.
 */

public class DataBeanAdapter extends RecyclerView.Adapter<DataBeanAdapter.ViewHolder>{


    public List<DataBean> itemsSaved = Collections.emptyList();
    public Context mContext;
    private OnTapListener onTapListener;


    public DataBeanAdapter(List<DataBean> items, Context context){
        this.itemsSaved = items;
        this.mContext = context;
    }

    public void setItems(final List<DataBean> items){
        itemsSaved.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DataBean item = itemsSaved.get(position);
        holder.titleSaved.setText(item.getTitle());
        holder.urlSaved.setText(item.getUrl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onTapListener != null){
                    onTapListener.onTapView(position);
                }

            }
        });

        //All the thing you gonna show in the item
    }

    @Override
    public int getItemCount() {
        return itemsSaved.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title_saved)
        TextView titleSaved;
        @Bind(R.id.tv_url)
        TextView urlSaved;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void setOnTapListener(OnTapListener onTapListener){
        this.onTapListener = onTapListener;
    }

}