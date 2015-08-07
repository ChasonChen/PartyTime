package com.csu.partytime.model;

import com.csu.partytime.model.base.BaseModel;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Chen_Xingchao} on 2015/4/22.
 */
@Parcel
public class Party extends BaseModel {
    public String id;
    public String title;
    public String accessToken;
    public String gatheringPlace;
    public String startTime;
    public List<ActivityCounter> activities;
    public List<AccountCounter> invitedMembers;
    public List<AccountCounter> joinedMembers;
    public String notice;
    public Long upvote;

    public String cTime;
    public String uTime;

    public Boolean isVotingEnd;
    public Boolean isPartyShared;

    public Party(){
        this.activities = new ArrayList<ActivityCounter>();
        this.invitedMembers = new ArrayList<AccountCounter>();
        this.joinedMembers = new ArrayList<AccountCounter>();
    }
}
