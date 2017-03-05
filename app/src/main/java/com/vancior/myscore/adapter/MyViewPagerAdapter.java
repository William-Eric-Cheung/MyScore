package com.vancior.myscore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vancior.myscore.view.MyFragment;

import java.util.List;

/**
 * Created by H on 2017/2/25.
 */

public class MyViewPagerAdapter extends FragmentStatePagerAdapter{

    private String[] mTitles;
    private List<MyFragment> mFragments;

    public MyViewPagerAdapter(FragmentManager fm, String[] mTitles, List<MyFragment> mFragments) {
        super(fm);
        this.mTitles = mTitles;
        this.mFragments = mFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
