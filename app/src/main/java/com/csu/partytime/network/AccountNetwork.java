package com.csu.partytime.network;

import com.csu.partytime.model.Account;
import com.csu.partytime.network.base.BaseNetwork;
import com.csu.partytime.util.NetworkUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by ${Chen_Xingchao} on 2015/4/14.
 */
public class AccountNetwork extends BaseNetwork {
    private static AccountService instance;

    public interface AccountService {

        @POST("/AccountServlet")
        public void addAccount(@Query("nick") String nick,
                               @Query("password") String password,
                               Callback<Object> callback);

        @POST("/LoginByQQServlet")
        public void loginByQQ(@Query("jsonStr") String jsonStr,
                              Callback<Account> callback);

        @POST("/GetAccountByAccessTokenServlet")
        public void getAccountByAccessToken(@Query("accessToken") String accessToken,
                                            Callback<Account> callback);

        @POST("/UpdateAccountLocation")
        public void updateLocation(@Query("accessToken") String accessToken,
                                   @Query("latitude") Double latitude,
                                   @Query("longitude") Double longitude,
                                   Callback<Object> callback);

        @POST("/GetAllAccountsServlet")
        public void getAllAccounts(@Query("accessToken") String accessToken,
                                   @Query("offset") Integer offset,
                                   @Query("size") Integer size,
                                   Callback<List<Account>> callback);

        @POST("/GetAccountsByAccessTokenServlet")
        public void getAccountsByAccessTokens(@Query("inviteMembers") String inviteMembers,
                                              Callback<List<Account>> callback);
    }

    public static synchronized AccountService getInstance() {
        if (null == instance) {
            instance = NetworkUtils.getService(AccountService.class, new BaseErrorHandler());
        }
        return instance;
    }

}
