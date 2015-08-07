package com.csu.partytime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.csu.partytime.R;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.Party;
import com.csu.partytime.network.AccountNetwork;
import com.csu.partytime.util.TimeUtils;
import com.csu.partytime.util.ToastUtil;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ${Chen_Xingchao} on 2015/4/30.
 */
public class PartyListAdapter extends BaseAdapter{
    Context context;
    LayoutInflater mInflater;
    List<Party> parties;

    public PartyListAdapter(Context context,List<Party> parties){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.parties = parties;
    }

    @Override
    public int getCount() {
        return parties.size();
    }

    @Override
    public Party getItem(int i) {
        return parties.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PartyViewHolder holder;
        if (null==view){
            view = mInflater.inflate(R.layout.item_party_list,null);
            holder = new PartyViewHolder();
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_party_list_title);
            holder.tvCreator = (TextView) view.findViewById(R.id.tv_party_list_creator);
            holder.tvStartTime = (TextView) view.findViewById(R.id.tv_party_list_start_time);
            holder.tvCTime = (TextView) view.findViewById(R.id.tv_party_list_c_time);
            view.setTag(holder);
        }else {
            holder = (PartyViewHolder) view.getTag();
        }

        dataToView(parties.get(i),holder);
        return view;
    }

    public void dataToView(Party party, final PartyViewHolder holder){

        holder.tvTitle.setText(party.title);
        holder.tvStartTime.setText(TimeUtils.getFormatTime(party.startTime));
        holder.tvCTime.setText(TimeUtils.getFormatTime(party.cTime));
        AccountNetwork.getInstance().getAccountByAccessToken(party.accessToken,
                new Callback<Account>() {
                    @Override
                    public void success(Account object, Response response) {
                        holder.tvCreator.setText(object.name);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ToastUtil.show(context,error.toString());
                    }
                });
    }

    public class PartyViewHolder{
        TextView tvTitle;
        TextView tvCreator;
        TextView tvStartTime;
        TextView tvCTime;
    }
}
