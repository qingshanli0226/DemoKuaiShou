package com.example.kuaishou.demokuaishou.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//T代表是Presenter类，Fragment，需要从服务端获取数据，那么就需要一个Presenter类，并且presenter类要实现IPresenter的接口
//V代表是该Fragment要通过该接口回调获取Presenter类从服务端获取的数据.并且V是实现了IView的接口
public abstract class BaseFragment<T extends IPresenter, V extends IView> extends Fragment {
    protected T httpPresenter;//定义presenter的变量，该变量需要子类初始化

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        httpPresenter.attachView((V)this);//让Fragment和presenter建立关联关系,并且强制子类必须实现V接口
    }

    protected abstract void initPresenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(getLayoutId(), container, false);
        initView(rootView);
        return rootView;
    }

    protected abstract void initView(View rootView);//让子类初始化控件的

    protected abstract int getLayoutId();//需要子类提供布局的id

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    protected abstract void initData();//让子类，通过该方法从服务端获取数据


    @Override
    public void onDestroy() {
        super.onDestroy();
        httpPresenter.detachView();
        destroy();
    }

    //子类可以当页面销毁时，子类如果需要做一些处理的话，可以重写destroy方法。
    public void destroy() {
    }
}
