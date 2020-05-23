package com.example.kuaishou.demokuaishou;

import android.app.Application;

import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.user.KSUserManager;

public class KSApplication extends Application {
    public static KSApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KSUserManager.getInstance().init(this);
        CacheManager.getInstance().init(this);
    }
}
