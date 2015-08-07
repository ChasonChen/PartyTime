package com.csu.partytime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csu.partytime.R;

/**
 * Created by ${Chen_Xingchao} on 2015/4/8.
 */
public class RecommendationListAdapter extends BaseAdapter {
//    List<PoiDetailResult> poiDetailResults;
    LayoutInflater mInflater;
    Context context;

    public RecommendationListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
//        this.poiDetailResults = poiDetailResults;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final RecommendationViewHolder viewHolder;
        if (view == null) {
            viewHolder = new RecommendationViewHolder();
            view = mInflater.inflate(R.layout.item_recommendation_list, null);
            viewHolder.ivImg = (ImageView) view.findViewById(R.id.iv_recommendation_item_img);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tv_recommendation_item_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (RecommendationViewHolder) view.getTag();
        }

        /*PoiDetailResult poiDetailResult = poiDetailResults.get(position);
        Ion.with(context)
                .load(poiDetailResult.getDetailUrl())
                .withBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                viewHolder.ivImg.setImageBitmap(result);
            }
        });*/
        /*viewHolder.tvName.setText(poiDetailResult.getName() +
                "=" + poiDetailResult.getType() +
                "=" + poiDetailResult.getTelephone() +
                "=" + poiDetailResult.getEnvironmentRating() +
                "=" + poiDetailResult.getTag());*/
        return view;
    }

    public class RecommendationViewHolder {
        ImageView ivImg;
        TextView tvName;
    }
}
