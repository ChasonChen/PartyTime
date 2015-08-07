package com.csu.partytime.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.model.Account;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.DensityUtil;
import com.csu.partytime.view.CircleImageView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

@EActivity(R.layout.activity_account)
public class AccountActivity extends BaseActivity {
    Account currentAccount;

    @ViewById(R.id.civ_account_info_avatar)
    CircleImageView civAvatar;
    @ViewById(R.id.tv_account_info_nickname)
    TextView tvNickname;
    @ViewById(R.id.tv_account_info_name)
    TextView tvName;
    @ViewById(R.id.tv_account_info_gender)
    TextView tvGender;
    @ViewById(R.id.tv_account_info_tel)
    TextView tvTel;
    @ViewById(R.id.tv_account_info_set_tel)
    TextView tvSetTel;
    @ViewById(R.id.tv_account_info_address)
    TextView tvAddress;

    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Account account = Parcels.unwrap(getIntent().getParcelableExtra("account"));
        if (null==account){
            currentAccount = PartyApplication.currentAccount;
        }else {
            currentAccount = account;
        }
        sb = new StringBuilder();
    }

    @AfterViews
    public void initViews(){
        ActionbarUtil.initBackwardTitleActionBar(this,"个人中心");
        Picasso.with(this).load(currentAccount.avatar.mid)
                .centerCrop().resize(DensityUtil.pix2dp(100),DensityUtil.pix2dp(100))
                .placeholder(R.mipmap.default_avatar)
                .into(civAvatar);
        tvNickname.setText(currentAccount.nickname);
        tvGender.setText(currentAccount.gender);
        if (null!= currentAccount.name){
            tvName.setText(currentAccount.name);
        }

        if (null!= currentAccount.tel){
            tvTel.setText(currentAccount.tel);
            tvSetTel.setVisibility(View.GONE);
        }else {
            tvSetTel.setVisibility(View.VISIBLE);
        }
        if (null!=currentAccount.province){
            sb.append(currentAccount.province);
        }

        if (null!=currentAccount.city){
            sb.append("  "+currentAccount.city);
        }

        if (sb.length()==0){
            tvAddress.setVisibility(View.GONE);
        }else {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(sb.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

}
