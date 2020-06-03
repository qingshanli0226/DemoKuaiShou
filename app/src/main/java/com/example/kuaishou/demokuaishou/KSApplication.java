package com.example.kuaishou.demokuaishou;

import android.app.Application;

import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.user.KSUserManager;
import com.tencent.rtmp.TXLiveBase;

public class KSApplication extends Application {
    public static KSApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KSUserManager.getInstance().init(this);
        CacheManager.getInstance().init(this);

        //配置腾讯直播
        String licenceKey = "84df209a712e18e8c9f3d5fd2a4dac3c"; // 获取到的 licence url
        String licenceURL = "http://license.vod2.myqcloud.com/license/v1/67cb9eca09b2c99776f8e5a2fe89e649/TXLiveSDK.licence"; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
    }
}
