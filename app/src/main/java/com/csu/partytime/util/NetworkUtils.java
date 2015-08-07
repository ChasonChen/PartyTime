package com.csu.partytime.util;

import android.widget.Adapter;

import com.csu.partytime.BuildConfig;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by ${Chen_Xingchao} on 2015/4/14.
 */
public class NetworkUtils {
    private static Client client;

    static RequestInterceptor interceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("accept", "application/json");
            request.addHeader("Charset","UTF-8");
        }
    };

    //, ErrorHandler errorHandler
    public static <T> T getService(Class<T> clazz,ErrorHandler errorHandler){
        RestAdapter restAdapter = getAdapter(errorHandler);
        return restAdapter.create(clazz);
    }

    public static RestAdapter getAdapter(ErrorHandler errorHandler){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();

        return getAdapterBuilder()
                .setClient(getClient())
                .setConverter(new GsonConverter(gson))
                .setErrorHandler(errorHandler)
                .setRequestInterceptor(interceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static Client getClient(){

        if (null == client){
            OkHttpClient okHttpClient = new OkHttpClient();
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            okHttpClient.setCookieHandler(cookieManager);
            client = new OkClient(okHttpClient);
        }
        return client;
    }

    public static RestAdapter.Builder getAdapterBuilder(){
        if (BuildConfig.IS_DEBUG){
            return new RestAdapter.Builder().setEndpoint(BuildConfig.IP);
        }else {
            return new RestAdapter.Builder().setEndpoint(BuildConfig.ON_LINE_IP);
        }
    }
}
