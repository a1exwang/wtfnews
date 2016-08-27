package com.ihandy.a2014011367.wtfnews.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class NewsApi {
    public static JSONObject getCategories() throws ExecutionException, InterruptedException, JSONException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = "http://assignment.crazz.cn/news/en/category?timestamp=" + System.currentTimeMillis();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, future, future);
        MyVolley.getRequestQueue().add(req);
        JSONObject jsonObject = future.get();
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject cs = data.getJSONObject("categories");
        Log.i("NewsApi", "getCategories API");
        Log.i("NewsApi", jsonObject.toString());
        return cs;
    }

    public static JSONArray getNews(String category) throws ExecutionException, InterruptedException, JSONException {
        return getNews(category, -1);
    }
    public static JSONArray getNews(String category, long maxId) throws ExecutionException, InterruptedException, JSONException {
        String newsIdParam = "";
        if (maxId != -1) {
            newsIdParam = "&max_news_id=" + maxId;
        }
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = "http://assignment.crazz.cn/news/query?locale=en&category=" + category + newsIdParam;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, future, future);
        MyVolley.getRequestQueue().add(req);
        JSONObject json = future.get();
        Log.i("NewsApi", "getNews API");
        Log.i("NewsApi", json.toString());
        return json.getJSONObject("data").getJSONArray("news");
    }
}
