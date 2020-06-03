package com.example.kuaishou.demokuaishou.record.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.kuaishou.demokuaishou.R;

import java.io.IOException;

public class TextureViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_texture_view);

        textureView= findViewById(R.id.textureView);

        //监听当前textureView准备好了
        textureView.setSurfaceTextureListener(this);

        WindowManager windowManager = getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        Log.d("LQS", "display width: " + display.getWidth() + " dispaly height = " + display.getHeight());


        findViewById(R.id.btnChangeSize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 200);
                textureView.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取相机预览尺寸
        Log.d("LQS", "camera preview width: " + previewSize.width + " preview size height = " + previewSize.height);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(previewSize.width, previewSize.height, Gravity.CENTER);
        textureView.setLayoutParams(layoutParams);
        try {
            camera.setPreviewTexture(surface);//设置预览的画布
            //textureView.setRotation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setDisplayOrientation(90);//改成相机竖屏预览

        camera.startPreview();//启动相机预览

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public static void launch(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity,TextureViewActivity.class);
        activity.startActivity(intent);
    }
}
