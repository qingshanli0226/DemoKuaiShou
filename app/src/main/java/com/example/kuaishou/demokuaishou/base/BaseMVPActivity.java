package com.example.kuaishou.demokuaishou.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

//定义Acitivity的MVP基类
public abstract class BaseMVPActivity<T extends IPresenter, V extends IView> extends BaseActivity {

    protected T httpPresenter;

    @Override
    protected void create() {
        initPresenter();
        httpPresenter.attachView((V)this);
        initData();

    }

    protected abstract void initPresenter();
    protected abstract void initData();

    protected void destroy() {
        httpPresenter.detachView();
    }
}
