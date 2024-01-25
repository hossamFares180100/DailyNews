package com.example.dailynews.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dailynews.MainActivity;
import com.example.dailynews.R;
import com.example.dailynews.SearchFragmentListener;
import com.example.dailynews.SearchTopics;
import com.example.dailynews.TopicEventListener;
import com.example.dailynews.adapters.LeftRecyclerSearchTopicsAdapter;
import com.example.dailynews.adapters.RightRecyclerSearchTopicsAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    RecyclerView leftRecyclerView , rightRecyclerView;
    LeftRecyclerSearchTopicsAdapter leftRecyclerSearchTopicsAdapter;
    RightRecyclerSearchTopicsAdapter rightRecyclerSearchTopicsAdapter;
    ArrayList<SearchTopics> leftSearchTopicsArrayList , rightSearchTopicsArrayList;
    CardView cardView;
    SearchFragmentListener searchFragmentListener;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        progressBar = view.findViewById(R.id.search_progress_bar);
        cardView = view.findViewById(R.id.top_headlines_card);
        progressBar.setVisibility(View.VISIBLE);
        fillLeftArrayList();
        fillRightArrayList();
        setLeftRecyclerView(view);
        setRightRecyclerView(view);
        setLeftSideClick();
        setRightSideClick();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragmentListener.onDataSent("");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigationHome);

            }
        });
        return view ;
    }
    private void fillLeftArrayList(){
        leftSearchTopicsArrayList = new ArrayList<>();
        leftSearchTopicsArrayList.add(new SearchTopics("Business",R.drawable.business,"business"));
        leftSearchTopicsArrayList.add(new SearchTopics("Sports",R.drawable.sports , "sports"));
        leftSearchTopicsArrayList.add(new SearchTopics("Entertainment",R.drawable.entartinment,"entertainment"));

    }
    private void fillRightArrayList(){
        rightSearchTopicsArrayList = new ArrayList<>();
        rightSearchTopicsArrayList.add(new SearchTopics("Health",R.drawable.health , "health"));
        rightSearchTopicsArrayList.add(new SearchTopics("Science",R.drawable.science,"science"));
        rightSearchTopicsArrayList.add(new SearchTopics("Technology",R.drawable.technology , "technology"));

    }
    private void setLeftRecyclerView(View view){
        leftRecyclerView = view.findViewById(R.id.left_rv);
        leftRecyclerSearchTopicsAdapter = new LeftRecyclerSearchTopicsAdapter(leftSearchTopicsArrayList);
        leftRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        leftRecyclerView.setAdapter(leftRecyclerSearchTopicsAdapter);

    }
    private void setRightRecyclerView(View view){
        rightRecyclerView = view.findViewById(R.id.right_rv);
        rightRecyclerSearchTopicsAdapter = new RightRecyclerSearchTopicsAdapter(rightSearchTopicsArrayList);
        rightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rightRecyclerView.setAdapter(rightRecyclerSearchTopicsAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void setLeftSideClick(){
        leftRecyclerSearchTopicsAdapter.setListener(new TopicEventListener() {
            @Override
            public void onItemClick(String topic) {
                searchFragmentListener.onDataSent(topic);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigationHome);

            }
        });
    }
    private void setRightSideClick(){
        rightRecyclerSearchTopicsAdapter.setListener(new TopicEventListener() {
            @Override
            public void onItemClick(String topic) {
                searchFragmentListener.onDataSent(topic);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigationHome);

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SearchFragmentListener){
            searchFragmentListener = (SearchFragmentListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement SearchFragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchFragmentListener = null;
    }
}