package com.ihandy.a2014011367.wtfnews.records;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class NewsRecord extends SugarRecord {
    @Unique
    private long newsId;
    private String title;

    public NewsRecord() {

    }
    public NewsRecord(long newsId, String title) {
        this.newsId = newsId;
        this.title = title;
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


}
