package com.example.kuaishou.demokuaishou.record.view;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.example.kuaishou.demokuaishou.R;

import java.io.File;
import java.io.IOException;

public class AudioRecorderActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRecord;
    private Button btnPlay;
    private MediaRecorder mediaRecorder;//android 原生的控件
    private MediaPlayer mediaPlayer;
    private File recordFile;
    private boolean isRecording;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_recorder);

        btnRecord = findViewById(R.id.recordMic);
        btnPlay = findViewById(R.id.playMic);


        btnRecord.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        init();
    }

    private void init() {
        mediaRecorder = new MediaRecorder();
        //设置音频来源于麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置音频输出格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
        //设置音频文件的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

        mediaPlayer = new MediaPlayer();

        //设置生成的文件
        recordFile = new File(getExternalFilesDir(null).getPath(), "/record.amr");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recordMic://录制音频
                if (isRecording == false) {//如果没有录制，则录制
                    record();
                } else {
                    stopRecord();
                }
                break;
            case R.id.playMic: //播放音频
                play();
                break;
                default:
                    break;
        }
    }

    //播放我们录制的音频文件
    private void play() {
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(recordFile.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //停止录制
    private void stopRecord() {
        mediaRecorder.stop();
        mediaRecorder.release();
        isRecording = false;
        btnRecord.setText("录制音频");
    }

    //开始录制
    private void record() {
        if (recordFile.exists()) {
            recordFile.delete();
        }

        try {
            recordFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置文件输出路径
        mediaRecorder.setOutputFile(recordFile.getPath());

        try {
            //准备
            mediaRecorder.prepare();
            //开始录制
            mediaRecorder.start();
            btnRecord.setText("暂停录制");
            //把标志位置成true
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
