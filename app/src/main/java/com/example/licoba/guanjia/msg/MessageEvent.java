package com.example.licoba.guanjia.msg;

import android.content.Context;

public class MessageEvent {
    private String message;
    private Context context;
    public  MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageEvent(String message, Context context) {
        this.message = message;
        this.context = context;
    }
}
