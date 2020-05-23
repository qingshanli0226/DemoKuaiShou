package com.example.kuaishou.demokuaishou.player;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.example.kuaishou.demokuaishou.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class IJKVideoViewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private IjkVideoView ijkVideoView;
    private String videoUrl;
    private SurfaceView redSurfaceView;
    private SurfaceHolder surfaceHolder;
    private Display display;


    //定义两个坐标变量
    private int x = 0;
    private int y = 0;
    private long time = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ijkvideoview);
        videoUrl = getIntent().getStringExtra("videoUrl");

        initIJKVideoView();

        initRedSurfaceView();

        //获取描述当前页面窗口的对象
        display = getWindowManager().getDefaultDisplay();
    }

    private void initRedSurfaceView() {
        redSurfaceView = findViewById(R.id.redSurfaceView);
        redSurfaceView.setZOrderOnTop(true);//将surfaceView放到IJKVideoVIew的上层
        redSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);//将SurfaceView的背景设置成透明
        surfaceHolder = redSurfaceView.getHolder();
        surfaceHolder.addCallback(this);

    }

    private void initIJKVideoView() {
        ijkVideoView = findViewById(R.id.ijkVideoView);
        //设置播放地址
        ijkVideoView.setVideoURI(Uri.parse(videoUrl));
        //设置准备的监听，当准备好之后，开始播放
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkVideoView.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ijkVideoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ijkVideoView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ijkVideoView.stopPlayback();
    }

    public static void launch(Activity activity, String videoUrl) {
        Intent intent = new Intent();
        intent.putExtra("videoUrl", videoUrl);
        intent.setClass(activity, IJKVideoViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        //设置touch事件,通过touch事件，获取用户点击的X，Y坐标
        redSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//获取用户点击的事件
                    //是不是双击
                    if (time == 0) {
                        time = System.currentTimeMillis();
                    } else {
                        //是双击
                        if (System.currentTimeMillis() - time < 200) {
                            x = (int) event.getX();
                            y = (int) event.getY();
                            //绘制小红心
                            drawRed();
                            //调用服务端，去告诉服务端，你对该主播的当前小视频表示点赞
                            handler.sendEmptyMessageDelayed(0, 500);

                        } else {//不是双击
                            time = System.currentTimeMillis();
                        }
                    }



                }
                return true;//返回true，代表这个点击事件，将由surfaceView来处理，事件分发到此结束
            }
        });


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            clearSurfaceView();
        }
    };


    private void clearSurfaceView() {
        Canvas canvas = surfaceHolder.lockCanvas();//锁定surfaceView的画布
        //声明一个画笔
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//清空屏幕
        paint.setColor(Color.TRANSPARENT);

        //定义一个矩形,这个矩形代表的是我们清空的大小
        RectF rectF = new RectF();
        rectF.set(0, 0,display.getWidth(),display.getHeight());
        canvas.drawRect(rectF,paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawRed() {
        //绘制
        Canvas canvas = surfaceHolder.lockCanvas();//锁定surfaceView的画布

        //声明一个画笔
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//清空屏幕
        paint.setColor(Color.TRANSPARENT);

        //定义一个矩形,这个矩形代表的是我们清空的大小
        RectF rectF = new RectF();
        rectF.set(0, 0,display.getWidth(),display.getHeight());
        canvas.drawRect(rectF,paint);

        //绘制红心
        //把红色的心脏的图片生成Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.red);
        canvas.drawBitmap(bitmap, x,y,null);
        //释放画布
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
