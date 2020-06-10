package com.example.kuaishou.demokuaishou.search.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

//第三种类型的自定义view，我们实现一个控件，该控件继承，系统一个控件，通过重写系统控件的方法，来实现增加或减少系统控件的功能.
public class SearchRecyclerView extends RecyclerView {
    private boolean isDisplayAll = false;//是否全部显示
    public SearchRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SearchRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //重写该方法，目的是控制RecyclerView列表显示的内容
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        int itemCount = getAdapter().getItemCount();

        //将列表中的内容全部平铺开,列表在ScrollView中,假如说，listview中有100条数据的话，这100条在Scrollview中全部展示完
        if (isDisplayAll) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
            super.onMeasure(widthSpec,expandSpec);
        } else {
            super.onMeasure(widthSpec, heightSpec);
            if (itemCount == 0) {
                setMeasuredDimension(widthSpec, 0);//当不是全部显示所有数据时，只显示两条数据
            } else if (itemCount == 1) {
                setMeasuredDimension(widthSpec, 100);//当不是全部显示所有数据时，只显示两条数据
            } else {
                setMeasuredDimension(widthSpec, 200);//当不是全部显示所有数据时，只显示两条数据
            }
        }
    }

    public boolean isDisplayAll() {
        return isDisplayAll;
    }

    public void setDisplayAll(boolean displayAll) {
        isDisplayAll = displayAll;
        requestLayout();//这个方法，可以去触发onMeasure方法的执行
    }
}
