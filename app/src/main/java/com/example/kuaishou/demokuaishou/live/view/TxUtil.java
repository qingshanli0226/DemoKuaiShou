package com.example.kuaishou.demokuaishou.live.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.rtmp.TXLiveConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TxUtil {
    private static final String TAG = "LQS";
    private static boolean mIsGettingRTMPURL =false;
    private static ProgressDialog  mFetchProgressDialog;

    /**
     * 从腾讯业务后台获取 RTMP 推流地址. 记住推流协议使用的RTMP协议
     */
    public static void getRTMPPusherFromServer(final Activity activity, final EditText rtmpPushEditText) {
        OkHttpClient mOkHttpClient = null;
        if (mIsGettingRTMPURL) return;
        mIsGettingRTMPURL = true;
        if (mFetchProgressDialog == null) {
            mFetchProgressDialog = new ProgressDialog(activity);
            mFetchProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            mFetchProgressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
            mFetchProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        }
        mFetchProgressDialog.show();

        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
        }
        String reqUrl = "https://lvb.qcloud.com/weapp/utils/get_test_pushurl";
        Request request = new Request.Builder()
                .url(reqUrl)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();
        Log.d(TAG, "start fetch push url");
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mFetchProgressDialog.dismiss();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsGettingRTMPURL = false;
                        Toast.makeText(activity, "获取推流地址失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mFetchProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    JSONObject jsonRsp = null;
                    try {
                        jsonRsp = new JSONObject(response.body().string());
                        final String rtmpPushUrl = jsonRsp.optString("url_push");            // RTMP 推流地址
                        final String rtmpPlayUrl = jsonRsp.optString("url_play_rtmp");   // RTMP 播放地址
                        final String flvPlayUrl = jsonRsp.optString("url_play_flv");     // FLA  播放地址
                        final String hlsPlayUrl = jsonRsp.optString("url_play_hls");     // HLS  播放地址
                        final String realtimePlayUrl = jsonRsp.optString("url_play_acc");// RTMP 加速流地址

                        Log.d(TAG, "rtmpPushUrl = " + rtmpPushUrl
                                + " rtmpPlayUrl = "  + rtmpPlayUrl
                                + " flvPalyUrl = " + flvPlayUrl
                                + " hlsPlayUrl = " + hlsPlayUrl);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rtmpPushEditText.setText(rtmpPushUrl);
                                Bundle params = new Bundle();
                                params.putString(TXLiveConstants.EVT_DESCRIPTION, "检查地址合法性");

                                mIsGettingRTMPURL = false;
                                if (TextUtils.isEmpty(rtmpPushUrl)) {
                                    Toast.makeText(activity, "获取推流地址失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, "获取推流地址成功，点击左上角二维码查看推流地址。", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        String[] arrays = new String[]{rtmpPlayUrl, flvPlayUrl, hlsPlayUrl, realtimePlayUrl};
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
