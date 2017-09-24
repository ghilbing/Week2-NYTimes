package com.example.codepath.nytimesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.codepath.nytimesapp.R;
import com.example.codepath.nytimesapp.activities.ArticleActivity;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.codepath.nytimesapp.database.DataBean;
import com.example.codepath.nytimesapp.models.Doc;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.codepath.nytimesapp.activities.ArticleActivity.EXTRA;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gretel on 9/19/17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Doc> articles;

    private final int TEXT = 0;
    private final int MULTIMEDIA = 1;





    public ArticleAdapter(Context context, List<Doc> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public int getItemViewType(int position){
        Doc article = articles.get(position);
        if(TextUtils.isEmpty(article.getImageAddress())){
            return TEXT;
        }

        return MULTIMEDIA;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case TEXT:
                View viewText = inflater.inflate(R.layout.item_text_only, parent, false);
                viewHolder = new TextViewHolder(viewText);
                break;
            case MULTIMEDIA:
                View viewMultimedia = inflater.inflate(R.layout.item_article, parent, false);
                viewHolder = new MultimediaViewHolder(viewMultimedia);
                break;
            default:
                viewMultimedia = inflater.inflate(R.layout.item_article, parent, false);
                viewHolder = new MultimediaViewHolder(viewMultimedia);
                break;
        }

        return viewHolder;



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Doc article = articles.get(position);

        switch (holder.getItemViewType()) {
            case TEXT:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.title.setText(article.getHeadline().getMain());
                //textViewHolder.desk.setText(article.getNewsDesk());
                String date = article.getPubDate().toString().trim();
                String subString = date.substring(0, 10);
                textViewHolder.beginDate.setText(subString);
                textViewHolder.desk.setText(article.setTextDesk());

                int colorNewsDesk = article.setColorId();
                Log.i("COLOR TEXT", String.valueOf(colorNewsDesk));


                if(!TextUtils.isEmpty(article.newsDesk)){

                    Log.i("Is NULL?", article.newsDesk);
                    textViewHolder.desk.setVisibility(View.VISIBLE);
                    textViewHolder.desk.setBackgroundResource(colorNewsDesk);
                    Log.i("VERIFY", article.newsDesk);
                }

                break;
            case MULTIMEDIA:

                MultimediaViewHolder multimediaViewHolder = (MultimediaViewHolder) holder;
                multimediaViewHolder.title.setText(article.getHeadline().getMain());
                String dateM = article.getPubDate().toString().trim();
                String subStringM = dateM.substring(0, 10);
                multimediaViewHolder.beginDate.setText(subStringM);
                multimediaViewHolder.desk.setText(article.setTextDesk());

                int colorNewsDeskM = article.setColorId();
                Log.i("COLOR TEXT", String.valueOf(colorNewsDeskM));


                if(!TextUtils.isEmpty(article.newsDesk)){

                    Log.i("Is NULL?", article.newsDesk);
                    multimediaViewHolder.desk.setVisibility(View.VISIBLE);
                    multimediaViewHolder.desk.setBackgroundResource(colorNewsDeskM);
                    Log.i("VERIFY", article.newsDesk);
                }



                Log.d("NEWS DESK", String.valueOf(article.setTextDesk()));
                Glide.with(context)
                        .load(article.getImageAddress())
                        .fitCenter()
                        .placeholder(R.drawable.ic_download)
                        .into(multimediaViewHolder.multimedia);

                break;
            default:
                MultimediaViewHolder multimViewHolder = (MultimediaViewHolder) holder;
                multimViewHolder.title.setText(article.getHeadline().getMain());
                multimViewHolder.beginDate.setText(article.getPubDate());
                multimViewHolder.desk.setText(article.getNewsDesk());
                Glide.with(context)
                        .load(article.getImageAddress())
                        .fitCenter()
                        .placeholder(R.drawable.ic_download)
                        .into(multimViewHolder.multimedia);
                break;


        }
    }



    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_title_to)
        TextView title;
        @Bind(R.id.tv_desk)
        TextView desk;
        @Bind(R.id.tv_beginDate)
        TextView beginDate;

        public TextViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    String url = articles.get(position).getWebUrl();
                   // String title = articles.get(position).getHeadline().getMain();

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

                    CustomTabsIntent customTabsIntent = builder.build();
                    int color = context.getResources().getColor(R.color.primary);
                    builder.setToolbarColor(color);
                    builder.setShowTitle(true);
                    builder.addDefaultShareMenuItem();
                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_navigation_arrow_back));

                    customTabsIntent.launchUrl(context, Uri.parse(url));



                    /*Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("title", title);
                    context.startActivity(intent);*/
                }
            });

            ButterKnife.bind(this, itemView);
        }

    }

    public class MultimediaViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.iv_multimedia)
        ImageView multimedia;
        @Bind(R.id.tv_desk)
        TextView desk;
        @Bind(R.id.tv_beginDate)
        TextView beginDate;

        public MultimediaViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    String url = articles.get(position).getWebUrl();
                  //  String title = articles.get(position).getHeadline().getMain();

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

                    CustomTabsIntent customTabsIntent = builder.build();
                    int color = context.getResources().getColor(R.color.primary);
                    builder.setToolbarColor(color);
                    builder.setShowTitle(true);
                    builder.addDefaultShareMenuItem();
                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_navigation_arrow_back));

                    customTabsIntent.launchUrl(context, Uri.parse(url));

                   // Intent intent = new Intent(context, ArticleActivity.class);
                   // intent.putExtra(EXTRA, Parcels.wrap(articles));
                   // intent.putExtra("url", url);
                   // intent.putExtra("title", title);
                   // context.startActivity(intent);
                }
            });

            ButterKnife.bind(this, itemView);
        }



        }
    }


