package com.csu.partytime.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.AccountActivity_;
import com.csu.partytime.adapter.PartnersListAdapter;
import com.csu.partytime.model.Account;
import com.csu.partytime.network.AccountNetwork;
import com.csu.partytime.util.ErrorUtils;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.LoadingDialog;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_partners)
public class PartnersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewById(R.id.srl_partners)
    SwipeRefreshLayout srlPartners;
    @ViewById(R.id.lv_partners_list)
    ListView lvPartners;

    List<Account> accounts;
    PartnersListAdapter adapter;
    Account currentAccount;

    LoadingDialog loadingDialog;
    PartnersOnMapFragment_ partnersOnMapFragment_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accounts = new ArrayList<>();
        currentAccount = PartyApplication.currentAccount;
    }

    @AfterViews
    public void initViews(){
        partnersOnMapFragment_ = new PartnersOnMapFragment_();
        loadingDialog = new LoadingDialog(getActivity());
        adapter = new PartnersListAdapter(getActivity(),accounts);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(lvPartners);
        lvPartners.setAdapter(animationAdapter);
        lvPartners.setDividerHeight(0);
        srlPartners.setColorSchemeResources(R.color.app_color);
        srlPartners.setOnRefreshListener(this);
        loadingDialog.show();
        getAccounts();
        setHasOptionsMenu(true);
    }

    @Override
    public void onRefresh() {
        getAccounts();
    }

    public void getAccounts(){
        AccountNetwork.getInstance().getAllAccounts(currentAccount.accessToken,
                null,null,new Callback<List<Account>>() {
                    @Override
                    public void success(List<Account> accounts, Response response) {
                        PartnersFragment.this.accounts.clear();
                        PartnersFragment.this.accounts.addAll(accounts);
                        adapter.addAll(accounts);
                        adapter.notifyDataSetChanged();
                        srlPartners.setRefreshing(false);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ErrorUtils.show(getActivity(),error);
                        srlPartners.setRefreshing(false);
                    }
                });
    }

    @ItemClick(R.id.lv_partners_list)
    public void onPartnerClick(Account account){
        Intent toAccountActivity = new Intent(getActivity(), AccountActivity_.class);
        toAccountActivity.putExtra("account", Parcels.wrap(account));
        getActivity().startActivity(toAccountActivity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("account",Parcels.wrap(accounts));
        partnersOnMapFragment_.setArguments(arguments);
        inflater.inflate(R.menu.menu_partners, menu);
        View view = menu.getItem(0).getActionView();
        TextView tvToMap= (TextView) view.findViewById(R.id.tv_menu_invited);
        tvToMap.setText("好友位置");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.rl_partners_activity, partnersOnMapFragment_)
                        .addToBackStack("partnersFragment")
                        .commit();
            }
        });
    }
}
