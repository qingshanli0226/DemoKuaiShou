package com.example.kuaishou.demokuaishou.player.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;

import java.util.ArrayList;
import java.util.List;

//它管理的数据和首页的数据是一致的，因为它滑动播放的数据就是我们首页数据
public class SlideVideoViewAdapter extends RecyclerView.Adapter<SlideVideoViewAdapter.SlideVideoViewHolder> {
    private List<FindVideoBean.ResultBean> data=new ArrayList<>();
    public void updateData(List<FindVideoBean.ResultBean> dataList) {
        data.clear();
        data.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SlideVideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_slide_video_view,viewGroup,false);
        return new SlideVideoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SlideVideoViewHolder findVideoViewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class SlideVideoViewHolder extends RecyclerView.ViewHolder {
        public IjkVideoView ijkVideoView;
        public SlideVideoViewHolder(View rootView) {
            super(rootView);

            ijkVideoView = rootView.findViewById(R.id.ijkVideoView);
        }
    }
}
