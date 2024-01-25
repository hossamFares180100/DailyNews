package com.example.dailynews.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dailynews.News;
import com.example.dailynews.NewsOffLine;
import com.example.dailynews.OnClickListenerItem;
import com.example.dailynews.OnClickOffListenerItem;
import com.example.dailynews.R;
import com.like.LikeButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleOffNewsAdapter extends RecyclerView.Adapter<RecycleOffNewsAdapter.NewsOffViewHolder> {
    ArrayList<NewsOffLine> list_news=null;
    Context context;
    OnClickOffListenerItem onClickListenerItem;

    public RecycleOffNewsAdapter(ArrayList<NewsOffLine> list_news, Context context, OnClickOffListenerItem onClickListenerItem) {
        this.list_news = list_news;
        this.context = context;
        this.onClickListenerItem = onClickListenerItem;
    }

    @NonNull
    @Override
    public NewsOffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_news,null,false);
        return new RecycleOffNewsAdapter.NewsOffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsOffViewHolder holder, int position) {
        NewsOffLine news=list_news.get(position);
        Bitmap img_new = BitmapFactory.decodeByteArray(news.getImg_news(),0,news.getImg_news().length);
        Bitmap img_so = BitmapFactory.decodeByteArray(news.getImg_source(),0,news.getImg_source().length);
        Glide.with(context).load(img_new).placeholder(R.drawable.in).error(R.drawable.breaking_news).into(holder.imageView);
        Glide.with(context).load(img_so).placeholder(R.drawable.in).error(R.drawable.breaking_news).into(holder.circleImageView);
        holder.txt_tit_so.setText(news.getTitle_source());
        holder.txt_news.setText(news.getTitle_news());
        holder.txt_date.setText(news.getDate_source());
    }

    @Override
    public int getItemCount() {
        return list_news.size();
    }

    public class NewsOffViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        ImageView imageView;
        TextView txt_tit_so,txt_date,txt_news;
        ImageView btn_like;
        ImageView share;
        public NewsOffViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.img_source);
            imageView=itemView.findViewById(R.id.img_news);
            txt_tit_so=itemView.findViewById(R.id.title_source);
            txt_date=itemView.findViewById(R.id.date_source);
            txt_news=itemView.findViewById(R.id.title_news);
            btn_like=itemView.findViewById(R.id.like_news);
            btn_like.setVisibility(View.GONE);
            share=itemView.findViewById(R.id.share_news);
            share.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListenerItem.onClick(list_news.get(getAdapterPosition()));
                }
            });
        }
    }
    public void filterList(ArrayList<NewsOffLine>n){
        list_news=n;
        notifyDataSetChanged();
    }
}
