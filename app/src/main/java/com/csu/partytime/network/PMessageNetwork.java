package com.csu.partytime.network;

import com.csu.partytime.model.PMessage;
import com.csu.partytime.model.Party;
import com.csu.partytime.network.base.BaseNetwork;
import com.csu.partytime.util.NetworkUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Chason on 2015/5/29.
 */
public class PMessageNetwork extends BaseNetwork {
    private static PMessageService instance;

    public interface PMessageService{
        @POST("/GetPMessagesServlet")
        public void getPMessage(@Query("accessToken")String accessToken,
                                @Query("offset")Integer offset,
                                @Query("size")Integer size,
                                Callback<List<PMessage>> callback);
    }

    public static synchronized PMessageService getInstance(){
        if (null==instance){
            instance = NetworkUtils.getService(PMessageService.class, new BaseNetwork.BaseErrorHandler());
        }
        return instance;
    }
}
