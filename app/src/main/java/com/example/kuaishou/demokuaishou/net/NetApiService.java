package com.example.kuaishou.demokuaishou.net;


import com.example.kuaishou.demokuaishou.common.BaseBean;
import com.example.kuaishou.demokuaishou.home.mode.CityVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoDataBean;
import com.example.kuaishou.demokuaishou.home.mode.FocusVideoBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

//网络请求的接口,相当于IMode
public interface NetApiService {

    @GET("findVideo")
    Observable<BaseBean<List<FindVideoDataBean>>> findVideo();
    @GET("findFocusVideo")
    Observable<FocusVideoBean> findFocusVideo();
    @GET("findCityVideo")
    Observable<CityVideoBean> findCityVideo();
}
