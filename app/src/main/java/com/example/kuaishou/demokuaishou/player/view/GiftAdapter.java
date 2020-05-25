package com.example.kuaishou.demokuaishou.player.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.common.Constant;
import com.example.kuaishou.demokuaishou.player.mode.GiftBean;

import java.util.ArrayList;
import java.util.List;

public class GiftAdapter extends BaseAdapter {
    private List<GiftBean.ResultBean> giftData = new ArrayList<>();

    public void updateData(List<GiftBean.ResultBean> giftDatas) {
        this.giftData.clear();
        this.giftData.addAll(giftDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return giftData.size();
    }

    @Override
    public Object getItem(int position) {
        return giftData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent,false);
        }
        ImageView giftImageView = convertView.findViewById(R.id.giftImagePop);
        String imageUrl = Constant.BASE_RESOURCE_URL+giftData.get(position).getGif_url();
        Glide.with(parent.getContext()).load(imageUrl).into(giftImageView);

        return convertView;
    }
}
