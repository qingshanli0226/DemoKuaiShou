package com.example.kuaishou.demokuaishou;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.home.view.MainActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        setContentView(textView);
        textView.setText("测试室白屏");
        textView.setTextSize(100);
        CacheManager.getInstance().init(this);
        //先初始化广告时间，防止应用程序冷启动时，弹出中间广告
        CacheManager.getInstance().saveAdrTime(System.currentTimeMillis());


        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MainActivity.launch(SplashActivity.this, 1);
                finish();
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }
}
