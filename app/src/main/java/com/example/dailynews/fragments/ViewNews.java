package com.example.dailynews.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailynews.R;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.http.Url;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewNews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewNews extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    WebView webView;
    static String url;
    TextView textView;
    static String description;


    private String mParam1;
    private String mParam2;

    public ViewNews() {
        // Required empty public constructor
    }


    public static ViewNews newInstance(String param1, String param2) {
        ViewNews fragment = new ViewNews();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_view_news, container, false);
        webView=view.findViewById(R.id.web_news);
        textView = view.findViewById(R.id.description_off);
        if(haveNetwork()){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            textView.setVisibility(View.GONE);
        }else{
            webView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(description);
            Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_LONG).show();
        }

        return view;
    }

    public static void instance(String l){
        url=l;
    }
    public static void getDescription(String desc){
        description = desc;
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
}