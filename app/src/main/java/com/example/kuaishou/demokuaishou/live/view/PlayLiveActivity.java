package com.example.kuaishou.demokuaishou.live.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.example.kuaishou.demokuaishou.R;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuSurfaceView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class PlayLiveActivity extends AppCompatActivity implements View.OnClickListener {

    private IjkVideoView ijkVideoView;
    private boolean isPause;

    private EditText inputText;
    private DanmakuSurfaceView danmakuSurfaceView;
    private DanmakuContext danmakuContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_live);

        ijkVideoView = findViewById(R.id.ijkVideoView);
        inputText = findViewById(R.id.inputText);

        startPlay();

        //初始化弹幕
        initDanmaku();
    }

    private void initDanmaku() {
        danmakuSurfaceView = findViewById(R.id.danmakuSurfaceView);
        danmakuSurfaceView.setZOrderOnTop(true);
        danmakuSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        danmakuContext = DanmakuContext.create();//创建弹幕view配置的上下文
        danmakuSurfaceView.prepare(parser, danmakuContext);//为发送弹幕做准备。两个参数，第一个参数，给弹幕视图，准备一个解析器，解析弹幕内容。另一个给它配置一个弹幕的配置类

        danmakuSurfaceView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                danmakuSurfaceView.start();//当弹幕的surfaceView准备好之后，开始启动弹幕库
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        
        findViewById(R.id.btnSend).setOnClickListener(this);

    }

    //初始化一个解析弹幕的内容的类
    BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    private void startPlay() {
        String playLiveUrl = getIntent().getStringExtra("liveUrl");


        ijkVideoView.setVideoURI(Uri.parse(playLiveUrl));
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkVideoView.start();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ijkVideoView.isPlaying()) {
            ijkVideoView.pause();
            isPause = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            ijkVideoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ijkVideoView.stopPlayback();
    }

    public static void launch(Activity activity, String videoUrl) {
        Intent intent = new Intent();
        intent.putExtra("liveUrl", videoUrl);
        intent.setClass(activity, PlayLiveActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                String danmakuContent = inputText.getText().toString().trim();
                if (TextUtils.isEmpty(danmakuContent)) {
                    Toast.makeText(this, "请正确输入弹幕内容",Toast.LENGTH_SHORT).show();
                } else {
                    sendOneDamaku(danmakuContent);
                }

                break;
        }
    }

    //开始发送弹幕
    private void sendOneDamaku(String danmakuContent) {
        //当前DanmakuSurfaceView，给它添加一个显示的弹幕内容实例，DanmakuSurfaceView它会自动在子线程中绘制，
        //这样，我们在Activity里面没必要启动新的子线程了.
        BaseDanmaku baseDanmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);//从左往右显示
        baseDanmaku.text = danmakuContent;//设置弹幕显示的内容
        baseDanmaku.textColor = Color.RED;
        baseDanmaku.rotationY = 90f;
        baseDanmaku.setTime(danmakuSurfaceView.getCurrentTime() + 1000);
        baseDanmaku.textSize = 30f;
        baseDanmaku.textShadowColor = Color.GREEN;
        danmakuSurfaceView.addDanmaku(baseDanmaku);
    }
}
