package com.csu.partytime.model;

import com.csu.partytime.model.base.BaseModel;

import org.parceler.Parcel;

/**
 * Created by ${Chen_Xingchao} on 2015/4/24.
 */
@Parcel
public class LatLonPoint extends BaseModel {
    public double Latitude;
    public double longitude;

    public LatLonPoint() {
    }

    public LatLonPoint(double latitude, double longitude) {
        Latitude = latitude;
        this.longitude = longitude;
    }

    public boolean equals(LatLonPoint o) {
        return this.longitude == o.longitude && this.Latitude == o.Latitude;
    }
}
