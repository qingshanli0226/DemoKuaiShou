package com.example.kuaishou.demokuaishou.net;


import com.example.kuaishou.demokuaishou.common.BaseBean;
import com.example.kuaishou.demokuaishou.home.mode.CityVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FocusVideoBean;
import com.example.kuaishou.demokuaishou.login.mode.LoginBean;
import com.example.kuaishou.demokuaishou.login.mode.RegisterBean;
import com.example.kuaishou.demokuaishou.player.mode.GiftBean;
import com.example.kuaishou.demokuaishou.player.mode.OrderInfoBean;
import com.example.kuaishou.demokuaishou.player.mode.UpdataMoneyBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

//网络请求的接口,相当于IMode
public interface NetApiService {

    @GET("findVideo")
    Observable<FindVideoBean> findVideo();
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
    @FormUrlEncoded//参数的形式是表单的
    Observable<LoginBean> autoLogin(@FieldMap HashMap<String,String> params);
    @GET("atguigu/json/gif.json")
    Observable<GiftBean> getGiftData();
    @POST("getOrderInfo")//参数的形式是json格式
    Observable<OrderInfoBean> getOrderInfo(@Body RequestBody body);
    @POST("updateMoney")
    @FormUrlEncoded//参数的形式是表单的
    Observable<UpdataMoneyBean> updateMoney(@FieldMap HashMap<String,String> params);

    //定义下载文件的接口
    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String url);


}
