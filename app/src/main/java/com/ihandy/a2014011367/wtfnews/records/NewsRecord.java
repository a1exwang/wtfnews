package com.ihandy.a2014011367.wtfnews.records;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class NewsRecord extends SugarRecord {
    @Unique
    private long newsId;
    private String title;
    private String url, sourceName;
    private String imgUrl;
    private long updatedTime;

    public NewsRecord() {
    }
    public NewsRecord(long newsId, String title, String sourceName, String url, String imgUrl, long updatedTime) {
        this.newsId = newsId;
        this.title = title;
        this.sourceName = sourceName;
        this.url = url;
        this.imgUrl = imgUrl;
        this.updatedTime = updatedTime;
    }

    public long getNewsId() {
        return newsId;
    }
    public String getTitle() {
        return title;
    }

    public boolean saveIfNotFound() {
        if (SugarRecord.count(NewsRecord.class, "news_id = ?", new String[] { String.valueOf(newsId) }) == 0) {
            save();
            return true;
        }
        return false;
    }
    public String getImgUrl() {
        return this.imgUrl;
    }
    public String getSourceName() {
        return this.sourceName;
    }
    public String getUrl() {
        return this.url;
    }
    public long getUpdatedTime() { return this.updatedTime; }
}
