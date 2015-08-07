package com.csu.partytime.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;
import android.widget.TextView;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.adapter.InviteAccountListAdapter;
import com.csu.partytime.event.RefreshEvent;
import com.csu.partytime.fragment.base.BaseFragment;
import com.csu.partytime.listener.TextViewTouchListener;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.Party;
import com.csu.partytime.network.AccountNetwork;
import com.csu.partytime.network.PartyNetwork;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.ErrorUtils;
import com.csu.partytime.util.ToastUtil;
import com.google.gson.Gson;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_all_accounts)
public class AllAccountsFragment
        extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    @ViewById(R.id.srl_invite_fragment)
    SwipeRefreshLayout srlListSwipe;
    @ViewById(R.id.lv_invite_fragment)
    ListView lvAccountList;

    Account currentAccount;
    List<Account> accounts;
    InviteAccountListAdapter inviteAccountListAdapter;

    Party party;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accounts = new ArrayList<>();
        currentAccount = PartyApplication.currentAccount;
        party= Parcels.unwrap(getArguments().getParcelable("party"));
    }

    @AfterViews
    public void initViews() {
        srlListSwipe.setOnRefreshListener(this);
        inviteAccountListAdapter = new InviteAccountListAdapter(this.getActivity(),accounts);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(inviteAccountListAdapter);
        alphaInAnimationAdapter.setAbsListView(lvAccountList);
        lvAccountList.setAdapter(alphaInAnimationAdapter);
        lvAccountList.setDividerHeight(0);
        srlListSwipe.setColorSchemeResources(R.color.app_color);
        setHasOptionsMenu(true);
        getAccounts();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_invite, menu);
        View view =  menu.getItem(0).getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite();
            }
        });
    }

    private void invite() {
        List<Account> invitedAccounts = inviteAccountListAdapter.getInvitedAccounts();
        List<String> accessTokens = new ArrayList<>();
        for (Account t:invitedAccounts){
            accessTokens.add(t.accessToken);
        }
        PartyNetwork.getInstance().inviteMembers(party.id,
                new Gson().toJson(accessTokens),
                new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        ToastUtil.show(getActivity(),"请柬已发出！");
                        EventBus.getDefault().post(new RefreshEvent());
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ErrorUtils.show(getActivity(),error);
                    }
                });
    }

    public void getAccounts() {
        AccountNetwork.getInstance().getAllAccounts(
                currentAccount.accessToken,
                null,
                null,
                new Callback<List<Account>>() {
                    @Override
                    public void success(List<Account> accounts, Response response) {
                        inviteAccountListAdapter.addAll(accounts);
                        srlListSwipe.setRefreshing(false);
                        inviteAccountListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ErrorUtils.show(getActivity(), error);
                        srlListSwipe.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        getAccounts();
    }
}
