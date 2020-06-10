package com.example.kuaishou.demokuaishou.player.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.example.kuaishou.demokuaishou.AdrActivity;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.base.BaseActivity;
import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.common.Constant;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.sina.weibo.sdk.constant.WBConstants;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class SlideVideoViewActivity extends BaseActivity {
    private SlideVideoViewAdapter slideVideoViewAdapter;


    //定义两个成员变量
    //当前视频播放的位置
    private int currentPosition;
    //当前播放视频的播放器
    private IjkVideoView currentVideoView;

    private RecyclerView slideRv;

    @Override
    protected void create() {
        initData();
    }

    @Override
    protected void initView() {
        initSlideRv();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_slide_video_view;
    }


    private void initData() {
        //初始化列表数据
        FindVideoBean findVideoBean = CacheManager.getInstance().getHomeData();
        slideVideoViewAdapter.updateData(findVideoBean.getResult());

        //获取播放列表视频的位置
        final int positon = getIntent().getIntExtra("position", 0);
        slideRv.scrollToPosition(positon);//让列表滑动到那个位置,滑动需要一定的时间.所以我们使用这个位置的ItemView需要做个延时处理
        //否则会出现空指针
        slideRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                playVideoByPosition(positon);
            }
        },2000);
    }

    private void initSlideRv() {
        slideRv = findViewById(R.id.slideRV);
        slideRv.setLayoutManager(new LinearLayoutManager(this));
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();//这个控件和RecyclerView关联起来，达到效果是，滑动RecyclerView时，一次只能滑动一个Item，让
        //RecyclerView滑动效果类似于viewPager滑动Fragment。类似于抖音滑动效果
        pagerSnapHelper.attachToRecyclerView(slideRv);//和RecyclerView建立关联
        slideVideoViewAdapter = new SlideVideoViewAdapter();
        slideRv.setAdapter(slideVideoViewAdapter);

        //注册scrollListener,去监听当前RecyclerView 的Item滚动

        slideRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //找到当前RecyclerView在屏幕显示的第一个ItemView的position
                int position = ((LinearLayoutManager)(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                View firstView = recyclerView.getLayoutManager().findViewByPosition(position);//找到屏幕上显示的第一个view
                //判断当前播放的视频位置是否和屏幕上显示的第一个位置一样，如果一样不需要做任何处理
                if (position>currentPosition) {//代表着第0个位置已经被推出屏幕，现在显示的第一个view的位置发生了改变，并且向上滑动
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {//RecyclerView停止滑动
                        //在新的位置播放新的视频,播放下一个视频
                        playVideoByPosition(position);
                    }
                    //当前RecyclerView第一个显示的ItemView必须全部显示完整，才开始播放
                } else if (position<currentPosition && firstView.getY()==0) {
                    //在新的位置播放新的视频，播放上一个视频
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {//RecyclerView停止滑动
                        //在新的位置播放新的视频,播放下一个视频
                        playVideoByPosition(position);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    //播放参数指定位置的视频
    private void playVideoByPosition(int playPosition) {
        View itemView = slideRv.getLayoutManager().findViewByPosition(playPosition);
        final IjkVideoView ijkVideoView = itemView.findViewById(R.id.ijkVideoView);//我找到，当前播放的位置的播放器

        //首先停掉之前播放的视频
        if (currentVideoView != null) {
            currentVideoView.stopPlayback();//停掉
            currentVideoView.setVisibility(View.GONE);
            currentVideoView = null;
        }


        //设置播放的视频地址
        ijkVideoView.setVideoURI(Uri.parse(Constant.BASE_RESOURCE_URL+slideVideoViewAdapter.getItem(playPosition).getVedioUrl()));
        ijkVideoView.setVisibility(View.VISIBLE);
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkVideoView.start();
            }
        });

        currentVideoView = ijkVideoView;
        currentPosition = playPosition;
    }


    public static void launch(Activity activity, int position) {
        Intent intent = new Intent();
        intent.setClass(activity,SlideVideoViewActivity.class);
        intent.putExtra("position", position);
        activity.startActivity(intent);
    }

    @Override
    protected void resume() {
        super.resume();

        if (currentVideoView!=null) {
            currentVideoView.start();
        }
    }

    @Override
    protected void pause() {
        super.pause();

        if (currentVideoView!=null && currentVideoView.isPlaying()) {
            currentVideoView.pause();
        }
    }

    @Override
    protected void destroy() {
        if (currentVideoView!=null) {
            currentVideoView.stopPlayback();
            currentVideoView=null;
        }
    }
}
