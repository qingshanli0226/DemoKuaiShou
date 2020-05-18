package com.example.kuaishou.demokuaishou.net;

import com.example.kuaishou.demokuaishou.common.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//使用RxJava+Retrofit网络框架,这个类是获取网络接口的API,通过该API获取网络数据
public class RetrofitCreator {

    private static NetApiService netApiService;

    public static NetApiService getNetApiService (){
        if (netApiService == null) {
            netApiService = createNetApiService();
        }
        return netApiService;
    }

    private static NetApiService createNetApiService() {
        //自定义okhttpclient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5,TimeUnit.SECONDS)//连接超时
                .readTimeout(5,TimeUnit.SECONDS)//读数据超时
                .writeTimeout(5,TimeUnit.SECONDS)//写网络数据超时
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))//设置打印log的级别
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//让接口返回Observerable而不是Call
                .addConverterFactory(GsonConverterFactory.create())//将json字符串转换成Bean
                .build();

        NetApiService netApiService = retrofit.create(NetApiService.class);

        return netApiService;
    }
}
