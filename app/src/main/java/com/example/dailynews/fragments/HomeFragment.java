package com.example.dailynews.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dailynews.HomeFragmentListener;
import com.example.dailynews.News;
import com.example.dailynews.NewsDB;
import com.example.dailynews.NewsOffLine;
import com.example.dailynews.OnClickListenerItem;
import com.example.dailynews.OnClickOffListenerItem;
import com.example.dailynews.R;
import com.example.dailynews.adapters.RecycleNewsAdapter;
import com.example.dailynews.adapters.RecycleOffNewsAdapter;
import com.example.dailynews.retrofit.ApiClient;
import com.example.dailynews.retrofit.ApiInterface;
import com.example.dailynews.retrofit.BaseClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class HomeFragment extends Fragment implements OnClickListenerItem, OnClickOffListenerItem {
    static ArrayList<News> newsArrayList;
    static RecycleNewsAdapter recycleNewsAdapter=null;
    RecyclerView recyclerView=null;
    static ArrayList<NewsOffLine> dataBaseArrayList;
    private static String nameOfType="";
    ProgressBar progressBar;
    HomeFragmentListener homeFragmentListener;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String id;
    NewsDB newsDB=null;
    static ArrayList<NewsOffLine> newsOffLineArrayList;
    static RecycleOffNewsAdapter recycleOffNewsAdapter=null;
    String name;
    String date;
    boolean check = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof HomeFragmentListener){
            homeFragmentListener = (HomeFragmentListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement SearchFragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeFragmentListener= null;
    }


    public static void filter(String item) {
        ArrayList<News>NSearch=new ArrayList<>();
        for (News n : newsArrayList) {
            if (n.getTitle_news().toLowerCase().contains(item.toLowerCase())) {
                NSearch.add(n);
            }
        }
        recycleNewsAdapter.filterList(NSearch);
    }
    public static void filterOff(String item) {
        ArrayList<NewsOffLine> NSearch=new ArrayList<>();
        for (NewsOffLine n :newsOffLineArrayList ) {
            if (n.getTitle_news().toLowerCase().contains(item.toLowerCase())) {
                NSearch.add(n);
            }
        }
        recycleOffNewsAdapter.filterList(NSearch);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("news").child(mAuth.getCurrentUser().getUid());
        newsDB=new NewsDB(getContext());
        newsOffLineArrayList =new ArrayList<>();
        newsArrayList = new ArrayList<>();
        dataBaseArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar=view.findViewById(R.id.pro_bar);
        recyclerView=view.findViewById(R.id.rv_news);
        if(haveNetwork()){
            if(newsArrayList.isEmpty()) {
                getData(getContext(), this);
                deleteOfflineDataWhileOnline();
            }
        }else{
            getAllDataWhileOffline(getContext(),this);
        }

        return view;
    }

    private void getAllDataWhileOffline(Context context, OnClickOffListenerItem onClickOffListenerItem) {
        if(nameOfType.equals("business")){
            newsOffLineArrayList.addAll(newsDB.getAllBusiness());
        }else if(nameOfType.equals("sports")){
            newsOffLineArrayList.addAll(newsDB.getAllSports());
        }else if(nameOfType.equals("entertainment")){
            newsOffLineArrayList.addAll(newsDB.getAllEntertainment());
        }else if(nameOfType.equals("health")){
            newsOffLineArrayList.addAll(newsDB.getAllHealth());
        }else if(nameOfType.equals("science")){
            newsOffLineArrayList.addAll(newsDB.getAllScience());
        }else if(nameOfType.equals("technology")){
            newsOffLineArrayList.addAll(newsDB.getAllTechnology());
        }else{
            newsOffLineArrayList.addAll(newsDB.getAllHeadline());
        }
        recycleOffNewsAdapter=new RecycleOffNewsAdapter(newsOffLineArrayList,context,onClickOffListenerItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(recycleOffNewsAdapter);
    }

    private void deleteOfflineDataWhileOnline() {
        if(nameOfType.equals("business")){
            newsDB.deleteBun();
        }else if(nameOfType.equals("sports")){
            newsDB.deleteSpo();
        }else if(nameOfType.equals("entertainment")){
            newsDB.deleteEnt();
        }else if(nameOfType.equals("health")){
            newsDB.deleteHea();
        }else if(nameOfType.equals("science")){
            newsDB.deleteSci();
        }else if(nameOfType.equals("technology")){
            newsDB.deleteTec();
        }
        else{
            newsDB.deleteHead();
        }
    }

    public void getData(Context context , OnClickListenerItem onClickListenerItem){
        dataBaseArrayList = new ArrayList<>();
        newsArrayList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseClass> call = apiInterface.getNews("eg",nameOfType,"11cbb68249454fdb90edc5086f6fd24a");
        call.enqueue(new Callback<BaseClass>() {
            @Override
            public void onResponse(Call<BaseClass> call, Response<BaseClass> response) {
                recycleNewsAdapter=new RecycleNewsAdapter(newsArrayList,context,onClickListenerItem);
                for (int i =0; i <20; i++) {
                    String img_source1 = getImgSource(response.body().getArticles().get(i).getNewsUrl());
                    String img_news1= response.body().getArticles().get(i).getImageUrl();
                    String nameTitle=response.body().getArticles().get(i).getSources().getName();
                    name="";
                    String date_url = response.body().getArticles().get(i).getDateUrl();
                    date="";
                    for (int k=0;k<nameTitle.length();k++){
                        if(nameTitle.charAt(k) == '.'  ){
                            break;
                        }
                        else {
                            name+=nameTitle.charAt(k);
                        }
                    }
                    for (int k=0;k<date_url.length();k++){
                        if(date_url.charAt(k) == 'T'  ){
                            date+=" ";
                            continue;
                        }
                        else if(date_url.charAt(k) == 'Z') {
                            break;
                        }else {
                            date+=date_url.charAt(k);
                        }

                    }
                    String titleNews = response.body().getArticles().get(i).getTitle();
                    String description = response.body().getArticles().get(i).getDescription();
                    String newsUrl = response.body().getArticles().get(i).getNewsUrl();
                    newsArrayList.add(new News(img_news1,img_source1,name,date,titleNews,description,newsUrl));
                    dataBaseArrayList.add(new NewsOffLine(titleNews,name,date,description));
                    recycleNewsAdapter.notifyItemInserted(i);
                    //Insert To Database
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(recycleNewsAdapter);
                recyclerView.setHasFixedSize(true);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<BaseClass> call, Throwable t) {
                Log.e("lol",t.getMessage());
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);

                for(int i =0; i<20;i++){
                    if(getActivity()==null){
                        return;
                    }
                    String img_news1 = newsArrayList.get(i).getImg_news();
                    String img_source1 = newsArrayList.get(i).getImg_source();
                    int finalI = i;
                    CustomTarget<Bitmap> theBitmap = Glide.with(getActivity()).asBitmap().load(img_news1).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            dataBaseArrayList.get(finalI).setImg_news(getBytesImg(resource));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
                    CustomTarget<Bitmap> theBitmap1 = Glide.with(getActivity()).asBitmap().load(img_source1).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            dataBaseArrayList.get(finalI).setImg_source(getBytesImg(resource));

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        for(int i =0; i<20;i++){
                            if(dataBaseArrayList.get(i).getImg_source() == null){
                                continue;

                           }
                            if(dataBaseArrayList.get(i).getImg_news() == null){
                                continue;
                            }
                            if(getActivity()==null){
                                return;
                            }
                            if(nameOfType.equals("business")){
                                newsDB.insertBusiness(dataBaseArrayList.get(i));
                            }else if(nameOfType.equals("sports")){
                                newsDB.insertSports(dataBaseArrayList.get(i));
                            }else if(nameOfType.equals("entertainment")){
                                newsDB.insertEntertainment(dataBaseArrayList.get(i));
                            }else if(nameOfType.equals("health")){
                                newsDB.insertHealth(dataBaseArrayList.get(i));
                            }else if(nameOfType.equals("science")){
                                newsDB.insertScience(dataBaseArrayList.get(i));
                            }else if(nameOfType.equals("technology")){
                                newsDB.insertTechnology(dataBaseArrayList.get(i));
                            }else{
                                newsDB.insertHeadline(dataBaseArrayList.get(i));

                            }
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                },5000);

            }
        },5000);



    }

    public static void upadteData(String topic){
        nameOfType = topic;
    }

    @Override
    public void onClick(News news) {
        homeFragmentListener.sendUrl(news.getNewsUrl());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new ViewNews()).commit();
    }

    @Override
    public void share(News news) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,news.getNewsUrl());
        intent.setType("text/plain");
        Intent intent1=Intent.createChooser(intent,null);
        startActivity(intent1);
    }

    @Override
    public void like(News news) {
        checkFavNews(news);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(check){
                    Snackbar.make(getView(),"Added Before...!",Snackbar.LENGTH_LONG).show();
                }
                else {
                    id = mDatabase.push().getKey();
                    news.setId(id);
                    mDatabase.child(id).setValue(news).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        },1000);


    }
    void checkFavNews(News news){
        check = false;
        mDatabase=FirebaseDatabase.getInstance().getReference("news").child(mAuth.getCurrentUser().getUid()).child("favourite");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    News n = dataSnapshot.getValue(News.class);
                    if (n.getTitle_news().equals(news.getTitle_news())) {
                        check = true;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            });

    }




    public String getImgSource(String uri){
        int k=0;
        String new_url="";
        String url=uri;
        for(int i=0;i<url.length();i++){
            if(url.charAt(i)=='/' && k<3){
                k++;
                continue;
            }else if(k==3){
                break;
            }
            else{
                new_url+=url.charAt(i);
            }
        }
        new_url+="/favicon.ico";
        return new_url;
    }
    public byte[] getBytesImg(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0, stream);
        return stream.toByteArray();
    }
    public boolean haveNetwork(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager =(ConnectivityManager)getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }

    @Override
    public void onClick(NewsOffLine news) {
        homeFragmentListener.sendDescription(news.getDescription());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new ViewNews()).commit();

    }
}