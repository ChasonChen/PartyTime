package com.csu.partytime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.csu.partytime.Constant;
import com.csu.partytime.R;
import com.csu.partytime.activity.MainActivity_;
import com.csu.partytime.event.SearchEvent;
import com.csu.partytime.fragment.base.BaseFragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_general_search)
public class GeneralSearchFragment extends BaseFragment {

    @ViewById(R.id.et_general_search_keyword)
    EditText etKeyWord;
    @ViewById(R.id.et_general_search_radius)
    EditText etRadius;
    @ViewById(R.id.iv_general_search_clear_keyword)
    ImageView ivClearKeyword;
    @ViewById(R.id.bt_general_search_food)
    Button btFood;
    @ViewById(R.id.bt_general_search_movie)
    Button btMovie;
    @ViewById(R.id.bt_general_search_ktv)
    Button btKTV;
    @ViewById(R.id.bt_general_search_reset)
    Button btReset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click({R.id.bt_general_search_food,
            R.id.bt_general_search_movie,
            R.id.bt_general_search_ktv,
            R.id.bt_general_search_reset,
            R.id.iv_general_search_clear_keyword,
            R.id.bt_general_search_search})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.bt_general_search_food:
            case R.id.bt_general_search_movie:
            case R.id.bt_general_search_ktv:
                setKeyWord((Button)view);
                break;

            case R.id.bt_general_search_reset:
                Constant.reSetSearchInfo();
                etKeyWord.setText(Constant.PARTY_ACTIVITY_QUERY_KEY_WORD);
                etKeyWord.setSelection(etKeyWord.getText().length());
                break;

            case R.id.iv_general_search_clear_keyword:
                etKeyWord.setText("");
                break;

            case R.id.bt_general_search_search:
                search();
                break;
        }
    }

    public void search(){
        String keyword = null;
        Integer radius = null;
        if (!TextUtils.isEmpty(etKeyWord.getText())){
            keyword = etKeyWord.getText().toString();
        }

        if (!TextUtils.isEmpty(etRadius.getText())){
            radius = Integer.valueOf(etRadius.getText().toString());
        }
        EventBus.getDefault().post(new SearchEvent(keyword,radius));
//        startActivity(new Intent(getActivity(), MainActivity_.class));
        getActivity().onBackPressed();
        getActivity().finish();
    }

    public void setKeyWord(Button btSuggestion){
        if (TextUtils.isEmpty(etKeyWord.getText())){
            etKeyWord.setText(btSuggestion.getText());
        }else {
            etKeyWord.setText(etKeyWord.getText().append("|").append(btSuggestion.getText()));
        }
        etKeyWord.setSelection(etKeyWord.getText().length());
    }
}
