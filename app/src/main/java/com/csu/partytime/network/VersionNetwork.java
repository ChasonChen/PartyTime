package com.csu.partytime.network;

import com.csu.partytime.model.VersionInfo;
import com.csu.partytime.network.base.BaseNetwork;
import com.csu.partytime.util.NetworkUtils;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.mime.TypedFile;

/**
 * Created by ${Chen_Xingchao} on 2015/5/9.
 */
public class VersionNetwork {
    private static VersionService instance;

    public interface VersionService{

        @GET("/app/version")
        public void getVersion(Callback<VersionInfo> version);
    }

    public static synchronized VersionService getInstance(){
        if (null==instance){
            instance = NetworkUtils.getService(VersionService.class, new BaseNetwork.BaseErrorHandler());
        }
        return instance;
    }
}
