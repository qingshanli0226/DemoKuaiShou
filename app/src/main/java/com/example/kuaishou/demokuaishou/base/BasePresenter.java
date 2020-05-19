package com.example.kuaishou.demokuaishou.base;

//实现抽象类，维护页面的回调接口.在抽象类里，可以建立Presenter和页面的关联，也可以解除关联.
public class BasePresenter<T extends IView> implements IPresenter<T> {
    protected T iHttpView;

    @Override
    public void attachView(T t) {
        iHttpView = t;
    }

    @Override
    public void detachView() {
        iHttpView = null;
    }
}
