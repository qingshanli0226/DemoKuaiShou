package com.example.kuaishou.demokuaishou.search.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class SearchRecyclerViewManager extends LinearLayoutManager {
    public SearchRecyclerViewManager(Context context) {
        super(context);
    }

    public SearchRecyclerViewManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SearchRecyclerViewManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //能不能垂直滑动
    @Override
    public boolean canScrollVertically() {
        return false;//如果返回false，那么RecyclerView将不能滑动
    }
}
