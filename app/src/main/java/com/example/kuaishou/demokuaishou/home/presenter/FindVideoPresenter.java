package com.example.kuaishou.demokuaishou.home.presenter;

import com.example.kuaishou.demokuaishou.common.BaseBean;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.home.contract.FindVideoContract;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoDataBean;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

//使用presenter的目的，是因为我们不要在VIew里做耗时的操作，把耗时的操作放到presenter中。所以presenter的方法全部是是耗时操作
//耗时操作会使用异步线程，所以presenter中的方法不能有返回值。
public class FindVideoPresenter implements FindVideoContract.IFindVideoPresenter {
    private FindVideoContract.IFindVideoView iFindVideoView;

    @Override
    public void findVideo() {//异步方法是不能使用返回值的
        //调用网络接口获取数据
        RetrofitCreator.getNetApiService().findVideo()
                .delay(2,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())//在子线程中获取数据
                .observeOn(AndroidSchedulers.mainThread())//获取数据后，切到主线程中，在主线程中回调IView，让Acitivity或者Fragment去更新UI
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        iFindVideoView.showLoading();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        iFindVideoView.hideLoading();
                    }
                })
                .subscribe(new Observer<BaseBean<List<FindVideoDataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<List<FindVideoDataBean>> videoBean) {
                        iFindVideoView.onFindVideo(videoBean.getResult());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setCode("-1");
                        errorBean.setMessage(e.getMessage());
                        iFindVideoView.showError(errorBean);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void attachView(FindVideoContract.IFindVideoView iFindVideoView) {
        this.iFindVideoView = iFindVideoView;
    }

    @Override
    public void detachView() {
       this.iFindVideoView = null;
    }
}
