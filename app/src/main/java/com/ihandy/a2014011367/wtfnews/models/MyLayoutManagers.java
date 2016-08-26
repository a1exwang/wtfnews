package com.ihandy.a2014011367.wtfnews.models;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import me.tatarka.bindingcollectionadapter.LayoutManagers;

public class MyLayoutManagers {
    public static LayoutManagers.LayoutManagerFactory linear() {
        return new LayoutManagers.LayoutManagerFactory() {
            @Override
            public RecyclerView.LayoutManager create(RecyclerView recyclerView) {

                return new LinearLayoutManager(recyclerView.getContext());
            }
        };
    }
}
