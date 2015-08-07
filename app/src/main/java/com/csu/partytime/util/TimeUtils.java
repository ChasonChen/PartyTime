package com.csu.partytime.util;

/**
 * Created by Chason on 2015/5/25.
 */
public class TimeUtils {
    public static String getFormatTime(String timeStr){
        return timeStr.split("-")[1]+"月"
                +timeStr.split("-")[2]+"日 "
                +getStringTime(timeStr.split("-")[3])+":"
                +getStringTime(timeStr.split("-")[4]);
    }

    private static String getStringTime(String str){
        int time=Integer.valueOf(str);
        if (time<10){
            return "0"+time;
        }else {
            return str;
        }
    }
}
