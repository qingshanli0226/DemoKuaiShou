package com.example.kuaishou.demokuaishou.login.presenter;

import com.bumptech.glide.Registry;
import com.example.kuaishou.demokuaishou.login.contract.RegisterContract;
import com.example.kuaishou.demokuaishou.login.mode.LoginBean;
import com.example.kuaishou.demokuaishou.login.mode.RegisterBean;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;

import java.util.HashMap;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenterImpl  extends RegisterContract.RegisterPresenter {
    @Override
    public void register(String name, String password) {
        final HashMap<String,String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);
        RetrofitCreator.getNetApiService().register(params)
                .flatMap(new Function<RegisterBean, ObservableSource<LoginBean>>() {
                    @Override
                    public ObservableSource<LoginBean> apply(RegisterBean registerBean) throws Exception {
                        return RetrofitCreator.getNetApiService().login(params);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        iHttpView.onLogin(loginBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
