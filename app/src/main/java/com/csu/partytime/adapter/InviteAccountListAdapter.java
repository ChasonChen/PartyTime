package com.csu.partytime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.csu.partytime.R;
import com.csu.partytime.model.Account;
import com.csu.partytime.util.DensityUtil;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chason on 2015/5/27.
 */
public class InviteAccountListAdapter extends BaseAdapter{
    Context context;
    LayoutInflater mInflater;
    List<Account> accounts;
    List<Account> invitedAccounts;

    public InviteAccountListAdapter(Context context,List<Account> accounts){
        this.context = context;
        this.accounts = accounts;
        this.mInflater = LayoutInflater.from(context);
        this.invitedAccounts = new ArrayList<>();
    }

    public void addAll(List<Account> accounts){
        this.accounts.clear();
        this.accounts.addAll(accounts);
    }

    public List<Account> getInvitedAccounts(){
        return this.invitedAccounts;
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
            view = mInflater.inflate(R.layout.item_invite_account_list,null);
            holder = new ViewHolder();
            holder.civAvatar = (CircleImageView) view.findViewById(R.id.civ_invite_account_avatar);
            holder.tvNickname = (TextView) view.findViewById(R.id.tv_invite_account_nickname);
            holder.tvCity = (TextView) view.findViewById(R.id.tv_invite_account_city);
            holder.cbIsInvited = (CheckBox) view.findViewById(R.id.cb_invite_account_is_invite);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        data2View(holder,accounts.get(position));

        return view;
    }

    protected void data2View(ViewHolder holder,Account account){
        Picasso.with(context)
                .load(account.avatar.mid)
                .centerCrop()
                .resize(DensityUtil.pix2dp(40),DensityUtil.pix2dp(40))
                .placeholder(R.mipmap.default_avatar)
                .into(holder.civAvatar);
        holder.tvNickname.setText(account.nickname);
        holder.tvCity.setText(account.province+" "+account.city);
        holder.cbIsInvited.setOnCheckedChangeListener(new CheckChangeListener(account));
    }

    protected class ViewHolder{
        CircleImageView civAvatar;
        TextView tvNickname;
        TextView tvCity;
        CheckBox cbIsInvited;
    }

    protected class CheckChangeListener implements CompoundButton.OnCheckedChangeListener{
        Account account;

        public CheckChangeListener(Account account){
            this.account = account;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                invitedAccounts.add(account);
            }else {
                invitedAccounts.remove(account);
            }
        }
    }
}
