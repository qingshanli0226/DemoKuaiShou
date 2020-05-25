package com.example.kuaishou.demokuaishou.home.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.common.Constant;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.player.view.IJKVideoViewActivity;

import java.util.ArrayList;
import java.util.List;

public class FindVideoAdapter extends RecyclerView.Adapter<FindVideoAdapter.FindVideoViewHolder> {
    private List<FindVideoBean.ResultBean> data=new ArrayList<>();
    public void updateData(List<FindVideoBean.ResultBean> dataList) {
        data.clear();
        data.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FindVideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_find_video,viewGroup,false);
        return new FindVideoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FindVideoViewHolder findVideoViewHolder, final int position) {

        findVideoViewHolder.videoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IJKVideoViewActivity.launch((Activity)(findVideoViewHolder.videoImg.getContext()),
                        Constant.BASE_RESOURCE_URL+data.get(position).getVedioUrl());
            }
        });
        Glide.with(findVideoViewHolder.videoImg.getContext()).load(data.get(position).getCoverImg()).into(findVideoViewHolder.videoImg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class FindVideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView videoImg;
        public FindVideoViewHolder(View rootView) {
            super(rootView);

            videoImg = rootView.findViewById(R.id.videoImage);
        }
    }
}
