package com.csu.partytime.activity;

import android.os.Bundle;

import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.fragment.AllAccountsFragment;
import com.csu.partytime.fragment.AllAccountsFragment_;
import com.csu.partytime.model.Party;
import com.csu.partytime.util.ActionbarUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.parceler.Parcels;

@EActivity(R.layout.activity_invite)
public class InviteActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void initViews(){
        AllAccountsFragment_ allAccountsFragment=new AllAccountsFragment_();
        Bundle arguments = new Bundle();
        arguments.putParcelable("party",getIntent().getParcelableExtra("party"));
        allAccountsFragment.setArguments(arguments);
        ActionbarUtil.initBackwardTitleActionBar(this, "邀请好友");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rl_invite_activity,allAccountsFragment)
                .commit();
    }

}
