package com.example.kuaishou.demokuaishou.live.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.R;

public class PreparePlayLiveActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_play_live);

        final EditText playLiveUrlEditText = findViewById(R.id.playLiveUrlEditText);


        findViewById(R.id.btnStartPlayLive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playLiveUrl = playLiveUrlEditText.getText().toString().trim();
                if (TextUtils.isEmpty(playLiveUrl)) {
                    Toast.makeText(PreparePlayLiveActivity.this, "请输入争取地址", Toast.LENGTH_SHORT).show();
                } else {
                    PlayLiveActivity.launch(PreparePlayLiveActivity.this, playLiveUrl);
                    finish();
                }
            }
        });


    }


    public static void launch(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, PreparePlayLiveActivity.class);
        activity.startActivity(intent);
    }
}
