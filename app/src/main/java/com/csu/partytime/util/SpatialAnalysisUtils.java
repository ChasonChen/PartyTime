package com.csu.partytime.util;



import com.csu.partytime.model.LatLonPoint;

import java.util.List;

/**
 * Created by Chason on 2015/5/24.
 */
public class SpatialAnalysisUtils {
    public static LatLonPoint getGravityCenter(List<LatLonPoint> points){
        int pointNum= points.size();
        double latSum = 0;
        double lonSum = 0;
        for (int i = 0; i<pointNum;i++){
            latSum+=points.get(i).Latitude;
            lonSum+=points.get(i).longitude;
        }
        return new LatLonPoint(latSum/pointNum,lonSum/pointNum);
    }
}
