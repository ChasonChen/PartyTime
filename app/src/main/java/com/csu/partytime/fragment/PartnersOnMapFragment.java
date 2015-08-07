package com.csu.partytime.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.csu.partytime.R;
import com.csu.partytime.model.Account;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.ToastUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_partners_on_map)
public class PartnersOnMapFragment extends Fragment {
    List<Account> accounts;
    @ViewById(R.id.mv_partners_on_map)
    MapView mvAmapView;
    AMap aMap;

    Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        accounts = Parcels.unwrap(getArguments().getParcelable("account"));
    }

    @AfterViews
    public void initViews(){
        mvAmapView.onCreate(savedInstanceState);
        aMap = mvAmapView.getMap();
        addMarkers();
    }

    public void addMarkers(){

        ArrayList<MarkerOptions> markerOptionses = new ArrayList<>();
        for (Account temp: accounts){
            MarkerOptions markerOptions = new MarkerOptions();
            ImageView ivIcon = new ImageView(getActivity());
            Picasso.with(getActivity()).load(temp.avatar.tiny).into(ivIcon);
            BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromView(ivIcon);
            markerOptions.icon(bitmapDescriptor);
            markerOptions.position(new LatLng(temp.currentPos.Latitude,temp.currentPos.longitude));
            markerOptions.anchor(0.5f,0.5f);
            markerOptionses.add(markerOptions);
        }
        aMap.addMarkers(markerOptionses,true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mvAmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvAmapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvAmapView.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvAmapView.onDestroy();
    }

}
