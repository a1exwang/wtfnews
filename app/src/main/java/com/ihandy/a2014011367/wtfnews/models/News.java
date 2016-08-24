package com.ihandy.a2014011367.wtfnews.models;

public class News {
    private class NewsImage {
        private String url;
    }
    private class NewsSource {
        private String name, url;
    }

    /* These are JSON fields */
    private String category, country, locale_category, origin, title = "title";
    private long fetched_time, news_id, updated_time;
    private NewsImage[] imgs;
    private News relative_news;
    private NewsSource source;

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCategory() { return category; }
    public String getUrl() { return source.url; }
    public String getImageUrl() {
        if (imgs.length > 0) {
            return imgs[0].url;
        }
        else {
            return null;
        }
    }
}
