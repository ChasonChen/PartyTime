package com.csu.partytime.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csu.partytime.PartyApplication;
import com.csu.partytime.R;
import com.csu.partytime.listener.BaseIUiListener;
import com.csu.partytime.model.Party;
import com.csu.partytime.util.DensityUtil;
import com.csu.partytime.util.TimeUtils;
import com.csu.partytime.util.ToastUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

/**
 * Created by Chason on 2015/5/25.
 */
public class ShareDialog{
    private final String SHARE_URL ="http://192.168.23.1:8080/PartyTimeWeb/GetSharedPartyServlet?partyID=" ;
    private final String IMAGE_URL = "http://192.168.23.1:8080/PartyTimeWeb/images/ic_logo_144.png";
    Context context;
    LayoutInflater mInflater;
    Dialog dialog;

    TextView tvTitle;
    ImageView ivQQ;
    ImageView ivWeiXin;
    ImageView ivPengYouquan;

    Party party;

    public ShareDialog(Context context){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        initView();
    }

    private void initView(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = mInflater.inflate(R.layout.dialog_share,null);
        tvTitle = (TextView) view.findViewById(R.id.tv_dialog_share_title);
        ivQQ = (ImageView) view.findViewById(R.id.iv_share_dialog_qq);
        ivWeiXin = (ImageView) view.findViewById(R.id.iv_share_dialog_weixin);
        ivPengYouquan = (ImageView) view.findViewById(R.id.iv_share_dialog_pengyouquan);
        ivQQ.setOnClickListener(new ShareDialogClickListener());
        ivWeiXin.setOnClickListener(new ShareDialogClickListener());
        ivPengYouquan.setOnClickListener(new ShareDialogClickListener());
        builder.setView(view);
        dialog = builder.create();
    }

    public void setParty(Party party){
        this.party = party;
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    private void resize(){
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DensityUtil.pix2dp(300);
        window.setAttributes(params);
    }

    public void show(){
        resize();
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    private class ShareDialogClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_share_dialog_qq:
                    share2QQ();
                    break;

                case R.id.iv_share_dialog_pengyouquan:
                case R.id.iv_share_dialog_weixin:
                    share2Weixin();
                    break;
            }
        }

        public void share2QQ(){
            Tencent tencent=PartyApplication.tencent;
            Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, party.title);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "《"+party.title+"》邀您参加！聚会时间："
                    + TimeUtils.getFormatTime(party.startTime));
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  SHARE_URL+party.id);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,IMAGE_URL);
            tencent.shareToQQ((Activity)context,params,new BaseIUiListener(BaseIUiListener.IUI_SHARE));
        }

        public void share2Weixin(){
            IWXAPI api = PartyApplication.api;
            ToastUtil.show(context,"Share to Weixin!");
        }
    }
}
