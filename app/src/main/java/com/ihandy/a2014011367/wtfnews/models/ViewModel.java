package com.ihandy.a2014011367.wtfnews.models;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.ihandy.a2014011367.wtfnews.BR;
import com.ihandy.a2014011367.wtfnews.R;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by alexwang on 8/22/16.
 */
public class ViewModel {
    public final ObservableList<String> items = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.item, R.layout.item);
}
