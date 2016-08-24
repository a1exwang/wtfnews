package com.ihandy.a2014011367.wtfnews.models;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.ihandy.a2014011367.wtfnews.BR;
import com.ihandy.a2014011367.wtfnews.R;

import java.util.Collections;

import me.tatarka.bindingcollectionadapter.ItemView;

public class CategoryViewModel {
    public CategoryViewModel(Category category) {
        Collections.addAll(items, category.getNews());
        this.name = category.getName();
    }

    public final ObservableList<News> items = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.news, R.layout.news_item);

    public final String name;
}
