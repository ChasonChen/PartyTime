package com.csu.partytime.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ${Chen_Xingchao} on 2015/4/5.
 */
public class ToastUtil {
    public static void show(Context context,String text){
        Toast toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}
