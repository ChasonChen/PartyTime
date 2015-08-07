package com.csu.partytime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.csu.partytime.R;
import com.csu.partytime.model.PMessage;
import com.csu.partytime.util.TimeUtils;

import java.util.List;

/**
 * Created by Chason on 2015/5/28.
 */
public class PMessageListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater mInflater;
    List<PMessage> pMessages;

    public PMessageListAdapter(Context context,List<PMessage> pMessages){
        this.context = context;
        this.pMessages = pMessages;
        this.mInflater = LayoutInflater.from(context);
    }

    public void addAll(List<PMessage> pMessages){
        this.pMessages.clear();
        this.pMessages.addAll(pMessages);
    }

    @Override
    public int getCount() {
        return pMessages.size();
    }

    @Override
    public PMessage getItem(int i) {
        return pMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null==view){
            view = mInflater.inflate(R.layout.item_pmessage_list,null);
            holder = new ViewHolder();
            holder.tvContent = (TextView) view.findViewById(R.id.tv_pmessage_list_content);
            holder.tvCTime = (TextView) view.findViewById(R.id.tv_pmessage_list_ctime);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        data2View(pMessages.get(position),holder);
        return view;
    }

    private void data2View(PMessage pMessage,ViewHolder holder){
        holder.tvContent.setText(pMessage.mContent);
        holder.tvCTime.setText(TimeUtils.getFormatTime(pMessage.cTime));
    }

    private class ViewHolder{
        TextView tvContent;
        TextView tvCTime;
    }
}
