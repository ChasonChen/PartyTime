package com.csu.partytime;

/**
 * Created by ${Chen_Xingchao} on 2015/4/13.
 */
public class Constant {
    public final static String VERSION_CODE ="version_code";
    public final static String VERSION_DESC = "version_desc";
    public final static String VERSION_UPDATE_TIME = "version_update_time";
    public final static String VERSION_NEW_URL = "version_new_url";
    public final static String APP_ID ="1104475777";
    public final static String SP_PARTYTIME = "sp_party_time";
    public final static String SP_ACCOUNT_INFO = "account_info";
    public final static Integer PARTY_ACTIVITY_PAGE_SIZE = 20;

    //search info
    public static Integer PARTY_ACTIVITY_SEARCH_RADIUS = 10000;
    public static String PARTY_ACTIVITY_QUERY_KEY_WORD="KTV|餐厅|游戏";

    public static void reSetSearchInfo(){
        PARTY_ACTIVITY_SEARCH_RADIUS = 10000;
        PARTY_ACTIVITY_QUERY_KEY_WORD = "KTV|餐厅|游戏";
    }

    public static void reSetKeyWord(){
        PARTY_ACTIVITY_QUERY_KEY_WORD="KTV|餐厅|游戏";
    }
}
