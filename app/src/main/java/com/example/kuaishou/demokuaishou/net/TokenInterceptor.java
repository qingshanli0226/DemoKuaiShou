package com.example.kuaishou.demokuaishou.net;

import com.example.kuaishou.demokuaishou.user.KSUserManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//增加一个拦截器，该拦截器在网络请求的header上添加token信息
public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request  request = chain.request();
        Request newRequest = request.newBuilder().addHeader("token", KSUserManager.getInstance().getToken())
                .build();
        return chain.proceed(newRequest);
    }
}
