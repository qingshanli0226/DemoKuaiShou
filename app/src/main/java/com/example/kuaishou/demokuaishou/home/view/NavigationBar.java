package com.example.kuaishou.demokuaishou.home.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.kuaishou.demokuaishou.R;


//第二种自定义view，定义一个控件，继承系统的一种布局，来实现对系统控件的管理.该种自定义view在实际项目中使用最多
public class NavigationBar extends LinearLayout {
    RadioButton focusR;
    RadioButton homeR;
    RadioButton cityR;

    private ISelectedListener iSelectedListener;

    public NavigationBar(Context context) {
        super(context);
        initView(context);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NavigationBar(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSelectedColor(int index) {
        switch (index) {
            case 0:
                focusR.setBackgroundColor(Color.RED);
                homeR.setBackgroundColor(Color.WHITE);
                cityR.setBackgroundColor(Color.WHITE);
                break;
            case 1:
                focusR.setBackgroundColor(Color.WHITE);
                homeR.setBackgroundColor(Color.RED);
                cityR.setBackgroundColor(Color.WHITE);
                break;
            case 2:
                focusR.setBackgroundColor(Color.WHITE);
                homeR.setBackgroundColor(Color.WHITE);
                cityR.setBackgroundColor(Color.RED);
                break;
        }
    }

    public void setTabTile(String[] tabTile) {
        focusR.setText(tabTile[0]);
        homeR.setText(tabTile[1]);
        cityR.setText(tabTile[2]);
    }

    private void initView(Context context) {
        //使用布局来生成view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_navigation, this);//后面的参数一定是this
         focusR = findViewById(R.id.focus);
         homeR = findViewById(R.id.home);
         cityR = findViewById(R.id.city);
        final RadioGroup radioGroup = findViewById(R.id.bottomGroup);//可以直接使用findviewbyId
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = -1;
                switch (checkedId) {
                    case R.id.focus:
                        index = 0;
                        focusR.setBackgroundColor(Color.RED);
                        homeR.setBackgroundColor(Color.WHITE);
                        cityR.setBackgroundColor(Color.WHITE);
                        break;
                    case R.id.home:
                        focusR.setBackgroundColor(Color.WHITE);
                        homeR.setBackgroundColor(Color.RED);
                        cityR.setBackgroundColor(Color.WHITE);
                        index = 1;
                        break;
                    case R.id.city:
                        index = 2;
                        focusR.setBackgroundColor(Color.WHITE);
                        homeR.setBackgroundColor(Color.WHITE);
                        cityR.setBackgroundColor(Color.RED);
                        break;
                }

                 if (iSelectedListener!=null) {
                     iSelectedListener.onTabSelected(index);
                 }
            }
        });


    }

    public ISelectedListener getiSelectedListener() {
        return iSelectedListener;
    }

    public void setiSelectedListener(ISelectedListener iSelectedListener) {
        this.iSelectedListener = iSelectedListener;
    }


    public interface ISelectedListener {
        void onTabSelected(int index);
    }
}
