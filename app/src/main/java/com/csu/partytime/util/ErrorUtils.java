package com.csu.partytime.util;

import android.content.Context;

import retrofit.RetrofitError;

/**
 * Created by Chason on 2015/5/24.
 */
public class ErrorUtils extends ToastUtil{
    public static void show(Context context, RetrofitError error){
        ToastUtil.show(context,error.toString());
    }
}
