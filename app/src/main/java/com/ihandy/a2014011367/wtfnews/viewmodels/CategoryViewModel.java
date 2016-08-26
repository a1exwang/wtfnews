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

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryViewModel {
    private boolean loadingDone = true;
    private Category category;
    public MyOnScrollListener getScrollListener() {
        return new MyOnScrollListener();
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
                        //Do pagination.. i.e. fetch new data
                        Observable<News> observable = category.getNewsFrom(items.size(), 10);
                        subscribe(observable);
                    }
                }
            }
        }
    }
    private void subscribe(Observable<News> observable) {
        loadingDone = false;
        observable
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {
                        loadingDone = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(News c) {
                        items.add(new NewsViewModel(c));
                    }
                });
    }
    public CategoryViewModel(@NonNull Category category) {
        this.category = category;
        this.name = category.getName();

        Observable<News> observable = category.getNewsFrom(0, 10);
        subscribe(observable);
    }

    public final ObservableList<NewsViewModel> items = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.newsViewModel, R.layout.news_item);

    public final String name;
}
