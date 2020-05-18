package com.example.kuaishou.demokuaishou.base;

//定义通用的业务接口，主要是attachView detachView
public interface IPresenter<T extends IView> {
    void attachView(T t);
    void detachView();
}
