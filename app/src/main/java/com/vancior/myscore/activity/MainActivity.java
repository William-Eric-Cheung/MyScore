package com.vancior.myscore.activity;

import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vancior.myscore.R;
import com.vancior.myscore.adapter.MyViewPagerAdapter;
import com.vancior.myscore.bean.Sheet;
import com.vancior.myscore.view.MyFragment;
import com.vancior.myscore.web.SearchTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchView mSearchView;

    private String[] mTitles;
    private List<MyFragment> mFragments;
    private MyViewPagerAdapter mViewPagerAdapter;

    public static File STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStorage();
        initViews();
        initData();
        configViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.replace(' ', '+');
                mFragments.get(0).onSearch(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void initStorage() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            STORAGE = getExternalCacheDir();
            Log.d(TAG, "initStorage: " + STORAGE);
        } else {
            STORAGE = getCacheDir();
            Log.d(TAG, "initStorage: " + STORAGE);
        }
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_coordinatorlayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.id_appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
    }

    private void initData() {
        mTitles = getResources().getStringArray(R.array.tab_titles);

        mFragments = new ArrayList<>();
//        for (int i = 0; i < mTitles.length; ++i) {
            MyFragment myFragment = new MyFragment();
            mFragments.add(myFragment);
//        }
    }

    private void configViews() {
        setSupportActionBar(mToolbar);

        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
//        mViewPager.addOnPageChangeListener();

        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }

}
