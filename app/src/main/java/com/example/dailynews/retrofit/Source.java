package com.example.dailynews.retrofit;

import com.google.gson.annotations.SerializedName;

public class Source  {
    @SerializedName("name")
    String name;

    public Source(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
