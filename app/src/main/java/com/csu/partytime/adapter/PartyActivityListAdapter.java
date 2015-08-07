package com.csu.partytime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.model.PartyActivity;
import com.csu.partytime.util.DensityUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ${Chen_Xingchao} on 2015/4/20.
 */
public class PartyActivityListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mInflater;
    List<PartyActivity> partyActivities;
    AMapLocation currentLocation;

    public PartyActivityListAdapter(Context context, List<PartyActivity> partyActivities) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.partyActivities = partyActivities;
    }

    @Override
    public int getCount() {
        return partyActivities.size();
    }

    @Override
    public Object getItem(int i) {
        return partyActivities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ActivityViewHolder holder;
        if (null == view) {
            holder = new ActivityViewHolder();
            view = mInflater.inflate(R.layout.item_partyactivity_list, null);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_activity_title);
            holder.tvAddress = (TextView) view.findViewById(R.id.tv_activity_address);
            holder.tvType = (TextView) view.findViewById(R.id.tv_activity_type);
            holder.ivPhoto = (ImageView) view.findViewById(R.id.iv_activity_photo);
            view.setTag(holder);
        } else {
            holder = (ActivityViewHolder) view.getTag();
        }

        dataToView(i, holder);
        return view;
    }

    private void dataToView(int position, final ActivityViewHolder holder) {
        PartyActivity activity = partyActivities.get(position);
        holder.tvTitle.setText(activity.title);
        holder.tvAddress.setText(activity.snippet);

        this.currentLocation = PartyApplication.myLocation;
        if (null!=currentLocation) {
            LatLng point = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            LatLng actLocation = new LatLng(activity.latLonPoint.Latitude, activity.latLonPoint.longitude);
            holder.tvType.setText((AMapUtils.calculateLineDistance(point, actLocation) / 1000) + "km");
        }else {
            holder.tvType.setText("初始化当前位置...");
        }

        if (null != activity.dining
                && null != activity.dining.photos
                && activity.dining.photos.size() > 0
                && null!= activity.dining.photos.get(0).url) {
            Picasso.with(context)
                    .load(activity.dining.photos.get(0).url)
                    .placeholder(R.mipmap.ic_default_activity_img)
                    .error(R.mipmap.ic_default_activity_img)
                    .centerCrop().resize(DensityUtil.pix2dp(80),DensityUtil.pix2dp(80))
                    .into(holder.ivPhoto);
        }else {
            holder.ivPhoto.setImageResource(R.mipmap.ic_default_activity_img);
        }
    }

    public class ActivityViewHolder {
        TextView tvTitle;
        TextView tvAddress;
        TextView tvType;
        ImageView ivPhoto;
    }

}
