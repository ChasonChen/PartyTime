package com.csu.partytime.model;

import com.csu.partytime.model.base.BaseModel;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ${Chen_Xingchao} on 2015/4/7.
 */
@Parcel
public class PartyActivity  extends BaseModel{
    @SerializedName("_id")
    public String _id;
    public String title;
    public String tel;
    public String type;
    public String cityName;
    public String provinceName;
    public String email;
    public int distance;
    public LatLonPoint latLonPoint;
    public String snippet;
    public boolean isGroupBuy;
    public boolean isDiscounts;
    public Dining dining;
}