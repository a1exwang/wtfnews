package com.ihandy.a2014011367.wtfnews.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ihandy.a2014011367.wtfnews.records.NewsRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class News implements Parcelable, Serializable {
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
    }

    /* These are JSON fields */
    private String category, country, locale_category, origin, title = "title";
    private long fetched_time, newsId, updated_time;
    private String sourceName, url, imgUrl;

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

    public NewsRecord toNewsRecord() {
        return new NewsRecord(newsId, title, sourceName, url, imgUrl);
    }
}
