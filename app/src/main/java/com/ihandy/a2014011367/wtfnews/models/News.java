package com.ihandy.a2014011367.wtfnews.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.ihandy.a2014011367.wtfnews.records.NewsRecord;
import com.ihandy.a2014011367.wtfnews.utils.Indexable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News implements Parcelable, Serializable, Comparable<News>, Indexable {
    public static News fromJSONObject(JSONObject jsonObject) throws JSONException {
        News news = new News();

        news.title = jsonObject.getString("title");
        news.newsId = jsonObject.getLong("news_id");
        JSONArray jsonArray =jsonObject.getJSONArray("imgs");
        if (jsonArray.length() > 0) {
            JSONObject jo = (JSONObject) jsonArray.get(0);
            news.imgUrl = jo.getString("url");
        }
        JSONObject source = jsonObject.getJSONObject("source");
        news.url = source.getString("url");
        news.sourceName = source.getString("name");
        news.updatedTime = jsonObject.getLong("updated_time");
        news.category = jsonObject.getString("category");

        return news;
    }
    public static Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel parcel) {
            return (News)parcel.readSerializable();
        }

        @Override
        public News[] newArray(int i) {
            return new News[0];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(this);
    }

    public News() {
    }
    public News(NewsRecord nr) {
        this.title = nr.getTitle();
        this.newsId = nr.getNewsId();
        this.imgUrl = nr.getImgUrl();
        this.sourceName = nr.getSourceName();
        this.url = nr.getUrl();
        this.updatedTime = nr.getUpdatedTime();
        this.category = nr.getCategory();
    }

    /* These are JSON fields */
    private String category, country, locale_category, origin, title = "title";
    private long fetched_time, newsId, updatedTime;
    private String sourceName, url, imgUrl;
    private volatile int indexInCategoryList;

    public void setIndex(int i) {
        this.indexInCategoryList = i;
    }
    public int getIndex() {
        return this.indexInCategoryList;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCategory() { return category; }
    public String getUrl() { return url; }
    public String getImageUrl() {
        return imgUrl;
    }
    public String getSourceName() {
        return sourceName;
    }
    public long getId() {
        return newsId;
    }
    public long getUpdatedTime() { return updatedTime; }
    public String getPrettyUpdatedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("E hh:mm:ss", Locale.US);
        return sdf.format(new Date(getUpdatedTime()));
    }
    public String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date(getUpdatedTime()));
    }

    public NewsRecord toNewsRecord() {
        return new NewsRecord(newsId, title, sourceName, url, imgUrl, updatedTime, category);
    }

    @Override
    public int compareTo(@NonNull News news) {
        long result = getId() - news.getId();
        if (result > 0)
            return -1;
        else if (result < 0)
            return 1;
        else
            return 0;
    }
}
