package com.csu.partytime.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapOptionsCreator;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.csu.partytime.Constant;
import com.csu.partytime.R;
import com.csu.partytime.activity.MainActivity_;
import com.csu.partytime.event.SearchEvent;
import com.csu.partytime.fragment.base.BaseFragment;
import com.csu.partytime.model.LatLonPoint;
import com.csu.partytime.util.DebugLog;
import com.csu.partytime.util.DensityUtil;
import com.csu.partytime.util.SpatialAnalysisUtils;
import com.csu.partytime.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_smart_search)
public class SmartSearchFragment extends BaseFragment
        implements LocationSource, AMapLocationListener {

    OnLocationChangedListener mListener;
    LocationManagerProxy mAMapLocationManager;

    @ViewById(R.id.mv_smart_search_map)
    MapView mapView;
    AMap aMap;
    Bundle savedInstanceState;

    ArrayList<LatLonPoint> analysisPoints;
    LatLonPoint myLatLonPoint;
    ArrayList<Marker> markers;
    Marker centerMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        analysisPoints = new ArrayList<>();
    }

    @AfterViews
    public void initViews() {
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapLongClickListener(new PTMapLongClickListener());
        aMap.setOnMarkerClickListener(new MarkerClickListener());
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_me));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
    }

    @Click(R.id.fab_smart_search_search)
    public void click(View view) {
        search();
    }

    public void search() {

        if (null != gravityCenter) {
            EventBus.getDefault().post(new SearchEvent(null, null, gravityCenter));
        }
        getActivity().onBackPressed();
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this.getActivity());
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != mListener && null != aMapLocation) {
            mListener.onLocationChanged(aMapLocation);
            if (null == myLatLonPoint) {
                myLatLonPoint = new LatLonPoint();
            } else {
                myLatLonPoint.Latitude = aMapLocation.getLatitude();
                myLatLonPoint.longitude = aMapLocation.getLongitude();
                analysisPoints.remove(myLatLonPoint);
                analysisPoints.add(myLatLonPoint);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }


    private class PTMapLongClickListener implements AMap.OnMapLongClickListener {
        ArrayList<MarkerOptions> markerOptions;

        public PTMapLongClickListener() {
            markerOptions = new ArrayList<>();
        }

        @Override
        public void onMapLongClick(LatLng latLng) {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_partner));
            markerOptions.add(markerOption);
            markers = aMap.addMarkers(markerOptions, true);
            analysisPoints.add(new LatLonPoint(latLng.latitude, latLng.longitude));
            updateCenter();
        }
    }

    LatLonPoint gravityCenter;

    private void updateCenter() {
        if (analysisPoints.size() > 1) {
            gravityCenter = SpatialAnalysisUtils.getGravityCenter(analysisPoints);
            LatLng position = new LatLng(gravityCenter.Latitude, gravityCenter.longitude);
            if (null == centerMarker) {
                MarkerOptions centerOptions = new MarkerOptions();
                centerOptions.position(position);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_party);
                centerOptions.icon(bitmapDescriptor);
                centerMarker = aMap.addMarker(centerOptions);
            } else {
                centerMarker.setPosition(position);
            }
            centerMarker.setVisible(true);
        } else {
            centerMarker.setVisible(false);
        }

    }

    private class MarkerClickListener implements AMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            for (int i = 0; i < analysisPoints.size(); i++) {
                LatLng latLng = marker.getPosition();
                if (analysisPoints.get(i).equals(new LatLonPoint(latLng.latitude, latLng.longitude))) {
                    analysisPoints.remove(i);
                }
            }
            marker.destroy();
            updateCenter();
            return false;
        }
    }
}
