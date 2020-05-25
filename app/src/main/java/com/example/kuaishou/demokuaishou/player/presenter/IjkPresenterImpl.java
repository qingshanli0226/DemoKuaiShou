package com.example.kuaishou.demokuaishou.player.presenter;

import com.example.kuaishou.demokuaishou.net.NetApiService;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;
import com.example.kuaishou.demokuaishou.player.mode.OrderInfoBean;
import com.example.kuaishou.demokuaishou.player.mode.UpdataMoneyBean;
import com.example.kuaishou.demokuaishou.player.presenter.IJKVideoViewContract.IjkPresenter;
import com.example.kuaishou.demokuaishou.user.KSUserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class IjkPresenterImpl extends IjkPresenter {
    @Override
    public void getOrderInfo(String subject, String price) {
        JSONArray jsonArray = new JSONArray();
        //用数组来存储购买的产品信息,使用JsonArray，是因为可能购买多个产品
        JSONObject object = new JSONObject();
        try {
            object.put("productName", "kuaishou");
            object.put("productId", "100010");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(object);//把信息添加到列表中

        //存放购买的原因，价格，以及上面生成的产品信息
        JSONObject subjectObject = new JSONObject();
        try {
            subjectObject.put("subject", subject);
            subjectObject.put("totalPrice", price);
            subjectObject.put("body", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //因为是json的方式，向服务端传递参数，所以生成requestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), subjectObject.toString());

        RetrofitCreator.getNetApiService().getOrderInfo(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OrderInfoBean orderInfoBean) {
                        iHttpView.onOrderInfo(orderInfoBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void updateMoney(String money) {
        HashMap<String,String> params = new HashMap<>();
        params.put("money", money);

        RetrofitCreator.getNetApiService().updateMoney(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdataMoneyBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UpdataMoneyBean updataMoneyBean) {
                        //更新虚拟币成功,在缓存中去更新个人账户的虚拟币
                        iHttpView.onUpdataMoneyData(updataMoneyBean);
                        KSUserManager.getInstance().updateMoney(updataMoneyBean.getResult());
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
