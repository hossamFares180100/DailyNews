package com.example.dailynews;

public class SearchTopics {
    String title;
    int topicImage;
    String topicName;

    public SearchTopics(String title, int topicImage, String topicName) {
        this.title = title;
        this.topicImage = topicImage;
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopicImage() {
        return topicImage;
    }

    public void setTopicImage(int topicImage) {
        this.topicImage = topicImage;
    }
}
