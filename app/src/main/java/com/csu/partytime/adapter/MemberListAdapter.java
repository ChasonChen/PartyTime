package com.csu.partytime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.csu.partytime.R;
import com.csu.partytime.model.Account;
import com.csu.partytime.util.DensityUtil;
import com.csu.partytime.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chason on 2015/5/28.
 */
public class MemberListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater mInflater;
    List<Account> accounts;

    public MemberListAdapter(Context context , List<Account> accounts){
        this.accounts = accounts;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void addAll(List<Account> accounts){
        this.accounts.clear();
        this.accounts.addAll(accounts);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Account getItem(int i) {
        return accounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null==view){
            view = mInflater.inflate(R.layout.item_members_list,null);
            holder = new ViewHolder();
            holder.civAvatar = (CircleImageView) view.findViewById(R.id.civ_member_list_item_avatar);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        data2View(accounts.get(position),holder);
        return view;
    }

    protected void data2View(Account account,ViewHolder holder){
        Picasso.with(context)
                .load(account.avatar.mid)
                .centerCrop()
                .resize(DensityUtil.pix2dp(80),DensityUtil.pix2dp(80))
                .placeholder(R.mipmap.default_avatar)
                .into(holder.civAvatar);
    }

    protected class ViewHolder{
        CircleImageView civAvatar;
    }
}
