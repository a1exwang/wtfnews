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

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;

public class Category extends BaseModel {
    public static class DataNotSatisfiedException extends Exception {
        public DataNotSatisfiedException(String str) {
            super(str);
        }
    }
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
        CategoryRecord cr = CategoryRecord.findOrCreateByKey(key, name);
        cr.setPriority(this.getPriority());
        cr.save();
        return cr;
    }

    public boolean getVisible() {
        return priority >= 0;
    }
    public void setVisible(boolean v) {
        priority = v ? 0 : -1;
        toRecord().save();
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
        return getAll(false);
    }
    public static Observable<Category> getAll(final boolean showAll) {
        return Observable.create(new Observable.OnSubscribe<Category>() {
            @Override
            public void call(final Subscriber<? super Category> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        getAll(showAll, new Callback<Category>() {
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

    private static void getAll(boolean showAll, Callback<Category> cb) throws ExecutionException, InterruptedException {
        // Cached categories
        List<CategoryRecord> crs;
        if (showAll)
            crs = CategoryRecord.findWithQuery(CategoryRecord.class, "select * from CATEGORY_RECORD order by priority desc");
        else
            crs = CategoryRecord.findWithQuery(CategoryRecord.class, "select * from CATEGORY_RECORD where priority >= 0 order by priority desc");

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

                boolean[] created = new boolean[1];
                CategoryRecord cr = CategoryRecord.findOrCreateByKey(key, name, created);
                if (created[0]) {
                    Category c = cr.toCategory();
                    if (c.getVisible())
                        cb.call(c);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateNews(final long maxId, int count, Callback<News> cb) throws ExecutionException, InterruptedException, JSONException, DataNotSatisfiedException, UnsupportedEncodingException {
        // first, search in memory
        int index = newsList.findBy(new SortedUniqueArrayList.FindCallback<News>() {
            @Override
            public boolean isTarget(News t) {
                return t.getId() == maxId;
            }
        });
        for (int i = index + 1; i < newsList.size(); ++i) {
            cb.call(newsList.get(i));
        }
        int rest = count - index - 1;

        // if not enough, search in database
        List<NewsRecord> newsRecords;
        if (newsList.size() == 0) {
            newsRecords = SugarRecord.find(NewsRecord.class, "category = ?", this.key);
        }
        else {
            News last = newsList.last();
            newsRecords = SugarRecord.find(NewsRecord.class,
                    "news_id < ? AND category = ?",
                    new String[] { String.valueOf(last.getId()), this.key },
                    null, // groupBy
                    "news_id desc",  // orderBy
                    "24"); // limit

        }
        for (NewsRecord nr : newsRecords) {
            try {
                News n = new News(nr);
                newsList.addSortedUnique(n);
                if (rest > 0) {
                    cb.call(n);
                    rest--;
                }
                Log.w("Category", "category: " + this.name + " news added to memory cache from database, index: " + (newsList.size() - 1));
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
            News news;
            try {
                news = News.fromJSONObject(jsonNewsObject, this.name);
            }
            catch (JSONException e) {
                // ignore bad json objects
                e.printStackTrace();
                continue;
            }

            // add to database and memory cache if new news does not exists
            news.toNewsRecord().saveIfNotFound();
            try {
                this.newsList.addSortedUnique(news);
                if (rest > 0) {
                    rest--;
                    cb.call(news);
                }
                Log.w("Category", "category: " + this.name + " news added to database and memory cache, index: " + (newsList.size() - 1));
            } catch (SortedUniqueArrayList.ElementAlreadyExistException e) {
                // ignore duplication
            }
        }

        if (rest > 0)
            throw new DataNotSatisfiedException("cannot get enough news from API");
    }
}
