package com.pait.dispatch_app.interfaces;

//Created by SNEHA on 10/26/2017.

public interface ServerCallbackList<T> {
    public void onSuccess(T result);
    public void onFailure(T result);
}
