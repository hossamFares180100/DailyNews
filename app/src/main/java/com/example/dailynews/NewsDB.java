package com.example.dailynews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NewsDB extends SQLiteOpenHelper {
    static String name_db="data_db";
    static int version_db=1;
    static String title_so="title_so",date="date",title_news="title_news",description="description",img_news="img_news",img_so="img_so";
    public NewsDB(Context context){
        super(context,name_db,null,version_db);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Business ("+title_news+" TEXT PRIMARY KEY,"+title_so+" TEXT,"+date+" TEXT,"+description+" TEXT,"+img_so+" BLOB ,"+img_news+" BLOB )");
        db.execSQL("CREATE TABLE Sports ("+title_news+" TEXT PRIMARY KEY,"+title_so+" TEXT,"+date+" TEXT,"+description+" TEXT,"+img_so+" BLOB ,"+img_news+" BLOB)");
        db.execSQL("CREATE TABLE Entertainment ("+title_news+" TEXT PRIMARY KEY,"+title_so+" TEXT,"+date+" TEXT,"+description+" TEXT,"+img_so+" BLOB ,"+img_news+" BLOB )");
        db.execSQL("CREATE TABLE Health ("+title_news+" TEXT PRIMARY KEY,"+title_so+" TEXT,"+date+" TEXT,"+description+" TEXT,"+img_so+" BLOB ,"+img_news+" BLOB )");
        db.execSQL("CREATE TABLE Science ("+title_news+" TEXT PRIMARY KEY,"+title_so+" TEXT,"+date+" TEXT,"+description+" TEXT,"+img_so+" BLOB ,"+img_news+" BLOB )");
        db.execSQL("CREATE TABLE Technology ("+title_news+" TEXT PRIMARY KEY,"+title_so+" TEXT,"+date+" TEXT,"+description+" TEXT,"+img_so+" BLOB ,"+img_news+" BLOB )");
        db.execSQL("CREATE TABLE Headline ("+title_news+" TEXT PRIMARY KEY,"+title_so+" TEXT,\n"+date+" TEXT,"+description+" TEXT,"+img_so+" BLOB ,"+img_news+" BLOB)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Business");
        db.execSQL("DROP TABLE IF EXISTS Sports");
        db.execSQL("DROP TABLE IF EXISTS Entertainment");
        db.execSQL("DROP TABLE IF EXISTS Health");
        db.execSQL("DROP TABLE IF EXISTS Science");
        db.execSQL("DROP TABLE IF EXISTS Technology");
        db.execSQL("DROP TABLE IF EXISTS Headline");
        onCreate(db);
    }

    public boolean insertBusiness(NewsOffLine news){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(title_news,news.getTitle_news());
        contentValues.put(title_so,news.getTitle_source());
        contentValues.put(date,news.getDate_source());
        contentValues.put(description,news.getDescription());
        contentValues.put(img_news,news.getImg_news());
        contentValues.put(img_so,news.getImg_source());
        long re=sqLiteDatabase.insert("Business",null,contentValues);
        return re!=-1;
    }
    public boolean insertSports(NewsOffLine news){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(title_news,news.getTitle_news());
        contentValues.put(title_so,news.getTitle_source());
        contentValues.put(date,news.getDate_source());
        contentValues.put(description,news.getDescription());
        contentValues.put(img_news,news.getImg_news());
        contentValues.put(img_so,news.getImg_source());
        long re=sqLiteDatabase.insert("Sports",null,contentValues);
        return re!=-1;
    }
    public boolean insertEntertainment(NewsOffLine news){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(title_news,news.getTitle_news());
        contentValues.put(title_so,news.getTitle_source());
        contentValues.put(date,news.getDate_source());
        contentValues.put(description,news.getDescription());
        contentValues.put(img_news,news.getImg_news());
        contentValues.put(img_so,news.getImg_source());
        long re=sqLiteDatabase.insert("Entertainment",null,contentValues);
        return re!=-1;
    }
    public boolean insertHealth(NewsOffLine news){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(title_news,news.getTitle_news());
        contentValues.put(title_so,news.getTitle_source());
        contentValues.put(date,news.getDate_source());
        contentValues.put(description,news.getDescription());
        contentValues.put(img_news,news.getImg_news());
        contentValues.put(img_so,news.getImg_source());
        long re=sqLiteDatabase.insert("Health",null,contentValues);
        return re!=-1;
    }
    public boolean insertScience(NewsOffLine news){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(title_news,news.getTitle_news());
        contentValues.put(title_so,news.getTitle_source());
        contentValues.put(date,news.getDate_source());
        contentValues.put(description,news.getDescription());
        contentValues.put(img_news,news.getImg_news());
        contentValues.put(img_so,news.getImg_source());
        long re=sqLiteDatabase.insert("Science",null,contentValues);
        return re!=-1;
    }
    public boolean insertTechnology(NewsOffLine news){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(title_news,news.getTitle_news());
        contentValues.put(title_so,news.getTitle_source());
        contentValues.put(date,news.getDate_source());
        contentValues.put(description,news.getDescription());
        contentValues.put(img_news,news.getImg_news());
        contentValues.put(img_so,news.getImg_source());
        long re=sqLiteDatabase.insert("Technology",null,contentValues);
        return re!=-1;
    }
    public boolean insertHeadline(NewsOffLine news){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(title_news,news.getTitle_news());
        contentValues.put(title_so,news.getTitle_source());
        contentValues.put(date,news.getDate_source());
        contentValues.put(description,news.getDescription());
        contentValues.put(img_news,news.getImg_news());
        contentValues.put(img_so,news.getImg_source());
        long re=sqLiteDatabase.insert("Headline",null,contentValues);
        return re!=-1;
    }



    public ArrayList<NewsOffLine> getAllBusiness(){
        ArrayList<NewsOffLine>newsOffLines=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Business ",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String title_new=cursor.getString(cursor.getColumnIndex(title_news));
                String title_sou=cursor.getString(cursor.getColumnIndex(title_so));
                String dat=cursor.getString(cursor.getColumnIndex(date));
                String des=cursor.getString(cursor.getColumnIndex(description));
                byte[] img_new=cursor.getBlob(cursor.getColumnIndex(img_news));
                byte[] img_s=cursor.getBlob(cursor.getColumnIndex(img_so));
                NewsOffLine newsOffLine=new NewsOffLine(title_new,title_sou,dat,des,img_s,img_new);
                newsOffLines.add(newsOffLine);
            }while (cursor.moveToNext());
        }
        return newsOffLines;
    }
    public ArrayList<NewsOffLine> getAllSports(){
        ArrayList<NewsOffLine>newsOffLines=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Sports ",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String title_new=cursor.getString(cursor.getColumnIndex(title_news));
                String title_sou=cursor.getString(cursor.getColumnIndex(title_so));
                String dat=cursor.getString(cursor.getColumnIndex(date));
                String des=cursor.getString(cursor.getColumnIndex(description));
                byte[] img_new=cursor.getBlob(cursor.getColumnIndex(img_news));
                byte[] img_s=cursor.getBlob(cursor.getColumnIndex(img_so));
                NewsOffLine newsOffLine=new NewsOffLine(title_new,title_sou,dat,des,img_s,img_new);
                newsOffLines.add(newsOffLine);
            }while (cursor.moveToNext());
        }
        return newsOffLines;
    }
    public ArrayList<NewsOffLine> getAllEntertainment(){
        ArrayList<NewsOffLine>newsOffLines=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Entertainment ",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String title_new=cursor.getString(cursor.getColumnIndex(title_news));
                String title_sou=cursor.getString(cursor.getColumnIndex(title_so));
                String dat=cursor.getString(cursor.getColumnIndex(date));
                String des=cursor.getString(cursor.getColumnIndex(description));
                byte[] img_new=cursor.getBlob(cursor.getColumnIndex(img_news));
                byte[] img_s=cursor.getBlob(cursor.getColumnIndex(img_so));
                NewsOffLine newsOffLine=new NewsOffLine(title_new,title_sou,dat,des,img_s,img_new);
                newsOffLines.add(newsOffLine);
            }while (cursor.moveToNext());
        }
        return newsOffLines;
    }
    public ArrayList<NewsOffLine> getAllHealth(){
        ArrayList<NewsOffLine>newsOffLines=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Health ",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String title_new=cursor.getString(cursor.getColumnIndex(title_news));
                String title_sou=cursor.getString(cursor.getColumnIndex(title_so));
                String dat=cursor.getString(cursor.getColumnIndex(date));
                String des=cursor.getString(cursor.getColumnIndex(description));
                byte[] img_new=cursor.getBlob(cursor.getColumnIndex(img_news));
                byte[] img_s=cursor.getBlob(cursor.getColumnIndex(img_so));
                NewsOffLine newsOffLine=new NewsOffLine(title_new,title_sou,dat,des,img_s,img_new);
                newsOffLines.add(newsOffLine);
            }while (cursor.moveToNext());
        }
        return newsOffLines;
    }
    public ArrayList<NewsOffLine> getAllScience(){
        ArrayList<NewsOffLine>newsOffLines=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Science ",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String title_new=cursor.getString(cursor.getColumnIndex(title_news));
                String title_sou=cursor.getString(cursor.getColumnIndex(title_so));
                String dat=cursor.getString(cursor.getColumnIndex(date));
                String des=cursor.getString(cursor.getColumnIndex(description));
                byte[] img_new=cursor.getBlob(cursor.getColumnIndex(img_news));
                byte[] img_s=cursor.getBlob(cursor.getColumnIndex(img_so));
                NewsOffLine newsOffLine=new NewsOffLine(title_new,title_sou,dat,des,img_s,img_new);
                newsOffLines.add(newsOffLine);
            }while (cursor.moveToNext());
        }
        return newsOffLines;
    }
    public ArrayList<NewsOffLine> getAllTechnology(){
        ArrayList<NewsOffLine>newsOffLines=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Technology ",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String title_new=cursor.getString(cursor.getColumnIndex(title_news));
                String title_sou=cursor.getString(cursor.getColumnIndex(title_so));
                String dat=cursor.getString(cursor.getColumnIndex(date));
                String des=cursor.getString(cursor.getColumnIndex(description));
                byte[] img_new=cursor.getBlob(cursor.getColumnIndex(img_news));
                byte[] img_s=cursor.getBlob(cursor.getColumnIndex(img_so));
                NewsOffLine newsOffLine=new NewsOffLine(title_new,title_sou,dat,des,img_s,img_new);
                newsOffLines.add(newsOffLine);
            }while (cursor.moveToNext());
        }
        return newsOffLines;
    }
    public ArrayList<NewsOffLine> getAllHeadline(){
        ArrayList<NewsOffLine>newsOffLines=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Headline ",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String title_new=cursor.getString(cursor.getColumnIndex(title_news));
                String title_sou=cursor.getString(cursor.getColumnIndex(title_so));
                String dat=cursor.getString(cursor.getColumnIndex(date));
                String des=cursor.getString(cursor.getColumnIndex(description));
                byte[] img_new=cursor.getBlob(cursor.getColumnIndex(img_news));
                byte[] img_s=cursor.getBlob(cursor.getColumnIndex(img_so));
                NewsOffLine newsOffLine=new NewsOffLine(title_new,title_sou,dat,des,img_s,img_new);
                newsOffLines.add(newsOffLine);
            }while (cursor.moveToNext());
        }
        return newsOffLines;
    }

    public void deleteBun(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete("Business",null,null);

    }
    public void deleteSpo(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete("Sports",null,null);

    }
    public void deleteEnt(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete("Entertainment",null,null);

    }
    public void deleteHea(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete("Health",null,null);

    }
    public void deleteSci(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete("Science",null,null);

    }
    public void deleteTec(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete("Technology",null,null);

    }
    public void deleteHead(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete("Headline",null,null);

    }

}
