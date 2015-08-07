package com.csu.partytime.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.activity.base.BaseActivity;
import com.csu.partytime.fragment.RecommendationFragment;
import com.csu.partytime.model.Account;
import com.csu.partytime.model.Party;
import com.csu.partytime.model.PartyActivity;
import com.csu.partytime.network.PartyNetwork;
import com.csu.partytime.util.ActionbarUtil;
import com.csu.partytime.util.DebugLog;
import com.csu.partytime.util.ToastUtil;
import com.csu.partytime.view.LoadingDialog;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_create_party)
public class CreatePartyActivity extends BaseActivity {
    public final static String EXTRA_PARTY_ID = "partyID";

    @ViewById(R.id.sv_create_activity)
    ScrollView scrollView;
    @ViewById(R.id.et_create_party_title)
    EditText etTitle;
    @ViewById(R.id.et_create_party_creator)
    EditText etCreator;
    @ViewById(R.id.et_create_party_tel)
    EditText etTel;
    @ViewById(R.id.et_create_party_gathering_place)
    EditText etGatheringPlace;
    @ViewById(R.id.tv_create_party_selected_time)
    TextView tvTime;
    @ViewById(R.id.et_create_party_notice)
    EditText etNotice;
    @ViewById(R.id.tv_create_party_added_activity)
    TextView tvAddedActivity;
    @ViewById(R.id.ll_create_party_added_activity)
    LinearLayout llAddedActivity;

    LoadingDialog loadingDialog;

    Gson gson;
    Party party;
    PartyActivity activity;
    Account creator;

    Calendar cal;
    boolean isDateSet;
    boolean isTimeSet;
    String date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        party = new Party();
        creator = PartyApplication.currentAccount;
        activity = Parcels.unwrap(getIntent().getParcelableExtra(RecommendationFragment.EXTRA_INFO_ACTIVITY));
        cal = Calendar.getInstance();
    }

    @AfterViews
    public void initViews() {
        ActionbarUtil.initBackwardTitleActionBar(this, "创建聚会");

        if (null != creator.name) {
            etCreator.setText(creator.name);
        }

        if (null != creator.tel) {
            etTel.setText(creator.tel);
        }

        if (null == activity) {
            llAddedActivity.setVisibility(View.GONE);
        } else {
            llAddedActivity.setVisibility(View.VISIBLE);
            tvAddedActivity.setText(activity.title);
        }
        loadingDialog = new LoadingDialog(this);
    }

    @Click({R.id.bt_create_party_create_party,
            R.id.bt_create_party_select_time})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.bt_create_party_create_party:
                createParty();
                break;

            case R.id.bt_create_party_select_time:
                setTime();
                break;
        }
    }

    public void createParty() {
        if (TextUtils.isEmpty(etCreator.getText())
                || TextUtils.isEmpty(etTel.getText())
                || TextUtils.isEmpty(tvTime.getText())
                || TextUtils.isEmpty(etTitle.getText())
                || !isDateSet || !isTimeSet) {
            ToastUtil.show(this, "聚会信息不完整。");
            return;
        } else {
            String name = etCreator.getText().toString();
            String tel = etTel.getText().toString();
            String title = etTitle.getText().toString();
            String activityId = activity == null ? null : activity._id;
            String activityTitle = activity == null ? null : activity.title;
            String startTime = date + "" + time;
            String gatheringPlace = etGatheringPlace.getText().toString();
            String notice = etNotice.getText().toString();
            loadingDialog.show();
            PartyNetwork.getInstance().addParty(creator.accessToken,
                    name, tel, activityId, activityTitle, title,
                    startTime, gatheringPlace,
                    notice,
                    new Callback<Object>() {
                        @Override
                        public void success(Object object, Response response) {
                            loadingDialog.dismiss();
                            Intent toPartyActivity = new Intent(CreatePartyActivity.this, PartyActivity_.class);
                            toPartyActivity.putExtra(EXTRA_PARTY_ID, object.toString());
                            CreatePartyActivity.this.startActivity(toPartyActivity);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            loadingDialog.dismiss();
                            ToastUtil.show(CreatePartyActivity.this, "创建聚会失败");
                        }
                    });
        }
    }

    private void setTime() {
        if (!isDateSet) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    date = i + "-" + (i2 + 1) + "-" + i3;
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(date);
                    isDateSet = true;
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        } else if (isDateSet && !isTimeSet) {
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    time = "-" + i + "-" + i2;
                    tvTime.setText(date + "\n" + i + ":" + i2);
                    isTimeSet = true;
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        } else if (isDateSet && isTimeSet) {
            isDateSet = false;
            isTimeSet = false;
            tvTime.setVisibility(View.GONE);
        }
    }

}
