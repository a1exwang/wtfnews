package com.ihandy.a2014011367.wtfnews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.ihandy.a2014011367.wtfnews.models.Category;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class CategoryViewPagerAdapter extends FragmentPagerAdapter {
    Observable<Category> mObservable;
    ArrayList<Category> mCategoryList = new ArrayList<>();
    Context mContext;
    public CategoryViewPagerAdapter(FragmentManager fm, Context context, Observable<Category> observable) {
        super(fm);
        this.mContext = context;
        this.mObservable = observable;
        this.refresh();
    }

    // make it easy to update
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    @Override
    public Fragment getItem(int position) {
        Category category = mCategoryList.get(position);
        return new CategoryFragment(category);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryList.get(position).getName();
    }

    public void refresh() {
        mCategoryList.clear();
        notifyDataSetChanged();
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Category>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext, "Loading done...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Category c) {
                        mCategoryList.add(c);
                        notifyDataSetChanged();
                    }
                });
    }
}
