package com.ihandy.a2014011367.wtfnews.models;

import android.util.Log;

import com.ihandy.a2014011367.wtfnews.api.NewsApi;
import com.ihandy.a2014011367.wtfnews.records.CategoryRecord;
import com.ihandy.a2014011367.wtfnews.records.NewsRecord;
import com.ihandy.a2014011367.wtfnews.utils.SortedUniqueArrayList;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;

public class Category extends BaseModel {
    private String name;
    private String key;
    private int priority;
//    private ArrayList<News> newsList = new ArrayList<>();
    private SortedUniqueArrayList<News> newsList = new SortedUniqueArrayList<>();

    public Category() {}
    public Category(String key, String name) {
        this.key = key;
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public int getPriority() {
        return this.priority;
    }
    public void setPriority(int p) { this.priority = p;}
    public CategoryRecord toRecord() {
        CategoryRecord cr = new CategoryRecord(key, name);
        cr.setPriority(this.getPriority());
        return cr;
    }
    public boolean visible() {
        return priority >= 0;
    }

    public Observable<News> updateNews(final long maxId, final int count) {
        return Observable.create(new Observable.OnSubscribe<News>() {
            @Override
            public void call(final Subscriber<? super News> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        updateNews(maxId, count, new Callback<News>() {
                            @Override
                            public void call(News n) {
                                subscriber.onNext(n);
                            }
                        });
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
    public static Observable<Category> getAll() {
        return Observable.create(new Observable.OnSubscribe<Category>() {
            @Override
            public void call(final Subscriber<? super Category> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        getAll(new Callback<Category>() {
                            @Override
                            public void call(Category category) {
                                observer.onNext(category);
                            }
                        });
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

    private static void getAll(Callback<Category> cb) throws ExecutionException, InterruptedException {
        // Cached categories
        List<CategoryRecord> crs = CategoryRecord.find(CategoryRecord.class, "priority > ?", new String[]{"0"}, null, "priority desc", null);
        for (CategoryRecord cr : crs) {
            cb.call(cr.toCategory());
        }

        // New categories
        try {
            JSONObject cs = NewsApi.getCategories();
            Iterator<String> itKey = cs.keys();
            while (itKey.hasNext()) {
                String key = itKey.next();
                String name = cs.getString(key);
                CategoryRecord cr = CategoryRecord.findOrCreateByKey(key, name);
                Category c = cr.toCategory();
                if (c.visible())
                    cb.call(c);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateNews(final long maxId, int count, Callback<News> cb) throws ExecutionException, InterruptedException, JSONException {
        // first, search in memory
        int index = newsList.findBy(new SortedUniqueArrayList.FindCallback<News>() {
            @Override
            public boolean isTarget(News t) {
                return t.getId() == maxId;
            }
        });
        if (index == -1) {
            // update all
        }
        else {
            for (int i = index + 1; i < newsList.size(); ++i) {
                cb.call(newsList.get(i));
            }
        }
        int rest = newsList.size() - index - 1;

        // if not enough, search in database
        List<NewsRecord> newsRecords;
        if (newsList.size() == 0) {
            newsRecords = SugarRecord.find(NewsRecord.class, "");
        }
        else {
            News last = newsList.last();
            newsRecords = SugarRecord.find(NewsRecord.class,
                    "news_id < ?",
                    new String[] { String.valueOf(last.getId()) },
                    null, // groupBy
                    "news_id desc",  // orderBy
                    "24"); // limit

        }
        for (NewsRecord nr : newsRecords) {
            try {
                News n = new News(nr);
                newsList.addSortedUnique(n);
                cb.call(n);
                rest--;
                Log.w("Category", "news added to memory cache from database, index: " + (newsList.size() - 1));
            } catch (SortedUniqueArrayList.ElementAlreadyExistException e) {
                // ignore duplicate elements
            }
        }

        if (rest <= 0)
            return;

        // finally, query from JSON API
        JSONArray jsonNews = newsList.size() == 0 ?
                NewsApi.getNews(this.key) :
                NewsApi.getNews(this.key, newsList.get(newsList.size() - 1).getId());

        for (int i = 0; i < jsonNews.length(); ++i) {
            JSONObject jsonNewsObject = (JSONObject) jsonNews.get(i);
            News news = News.fromJSONObject(jsonNewsObject);

            // add to database and memory cache if new news does not exists
            news.toNewsRecord().saveIfNotFound();
            try {
                this.newsList.addSortedUnique(news);
                Log.w("Category", "news added to database and memory cache, index: " + (newsList.size() - 1));
            } catch (SortedUniqueArrayList.ElementAlreadyExistException e) {
                // ignore duplication
            }
        }

        if (rest > 0)
            throw new ArrayIndexOutOfBoundsException("cannot get enough news from API");
    }
}
