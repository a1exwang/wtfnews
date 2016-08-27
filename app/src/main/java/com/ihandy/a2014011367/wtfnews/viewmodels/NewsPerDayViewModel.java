package com.ihandy.a2014011367.wtfnews.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.ihandy.a2014011367.wtfnews.BR;
import com.ihandy.a2014011367.wtfnews.R;
import com.ihandy.a2014011367.wtfnews.models.News;
import com.ihandy.a2014011367.wtfnews.utils.SortedUniqueArrayList;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

public class NewsPerDayViewModel {

    public NewsPerDayViewModel(String day, List<News> news) {
        this.day = day;
        for (News n : news) {
            addNews(n);
        }

    }

    public long getMaxId() {
        News last = newsList.last();
        if (last != null) {
            return last.getId();
        }
        else {
            return -1;
        }
    }

    public void addNews(News news) {
        try {
            int index = newsList.addSortedUnique(news);
            items.add(index, new NewsViewModel(news));
        } catch (SortedUniqueArrayList.ElementAlreadyExistException e) {
            e.printStackTrace();
        }
    }

    private SortedUniqueArrayList<News> newsList = new SortedUniqueArrayList<>();
    public final ObservableList<NewsViewModel> items = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.newsViewModel, R.layout.news_item);
    public final String day;

}
