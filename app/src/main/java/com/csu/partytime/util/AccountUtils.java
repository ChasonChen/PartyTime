package com.csu.partytime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.csu.partytime.Constant;
import com.csu.partytime.model.Account;
import com.google.gson.Gson;

/**
 * Created by Chason on 2015/5/24.
 */
public class AccountUtils {
    public static void clearSPAccount(Context context){
        SharedPreferences sp=context.getSharedPreferences(Constant.SP_ACCOUNT_INFO,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public static void addSPAccount(Context context,Account account){
        SharedPreferences sp= context.getSharedPreferences(Constant.SP_ACCOUNT_INFO,Context.MODE_PRIVATE);
        sp.edit().putString(Constant.SP_ACCOUNT_INFO,new Gson().toJson(account)).commit();
    }

    public static Account getSPAccount(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_ACCOUNT_INFO,Context.MODE_PRIVATE);
        String strAccount=sp.getString(Constant.SP_ACCOUNT_INFO,"");
        if (strAccount.equals("")){
            return null;
        }else {
            return new Gson().fromJson(strAccount,Account.class);
        }
    }
}
