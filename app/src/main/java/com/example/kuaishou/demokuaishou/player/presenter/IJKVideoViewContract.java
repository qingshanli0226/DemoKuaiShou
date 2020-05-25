package com.example.kuaishou.demokuaishou.player.presenter;

import com.example.kuaishou.demokuaishou.base.BasePresenter;
import com.example.kuaishou.demokuaishou.base.IView;
import com.example.kuaishou.demokuaishou.player.mode.OrderInfoBean;
import com.example.kuaishou.demokuaishou.player.mode.UpdataMoneyBean;

public class IJKVideoViewContract {
    public interface IIJKVideoView extends IView {
        void onUpdataMoneyData(UpdataMoneyBean updataMoneyBean);
        void onOrderInfo(OrderInfoBean orderInfoBean);
    }

    public static abstract class IjkPresenter extends BasePresenter<IIJKVideoView> {
        public abstract void getOrderInfo(String subject, String price);
        public abstract void updateMoney(String money);
    }
}
