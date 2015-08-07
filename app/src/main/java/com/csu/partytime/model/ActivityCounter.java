package com.csu.partytime.model;

import com.csu.partytime.model.base.BaseModel;

import org.parceler.Parcel;

/**
 * Created by ${Chen_Xingchao} on 2015/4/25.
 */
@Parcel
public class ActivityCounter extends BaseModel {
    public String activityID;
    public String activityTitle;
    public Integer votes;
}
