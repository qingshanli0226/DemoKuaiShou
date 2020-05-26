package com.example.kuaishou.demokuaishou.player.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.example.kuaishou.demokuaishou.R;

public class SlideVideoViewActivity extends AppCompatActivity {
    private SlideVideoViewAdapter slideVideoViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slide_video_view);

        initSlideRv();
    }

    private void initSlideRv() {
        RecyclerView slideRv = findViewById(R.id.slideRV);
        slideRv.setLayoutManager(new LinearLayoutManager(this));
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();//这个控件和RecyclerView关联起来，达到效果是，滑动RecyclerView时，一次只能滑动一个Item，让
        //RecyclerView滑动效果类似于viewPager滑动Fragment。类似于抖音滑动效果
        pagerSnapHelper.attachToRecyclerView(slideRv);//和RecyclerView建立关联
        slideVideoViewAdapter = new SlideVideoViewAdapter();
        slideRv.setAdapter(slideVideoViewAdapter);
    }
}
