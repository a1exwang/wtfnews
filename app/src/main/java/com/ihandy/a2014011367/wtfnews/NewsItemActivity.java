package com.ihandy.a2014011367.wtfnews;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ihandy.a2014011367.wtfnews.databinding.ActivityMainBinding;
import com.ihandy.a2014011367.wtfnews.models.Category;
import com.ihandy.a2014011367.wtfnews.models.News;
import com.ihandy.a2014011367.wtfnews.models.ViewModel;

public class NewsItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        News news = new News();
        binding.setNews(news);
        ViewModel viewModel = new ViewModel();
        viewModel.items.add("Sports");
        viewModel.items.add("World");
        binding.setViewModel(viewModel);

        WebView webView = (WebView) findViewById(R.id.webViewNewsItem);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.baidu.com");
    }


}
