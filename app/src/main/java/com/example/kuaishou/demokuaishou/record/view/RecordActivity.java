package com.example.kuaishou.demokuaishou.record.view;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.R;

import java.io.File;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private MediaRecorder mediaRecorder;//录制视频的类
    private Camera camera;//操作硬件相机的类，通过这个类可以拿到相机拍摄的相机数据
    private MediaPlayer mediaPlayer;//播放视频数据的类
    private boolean isRecord = false; //标记，标记当前是否正在录制

    private SurfaceView surfaceView;//通过surfaceView来预览录制的视频内容
    private SurfaceHolder surfaceHolder;

    private File videoFile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_recorder);

        mediaPlayer = new MediaPlayer();

        findViewById(R.id.btnStartCamera).setOnClickListener(this);
        findViewById(R.id.btnStopCamera).setOnClickListener(this);
        findViewById(R.id.btnPlayCamera).setOnClickListener(this);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        //相机将数据推送到surfaceView，需要准备一个缓存
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStartCamera://开始录制
                if (!isRecord) {
                    startRecord();
                }
                break;
            case R.id.btnStopCamera://结束录制
                if (isRecord) {
                    stopRecord();
                }
                break;
            case R.id.btnPlayCamera://播放录制好的视频
                if (videoFile.exists()) {
                    playVideo();
                }

                break;
        }

    }

    private void playVideo() {
        try {
            mediaPlayer.setDataSource(videoFile.getPath());//设置播放的资源路径
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放资源数据类型
            mediaPlayer.prepare();
            mediaPlayer.setSurface(surfaceHolder.getSurface());
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        isRecord = false;
        camera.lock();//把相机资源还给系统
        camera.release();
        camera = null;

    }

    private void startRecord() {
        mediaRecorder = new MediaRecorder();
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);//打开后摄像头
        camera.setDisplayOrientation(90);//改为竖向拍摄
        camera.unlock();//获取使用硬件相机的权力
        mediaRecorder.setCamera(camera);//将录制视频的类和相机关联起来
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//音频数据从麦克风
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频时速局从相机中获取
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//生成视频文件的格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//设置视频编码器
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置音频编码器
        mediaRecorder.setVideoSize(640,480);//设置视频图像的大小
        mediaRecorder.setVideoFrameRate(30);//设置录制视频的帧率
        mediaRecorder.setVideoEncodingBitRate(3*1024*1024);//设置录制视频的码率
        mediaRecorder.setOrientationHint(90);
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());//配置录制视频的预览页面

        //配置录制视频的文件
        videoFile = new File(getExternalFilesDir(null), "/1712.mp4");
        if (videoFile.exists()) {
            videoFile.delete();
        }
        try {
            videoFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.setOutputFile(videoFile.getPath());//配置录制视频存放的文件路径
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecord = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public static void launch(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, RecordActivity.class);
        activity.startActivity(intent);
    }
}
