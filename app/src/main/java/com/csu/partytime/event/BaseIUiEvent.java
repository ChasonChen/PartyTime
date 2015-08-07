package com.csu.partytime.event;

import org.json.JSONObject;

/**
 * Created by ${Chen_Xingchao} on 2015/4/16.
 */
public class BaseIUiEvent {
    public final static String BASE_IUI_COMPLETE = "base_iui_complete";
    public final static String BASE_IUI_ERROR = "base_iui_error";
    public final static String BASE_IUI_CANCEL = "base_iui_cancel";

    public String eventType;
    public String iuiType;
    public Object uiData;

    public BaseIUiEvent(Object uiData, String eventType) {
        this.eventType = eventType;
        this.uiData = uiData;
    }

    public BaseIUiEvent(Object uiData, String iuiType, String eventType) {
        this.eventType = eventType;
        this.iuiType = iuiType;
        this.uiData = uiData;
    }
}
