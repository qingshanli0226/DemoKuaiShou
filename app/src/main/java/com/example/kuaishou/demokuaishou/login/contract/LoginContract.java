package com.example.kuaishou.demokuaishou.login.contract;

import com.example.kuaishou.demokuaishou.base.BasePresenter;
import com.example.kuaishou.demokuaishou.base.IView;
import com.example.kuaishou.demokuaishou.login.mode.LoginBean;

public class LoginContract {

    public interface ILoginView extends IView {
        void onLogin(LoginBean loginBean);
    }

    public static abstract class LoginPresenter extends BasePresenter<ILoginView> {
        public abstract void login(String name,String password);
    }
}
