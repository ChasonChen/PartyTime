package com.csu.partytime.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Window;

import com.csu.partytime.R;
import com.csu.partytime.util.ToastUtil;

/**
 * Created by ${Chen_Xingchao} on 2015/4/19.
 */
public class LoadingDialog {
    LayoutInflater mInflater;
    Context context;
    Dialog dialog;
    AlertDialog.Builder builder;
    final static int TIME_OUT = 15;

    public LoadingDialog(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
    }

    public Dialog getDialog(){
        return dialog;
    }
    public void show() {
        dialog.show();
        Window window = dialog.getWindow();
        window.setDimAmount(0.7f);
        dialog.setContentView(R.layout.dialog_loading);
        new Thread() {
            @Override
            public void run() {
                int time = 0;
                while (true) {
                    try {
                        sleep(1000);
                        time += 1;
                        if (time >= TIME_OUT && dialog.isShowing()) {
                            dialog.dismiss();
                            Looper.prepare();
                            ToastUtil.show(context, "网络连接超时");
                            Looper.loop();
                            return;
                        } else if (time <= TIME_OUT && !dialog.isShowing()) {
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }//super.run();
                }
            }
        }.start();
    }

    public boolean isShow() {
        return dialog.isShowing();
    }

    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }
}

