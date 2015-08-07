package com.csu.partytime.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.csu.partytime.R;
import com.csu.partytime.fragment.RecommendationFragment_;
import com.csu.partytime.util.ActionbarUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_add_act)
public class AddActActivity extends ActionBarActivity {
    RecommendationFragment_ recommendationFragment_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendationFragment_ = new RecommendationFragment_();
        Bundle arguments = new Bundle();
        arguments.putString(RecommendationFragment_.ARGUMENTS_ATTACH_TO,
                RecommendationFragment_.ATTACH_TO_ADD_ACT);
        recommendationFragment_.setArguments(arguments);
    }

    @AfterViews
    public void initView(){
        ActionbarUtil.initBackwardTitleActionBar(this,"添加活动");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rl_add_act_fragment,recommendationFragment_)
                .commit();
    }

}
