package com.example.dailynews;

public interface OnFavoriteItemListener {
    public void onDelete(int position);
    public void onClick(News news);
    public void share(News news);
}
