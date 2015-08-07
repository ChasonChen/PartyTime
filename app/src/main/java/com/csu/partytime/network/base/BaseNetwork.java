package com.csu.partytime.network.base;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by ${Chen_Xingchao} on 2015/4/13.
 */
public class BaseNetwork {

    public static class BaseErrorHandler implements ErrorHandler {
        @Override public Throwable handleError(RetrofitError cause) {
            if (cause.isNetworkError()){

            } else if (cause.getCause() == null){
                //is http error
            } else {
                //other error

            }
            return cause;
        }
    }



}
