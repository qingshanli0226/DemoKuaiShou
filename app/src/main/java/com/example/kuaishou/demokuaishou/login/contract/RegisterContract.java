package com.example.kuaishou.demokuaishou.login.contract;

import com.example.kuaishou.demokuaishou.base.BasePresenter;
import com.example.kuaishou.demokuaishou.base.IView;
import com.example.kuaishou.demokuaishou.login.mode.LoginBean;
import com.example.kuaishou.demokuaishou.login.mode.RegisterBean;

public class RegisterContract {

    public interface IRegisterView extends IView {
        void onRegister(RegisterBean registerBean);
        void onLogin(LoginBean loginBean);
    }

    public static abstract class RegisterPresenter extends BasePresenter<IRegisterView> {
        public abstract void register(String name, String password);
    }

}
