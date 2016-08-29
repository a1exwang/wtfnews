package com.ihandy.a2014011367.wtfnews.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.ihandy.a2014011367.wtfnews.BR;
import com.ihandy.a2014011367.wtfnews.R;
import com.ihandy.a2014011367.wtfnews.models.Category;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingsViewModel {
    public SettingsViewModel() {
        Observable<Category> obs = Category.getAll(true);
        obs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Category>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Category c) {
                        categories.add(new SettingsCategoryViewModel(c));
                    }
                });

    }
    public final ObservableList<SettingsCategoryViewModel> categories = new ObservableArrayList<>();
    public final ItemView categoryItemView = ItemView.of(BR.vm, R.layout.settings_category_item);
}
