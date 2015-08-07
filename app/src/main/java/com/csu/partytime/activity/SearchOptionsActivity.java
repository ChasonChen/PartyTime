package com.csu.partytime.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.adapter.SearchContentAdapter;
import com.csu.partytime.fragment.GeneralSearchFragment_;
import com.csu.partytime.fragment.SmartSearchFragment_;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.DebugLog;
import com.csu.partytime.util.DensityUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_search_options)
public class SearchOptionsActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener {
    public final static String ACTIVITY_FROM = "activityFrom";
    public final static String FROM_MAIN = "fromMain";
    public final static String FROM_PARTY = "fromParty";

    @ViewById(R.id.psts_search_options_tabs)
    PagerSlidingTabStrip sptsTabs;
    @ViewById(R.id.vp_search_options_content)
    ViewPager vpContent;

    List<Fragment> fragments;
    SearchContentAdapter contentAdapter;
    String[] tabs = {"普通检索", "智能检索"};

    String activityFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<Fragment>();
        activityFrom = getIntent().getStringExtra(ACTIVITY_FROM);
    }

    @AfterViews
    public void initViews() {
        ActionbarUtil.initBackwardTitleActionBar(this, "自定义检索");
        initContent();
    }

    public void initContent() {
        fragments.add(new GeneralSearchFragment_());
        fragments.add(new SmartSearchFragment_());
        contentAdapter = new SearchContentAdapter(getSupportFragmentManager(), fragments, tabs);
        vpContent.setAdapter(contentAdapter);
        DebugLog.d("tabs:" + sptsTabs);
        sptsTabs.setViewPager(vpContent);
        sptsTabs.setTextSize(DensityUtil.pix2dp(16));
        sptsTabs.setTextColorResource(R.color.white);
        sptsTabs.setIndicatorColorResource(R.color.white_50tp);
        sptsTabs.setIndicatorHeight(DensityUtil.pix2dp(4));
        sptsTabs.setUnderlineHeight(DensityUtil.pix2dp(0));
        sptsTabs.setShouldExpand(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
