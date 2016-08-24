package com.ihandy.a2014011367.wtfnews;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ihandy.a2014011367.wtfnews.models.Category;

public class CategoryActivity extends AppCompatActivity {

    class CategoryViewPagerAdapter extends FragmentPagerAdapter {
        public CategoryViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
           return Category.getAll().length;
        }

        @Override
        public Fragment getItem(int position) {
            Category category = Category.getAll()[position];
            return new CategoryFragment(category);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Category.getAll()[position].getName();
        }

    }

    ViewPager mViewPager;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mViewPager = (ViewPager) findViewById(R.id.viewPagerCategory);
        mViewPager.setAdapter(new CategoryViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutCategory);
        tabLayout.setupWithViewPager(mViewPager);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
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
