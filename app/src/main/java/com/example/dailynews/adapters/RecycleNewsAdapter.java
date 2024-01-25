package com.example.dailynews.adapters;

import android.annotation.SuppressLint;
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
import com.example.dailynews.OnClickListenerItem;
import com.example.dailynews.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleNewsAdapter extends RecyclerView.Adapter<RecycleNewsAdapter.NewsViewHolder> {

    ArrayList<News>list_news=null;
    Context context;
    OnClickListenerItem onClickListenerItem;

    public RecycleNewsAdapter(ArrayList<News> list_news, Context context, OnClickListenerItem onClickListenerItem) {
        this.list_news = list_news;
        this.context = context;
        this.onClickListenerItem = onClickListenerItem;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_news,null,false);
        return new NewsViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news=list_news.get(position);
        Glide.with(context).load(news.getImg_news()).placeholder(R.drawable.in).error(R.drawable.breaking_news).into(holder.imageView);
        Glide.with(context).load(news.getImg_source()).placeholder(R.drawable.in).error(R.drawable.breaking_news).into(holder.circleImageView);
        holder.txt_tit_so.setText(news.getTitle_source());
        holder.txt_news.setText(news.getTitle_news());
        holder.txt_date.setText(news.getDate_source());



    }

    @Override
    public int getItemCount() {
        return list_news.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        ImageView imageView;
        TextView txt_tit_so,txt_date,txt_news;
        ImageView btn_like;
        ImageView share;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.img_source);
            imageView=itemView.findViewById(R.id.img_news);
            txt_tit_so=itemView.findViewById(R.id.title_source);
            txt_date=itemView.findViewById(R.id.date_source);
            txt_news=itemView.findViewById(R.id.title_news);
            btn_like=itemView.findViewById(R.id.like_news);
            share=itemView.findViewById(R.id.share_news);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListenerItem.onClick(list_news.get(getAdapterPosition()));
                }
            });
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListenerItem.like(list_news.get(getAdapterPosition()));
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListenerItem.share(list_news.get(getAdapterPosition()));
                }
            });
        }
    }

    public void filterList(ArrayList<News>n){
        list_news=n;
        notifyDataSetChanged();
    }


}
