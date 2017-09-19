package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.codepath.nytimesapp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Doc;

import static android.R.attr.font;

/**
 * Created by gretel on 9/19/17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    private List<Doc> articles;



    public ArticleAdapter(Context context, List<Doc> articles) {
        this.context = context;
        this.articles = articles;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View articleView = inflater.inflate(R.layout.item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
    }
}
