package com.example.dailynews.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseClass {
    @SerializedName("articles")
    List<Articles> articles;
    @SerializedName("totalResults")
    int totalResults;



    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
