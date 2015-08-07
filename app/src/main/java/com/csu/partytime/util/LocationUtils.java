package com.csu.partytime.util;

import com.amap.api.location.AMapLocation;

/**
 * Created by ${Chen_Xingchao} on 2015/4/11.
 */
public class LocationUtils {
    public static void toFormat(AMapLocation aMapLocation){
        DebugLog.d("location format:"+aMapLocation.getAddress()+","
                +aMapLocation.getCity()+","
                +aMapLocation.getDistrict()+","
                +aMapLocation.getFloor()+","
                +aMapLocation.getPoiId()+","
                +aMapLocation.getProvince()+","
                +aMapLocation.getStreet()+","
                +aMapLocation.getAccuracy()+",["
                +aMapLocation.getLatitude()+","
                +aMapLocation.getLongitude()+"]");
    }
}
