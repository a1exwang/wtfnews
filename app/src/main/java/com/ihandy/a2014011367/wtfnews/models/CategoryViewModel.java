package com.ihandy.a2014011367.wtfnews.models;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ihandy.a2014011367.wtfnews.BR;
import com.ihandy.a2014011367.wtfnews.R;

import java.util.Collections;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryViewModel {
    private Category category;
    public MyOnScrollListener getScrollListener() {
        return new MyOnScrollListener();
    }
    public class MyOnScrollListener extends RecyclerView.OnScrollListener {

        boolean loading = true;
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

                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        Log.v("...", "Last Item Wow !");
                        //Do pagination.. i.e. fetch new data
                        Observable<List<News>> observable = category.getNewsFrom(items.size(), 10);
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<News>>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(List<News> c) {
                                        items.addAll(c);
                                    }
                                });
                    }
                }
            }
        }
    }
    public CategoryViewModel(@NonNull Category category) {
        Observable<List<News>> observable = category.getNewsFrom(0, 10);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<News>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<News> c) {
                        items.addAll(c);
                    }
                });

        this.category = category;
        this.name = category.getName();
    }

    public final ObservableList<News> items = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.news, R.layout.news_item);

    public final String name;
}
