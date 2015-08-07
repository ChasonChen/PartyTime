package com.csu.partytime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.adapter.PMessageListAdapter;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.PMessage;
import com.csu.partytime.network.PMessageNetwork;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.ErrorUtils;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.LoadingDialog;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_notification)
public class NotificationActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener{
    @ViewById(R.id.srl_notification_activity)
    SwipeRefreshLayout srlSwipe;
    @ViewById(R.id.lv_notification_activity)
    ListView lvPMessages;

    PMessageListAdapter adapter;
    List<PMessage> pMessages;
    Account currentAccount;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pMessages = new ArrayList<>();
        currentAccount = PartyApplication.currentAccount;
    }

    @AfterViews
    public void initViews(){
        loadingDialog = new LoadingDialog(this);
        ActionbarUtil.initBackwardTitleActionBar(this,"通知");
        adapter = new PMessageListAdapter(this,pMessages);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(lvPMessages);
        lvPMessages.setAdapter(animationAdapter);
        lvPMessages.setDividerHeight(0);
        srlSwipe.setColorSchemeResources(R.color.app_color);
        srlSwipe.setOnRefreshListener(this);
        loadingDialog.show();
        getPMessages();
    }

    @Override
    public void onRefresh() {
        getPMessages();
    }

    public void getPMessages(){
        PMessageNetwork.getInstance().getPMessage(currentAccount.accessToken,
                null,null,new Callback<List<PMessage>>() {
                    @Override
                    public void success(List<PMessage> pMessages, Response response) {
                        loadingDialog.dismiss();
                        srlSwipe.setRefreshing(false);
                        adapter.addAll(pMessages);
                        adapter.notifyDataSetChanged();
                        if (pMessages.size()==0){
                            ToastUtil.show(NotificationActivity.this,"客官，没有消息提醒哦！");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        loadingDialog.dismiss();
                        srlSwipe.setRefreshing(false);
                        ErrorUtils.show(NotificationActivity.this,error);
                    }
                });
    }

    @ItemClick(R.id.lv_notification_activity)
    public void itemClick(PMessage pMessage){
        Intent toPartyActivity = new Intent(this,PartyActivity_.class);
        toPartyActivity.putExtra("partyID",pMessage.partyID);
        startActivity(toPartyActivity);
    }
}
