package com.csu.partytime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.adapter.PartyListAdapter;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.Party;
import com.csu.partytime.network.PartyNetwork;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.DebugLog;
import com.csu.partytime.util.ErrorUtils;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
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

@EActivity(R.layout.activity_party_list)
public class PartyListActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    @ViewById(R.id.srl_party_activity_swipe)
    SwipeRefreshLayout srlListSwipe;
    @ViewById(R.id.lv_party_activity_list)
    ListView lvPartyList;

    String listType;
    Account currentAccount;
    PartyListAdapter adapter;
    List<Party> parties;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listType = getIntent().getStringExtra(MainActivity_.LIST_TYPE);
        currentAccount = PartyApplication.currentAccount;
        parties = new ArrayList<Party>();
        loadingDialog = new LoadingDialog(this);
    }

    @AfterViews
    public void initViews() {
        adapter = new PartyListAdapter(this, parties);
        AnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(lvPartyList);
        lvPartyList.setDividerHeight(0);
        lvPartyList.setAdapter(animationAdapter);
        srlListSwipe.setColorSchemeResources(R.color.app_color);
        srlListSwipe.setOnRefreshListener(this);

        loadingDialog.show();
        switch (listType) {
            case MainActivity_.PARTIES_I_CREATE:
                ActionbarUtil.initBackwardTitleActionBar(this, "我创建的聚会");
                getPartiesICreated();
                break;

            case MainActivity_.PARTIES_RELATIVE_TO_ME:
                ActionbarUtil.initBackwardTitleActionBar(this, "我参与的聚会");
                getPartiesIJoined();
                break;
        }
    }

    public void getPartiesICreated() {
        PartyNetwork.getInstance().getPartiesICreated(currentAccount.accessToken,
                null, null, new Callback<List<Party>>() {
                    @Override
                    public void success(List<Party> partyRes, Response response) {
                        srlListSwipe.setRefreshing(false);
                        PartyListActivity.this.parties.addAll(partyRes);
                        adapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                        if (partyRes.size()==0){
                            ToastUtil.show(PartyListActivity.this,"官人，您还没创建过聚会！");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ErrorUtils.show(PartyListActivity.this, error);
                        loadingDialog.dismiss();
                    }
                });
    }

    public void getPartiesIJoined(){
        PartyNetwork.getInstance().getPartiesIJoined(
                currentAccount.accessToken,
                null,null,
                new Callback<List<Party>>() {
                    @Override
                    public void success(List<Party> parties, Response response) {
                        srlListSwipe.setRefreshing(false);
                        PartyListActivity.this.parties.addAll(parties);
                        adapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                        if (parties.size()==0){
                            ToastUtil.show(PartyListActivity.this,"官人，您还没参加过聚会！");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ErrorUtils.show(PartyListActivity.this, error);
                        loadingDialog.dismiss();
                    }
                }
        );
    }

    @Override
    public void onRefresh() {
        parties.clear();
        switch (listType) {
            case MainActivity_.PARTIES_I_CREATE:
                getPartiesICreated();
                break;

            case MainActivity_.PARTIES_RELATIVE_TO_ME:
                getPartiesIJoined();
                break;
        }
    }

    @ItemClick(R.id.lv_party_activity_list)
    public void partyItemClick(Party party) {
        Intent toPartyActivity = new Intent(this, PartyActivity_.class);
        toPartyActivity.putExtra("partyID", party.id);
        startActivity(toPartyActivity);
    }
}
