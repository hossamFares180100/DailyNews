package com.example.dailynews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailynews.R;
import com.example.dailynews.SearchTopics;
import com.example.dailynews.TopicEventListener;

import java.util.ArrayList;

public class RightRecyclerSearchTopicsAdapter extends RecyclerView.Adapter<RightRecyclerSearchTopicsAdapter.SearchTopicsViewHolder> {
    static ArrayList<SearchTopics> searchTopicsArrayList;
    TopicEventListener listener;
    public RightRecyclerSearchTopicsAdapter(ArrayList<SearchTopics> searchTopicsArrayList) {
        this.searchTopicsArrayList = searchTopicsArrayList;
    }

    public void setListener(TopicEventListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchTopicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_topics,parent,false);
        return new SearchTopicsViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTopicsViewHolder holder, int position) {
        SearchTopics searchTopics = searchTopicsArrayList.get(position);
        holder.searchTopicImage.setImageResource(searchTopics.getTopicImage());
        holder.searchTopicText.setText(searchTopics.getTitle());
    }

    @Override
    public int getItemCount() {
        return searchTopicsArrayList.size();
    }

    public static class SearchTopicsViewHolder extends RecyclerView.ViewHolder {
        ImageView searchTopicImage;
        TextView searchTopicText;
        public SearchTopicsViewHolder(@NonNull View itemView , final TopicEventListener listener) {
            super(itemView);
            searchTopicImage = itemView.findViewById(R.id.search_topic_image);
            searchTopicText = itemView.findViewById(R.id.search_topic_tv);
            searchTopicImage.setClipToOutline(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            listener.onItemClick(searchTopicsArrayList.get(pos).getTopicName());
                        }
                    }
                }
            });
        }
    }
}
