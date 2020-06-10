package com.example.kuaishou.demokuaishou.cache;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.util.Map;

public class MessageManger {

    private static MessageManger instance;

    private MessageManger() {

    }

    public static MessageManger getInstance() {
        if (instance == null) {
            instance = new MessageManger();
        }

        return instance;
    }

    public void init(Context context) {

        final UmengMessageHandler umengMessageHandler = new UmengMessageHandler() {
            //处理通知
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {

                Log.d("LQS: title =", uMessage.title + " notification text :" + uMessage.text);
                return super.getNotification(context, uMessage);
            }
            //处理自定义消息
            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                super.dealWithCustomMessage(context, uMessage);
                Log.d("LQS: title =", uMessage.title + " notification custom :" + uMessage.custom);
                for (Map.Entry entry : uMessage.extra.entrySet()) {//遍历附加的参数
                    Object key = entry.getKey();
                    Object value = entry.getValue();

                    Log.d("LQS", " key = " + (String)key + " value = " + (String)value);
                }


            }
        };

        PushAgent.getInstance(context).setMessageHandler(umengMessageHandler);
    }
}
