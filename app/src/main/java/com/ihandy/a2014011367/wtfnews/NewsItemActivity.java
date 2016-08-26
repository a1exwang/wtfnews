package com.ihandy.a2014011367.wtfnews;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ihandy.a2014011367.wtfnews.databinding.ActivityNewsItemBinding;
import com.ihandy.a2014011367.wtfnews.models.News;

public class NewsItemActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        News news = getIntent().getExtras().getParcelable(getString(R.string.newsActivityDataKeyNewsUrl));

        ActivityNewsItemBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_news_item);
        binding.setNews(news);

        if (news != null) {
            WebView webView = (WebView) findViewById(R.id.webViewNewsItem);
            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);


            webView.loadUrl(news.getUrl());
        }
    }


}
