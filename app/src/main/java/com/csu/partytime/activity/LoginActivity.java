package com.csu.partytime.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.csu.partytime.Constant;
import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.event.BaseIUiEvent;
import com.csu.partytime.listener.BaseIUiListener;
import com.csu.partytime.model.Account;
import com.csu.partytime.network.AccountNetwork;
import com.csu.partytime.util.AccountUtils;
import com.csu.partytime.util.DebugLog;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.LoadingDialog;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    Tencent tencent;
    Account currentAccount;
    Gson gson;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tencent = PartyApplication.tencent;
        gson = new Gson();
    }

    @AfterViews
    public void initViews() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog = new LoadingDialog(this);
        currentAccount = AccountUtils.getSPAccount(this);
        if (null!=currentAccount){
            PartyApplication.currentAccount=currentAccount;
            startActivity(new Intent(this,MainActivity_.class));
            finish();
        }
    }

    @Click(R.id.ibt_qq_login)
    public void qqLogin(View view) {
        if (!tencent.isSessionValid()) {
            tencent.login(this, "all", new BaseIUiListener(BaseIUiListener.IUI_LOGIN));
            loadingDialog.show();
        }
    }

    public void onEventMainThread(BaseIUiEvent baseIUiEvent) {
        try {
            JSONObject jsonData = new JSONObject(baseIUiEvent.uiData.toString());
            if (jsonData.getInt("ret") == 0) {
                switch (baseIUiEvent.eventType) {
                    case BaseIUiEvent.BASE_IUI_COMPLETE:
                        switch (baseIUiEvent.iuiType) {
                            case BaseIUiListener.IUI_LOGIN:
                                if (jsonData.getInt("ret") == 0) {
                                    UserInfo userInfo = new UserInfo(this, tencent.getQQToken());
                                    userInfo.getUserInfo(new BaseIUiListener(BaseIUiListener.IUI_GET_USER_INFO));
                                    currentAccount = new Account();
                                    currentAccount.accessToken = jsonData.getString("access_token");
                                }
                                break;

                            case BaseIUiListener.IUI_GET_USER_INFO:
                                initAccount(jsonData);
                                break;
                        }
                        break;

                    case BaseIUiEvent.BASE_IUI_ERROR:
                    case BaseIUiEvent.BASE_IUI_CANCEL:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initAccount(JSONObject jsonData) throws JSONException {
        DebugLog.d("======qqlogin:"+jsonData);
        currentAccount.nickname = jsonData.getString("nickname");
        currentAccount.province = jsonData.getString("province");
        currentAccount.city = jsonData.getString("city");
        currentAccount.gender = jsonData.getString("gender");
        currentAccount.avatar.tiny = jsonData.getString("figureurl_qq_1");
        currentAccount.avatar.mid = jsonData.getString("figureurl_qq_2");

        String jsonStr=gson.toJson(currentAccount);
        AccountNetwork.getInstance().loginByQQ(jsonStr,new Callback<Account>() {
            @Override
            public void success(Account o, Response response) {
                PartyApplication.currentAccount = o;
                AccountUtils.addSPAccount(LoginActivity.this,currentAccount);
                loadingDialog.dismiss();
                startActivity(new Intent(PartyApplication.context, MainActivity_.class));
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                loadingDialog.dismiss();
                ToastUtil.show(LoginActivity.this,error.toString());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
