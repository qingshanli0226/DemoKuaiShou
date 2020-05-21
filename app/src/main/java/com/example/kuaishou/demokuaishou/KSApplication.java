package com.example.kuaishou.demokuaishou;

import android.app.Application;

public class KSApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KSUserManager.getInstance().init(this);
    }
}
