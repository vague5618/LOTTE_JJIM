package com.example.n.lotte_jjim.Activity.Application;

import android.app.Application;


public class MyApplication extends Application{

    static private String mUserID;

    @Override
    public void onCreate() {

        mUserID = "아이디 미입력";
        super.onCreate();
    }

    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    public void SetUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String GetUserID() {
        return mUserID;
    }

}
