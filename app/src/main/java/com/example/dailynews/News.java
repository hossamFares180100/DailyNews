package com.example.dailynews;

import java.io.Serializable;

public class News implements Serializable {

    String img_news,img_source;
    String title_source,date_source,title_news , description , newsUrl,id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public News (){

    }

    public News(String img_news, String img_source, String title_source, String date_source, String title_news, String description, String newsUrl) {
        this.img_news = img_news;
        this.img_source = img_source;
        this.title_source = title_source;
        this.date_source = date_source;
        this.title_news = title_news;
        this.description = description;
        this.newsUrl = newsUrl;
    }

    public String getImg_source() {
        return img_source;
    }

    public void setImg_source(String img_source) {
        this.img_source = img_source;
    }

    public String getImg_news() {
        return img_news;
    }

    public void setImg_news(String img_news) {
        this.img_news = img_news;
    }

    public String getTitle_source() {
        return title_source;
    }

    public void setTitle_source(String title_source) {
        this.title_source = title_source;
    }

    public String getDate_source() {
        return date_source;
    }

    public void setDate_source(String date_source) {
        this.date_source = date_source;
    }

    public String getTitle_news() {
        return title_news;
    }

    public void setTitle_news(String title_news) {
        this.title_news = title_news;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
