package com.example.kuaishou.demokuaishou.home.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.home.mode.FocusVideoBean;

import java.util.ArrayList;
import java.util.List;

public class FocusVideoAdapter extends RecyclerView.Adapter<FocusVideoAdapter.FocusVideoViewHolder> {
    private List<FocusVideoBean.ResultBean> data=new ArrayList<>();
    public void updateData(List<FocusVideoBean.ResultBean> dataList) {
        data.clear();
        data.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FocusVideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_find_video,viewGroup,false);
        return new FocusVideoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull FocusVideoViewHolder focusVideoViewHolder, int position) {
        Glide.with(focusVideoViewHolder.videoImg.getContext()).load(data.get(position).getCoverImg()).into(focusVideoViewHolder.videoImg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class FocusVideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView videoImg;
        public FocusVideoViewHolder(View rootView) {
            super(rootView);

            videoImg = rootView.findViewById(R.id.videoImage);
        }
    }
}
