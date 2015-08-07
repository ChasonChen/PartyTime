package com.csu.partytime.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.AMapUtils;
import com.csu.partytime.BuildConfig;
import com.csu.partytime.Constant;
import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.VersionInfo;
import com.csu.partytime.network.FileNetwork;
import com.csu.partytime.network.VersionNetwork;
import com.csu.partytime.util.AccountUtils;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.DebugLog;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.NormalDialog;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
//    NiftyDialogBuilder dialogBuilder;
    NormalDialog normalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void initViews() {
        ActionbarUtil.initBackwardTitleActionBar(this, "设置");
        normalDialog = new NormalDialog(this);
        normalDialog.setTitle("新版信息");
        normalDialog.getBtCancel().setText("取消");
        normalDialog.getBtOk().setText("更新");
        /*dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDialogColor(getResources()
                .getColor(R.color.app_color))
                .withTitleColor(getResources().getColor(R.color.white))
                .withMessageColor(getResources().getColor(R.color.white))
                .withEffect(Effectstype.Fall).withDuration(100);*/
    }

    @Click({R.id.bt_setting_activity_logout,
            R.id.bt_setting_activity_check_update})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.bt_setting_activity_logout:
                logout();
                break;
            case R.id.bt_setting_activity_check_update:
                checkUpdate();
                break;
        }
    }

    public void logout() {
        AccountUtils.clearSPAccount(this);
        PartyApplication.currentAccount = null;
        PartyApplication.currentAccount = new Account();
        if (PartyApplication.tencent.isSessionValid()) {
            PartyApplication.tencent.logout(this);
        }
        Intent toLoginActivity = new Intent(new Intent(this, LoginActivity_.class));
        toLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        toLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toLoginActivity);
    }

    public void checkUpdate() {
        VersionNetwork.getInstance().getVersion(new Callback<VersionInfo>() {
            @Override
            public void success(VersionInfo version, Response response) {
                DebugLog.d(version.toString());
                update(version);
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.show(SettingActivity.this, "Network error!");
            }
        });
    }

    public void update(final VersionInfo version) {
        if (BuildConfig.VERSION_NAME.equals(version.versionName)) {
            ToastUtil.show(this, "应用已是最新版！");
        } else {
            normalDialog.show();
            normalDialog.setContent(version.versionName + "\n"
                    + version.versionDesc);
            normalDialog.getBtOk().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(version.versionNewUrl);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    normalDialog.dismiss();
                }
            });
            /*dialogBuilder.withTitle("版本信息")
                    .withMessage(version.versionName + "\n"
                             + version.versionDesc)
                    .withButton1Text("取消")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogBuilder.dismiss();
                        }
                    }).withButton2Text("更新")
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse(version.versionNewUrl);
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                            dialogBuilder.dismiss();
                        }
                    }).show();*/
        }
    }
}
