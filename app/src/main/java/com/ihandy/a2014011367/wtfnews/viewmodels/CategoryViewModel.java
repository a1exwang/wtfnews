package com.ihandy.a2014011367.wtfnews.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ihandy.a2014011367.wtfnews.BR;
import com.ihandy.a2014011367.wtfnews.R;
import com.ihandy.a2014011367.wtfnews.models.Category;
import com.ihandy.a2014011367.wtfnews.models.News;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryViewModel {
    private boolean loadingDone = true;
    private Category category;
    public MyOnScrollListener getScrollListener() {
        return new MyOnScrollListener();
    }

    private long getMaxId() {
        if (items.size() == 0) {
            return -1;
        }
        else {
            NewsPerDayViewModel vm = items.get(items.size() - 1);
            return vm.getMaxId();
        }
    }

    private void loadMore(int count) {
        loadingDone = false;
        category.updateNews(getMaxId(), count)
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {
                        loadDone();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadDone();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(News c) {
                        addNews(c);
                    }
                });
    }
    private void loadDone() {
        loadingDone = true;
    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            if (dy > 0) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loadingDone) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        Log.v("...", "Last Item Wow !");
                        loadMore(10);
                    }
                }
            }
        }
    }
    public CategoryViewModel(@NonNull Category category) {
        this.category = category;
        this.name = category.getName();
        loadMore(10);
    }

    private void addNews(News n) {
        List<News> newsList = new ArrayList<>();
        newsList.add(n);
        NewsPerDayViewModel vm = new NewsPerDayViewModel(n.getDay(), newsList);

        boolean added = false;
        for (int i = 0; i < items.size(); ++i) {
            int result = String.CASE_INSENSITIVE_ORDER.compare(n.getDay(), items.get(i).day);
            // the news to add is later than this news
            if (result > 0) {
                added = true;
                items.add(i, vm);
                break;
            }
            else if (result == 0) {
                added = true;
                items.get(i).addNews(n);
                break;
            }
        }
        if (!added)
            items.add(vm);
    }

//    public final ObservableList<NewsViewModel> items = new ObservableArrayList<>();
//    public final ItemView itemView = ItemView.of(BR.newsViewModel, R.layout.news_item);
    public final ObservableList<NewsPerDayViewModel> items = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.newsPerDayViewModel, R.layout.news_per_day_item);

    public final String name;
    public String fuck;
}
