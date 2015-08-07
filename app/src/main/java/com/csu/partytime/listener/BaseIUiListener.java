package com.csu.partytime.listener;

import com.csu.partytime.event.BaseIUiEvent;
import com.csu.partytime.util.DebugLog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import de.greenrobot.event.EventBus;

/**
 * Created by ${Chen_Xingchao} on 2015/4/16.
 */
public class BaseIUiListener implements IUiListener {
    public final static String IUI_LOGIN = "iui_login";
    public final static String IUI_GET_USER_INFO = "iui_get_user_info";
    public final static String IUI_SHARE = "iui_share";

    public String iuiType;

    public BaseIUiListener(){};

    public BaseIUiListener(String iuiType){
        this.iuiType = iuiType;
    }

    @Override
    public void onComplete(Object o) {
        EventBus.getDefault().post(new BaseIUiEvent(o,this.iuiType, BaseIUiEvent.BASE_IUI_COMPLETE));
    }

    @Override
    public void onError(UiError uiError) {
        DebugLog.d("===login:error"+uiError.errorDetail);
        EventBus.getDefault().post(new BaseIUiEvent(uiError, BaseIUiEvent.BASE_IUI_ERROR));
    }

    @Override
    public void onCancel() {
        DebugLog.d("===login:cancel");
        EventBus.getDefault().post(new BaseIUiEvent("cancel", BaseIUiEvent.BASE_IUI_CANCEL));
    }
}
