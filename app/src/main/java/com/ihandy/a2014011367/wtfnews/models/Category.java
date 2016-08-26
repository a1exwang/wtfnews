package com.ihandy.a2014011367.wtfnews.models;

import android.util.Log;

import com.ihandy.a2014011367.wtfnews.api.NewsApi;
import com.ihandy.a2014011367.wtfnews.records.CategoryRecord;
import com.ihandy.a2014011367.wtfnews.records.NewsRecord;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;

public class Category extends BaseModel {
    private String name;
    private String key;
    private ArrayList<News> newsList = new ArrayList<>();
    public Category() {}
    public Category(String key, String name) {
        this.key = key;
        this.name = name;
    }
    private static void getAll(Callback<Category> cb) throws ExecutionException, InterruptedException {
        // Cached categories
        Iterator<CategoryRecord> it = CategoryRecord.findAll(CategoryRecord.class);
        while (it.hasNext()) {
            CategoryRecord cr = it.next();
            Category c = new Category();
            c.name = cr.getName();
            c.key = cr.getKey();
            cb.call(c);
        }

        // New categories
        JSONObject json = NewsApi.getCategories();

        try {
            JSONObject data = json.getJSONObject("data");
            JSONObject cs = data.getJSONObject("categories");
            Iterator<String> itKey = cs.keys();
            while (itKey.hasNext()) {
                String key = itKey.next();
                String name = cs.getString(key);
                List<CategoryRecord> result = CategoryRecord.find(CategoryRecord.class, "key = ?", key);
                if (result.size() == 0) {
                    CategoryRecord record = new CategoryRecord(key, name);
                    record.save();

                    Category c = new Category();
                    c.name = record.getName();
                    c.key = record.getKey();
                    cb.call(c);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    private News getNews(int index) throws ExecutionException, InterruptedException, JSONException {
        // first, search in memory
        if (index < newsList.size()) {
            return newsList.get(index);
        }

        // if not found, search in database
        List<NewsRecord> newsRecords;
        if (newsList.size() == 0) {
            newsRecords = SugarRecord.find(NewsRecord.class, "");
        }
        else {
            News last = newsList.get(newsList.size() - 1);
            last.getId();
            newsRecords = SugarRecord.find(NewsRecord.class,
                    "news_id < ?",
                    new String[] { String.valueOf(last.getId()) },
                    null, // groupBy
                    "news_id desc",  // orderBy
                    "24"); // limit

        }
        for (NewsRecord nr : newsRecords) {
            newsList.add(new News(nr));
            Log.w("Category", "news added to memory cache from database, index: " + (newsList.size() - 1));
        }

        if (index < newsList.size()) {
            return newsList.get(index);
        }


        // finally, query from JSON API
        JSONArray jsonNews;
        if (newsList.size() == 0) {
            jsonNews = NewsApi.getNews(this.key);
        }
        else {
            jsonNews = NewsApi.getNews(this.key, newsList.get(newsList.size() - 1).getId());
        }
        for (int i = 0; i < jsonNews.length(); ++i) {
            JSONObject jsonNewsObject = (JSONObject) jsonNews.get(i);
            News news = News.fromJSONObject(jsonNewsObject);

            // add to database and memory cache if new news does not exists
            if (news.toNewsRecord().saveIfNotFound()) {
                this.newsList.add(news);
                Log.w("Category", "news added to database and memory cache, index: " + (newsList.size() - 1));
            }
        }

        if (index < this.newsList.size())
            return this.newsList.get(index);
        throw new ArrayIndexOutOfBoundsException("cannot get more news from API");
    }

    public Observable<News> getNewsFrom(final int start, final int count) {
        return Observable.create(new Observable.OnSubscribe<News>() {
            @Override
            public void call(final Subscriber<? super News> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        for (int i = start; i < start + count; ++i) {
                            subscriber.onNext(getNews(i));
                        }
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<Category> getAllObservable() {
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

}
