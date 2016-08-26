package com.ihandy.a2014011367.wtfnews.viewmodels;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ihandy.a2014011367.wtfnews.NewsItemActivity;
import com.ihandy.a2014011367.wtfnews.R;
import com.ihandy.a2014011367.wtfnews.models.News;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class NewsViewModel {
    public final News news;
    public NewsViewModel(News news) {
        this.news = news;
    }
    public final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NewsItemActivity.class);
            intent.putExtra(view.getContext().getString(R.string.newsActivityDataKeyNewsUrl), (Parcelable) news);
            view.getContext().startActivity(intent);
        }
    };
    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(final ImageView imageView, final String url) {
        Picasso.with(imageView.getContext()).setIndicatorsEnabled(true);
        Picasso.with(imageView.getContext())
                .load(url)
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(imageView.getContext())
                                .load(url)
                                .fit()
                                .centerCrop()
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });
    }

}
