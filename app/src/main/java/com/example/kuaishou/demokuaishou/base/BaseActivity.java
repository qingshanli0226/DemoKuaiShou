package com.example.kuaishou.demokuaishou.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.kuaishou.demokuaishou.AdrActivity;
import com.example.kuaishou.demokuaishou.cache.CacheManager;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Log.d("LQS", "onCreate....");
        initView();
        create();
    }

    protected abstract void create();
    protected abstract void initView();
    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        Log.d("LQS", "onResume....");
        super.onResume();
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    protected void pause(){
        CacheManager.getInstance().saveAdrTime(System.currentTimeMillis());//开始存广告时间
    }

    protected void resume() {
        long adrTime = CacheManager.getInstance().getAdrTime();
        if (System.currentTimeMillis() - adrTime > 5 * 1000) {
            //启动广告页
            AdrActivity.launch(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroy();
    }

    protected void destroy() {

    }
}
