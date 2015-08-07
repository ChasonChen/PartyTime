package com.csu.partytime.event;

import com.csu.partytime.model.PartyActivity;

/**
 * Created by Chason on 2015/5/28.
 */
public class RefreshEvent {
    public PartyActivity activity;

    public RefreshEvent(){}

    public RefreshEvent(PartyActivity activity){
        this.activity = activity;
    }

}
