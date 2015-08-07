package com.csu.partytime.model;

import com.csu.partytime.model.base.BaseModel;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Chen_Xingchao} on 2015/4/14.
 */
@Parcel
public class Account extends BaseModel{
    public String _id;
    public String accessToken;
    public String nickname;
    public String name;
    public String tel;
    public String password;
    public Avatar avatar;
    public String city;
    public String province;
    public String gender;
    public String email;
    public String signature;
    public LatLonPoint currentPos;
    public List<String> friends;
    public String birthday;
    public String cTime;
    public String uTime;
    public String lastLoginTime;

    public Boolean isCreator;
    public Boolean isMember;

    public Account(){
        this.avatar = new Avatar();
        this.friends = new ArrayList<String>();
    }
}
