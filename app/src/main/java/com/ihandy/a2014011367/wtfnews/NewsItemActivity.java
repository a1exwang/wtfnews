package com.ihandy.a2014011367.wtfnews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ihandy.a2014011367.wtfnews.databinding.ActivityNewsItemBinding;
import com.ihandy.a2014011367.wtfnews.models.News;
import com.ihandy.a2014011367.wtfnews.records.NewsRecord;

public class NewsItemActivity extends AppCompatActivity {

    News mNews;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNews = getIntent().getExtras().getParcelable(getString(R.string.newsActivityDataKeyNewsUrl));

        ActivityNewsItemBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_news_item);
        binding.setNews(mNews);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);

        if (mNews != null) {
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

            if (mNews.isUrlAvailable()) {
                webView.loadUrl(mNews.getUrl());
            } else {
                webView.loadData("<h3>With JSON API server fucked up</h3><h3>News is not available</h3>", "text/html", "UTF-8");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_item, menu);

        menu.findItem(R.id.action_news_item_save).setIcon(
                mNews.getSaved() ? R.drawable.ic_save_white_24dp : R.drawable.ic_save_black_24dp
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_news_item_share) {

            String simpleData = mNews.getTitle() + " ";
            if (mNews.isUrlAvailable())
                simpleData += mNews.getUrl();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, simpleData);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }
        else if (id == R.id.action_news_item_save) {
            mNews.toggleSaved();

            item.setIcon(
                    mNews.getSaved() ? R.drawable.ic_save_white_24dp : R.drawable.ic_save_black_24dp
            );
        }

        return super.onOptionsItemSelected(item);
    }

}
