package com.csu.partytime;

import android.app.Application;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.Party;
import com.csu.partytime.model.PartyActivity;
import com.csu.partytime.util.DebugLog;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.tauth.Tencent;

/**
 * Created by Chen Xingchao on 2015/4/2.
 */
public class PartyApplication extends Application{
    public static Context context;
    public static Tencent tencent;
    public static Account currentAccount;
    public static AMapLocation myLocation;
    public static IWXAPI api;

    public final static String APP_ID = "wx47d6523abc3e5597";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        StatConfig.setDebugEnable(true);
        StatService.trackCustomEvent(this,"onCreate","");

        currentAccount = new Account();
        tencent = getTencent();

        api = WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);

        Constant.reSetSearchInfo();
    }

    private static Tencent getTencent(){
        if (null==tencent){
            return Tencent.createInstance(Constant.APP_ID,context);
        }else {
            return tencent;
        }
    }
}


