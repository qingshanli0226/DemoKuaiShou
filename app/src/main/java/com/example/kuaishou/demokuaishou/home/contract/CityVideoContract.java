package com.example.kuaishou.demokuaishou.home.contract;

import com.example.kuaishou.demokuaishou.base.BasePresenter;
import com.example.kuaishou.demokuaishou.base.IPresenter;
import com.example.kuaishou.demokuaishou.base.IView;
import com.example.kuaishou.demokuaishou.home.mode.CityVideoBean;

public class CityVideoContract {
    public interface ICityVideoView extends IView {
        void onCityVideo(CityVideoBean cityVideoBean);
    }

    public static abstract class CityVideoPresenter extends BasePresenter<ICityVideoView> {
        public abstract void findCityVideo();
    }
}
