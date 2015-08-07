package com.csu.partytime.model;

import com.csu.partytime.model.base.BaseModel;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Chason on 2015/5/27.
 */
@Parcel
public class PMessage extends BaseModel {
    @SerializedName("_id")
    public String id;
    public String partyID;
    public Party party;
    public String mContent;
    public List<MState> mStates;

    public String cTime;
}
