package com.csu.partytime.network;

import com.csu.partytime.network.base.BaseNetwork;
import com.csu.partytime.util.NetworkUtils;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.mime.TypedFile;

/**
 * Created by ${Chen_Xingchao} on 2015/5/4.
 */
public class FileNetwork {
    private static FileService instance;

    public interface FileService{

        @GET("/app/app-debug.apk")
        public void getNewApp(Callback<TypedFile> fileCallback);
    }

    public static synchronized FileService getInstance(){
        if (null==instance){
            instance = NetworkUtils.getService(FileService.class, new BaseNetwork.BaseErrorHandler());
        }
        return instance;
    }
}
