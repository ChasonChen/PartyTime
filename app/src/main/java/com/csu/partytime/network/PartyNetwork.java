package com.csu.partytime.network;

import com.csu.partytime.model.Party;
import com.csu.partytime.network.base.BaseNetwork;
import com.csu.partytime.util.NetworkUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by ${Chen_Xingchao} on 2015/4/24.
 */
public class PartyNetwork {
    private static PartyService instance;

    public interface PartyService{
        @POST("/AddPartyServlet")
        public void addParty(@Query("access_token")String accessToken,
                             @Query("name")String name,
                             @Query("tel")String tel,
                             @Query("added_activity_id")String activityId,
                             @Query("activity_title")String activityTitle,
                             @Query("title")String title,
                             @Query("start_time")String startTime,
                             @Query("gathering_place")String gatheringPlace,
                             @Query("notice")String notice,
                             Callback<Object> callback);

        @POST("/GetPartyByIDServlet")
        public void getPartyByID(@Query("partyID") String partyID,
                                 Callback<Party> callback);

        @POST("/GetPartiesByAccessTokenServlet")
        public void getPartiesICreated(@Query("accessToken")String accessToken,
                                       @Query("offset")Integer offset,
                                       @Query("size")Integer size,
                                       Callback<List<Party>> parties);

        @POST("/InviteMembersServlet")
        public void inviteMembers(@Query("partyID")String partyID,
                                  @Query("accessTokens")String accessToken,
                                  Callback<Object> callback);

        @POST("/AddActivityServlet")
        public void addActivity(@Query("partyID")String partyID,
                                @Query("activityID")String activityID,
                                @Query("activityTitle")String activityTitle,
                                Callback<String> callback);

        @POST("/GetPartiesByMemberAccessToken")
        public void getPartiesIJoined(@Query("accessToken")String accessToken,
                                                  @Query("offset")Integer offset,
                                                  @Query("size")Integer size,
                                                  Callback<List<Party>> parties);
    }

    public static synchronized PartyService getInstance(){
        if (null==instance){
            instance = NetworkUtils.getService(PartyService.class, new BaseNetwork.BaseErrorHandler());
        }
        return instance;
    }
}
