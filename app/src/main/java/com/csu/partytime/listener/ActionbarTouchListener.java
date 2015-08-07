package com.csu.partytime.listener;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ${Chen_Xingchao} on 2015/4/19.
 */
public class ActionbarTouchListener extends TextViewTouchListener{
    ActionBarActivity activity;

    public ActionbarTouchListener(Context context, int colorResNormal, int colorResPress) {
        super(context, colorResNormal, colorResPress);
        this.activity = (ActionBarActivity) context;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        textView = (TextView) view;
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                textView.setTextColor(context.getResources().getColor(colorResPress));
                break;

            case MotionEvent.ACTION_UP:
                textView.setTextColor(context.getResources().getColor(colorResNormal));
                activity.onBackPressed();
                break;
        }
        return true;
    }
}
