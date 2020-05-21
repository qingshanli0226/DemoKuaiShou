package com.example.kuaishou.demokuaishou.net;


import com.example.kuaishou.demokuaishou.common.BaseBean;
import com.example.kuaishou.demokuaishou.home.mode.CityVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoDataBean;
import com.example.kuaishou.demokuaishou.home.mode.FocusVideoBean;
import com.example.kuaishou.demokuaishou.login.mode.LoginBean;
import com.example.kuaishou.demokuaishou.login.mode.RegisterBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

//网络请求的接口,相当于IMode
public interface NetApiService {

    @GET("findVideo")
    Observable<BaseBean<List<FindVideoDataBean>>> findVideo();
    @GET("findFocusVideo")
    Observable<FocusVideoBean> findFocusVideo();
    @GET("findCityVideo")
    Observable<CityVideoBean> findCityVideo();
    @POST("register")
    @FormUrlEncoded
    Observable<RegisterBean> register(@FieldMap HashMap<String,String> params);
    @POST("login")
    @FormUrlEncoded
    Observable<LoginBean> login(@FieldMap HashMap<String,String> params);
    @POST("autoLogin")
    @FormUrlEncoded
    Observable<LoginBean> autoLogin(@FieldMap HashMap<String,String> params);
}
