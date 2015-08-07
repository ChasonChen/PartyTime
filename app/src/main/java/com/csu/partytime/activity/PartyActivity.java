package com.csu.partytime.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.poisearch.PoiSearch;
import com.csu.partytime.Constant;
import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.adapter.MemberListAdapter;
import com.csu.partytime.event.RefreshEvent;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.Party;
import com.csu.partytime.network.AccountNetwork;
import com.csu.partytime.network.PartyNetwork;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.ErrorUtils;
import com.csu.partytime.util.TimeUtils;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.LoadingDialog;
import com.csu.partytime.view.ShareDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.gson.Gson;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_party)
public class PartyActivity extends BaseActivity {
    public final static String PARTY_ACTIVITY_EXTRA = "party_intent_extra";

    @ViewById(R.id.tv_party_activity_creator)
    TextView tvCreator;
    @ViewById(R.id.tv_party_activity_start_time)
    TextView tvStartTime;
    @ViewById(R.id.tv_party_activity_gathering_place)
    TextView tvGatheringPlace;
    @ViewById(R.id.tv_party_activity_member_count)
    TextView tvMemberCount;
    @ViewById(R.id.bt_party_activity_invite_member)
    Button inviteMember;
    @ViewById(R.id.hlv_party_activity_account_list)
    HListView hlvMembers;
    @ViewById(R.id.bt_party_activity_add_activity)
    Button addActivity;
    @ViewById(R.id.bt_party_activity_end_vote)
    Button endVote;
    @ViewById(R.id.pc_party_activity_votes)
    PieChart pcVotes;
    @ViewById(R.id.bt_party_activity_share)
    Button shareParty;

    String partyID;
    Party party;

    PoiSearch poiSearch;
    AMapLocation currentPos;
    PoiSearch.Query query;
    LoadingDialog loadingDialog;
    ShareDialog shareDialog;

    MemberListAdapter adapter;
    List<Account> inviteMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        partyID = getIntent().getStringExtra(CreatePartyActivity.EXTRA_PARTY_ID);
        currentPos = PartyApplication.myLocation;
        query = new PoiSearch.Query(Constant.PARTY_ACTIVITY_QUERY_KEY_WORD, "", currentPos.getCity());
        poiSearch = new PoiSearch(this, query);
        loadingDialog = new LoadingDialog(this);
        shareDialog = new ShareDialog(this);
        inviteMembers = new ArrayList<>();
    }

    @AfterViews
    public void initViews() {
        shareDialog.setTitle("分享聚会");
        adapter = new MemberListAdapter(this,inviteMembers);
        hlvMembers.setAdapter(adapter);
        hlvMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toAccountActivity = new Intent(PartyActivity.this,AccountActivity_.class);
                toAccountActivity.putExtra("account", Parcels.wrap(inviteMembers.get(i)));
                startActivity(toAccountActivity);
            }
        });
        getParty(partyID);
    }

    public void getParty(final String partyID) {
        loadingDialog.show();
        PartyNetwork.getInstance().getPartyByID(partyID, new Callback<Party>() {
            @Override
            public void success(Party o, Response response) {
                party = o;
                shareDialog.setParty(o);
                ActionbarUtil.initBackwardTitleActionBar(PartyActivity.this, party.title);
                initPartyAccount(party);
                if (party.activities.size() > 0) {
                    initPieChart();
                }
                loadingDialog.dismiss();
                initInviteMembers(o);
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.show(PartyActivity.this, error.toString());
                loadingDialog.dismiss();
            }
        });
    }

    public void initInviteMembers(Party p){
        if (p.invitedMembers.size()>0){
            AccountNetwork.getInstance().getAccountsByAccessTokens(
                    new Gson().toJson(p.invitedMembers),
                    new Callback<List<Account>>() {
                        @Override
                        public void success(List<Account> accounts, Response response) {
                            adapter.addAll(accounts);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            ErrorUtils.show(PartyActivity.this,error);
                        }
                    }
            );
        }
    }

    public void initPieChart() {
        pcVotes.setVisibility(View.VISIBLE);
        pcVotes.setUsePercentValues(true);
        pcVotes.setDescription("");
        pcVotes.setDrawHoleEnabled(true);
        pcVotes.setHoleColorTransparent(true);
        pcVotes.setTransparentCircleColor(Color.WHITE);
        pcVotes.setHoleRadius(48f);
        pcVotes.setTransparentCircleRadius(61f);
        pcVotes.setDrawCenterText(true);
        pcVotes.setRotationAngle(0);
        pcVotes.setRotationEnabled(true);
        pcVotes.setCenterText("活动投票");
        pcVotes.setCenterTextSize(18f);
        pcVotes.setCenterTextColor(getResources().getColor(R.color.app_color));
        setData(party.activities.size(), party.invitedMembers.size());
    }

    public void setData(int count, float range) {
        float mult = range;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float) (party.activities.get(i).votes * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(party.activities.get(i).activityTitle);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "活动图例");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(getResources().getColor(R.color.app_color));
        pcVotes.setData(data);

        // undo all highlights
        pcVotes.highlightValues(null);
        pcVotes.invalidate();
    }

    public void initPartyAccount(Party p) {
        tvStartTime.setText(TimeUtils.getFormatTime(p.startTime));
        if (null == p.gatheringPlace || "".equals(p.gatheringPlace)) {
            tvGatheringPlace.setText("聚会地点待定");
        } else {
            tvGatheringPlace.setText(p.gatheringPlace);
        }
        tvMemberCount.setText("" + p.invitedMembers.size());
        AccountNetwork.getInstance().getAccountByAccessToken(p.accessToken,
                new Callback<Account>() {
                    @Override
                    public void success(Account account, Response response) {
                        tvCreator.setText(account.name);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ToastUtil.show(PartyActivity.this, error.toString());
                    }
                });
    }


    @Click({R.id.bt_party_activity_add_activity,
            R.id.bt_party_activity_share,
            R.id.bt_party_activity_invite_member})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.bt_party_activity_add_activity:
                Intent toAddActActivity = new Intent(this, AddActActivity_.class);
                toAddActActivity.putExtra(PARTY_ACTIVITY_EXTRA, "fromPartyActivity");
                startActivity(toAddActActivity);
                break;

            case R.id.bt_party_activity_share:
                shareDialog.show();
                break;

            case R.id.bt_party_activity_invite_member:
                Intent toInviteActivity = new Intent(this,InviteActivity_.class);
                toInviteActivity.putExtra("party", Parcels.wrap(party));
                startActivity(toInviteActivity);
                break;
        }
    }

    public void onEvent(RefreshEvent e){
        if (null==e.activity) {
            getParty(partyID);
        }else {
            addActivity(e.activity);
        }
    }

    public void addActivity(com.csu.partytime.model.PartyActivity activity){
        PartyNetwork.getInstance().addActivity(
                partyID,
                activity._id,
                activity.title,
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        if (s.equals("isExist")){
                            ToastUtil.show(PartyActivity.this,"这个活动已经添加过了哦！");
                        }else if (s.equals("isAdded")){
                            getParty(partyID);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ErrorUtils.show(PartyActivity.this,error);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
