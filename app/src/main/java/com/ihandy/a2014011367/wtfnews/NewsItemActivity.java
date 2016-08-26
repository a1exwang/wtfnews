package com.ihandy.a2014011367.wtfnews;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

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

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);

        if (news != null) {
            WebView webView = (WebView) findViewById(R.id.webViewNewsItem);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100) {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        progressBar.setProgress(progress);
                    }
                    else {
                        progressBar.setVisibility(ProgressBar.GONE);
                    }
                }

            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.loadUrl(news.getUrl());
        }
    }


}
