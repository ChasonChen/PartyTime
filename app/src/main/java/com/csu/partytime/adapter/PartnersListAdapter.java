package com.csu.partytime.adapter;

import android.content.Context;
import android.view.View;

import com.csu.partytime.model.Account;

import java.util.List;

/**
 * Created by Chason on 2015/6/7.
 */
public class PartnersListAdapter extends InviteAccountListAdapter {

    public PartnersListAdapter(Context context, List<Account> accounts) {
        super(context, accounts);
    }

    @Override
    protected void data2View(ViewHolder holder, Account account) {
        super.data2View(holder, account);
        holder.cbIsInvited.setVisibility(View.GONE);
        holder.tvNickname.setText(null == account.name ? account.nickname : account.name);
    }
}
