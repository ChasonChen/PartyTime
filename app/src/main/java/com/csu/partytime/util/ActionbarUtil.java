package com.csu.partytime.util;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.csu.partytime.R;
import com.csu.partytime.listener.ActionbarTouchListener;

/**
 * Created by ${Chen_Xingchao} on 2015/4/19.
 */
public class ActionbarUtil{
    public static TextView actionBarTitle;

    public static ActionBar initBackwardTitleActionBar(final ActionBarActivity activity, int titleId) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        View actionbarLeft = activity.getLayoutInflater().inflate(R.layout.actionbar_backward_title, null);
        actionBarTitle = (TextView) actionbarLeft.findViewById(R.id.tv_partytime_activity_actionbar_title);
        actionBarTitle.setText(titleId);
        actionBarTitle.setOnTouchListener(new ActionbarTouchListener(activity,R.color.white,R.color.white_50tp));
        actionBar.setCustomView(actionbarLeft);
        actionBar.getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        return actionBar;
    }

    public static ActionBar initBackwardTitleActionBar(final ActionBarActivity activity, String titleId) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        View actionbarLeft = activity.getLayoutInflater().inflate(R.layout.actionbar_backward_title, null);
        actionBarTitle = (TextView) actionbarLeft.findViewById(R.id.tv_partytime_activity_actionbar_title);
        actionBarTitle.setText(titleId);
        actionBarTitle.setOnTouchListener(new ActionbarTouchListener(activity,R.color.white,R.color.white_50tp));
        actionBar.setCustomView(actionbarLeft);
        actionBar.getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        return actionBar;
    }

    public static ActionBar initTitleActionBar(final ActionBarActivity activity, int titleId) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        View actionbarLeft = activity.getLayoutInflater().inflate(R.layout.actionbar_title, null);
        actionBarTitle = (TextView) actionbarLeft.findViewById(R.id.tv_forum_activity_actionbar_title);
        actionBarTitle.setText(titleId);
        actionBar.setCustomView(actionbarLeft);
        return actionBar;
    }

    public static ActionBar initTitleActionBar(final ActionBarActivity activity, String titleId) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        View actionbarLeft = activity.getLayoutInflater().inflate(R.layout.actionbar_title, null);
        actionBarTitle = (TextView) actionbarLeft.findViewById(R.id.tv_forum_activity_actionbar_title);
        actionBarTitle.setText(titleId);
        actionBar.setCustomView(actionbarLeft);
        return actionBar;
    }

    public static void setActionBarTitle(String title){
        if (null!=actionBarTitle){
            actionBarTitle.setText(title);
        }
    }
}
