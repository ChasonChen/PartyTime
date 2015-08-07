package com.csu.partytime.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.fragment.PartnersFragment_;
import com.csu.partytime.fragment.PartnersOnMapFragment_;
import com.csu.partytime.fragment.SmartSearchFragment_;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_partners)
public class PartnersActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void initViews(){
        ActionbarUtil.initBackwardTitleActionBar(this,"好友列表");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_partners_activity, new PartnersFragment_(), "partnersFragment")
                .commit();
    }

}
