package com.ihandy.a2014011367.wtfnews.models;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.common.primitives.Booleans;
import com.google.gson.Gson;
import com.ihandy.a2014011367.wtfnews.MyVolley;
import com.ihandy.a2014011367.wtfnews.records.CategoryRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Category extends BaseModel {
    private String name;
    private String key;
    private ArrayList<News> newsList = new ArrayList<>();
    private static void getAll(Func1<Category, Boolean> cb) throws ExecutionException, InterruptedException, JSONException {

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = "http://assignment.crazz.cn/newsList/en/category?timestamp=" + System.currentTimeMillis();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, future, future);
        MyVolley.getRequestQueue().add(req);
        JSONObject json = future.get();

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
                    // new record
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

        Iterator<CategoryRecord> it = CategoryRecord.findAll(CategoryRecord.class);
        while (it.hasNext()) {
            CategoryRecord cr = it.next();
            Category c = new Category();
            c.name = cr.getName();
            c.key = cr.getKey();
            // new record
            cb.call(c);
        }
    }

    public String getName() {
        return this.name;
    }

    private News getNews(int index) throws ExecutionException, InterruptedException, JSONException {
        if (index >= newsList.size()) {
            Gson gson = new Gson();
            long maxId;
            if (newsList.size() == 0) {
                maxId = -1;
            }
            else {
                maxId = newsList.get(newsList.size()-1).getId();
            }
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://assignment.crazz.cn/news/query?locale=en&category=" + this.key + "&max_news_id=" + maxId;
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, future, future);
            MyVolley.getRequestQueue().add(req);
            JSONObject json = future.get();
            JSONArray jsonNews = json.getJSONObject("data").getJSONArray("news");
            for (int i = 0; i < jsonNews.length(); ++i) {
                JSONObject jsonNewsObject = (JSONObject) jsonNews.get(i);
                News news = gson.fromJson(jsonNewsObject.toString(), News.class);
                this.newsList.add(news);
            }
        }
        if (index < this.newsList.size())
            return this.newsList.get(index);
        throw new ArrayIndexOutOfBoundsException("news index does not exists");
    }

    public Observable<List<News>> getNewsFrom(final int start, final int count) {
        return Observable.fromCallable(new Callable<List<News>>() {
            @Override
            public List<News> call() throws Exception {
                ArrayList<News> newsList = new ArrayList<>();
                for (int i = 0; i < start + count; ++i) {
                    newsList.add(getNews(i));
                }
                return newsList;
            }
        });
    }

    public static Observable<Category> getAllObservable() {
        return Observable.create(new Observable.OnSubscribe<Category>() {
            @Override
            public void call(final Subscriber<? super Category> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        Func1<Category, Boolean> func1 = new Func1<Category, Boolean>() {
                            @Override
                            public Boolean call(Category category) {
                                observer.onNext(category);
                                return true;
                            }
                        };
                        getAll(func1);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

}
