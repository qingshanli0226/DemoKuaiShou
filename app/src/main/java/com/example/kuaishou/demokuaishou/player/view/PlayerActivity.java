package com.example.kuaishou.demokuaishou.player.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dou361.ijkplayer.widget.PlayerView;
import com.example.kuaishou.demokuaishou.R;

public class PlayerActivity extends AppCompatActivity {
    private String videoUrl;//播放的视频地址
    private PlayerView playerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoUrl = getIntent().getStringExtra("videoUrl");
        initPlayer();
    }

    private void initPlayer() {
        View rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(rootView);//设置IJKVideoVIew库中提供的布局

        playerView = new PlayerView(this)
                .setTitle("1712")
                .setPlaySource(videoUrl)
                .hideBack(true)
                .hideHideTopBar(true)
                .startPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerView.onPause();
    }


    public static void launch(Activity activity, String videoUrl) {
        Intent intent = new Intent();
        intent.putExtra("videoUrl", videoUrl);
        intent.setClass(activity, PlayerActivity.class);
        activity.startActivity(intent);
    }
}
