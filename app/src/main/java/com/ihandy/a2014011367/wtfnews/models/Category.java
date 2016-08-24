package com.ihandy.a2014011367.wtfnews.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Category {
    private String name;
    private ArrayList<News> news = new ArrayList<>();
    public static Category[] getAll() {
        ArrayList<Category> categories = new ArrayList<>();

        Category c = new Category();
        c.name = "Global";
        News news1 = new News(); news1.setTitle("title1"); c.news.add(news1);
        news1 = new News(); news1.setTitle("good news"); c.news.add(news1);
        news1 = new News(); news1.setTitle("bad news"); c.news.add(news1);
        categories.add(c);

        c = new Category();
        c.name = "Sports";
        categories.add(c);

        return categories.toArray(new Category[categories.size()]);
    }

    public String getName() {
        return this.name;
    }
    public News[] getNews() { return news.toArray(new News[news.size()]); }
}
