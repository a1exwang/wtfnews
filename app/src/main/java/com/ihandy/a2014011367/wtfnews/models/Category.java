package com.ihandy.a2014011367.wtfnews.models;

import java.util.ArrayList;

/**
 * Created by alexwang on 8/22/16.
 */
public class Category {
    private String name;
    public static Category[] getAll() {
        ArrayList<Category> categories = new ArrayList<>();

        Category c = new Category();
        c.name = "Global";
        categories.add(c);

        c = new Category();
        c.name = "Sports";
        categories.add(c);

        return categories.toArray(new Category[categories.size()]);
    }

    public String getName() {
        return this.name;
    }
}
