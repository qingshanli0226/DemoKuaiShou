package com.example.kuaishou.demokuaishou.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.kuaishou.demokuaishou.login.mode.LoginBean;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//定义单例，存储当前用户登录状态
public class KSUserManager {

    private LoginBean loginBean;

    private static KSUserManager instance;

    private List<ILoginListener> loginListenerList = new LinkedList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private KSUserManager() {
    }

    public static KSUserManager getInstance() {
        if (instance == null) {
            instance = new KSUserManager();
        }
        return instance;
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences("ks", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //实现自动登录功能
        autoLogin();
    }

    public String getUserName() {
        return loginBean == null || loginBean.getResult() == null? "":loginBean.getResult().getName();
    }

    private void autoLogin() {
        if (TextUtils.isEmpty(getToken())) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("token", getToken());
        RetrofitCreator.getNetApiService().autoLogin(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        if (loginBean.getCode().equals("200")) {
                            saveLoginBean(loginBean);
                            saveToken(loginBean.getResult().getToken());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void saveToken(String token) {
        editor.putString("token", token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }


    //判断当前用户是否登录
    public boolean isLogin() {
        return loginBean != null;
    }

    public void saveLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
        notifyLoginStatus();
    }

    private void notifyLoginStatus() {
        if (loginListenerList.size() == 0) {
            return;
        }
        for(ILoginListener iLoginListener:loginListenerList) {
            if (loginBean != null) {
                iLoginListener.onLogin();
            }else {
                iLoginListener.onLogout();
            }
        }

    }

    //返回当前用户的账户资金数
    public String getMoney() {
        return (String) loginBean.getResult().getMoney();
    }
    //更新个人账户
    public void updateMoney(String money) {
        loginBean.getResult().setMoney(money);
    }

    public void addLoginLister(ILoginListener iLoginListener) {
        if (!loginListenerList.contains(iLoginListener)) {
            loginListenerList.add(iLoginListener);
        }
    }

    public void removeLoginListener(ILoginListener iLoginListener) {
        if (loginListenerList.contains(iLoginListener)) {
            loginListenerList.remove(iLoginListener);
        }
    }

    public interface ILoginListener{
        void onLogin();
        void onLogout();
    }
}
