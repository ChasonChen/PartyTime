package com.csu.partytime.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.csu.partytime.Constant;
import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.CreatePartyActivity_;
import com.csu.partytime.activity.SearchOptionsActivity_;
import com.csu.partytime.adapter.PartyActivityListAdapter;
import com.csu.partytime.event.RefreshEvent;
import com.csu.partytime.event.SearchEvent;
import com.csu.partytime.fragment.base.BaseFragment;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.PartyActivity;
import com.csu.partytime.network.AccountNetwork;
import com.csu.partytime.util.ModelUtils;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.R;
import com.csu.partytime.activity.CreatePartyActivity_;
import com.csu.partytime.adapter.PartyActivityListAdapter;
import com.csu.partytime.fragment.base.BaseFragment;
import com.csu.partytime.model.PartyActivity;
import com.csu.partytime.util.DebugLog;
import com.csu.partytime.util.LocationUtils;
import com.csu.partytime.util.ModelUtils;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.LoadingDialog;
import com.csu.partytime.view.NormalDialog;
import com.gc.materialdesign.widgets.Dialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */

@EFragment(R.layout.fragment_recommendation)
public class RecommendationFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        LocationSource,
        AMapLocationListener,
        PoiSearch.OnPoiSearchListener,
        AbsListView.OnScrollListener {
    Bundle savedInstanceState;
    public final static String EXTRA_INFO_ACTIVITY = "activityInfo";
    public final static String ARGUMENTS_ATTACH_TO = "attachTo";
    public final static String ATTACH_TO_MAIN = "attachToMain";
    public final static String ATTACH_TO_ADD_ACT = "attachToAddAct";

    @ViewById(R.id.mv_fragment_recommendation)
    MapView mapView;
    AMap aMap;
    OnLocationChangedListener mListener;
    LocationManagerProxy mAMapLocationManager;
    //    AMapLocation queryLocation;
    AMapLocation queryLocation;
    PoiSearch poiSearch;
    List<PartyActivity> partyAcitivities;
    PartyActivityListAdapter activityListAdapter;

    @ViewById(R.id.srl_recommend_list)
    SwipeRefreshLayout srlRecommendations;
    @ViewById(R.id.lv_partyactivity_list)
    ListView lvPartyActivityList;

    @ViewById(R.id.fab_create_party)
    FloatingActionButton createParty;
    @ViewById(R.id.fam_menu)
    FloatingActionMenu famMenu;
    @ViewById(R.id.fab_create_party)
    FloatingActionButton fabCreateParty;

    LoadingDialog loadingDialog;

    Account account;
    boolean isCustomOptions;

    String fragmentAttachTo;
    NormalDialog normalDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        partyAcitivities = new ArrayList<>();
        account = PartyApplication.currentAccount;
        EventBus.getDefault().register(this);
        isCustomOptions = false;
        fragmentAttachTo=getArguments().getString(ARGUMENTS_ATTACH_TO);
    }

    @AfterViews
    public void initViews() {
        loadingDialog = new LoadingDialog(this.getActivity());
        normalDialog = new NormalDialog(this.getActivity());
        normalDialog.setTitle("活动信息");
        initMapView();
        initRecommendationsList();
    }

    private void initMapView() {
        mapView.onCreate(savedInstanceState);
        if (fragmentAttachTo.equals(RecommendationFragment_.ATTACH_TO_ADD_ACT)){
            mapView.setVisibility(View.GONE);
            fabCreateParty.setVisibility(View.GONE);
        }else {
            mapView.setVisibility(View.VISIBLE);
            fabCreateParty.setVisibility(View.VISIBLE);
        }
        aMap = mapView.getMap();
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    private void initRecommendationsList() {
        activityListAdapter = new PartyActivityListAdapter(getActivity(), partyAcitivities);
        AnimationAdapter animationAdapter = new AlphaInAnimationAdapter(activityListAdapter);
        animationAdapter.setAbsListView(lvPartyActivityList);
        lvPartyActivityList.setAdapter(animationAdapter);
        lvPartyActivityList.setDividerHeight(0);
        lvPartyActivityList.setOnScrollListener(this);
        srlRecommendations.setColorSchemeResources(R.color.app_color);
        srlRecommendations.setOnRefreshListener(this);
        loadingDialog.show();
    }

    @Override
    public void onRefresh() {
        if (null != queryLocation) {
            initPoiQuery(1);
            partyAcitivities.clear();
            poiSearch.searchPOIAsyn();
        }
    }

    public void updateAccountPos(final AMapLocation location) {
        AccountNetwork.getInstance().updateLocation(
                account.accessToken,
                location.getLatitude(),
                location.getLongitude(),
                new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        account.currentPos = new com.csu.partytime.model.LatLonPoint(
                                location.getLatitude(),
                                location.getLongitude()
                        );
                        PartyApplication.myLocation = location;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ToastUtil.show(getActivity(), error.toString());
                    }
                }
        );
    }

    @ItemClick(R.id.lv_partyactivity_list)
    public void onItemClick(final PartyActivity activity) {
        normalDialog.show();
        if (fragmentAttachTo.equals(ATTACH_TO_ADD_ACT)){
            normalDialog.setContent(activity.title+"\n"+activity.type);
            normalDialog.getBtOk().setText("添加");
            normalDialog.getBtOk().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new RefreshEvent(activity));
                    normalDialog.dismiss();
                    getActivity().onBackPressed();
                }
            });
        }else {
            normalDialog.setContent("创建聚会，并添加活动"+activity.title+"\n"+activity.type);
            normalDialog.getBtOk().setText("创建聚会");
            normalDialog.getBtOk().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    normalDialog.dismiss();
                    Intent toCreatePartyActivity = new Intent(getActivity(), CreatePartyActivity_.class);
                    toCreatePartyActivity.putExtra(EXTRA_INFO_ACTIVITY, Parcels.wrap(activity));
                    startActivity(toCreatePartyActivity);
                }
            });
        }
    }

    @Click({R.id.fab_create_party, R.id.fab_exchange_search_addition})
    public void actionButtonClick(View view) {
        switch (view.getId()) {
            case R.id.fab_create_party:
                startActivity(new Intent(getActivity(), CreatePartyActivity_.class));
                break;

            case R.id.fab_exchange_search_addition:
                startActivity(new Intent(getActivity(), SearchOptionsActivity_.class));
                break;
        }
        famMenu.close(true);
    }

    private void initPoiQuery(int pageNumber) {
        PoiSearch.Query poiQuery = new PoiSearch.Query(Constant.PARTY_ACTIVITY_QUERY_KEY_WORD,
                "", queryLocation.getCityCode());
        poiQuery.setPageSize(Constant.PARTY_ACTIVITY_PAGE_SIZE);
        poiQuery.setPageNum(pageNumber);
        poiSearch = new PoiSearch(this.getActivity(), poiQuery);
        LatLonPoint latLonPoint = new LatLonPoint(queryLocation.getLatitude(),
                queryLocation.getLongitude());
        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint,
                Constant.PARTY_ACTIVITY_SEARCH_RADIUS));
        poiSearch.setOnPoiSearchListener(this);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mListener != null && aLocation != null) {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
            updateAccountPos(aLocation);
            if (!isCustomOptions) {
                this.queryLocation = aLocation;
            }
            if (partyAcitivities.size() == 0) {
                onRefresh();
            }
        }
    }

    /**
     * Poi搜索成功后的回调函数
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == 0) {
            if (null != poiResult&&null!=poiResult.getPois()) {
                loadingDialog.dismiss();
                if (poiResult.getPois().size() == 0) {
                    srlRecommendations.setRefreshing(false);
                    ToastUtil.show(getActivity(), "Nothing to be searched!");
                    return;
                }
                for (PoiItem poiItem : poiResult.getPois()) {
                    poiSearch.searchPOIDetailAsyn(poiItem.getPoiId());
                }
            } else {
                ToastUtil.show(getActivity(), "Nothing to be searched!");
            }
        } else {
            ToastUtil.show(getActivity(), "Fail to search!");
        }
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {
        if (null != poiItemDetail) {
            PartyActivity activity = ModelUtils.poiDetailItem2PartyActivity(poiItemDetail);
            partyAcitivities.add(activity);
            activityListAdapter.notifyDataSetChanged();
            srlRecommendations.setRefreshing(false);
        }
    }

    public void onEvent(SearchEvent e) {
        if (null != e.keyword) {
            Constant.PARTY_ACTIVITY_QUERY_KEY_WORD = e.keyword;
        }

        if (null != e.radius) {
            Constant.PARTY_ACTIVITY_SEARCH_RADIUS = e.radius * 1000;
        }

        if (null != e.point) {
            isCustomOptions = true;
            queryLocation.setLatitude(e.point.Latitude);
            queryLocation.setLongitude(e.point.longitude);
        }
        onRefresh();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this.getActivity());
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    /**
     * 停止定位
     */
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int state) {
        if (state == 0) {
            ArrayList<MarkerOptions> markers = new ArrayList<>();
            aMap.clear();
            for (int i = firstVisibleItem; i < (firstVisibleItem + visibleCount); i++) {
                PartyActivity activity = partyAcitivities.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(activity.latLonPoint.Latitude, activity.latLonPoint.longitude));
                markerOptions.title(activity.title);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_activity));
                markers.add(markerOptions);
            }
            aMap.addMarkers(markers, true);
        }
    }

    int firstVisibleItem, visibleCount, totalCount;

    @Override
    public void onScroll(AbsListView absListView,
                         int firstVisibleItem,
                         int visibleCount,
                         int totalCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.visibleCount = visibleCount;
        this.totalCount = totalCount;
    }

    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
