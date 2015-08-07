package com.csu.partytime.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ${Chen_Xingchao} on 2015/5/7.
 */
public class SearchContentAdapter extends FragmentPagerAdapter{
    List<Fragment> fragments;
    String[] tabs;

    public SearchContentAdapter(FragmentManager fm,List<Fragment> fragments,String[] tabs) {
        super(fm);
        this.fragments = fragments;
        this.tabs = tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
