package com.example.kuaishou.demokuaishou.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

//定义Acitivity的MVP基类
public abstract class BaseActivity<T extends IPresenter, V extends IView> extends AppCompatActivity {

    protected T httpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initPresenter();
        httpPresenter.attachView((V)this);
        initData();

    }

    protected abstract void initPresenter();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    protected void pause(){

    }

    protected void resume() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        httpPresenter.detachView();
        destroy();
    }

    protected void destroy() {

    }
}
