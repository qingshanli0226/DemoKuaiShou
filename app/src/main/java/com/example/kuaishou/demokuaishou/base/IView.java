package com.example.kuaishou.demokuaishou.base;

import com.example.kuaishou.demokuaishou.common.ErrorBean;

public interface IView {
    void showError(ErrorBean errorBean);//网络请求失败调用的接口
    void showLoading();//网络请求，在页面显示加载的圆圈的方法
    void hideLoading();//网络数据获得后关闭加载圆圈
}
