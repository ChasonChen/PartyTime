package com.csu.partytime.model.base;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.parceler.Parcel;

import java.lang.reflect.Type;

/**
 * Created by ${Chen_Xingchao} on 2015/4/7.
 */
@Parcel
public class BaseModel implements Type{
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }
}
