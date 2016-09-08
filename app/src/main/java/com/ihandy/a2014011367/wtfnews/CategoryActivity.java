package com.ihandy.a2014011367.wtfnews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ihandy.a2014011367.wtfnews.api.MyVolley;
import com.ihandy.a2014011367.wtfnews.models.Category;

import rx.Observable;

public class CategoryActivity extends AppCompatActivity {
    ViewPager mViewPager;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    CategoryViewPagerAdapter mPagerAdapter;
    Observable<Category> mObservable;
    TabLayout mTabLayout;
    boolean mFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialize Volley queue
        MyVolley.init(this);

        mViewPager = (ViewPager) findViewById(R.id.viewPagerCategory);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayoutCategory);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);

        mObservable = Category.getAll();
        mPagerAdapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), this, mObservable);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mFirstTime = true;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent(CategoryActivity.this, SettingsActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Do not refresh on the first time the activity starts
        if (!mFirstTime) {
            mPagerAdapter.refresh();
        }
        mFirstTime = false;
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
}
