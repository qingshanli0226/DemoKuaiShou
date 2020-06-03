package com.example.kuaishou.demokuaishou.live.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kuaishou.demokuaishou.R;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class LiveActivity extends AppCompatActivity implements View.OnClickListener {

    private TXLivePusher txLivePusher;
    private TXLivePushConfig txLivePushConfig;

    private TXCloudVideoView txCloudVideoView;//预览用的
    private EditText rtmpPushEditText;

    private TextView bitSpeedTv;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        initPusher();

        //获取推流地址
        TxUtil.getRTMPPusherFromServer(this, rtmpPushEditText);

        findViewById(R.id.startLive).setOnClickListener(this);
        findViewById(R.id.stopLive).setOnClickListener(this);
        bitSpeedTv = findViewById(R.id.bitSpeed);
        bitSpeedTv.setText("0-Kbps");//开始速度为0
    }

    private void initPusher() {
        txLivePusher = new TXLivePusher(this);
        txLivePushConfig = new TXLivePushConfig();
        txLivePusher.setConfig(txLivePushConfig);
        txCloudVideoView = findViewById(R.id.previewUI);
        rtmpPushEditText = findViewById(R.id.rtmpPushUrl);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startLive:
                startLive();
                break;
            case R.id.stopLive:
                stopLive();
                break;
        }
    }

    private void stopLive() {
        txLivePusher.stopCameraPreview(true);
        txLivePusher.stopPusher();
    }

    private void startLive() {
        String rtmpPushUrl = rtmpPushEditText.getText().toString().trim();
        Log.d("LQS", "rtmpPushUrl = " + rtmpPushUrl);
        int result = txLivePusher.startPusher(rtmpPushUrl);//开始推流
        Log.d("LQS", "result = " + result);
        txLivePusher.startCameraPreview(txCloudVideoView);

//去监听当前直播的状态
        txLivePusher.setPushListener(new ITXLivePushListener() {
            //监听当前直播发生的事件，例如网络连接情况，硬件使用情况，网速等
            @Override
            public void onPushEvent(int event, Bundle bundle) {
                Log.d("LQS", "event = " + event);
            }

            //显示当前直播的数据,例如帧率，码率，大小
            @Override
            public void onNetStatus(final Bundle status) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bitSpeedTv.setText(status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
                    }
                });
                Log.d("LQS", "Current status, CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                        ", RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                        ", SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                        ", FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                        ", ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                        ", VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
            }
        });
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LiveActivity.class);
        activity.startActivity(intent);
    }
}
