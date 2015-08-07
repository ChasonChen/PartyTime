package com.csu.partytime.model;

import com.csu.partytime.model.base.BaseModel;

import org.parceler.Parcel;

/**
 * Created by ${Chen_Xingchao} on 2015/5/9.
 */
@Parcel
public class VersionInfo extends BaseModel{

    public String versionName;
    public String versionDesc;
    public String versionUpdateTime;
    public String versionNewUrl;

}
