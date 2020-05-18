package com.example.kuaishou.demokuaishou.home.presenter;

import com.example.kuaishou.demokuaishou.home.contract.FindVideoContract;
import com.example.kuaishou.demokuaishou.home.contract.FocusVideoContract;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FocusVideoBean;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//使用presenter的目的，是因为我们不要在VIew里做耗时的操作，把耗时的操作放到presenter中。所以presenter的方法全部是是耗时操作
//耗时操作会使用异步线程，所以presenter中的方法不能有返回值。
public class FocusVideoPresenter implements FocusVideoContract.IFocusVideoPresenter {
    private FocusVideoContract.IFocusView iFocusView;


    @Override
    public void focusVideo() {
        //调用网络接口获取数据
        RetrofitCreator.getNetApiService().findFocusVideo()
                .subscribeOn(Schedulers.io())//在子线程中获取数据
                .observeOn(AndroidSchedulers.mainThread())//获取数据后，切到主线程中，在主线程中回调IView，让Acitivity或者Fragment去更新UI
                .subscribe(new Observer<FocusVideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(FocusVideoBean focusVideoBean) {
                        iFocusView.onFocusVideo(focusVideoBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void attchView(FocusVideoContract.IFocusView iFocusView) {
        this.iFocusView = iFocusView;
    }

    public void detachView() {
        this.iFocusView = null;
    }
}
