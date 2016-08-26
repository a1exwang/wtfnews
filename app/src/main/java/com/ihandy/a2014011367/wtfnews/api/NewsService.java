package com.ihandy.a2014011367.wtfnews.api;

import com.ihandy.a2014011367.wtfnews.models.Category;
import com.ihandy.a2014011367.wtfnews.service.CategoryJSONDeserializer;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface NewsService {
    class Builder {
        public static NewsService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NewsService.SERVICE_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(CategoryJSONDeserializer.createGson()))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(NewsService.class);
        }
    }
    String SERVICE_ENDPOINT = "http://assignment.crazz.cn";

    @GET("/news/en/category")
    Observable<Category> getCategories(@Query("timestamp") long timestamp);

}
