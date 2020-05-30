package com.example.kuaishou.demokuaishou.search.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.search.mode.SearhEntity;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<SearhEntity> searhEntityList = new ArrayList<>();

    public void updateData(List<SearhEntity> searhEntities) {
        searhEntityList = searhEntities;
        notifyDataSetChanged();
    }

    public void updateOneData(int index) {
        notifyDataSetChanged();
        //notifyItemInserted(index);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search,viewGroup,false);
        return new SearchViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder searchViewHolder, final int i) {

        searchViewHolder.contentTv.setText(searhEntityList.get(i).getSearchContent());
        searchViewHolder.contentTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CacheManager.getInstance().getSingleExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {
                        CacheManager.getInstance().getSearhEntityDao().delete(searhEntityList.get(i));
                        searhEntityList.remove(i);
                    }
                });
                notifyItemRemoved(i);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return searhEntityList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView contentTv;
        public SearchViewHolder(View rootView) {
            super(rootView);
            contentTv = rootView.findViewById(R.id.content);
        }
    }
}
