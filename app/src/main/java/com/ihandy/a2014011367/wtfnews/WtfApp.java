package com.ihandy.a2014011367.wtfnews;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class WtfApp extends com.orm.SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, getResources().getInteger(R.integer.maxImageCacheSize)));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
