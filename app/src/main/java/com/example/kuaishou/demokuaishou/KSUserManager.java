package com.example.kuaishou.demokuaishou;

//定义单例，存储当前用户登录状态
public class KSUserManager {

    private boolean loginStaus = false;

    private static KSUserManager instance;

    private KSUserManager() {
    }

    public static KSUserManager getInstance() {
        if (instance == null) {
            instance = new KSUserManager();
        }
        return instance;
    }

    //判断当前用户是否登录
    public boolean isLogin() {
        return loginStaus;
    }

    public void setLoginStaus(boolean staus) {
        loginStaus = staus;
    }
}
