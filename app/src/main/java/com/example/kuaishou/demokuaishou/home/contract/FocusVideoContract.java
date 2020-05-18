package com.example.kuaishou.demokuaishou.home.contract;

import com.example.kuaishou.demokuaishou.home.mode.FocusVideoBean;

public class FocusVideoContract {

    public interface IFocusView{
        //返回数据
        void onFocusVideo(FocusVideoBean focusVideoBean);
    }

    public interface IFocusVideoPresenter {
        void focusVideo();
    }
}
