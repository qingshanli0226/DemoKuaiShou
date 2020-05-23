package com.example.kuaishou.demokuaishou.home.contract;

import com.example.kuaishou.demokuaishou.base.IPresenter;
import com.example.kuaishou.demokuaishou.base.IView;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;

import java.util.List;

//给每个页面定义约定，约定Presenter实现的接口，约定页面实现的回调接口
public class FindVideoContract {

    public interface IFindVideoView extends IView {//通过该接口，Presenter将获取的数据回调给UI
        void onFindVideo(FindVideoBean findVideoBean);
    }

    public interface IFindVideoPresenter extends IPresenter<IFindVideoView> {
        void findVideo();
    }
}
