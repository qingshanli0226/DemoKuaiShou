package com.example.kuaishou.demokuaishou;

import android.app.Application;
import android.util.Log;

import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.cache.MessageManger;
import com.example.kuaishou.demokuaishou.user.KSUserManager;
import com.tencent.rtmp.TXLiveBase;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;

public class KSApplication extends Application {
    public static KSApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KSUserManager.getInstance().init(this);

        //配置腾讯直播
        String licenceKey = "84df209a712e18e8c9f3d5fd2a4dac3c"; // 获取到的 licence url
        String licenceURL = "http://license.vod2.myqcloud.com/license/v1/67cb9eca09b2c99776f8e5a2fe89e649/TXLiveSDK.licence"; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);


        //友盟配置start

        UMConfigure.init(this, "5ed6eda5895cca71ee00007c", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "f69626329c33fae4b9a8a3d242dd986c");
       //获取消息推送代理示例
        PushAgent pushAgent = PushAgent.getInstance(this);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {//友盟准备好之后，返回一个设备的token，通过token，可以给该设备发送消息
                Log.d("LQS deviceToken = ", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        MessageManger.getInstance().init(this);
        initSharePlatform();

        //可以统计应用程序使用时长
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        //统计错误
        MobclickAgent.setCatchUncaughtExceptions(true);
        //友盟配置end

    }

    private void initSharePlatform() {
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQFileProvider("com.example.kuaishou.demokuaishou.fileprovider");
    }
}
