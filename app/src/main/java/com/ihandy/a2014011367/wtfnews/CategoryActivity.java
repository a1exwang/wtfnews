package com.ihandy.a2014011367.wtfnews;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ihandy.a2014011367.wtfnews.api.MyVolley;
import com.ihandy.a2014011367.wtfnews.models.Category;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryActivity extends AppCompatActivity {

    ArrayList<Category> categoryArrayList = new ArrayList<>();

    class CategoryViewPagerAdapter extends FragmentPagerAdapter {
        public CategoryViewPagerAdapter(FragmentManager fm, Observable<Category> observable) {
            super(fm);
            observable.subscribeOn(Schedulers.io())
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
                            categoryArrayList.add(c);
                            notifyDataSetChanged();
                        }
                    });
        }

        @Override
        public int getCount() {
            return categoryArrayList.size();
        }

        @Override
        public Fragment getItem(int position) {
            Category category = categoryArrayList.get(position);
            return new CategoryFragment(category);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryArrayList.get(position).getName();
        }

    }

    ViewPager mViewPager;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    CategoryViewPagerAdapter mPagerAdapter;
    Observable<Category> mObservable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        MyVolley.init(this);

        mObservable = Category.getAllObservable();

        mViewPager = (ViewPager) findViewById(R.id.viewPagerCategory);
        mPagerAdapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), mObservable);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutCategory);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static final int MSG_UPDATE_TABS = 0x100;

    class CategoryHandler extends Handler {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case MSG_UPDATE_TABS:
                    mViewPager.setAdapter(mPagerAdapter);
                    break;
            }

        }

    }
    CategoryHandler mHandler = new CategoryHandler();
}
