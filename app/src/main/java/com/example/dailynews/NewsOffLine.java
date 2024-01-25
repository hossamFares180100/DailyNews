package com.example.dailynews;

public class NewsOffLine {
    byte[] img_source,img_news;
    String title_source,date_source,title_news , description;

    public NewsOffLine( String title_news, String title_source, String date_source, String description,byte[] img_source, byte[] img_news) {
        this.img_source = img_source;
        this.img_news = img_news;
        this.title_source = title_source;
        this.date_source = date_source;
        this.title_news = title_news;
        this.description = description;
    }
    public NewsOffLine(){

    }
    public NewsOffLine( String title_news, String title_source, String date_source, String description) {
        this.title_source = title_source;
        this.date_source = date_source;
        this.title_news = title_news;
        this.description = description;
    }

    public byte[] getImg_source() {
        return img_source;
    }

    public void setImg_source(byte[] img_source) {
        this.img_source = img_source;
    }

    public byte[] getImg_news() {
        return img_news;
    }

    public void setImg_news(byte[] img_news) {
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
}
