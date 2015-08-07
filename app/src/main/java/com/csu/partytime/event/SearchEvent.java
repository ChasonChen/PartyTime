package com.csu.partytime.event;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.csu.partytime.model.LatLonPoint;

/**
 * Created by Chason on 2015/5/11.
 */
public class SearchEvent {
    public String keyword;
    public Integer radius;
//    public AMapLocation location;
    public LatLonPoint point;

    public SearchEvent(String keyword, Integer radius) {
        this.keyword = keyword;
        this.radius = radius;
    }

    public SearchEvent(String keyword, Integer radius, LatLonPoint point) {
        this.keyword = keyword;
        this.radius = radius;
        this.point = point;
    }
}
