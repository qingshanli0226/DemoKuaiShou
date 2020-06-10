package com.example.kuaishou.demokuaishou.record.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.logging.LogRecord;


//实现录制时间的显示,第一种类型的自定义view，我们写一个控件，去继承view，并且实现onDraw方法，绘制相应的图形
public class RecordTimeView extends View {
    private RectF rectF;
    private float startAngle;
    private float stepAngle;//每秒增加的角度
    private float offsetAngle;//相对于起始角度的偏移量
    private Paint paint;
    private float recordedTime;

    private int left, top, right,bottom;//绘制区域的坐标

    private IRecordTimeEndListener iRecordTimeEndListener;//定义一个Listener，当录制时间结束后，通知Activity，停止录制视频
    public RecordTimeView(Context context) {
        super(context);
        initView(context, null);
    }

    public RecordTimeView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
        initCord();
    }


    public RecordTimeView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs) {
        //先定义参数，1，定义一个绘制扇形的矩形
        //2,定义一个扇形的绘制起始角度
        startAngle = -90.0f;
        //3,定义一个画笔
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不是填充的，只绘制边缘部分
        paint.setStrokeWidth(2);
        //定义每秒增加的角度。因为绘制是动态的，扇形是逐渐加大的，这个变量代表的就是每次增加的角度值
        stepAngle = 360.0f / 110.0f;
        //定义偏移量的初始值
        offsetAngle = 0;
    }




    //初始化绘制区域的坐标
    private void initCord() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                left = getMeasuredWidth()/2 - 150;
                top = getMeasuredHeight()/2 -150;
                right = getMeasuredWidth()/2 + 150;
                bottom = getMeasuredHeight()/2 + 150;
                rectF = new RectF(left, top, right, bottom);
                RecordTimeView.this.invalidate();
            }
        }, 500);//加延迟的目的，确保view在系统中已经绘制完毕，如果不加延迟的话，这个的尺寸的获取是不对的

    }

    public void startRecord() {
        handler.sendEmptyMessageDelayed(1,100);
        recordedTime = 0;
        if (iRecordTimeEndListener!=null) {
            iRecordTimeEndListener.onRecordTimeUpdate(recordedTime+"");
        }
    }

    public void stopRecord() {
        handler.removeCallbacksAndMessages(null);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rectF == null) {
            return;
        }
        canvas.drawRect(rectF, paint);
        canvas.drawArc(rectF, startAngle, offsetAngle, false, paint);
        offsetAngle = offsetAngle + stepAngle;//每次绘制完后，将扇形的绘制的偏移角度增加一个幅度
    }

    //通过Handler逐步将扇形绘制成圆形
    private Handler handler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecordTimeView.this.invalidate();//触发新的绘制,执行onDraw函数

            recordedTime+= 0.1;

            //增加绘制角度的偏移量
            if (offsetAngle >= 360) {
                if (iRecordTimeEndListener!=null) {
                    iRecordTimeEndListener.onRecordTimeEnd();
                }
                return;//如果已经绘制完毕，直接结束
            } else {
                if (iRecordTimeEndListener!=null) {
                    iRecordTimeEndListener.onRecordTimeUpdate(recordedTime+"");
                }
                handler.sendEmptyMessageDelayed(1,100);
            }
        }
    };

    public IRecordTimeEndListener getiRecordTimeEndListener() {
        return iRecordTimeEndListener;
    }

    public void setiRecordTimeEndListener(IRecordTimeEndListener iRecordTimeEndListener) {
        this.iRecordTimeEndListener = iRecordTimeEndListener;
    }


    public interface IRecordTimeEndListener{
        void onRecordTimeEnd();
        void onRecordTimeUpdate(String time);
    }
}
