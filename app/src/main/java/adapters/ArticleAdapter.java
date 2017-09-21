package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.codepath.nytimesapp.R;
import com.example.codepath.nytimesapp.activities.ArticleActivity;
import com.example.codepath.nytimesapp.activities.SearchActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Doc;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.bufferType;
import static android.R.attr.font;
import static android.R.attr.switchMinWidth;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import static java.security.AccessController.getContext;

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


        /*
        View articleView = inflater.inflate(R.layout.item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;*/

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Doc article = articles.get(position);

        switch (holder.getItemViewType()) {
            case TEXT:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.title.setText(article.getHeadline().getMain());
                textViewHolder.desk.setText(article.getNewsDesk());
                textViewHolder.beginDate.setText(article.getPubDate());

                break;
            case MULTIMEDIA:

                MultimediaViewHolder multimediaViewHolder = (MultimediaViewHolder) holder;
                multimediaViewHolder.title.setText(article.getHeadline().getMain());
                String dateArticle = article.getPubDate();

               // multimediaViewHolder.beginDate.setText(day);

                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateArticle);
                    String formattedDate = new SimpleDateFormat()
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // multimediaViewHolder.beginDate.setText(article.getPubDate());

                if (!TextUtils.isEmpty(article.newsDesk)){

                    multimediaViewHolder.desk.setText(article.getNewsDesk());
                    multimediaViewHolder.desk.setVisibility(View.VISIBLE);
                    multimediaViewHolder.desk.setBackgroundColor(ContextCompat.getColor(context, article.colorId));
                } else {
                    multimediaViewHolder.desk.setVisibility(View.GONE);
                }

                Log.d("VER", String.valueOf(article.getNewsDesk()));
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


/*
        Doc article = articles.get(position);

        //Set items
        holder.multimedia.setImageResource(0);
        holder.title.setText(article.getHeadline().getMain());
        if(!article.hasImage()){
            holder.multimedia.setVisibility(View.GONE);
           // holder.title.setText;
            return;
        }

        Glide.with(context).load(article.getImageAddress())
                .fitCenter()
                .placeholder(R.drawable.ic_download)
                .into(holder.multimedia);

    }*/

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
                    Toast.makeText(context, "The item is: " + getPosition(), Toast.LENGTH_LONG).show();

                    String url = articles.get(position).webUrl;

                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
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
                    Toast.makeText(context, "The item is: " + getPosition(), Toast.LENGTH_LONG).show();

                    String url = articles.get(position).webUrl;

                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                }
            });

            ButterKnife.bind(this, itemView);
        }



        }
    }

   /* public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_multimedia)
        ImageView multimedia;
        @Bind(R.id.tv_title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        @Override
        public void onClick(View view) {

        }
    }*/

