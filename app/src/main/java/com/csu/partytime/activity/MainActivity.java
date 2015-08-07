package com.csu.partytime.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.fragment.RecommendationFragment_;
import com.csu.partytime.model.Account;
import com.csu.partytime.network.AccountNetwork;
import com.csu.partytime.util.AccountUtils;
import com.csu.partytime.util.DensityUtil;
import com.csu.partytime.util.ErrorUtils;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.CircleImageView;
import com.csu.partytime.view.LoadingDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//OnTabChangeListener
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    public final static String LIST_TYPE = "listType";
    public final static String PARTIES_I_CREATE = "parties_i_create";
    public final static String PARTIES_RELATIVE_TO_ME = "parties_relative_to_me";

    @ViewById(R.id.tb_main_activity_actionbar)
    Toolbar actionbar;
    @ViewById(R.id.dl_main_activity_side_menu)
    DrawerLayout dlSideMenu;
    @ViewById(R.id.tv_side_menu_parties_i_create)
    TextView tvSideMenuCreate;
    @ViewById(R.id.tv_side_menu_parties_relative_to_me)
    TextView tvSideMenuRelative;
    @ViewById(R.id.tv_side_menu_setting)
    TextView tvSideMenuSetting;

    @ViewById(R.id.civ_side_menu_avatar)
    CircleImageView civMenuAccount;
    @ViewById(R.id.tv_side_menu_account_name)
    TextView tvAccountName;

    RecommendationFragment_ recommendationFragment_;
    boolean isExist;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendationFragment_ = new RecommendationFragment_();
        Bundle arguments = new Bundle();
        arguments.putString(RecommendationFragment_.ARGUMENTS_ATTACH_TO,
                RecommendationFragment_.ATTACH_TO_MAIN);
        recommendationFragment_.setArguments(arguments);
        loadingDialog = new LoadingDialog(this);
    }

    @AfterViews
    public void initViews() {

        initActionBar();

        initSideMenu();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ll_main_activity_content, recommendationFragment_)
                .commit();
    }

    private void initSideMenu() {
        Account currentAccount = PartyApplication.currentAccount;

        Picasso.with(this).load(currentAccount.avatar.mid)
                .centerCrop().resize(DensityUtil.pix2dp(80), DensityUtil.pix2dp(80))
                .placeholder(R.mipmap.default_avatar)
                .into(civMenuAccount);
        tvAccountName.setText(currentAccount.nickname);
    }

    private void initActionBar() {
        setSupportActionBar(actionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View actionbarView = getLayoutInflater().inflate(R.layout.actionbar_main_title, null);
        TextView tvTitle = (TextView) actionbarView.findViewById(R.id.tv_actionbar_main_title);
        tvTitle.setText("活动推荐");
        ImageView ivMenu = (ImageView) actionbarView.findViewById(R.id.iv_actionbar_main_menu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlSideMenu.openDrawer(Gravity.START);
            }
        });
        getSupportActionBar().setCustomView(actionbarView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotificationActivity_.class));
            }
        });
        return true;
    }

    @Click({R.id.ll_menu_account,
            R.id.tv_side_menu_parties_i_create,
            R.id.tv_side_menu_parties_relative_to_me,
            R.id.tv_side_menu_setting,
    R.id.tv_side_menu_partners})
    public void click(View view) {
        Intent toPartyListActivity = new Intent(this, PartyListActivity_.class);
        switch (view.getId()) {
            case R.id.ll_menu_account:
                startActivity(new Intent(this, AccountActivity_.class));
                break;

            case R.id.tv_side_menu_setting:
                startActivity(new Intent(this, SettingActivity_.class));
                break;

            case R.id.tv_side_menu_parties_i_create:
                toPartyListActivity.putExtra(LIST_TYPE, PARTIES_I_CREATE);
                startActivity(toPartyListActivity);
                break;

            case R.id.tv_side_menu_parties_relative_to_me:
                toPartyListActivity.putExtra(LIST_TYPE, PARTIES_RELATIVE_TO_ME);
                startActivity(toPartyListActivity);
                break;

            case R.id.tv_side_menu_partners:
                startActivity(new Intent(this,PartnersActivity_.class));
                break;
        }
        dlSideMenu.closeDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        if (isExist) {
            loadingDialog.show();
            updateAccount();
        } else {
            ToastUtil.show(this, "再次点击退出程序");
            isExist = true;
        }
    }

    private void updateAccount() {
        AccountNetwork
                .getInstance()
                .getAccountByAccessToken(PartyApplication.currentAccount.accessToken,
                        new Callback<Account>() {
                            @Override
                            public void success(Account account, Response response) {
                                loadingDialog.dismiss();
                                AccountUtils.addSPAccount(MainActivity.this,account);
                                finish();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                loadingDialog.dismiss();
                                ErrorUtils.show(MainActivity.this, error);
                            }
                        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        isExist = false;
    }
}
