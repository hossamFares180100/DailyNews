package com.example.dailynews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dailynews.News;
import com.example.dailynews.OnFavoriteItemListener;
import com.example.dailynews.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleFavNewsAdapter extends RecyclerView.Adapter<RecycleFavNewsAdapter.NewsFavViewHolder> {
    ArrayList<News> list_news=null;
    Context context;
   OnFavoriteItemListener onClickListenerItem;

    public RecycleFavNewsAdapter(ArrayList<News> list_news, Context context) {
        this.list_news = list_news;
        this.context = context;
    }

    public void setOnClickListenerItem(OnFavoriteItemListener onClickListenerItem) {
        this.onClickListenerItem = onClickListenerItem;
    }

    @NonNull
    @Override
    public NewsFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.fav_layout,null,false);
        return new RecycleFavNewsAdapter.NewsFavViewHolder(view,onClickListenerItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFavViewHolder holder, int position) {
        News news=list_news.get(position);
        Glide.with(context).load(news.getImg_news()).into(holder.circleImageView);
        Glide.with(context).load(news.getImg_news()).into(holder.imageView);
        holder.txt_tit_so.setText(news.getTitle_source());
        holder.txt_news.setText(news.getTitle_news());
        holder.txt_date.setText(news.getDate_source());
    }

    @Override
    public int getItemCount() {
        return list_news.size();
    }

    public class NewsFavViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        ImageView imageView;
        TextView txt_tit_so,txt_date,txt_news;
        ImageView btn_del;
        ImageView share;
        public NewsFavViewHolder(@NonNull View itemView, OnFavoriteItemListener onFavoriteItemListener) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.img_source_fav);
            imageView=itemView.findViewById(R.id.img_news_fav);
            txt_tit_so=itemView.findViewById(R.id.title_source_fav);
            txt_date=itemView.findViewById(R.id.date_source_fav);
            txt_news=itemView.findViewById(R.id.title_news_fav);
            btn_del=itemView.findViewById(R.id.like_news_fav);
            share=itemView.findViewById(R.id.share_news_fav);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteItemListener.onClick(list_news.get(getAdapterPosition()));
                }
            });
            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteItemListener.onDelete(getAdapterPosition());
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteItemListener.share(list_news.get(getAdapterPosition()));
                }
            });

        }
    }
    public void filterList(ArrayList<News>n){
        list_news=n;
        notifyDataSetChanged();
    }

}
