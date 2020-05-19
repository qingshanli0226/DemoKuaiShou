package com.example.kuaishou.demokuaishou.home.presenter;

import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.home.contract.CityVideoContract.CityVideoPresenter;
import com.example.kuaishou.demokuaishou.home.mode.CityVideoBean;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CityVideoPresenterImpl extends CityVideoPresenter {

    @Override
    public void findCityVideo() {
        //调用网络接口获取数据
        RetrofitCreator.getNetApiService().findCityVideo()
                .subscribeOn(Schedulers.io())//在子线程中获取数据
                .observeOn(AndroidSchedulers.mainThread())//获取数据后，切到主线程中，在主线程中回调IView，让Acitivity或者Fragment去更新UI
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        iHttpView.showLoading();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        iHttpView.hideLoading();
                    }
                })
                .subscribe(new Observer<CityVideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CityVideoBean videoBean) {
                        iHttpView.onCityVideo(videoBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setCode("-1");
                        errorBean.setMessage(e.getMessage());
                        iHttpView.showError(errorBean);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

}
