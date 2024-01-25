package com.example.dailynews.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dailynews.FavoriteFragmentListener;
import com.example.dailynews.News;
import com.example.dailynews.OnFavoriteItemListener;
import com.example.dailynews.R;
import com.example.dailynews.adapters.RecycleFavNewsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Favorites extends Fragment  {
    static ArrayList<News> news;
    static RecycleFavNewsAdapter recycleFavNewsAdapter=null;
    RecyclerView recyclerView=null;
    ProgressBar progressBar;
    DatabaseReference mDatabase;
    FavoriteFragmentListener favoriteFragmentListener;
    FirebaseAuth mAuth;


    public Favorites() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FavoriteFragmentListener){
            favoriteFragmentListener = (FavoriteFragmentListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement SearchFragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        favoriteFragmentListener= null;
    }


    public void getFav(){

        mDatabase=FirebaseDatabase.getInstance().getReference("news").child(mAuth.getCurrentUser().getUid()).child("favourite");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                news.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    News n= dataSnapshot.getValue(News.class);
                    news.add(n);
                }
                recycleFavNewsAdapter=new RecycleFavNewsAdapter(news,getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(recycleFavNewsAdapter);
                progressBar.setVisibility(View.GONE);
                recycleFavNewsAdapter.setOnClickListenerItem(new OnFavoriteItemListener() {
                    @Override
                    public void onDelete(int position) {
                        deleteFavNews(position);
                    }

                    @Override
                    public void onClick(News news) {
                        onNewsClicked(news);
                    }

                    @Override
                    public void share(News news) {
                        onShareClicked(news);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void filter(String item) {
        ArrayList<News>NSearch=new ArrayList<>();
        for (News n : news) {
            if (n.getTitle_news().toLowerCase().contains(item.toLowerCase())) {
                NSearch.add(n);
            }
        }
        recycleFavNewsAdapter.filterList(NSearch);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference("news").child(mAuth.getCurrentUser().getUid()).child("favourite");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favorites, container, false);
        news=new ArrayList<>();
        progressBar=view.findViewById(R.id.pro_bar_fav);
        recyclerView=view.findViewById(R.id.rv_fav);
        getFav();
        return view;
    }

    public void deleteFavNews(int pos){
        mDatabase.child(news.get(pos).getId()).removeValue();
        getFav();
    }
    public void onNewsClicked(News news){
        favoriteFragmentListener.onClick(news);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new ViewNews()).commit();
    }
    public void onShareClicked(News news){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,news.getNewsUrl());
        intent.setType("text/plain");
        Intent intent1=Intent.createChooser(intent,null);
        startActivity(intent1);
    }


}