package com.csu.partytime.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.csu.partytime.R;

/**
 * Created by ${Chen_Xingchao} on 2015/4/19.
 */
public class TextViewTouchListener implements View.OnTouchListener {
    Context context;
    TextView textView;
    int colorResNormal;
    int colorResPress;

    public TextViewTouchListener(Context context,int colorResNormal,int colorResPress){
        this.context = context;
        this.colorResNormal = colorResNormal;
        this.colorResPress = colorResPress;
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
                break;
        }
        return true;
    }
}

