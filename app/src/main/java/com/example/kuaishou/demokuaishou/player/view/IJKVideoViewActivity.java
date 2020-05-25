package com.example.kuaishou.demokuaishou.player.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.widget.IjkVideoView;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.base.BaseActivity;
import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.common.Constant;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.player.mode.GiftBean;
import com.example.kuaishou.demokuaishou.player.mode.OrderInfoBean;
import com.example.kuaishou.demokuaishou.player.mode.UpdataMoneyBean;
import com.example.kuaishou.demokuaishou.player.presenter.IJKVideoViewContract;
import com.example.kuaishou.demokuaishou.player.presenter.IjkPresenterImpl;
import com.example.kuaishou.demokuaishou.user.KSUserManager;

import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

//需要和服务端交互，所以使用MVP框架
public class IJKVideoViewActivity extends BaseActivity<IJKVideoViewContract.IjkPresenter, IJKVideoViewContract.IIJKVideoView> implements SurfaceHolder.Callback, IJKVideoViewContract.IIJKVideoView, View.OnClickListener {
    private IjkVideoView ijkVideoView;
    private String videoUrl;
    private SurfaceView redSurfaceView;
    private SurfaceHolder surfaceHolder;
    private Display display;
    private ImageView giftAnimImage;

    //礼物
    private ImageView giftImage;
    private Path path = new Path();
    private PathMeasure pathMeasure;
    private float[] currentPosition = new float[2];//当前贝塞尔曲线上的坐标值
    private RelativeLayout rootView;

    private int[] startLocation = new int[2];//起始坐标
    private int[] endLocation = new int[2];//终点坐标
    private int[] controlLoaction1 = new int[2];//控制坐标1
    private int[] controlLocation2 = new int[2];//控制坐标2

    //定义两个坐标变量
    private int x = 0;
    private int y = 0;
    private long time = 0;

    private PopupWindow popupWindow;
    private GiftAdapter giftAdapter;

    private int addMoneyValue;

    @Override
    protected void initPresenter() {
        httpPresenter = new IjkPresenterImpl();
    }

    @Override
    protected void initData() {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//设置沙箱环境.
    }

    @Override
    protected void initView() {
        videoUrl = getIntent().getStringExtra("videoUrl");
        rootView = findViewById(R.id.rootView);
        giftImage = findViewById(R.id.giftImage);

        giftImage.setOnClickListener(this);
        giftAnimImage = findViewById(R.id.giftAnimImage);

        initIJKVideoView();
        initRedSurfaceView();

        //获取描述当前页面窗口的对象
        display = getWindowManager().getDefaultDisplay();
        //animationHandler.sendEmptyMessageDelayed(1,1000);

        initPopupWindow();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ijkvideoview;
    }

    private Handler animationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startGiftAnimation();
            animationHandler.sendEmptyMessageDelayed(1,1000);
        }
    };

    private void startGiftAnimation() {
        //初始化平移的控件
        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.red);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,120);
        imageView.setLayoutParams(layoutParams);
        rootView.addView(imageView);


        giftImage.getLocationInWindow(startLocation);//系统给起始坐标赋值

        endLocation[0] = startLocation[0];
        endLocation[1] = startLocation[1]-1200;

        controlLoaction1[0] = startLocation[0] - 200;
        controlLoaction1[1] = startLocation[1] - 400;

        controlLocation2[0] = startLocation[0] + 200;
        controlLocation2[1] = startLocation[1] - 800;


        path.moveTo(startLocation[0],startLocation[1]);
        path.cubicTo(controlLoaction1[0],controlLoaction1[1],controlLocation2[0],controlLocation2[1],endLocation[0],endLocation[1]);

        pathMeasure = new PathMeasure(path, false);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());//实例化属性动画类，并且告诉属性动画，平移的长度
        valueAnimator.setDuration(10000);//动画持续时间
        //属性动画执行时，不停的调用该回调方法，在该方法中，去设置属性动画的属性值，让属性动画，不停的按照属性值执行动画
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value,currentPosition,null);//是为了获取下次坐标点
                imageView.setTranslationX(currentPosition[0]);
                imageView.setTranslationY(currentPosition[1]);
                long time = animation.getCurrentPlayTime();//代表已经动画执行的时间
                float percent = (float) (time/10000.0);//算出比例关系

                imageView.setScaleX(percent);//按照比例关系去缩放我们的控件，开始时控件很小，后来变大
                imageView.setScaleY(percent);
                imageView.setAlpha(1-percent);//开始图片清晰，后来变消失
            }
        });

        valueAnimator.start();
    }

    private void initRedSurfaceView() {
        redSurfaceView = findViewById(R.id.redSurfaceView);
        redSurfaceView.setZOrderOnTop(true);//将surfaceView放到IJKVideoVIew的上层
        redSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);//将SurfaceView的背景设置成透明
        surfaceHolder = redSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    private void initIJKVideoView() {
        ijkVideoView = findViewById(R.id.ijkVideoView);
        //设置播放地址
        ijkVideoView.setVideoURI(Uri.parse(videoUrl));
        //设置准备的监听，当准备好之后，开始播放
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkVideoView.start();
            }
        });
    }

    @Override
    protected void resume() {
        ijkVideoView.onResume();
    }

    @Override
    protected void pause() {
        ijkVideoView.onPause();
    }

    @Override
    protected void destroy() {
        ijkVideoView.stopPlayback();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        //设置touch事件,通过touch事件，获取用户点击的X，Y坐标
        redSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//获取用户点击的事件
                    //是不是双击
                    if (time == 0) {
                        time = System.currentTimeMillis();
                    } else {
                        //是双击
                        if (System.currentTimeMillis() - time < 200) {
                            x = (int) event.getX();
                            y = (int) event.getY();
                            //绘制小红心
                            drawRed();
                            //调用服务端，去告诉服务端，你对该主播的当前小视频表示点赞
                            handler.sendEmptyMessageDelayed(0, 500);

                        } else {//不是双击
                            time = System.currentTimeMillis();
                        }
                    }



                }
                return true;//返回true，代表这个点击事件，将由surfaceView来处理，事件分发到此结束
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            clearSurfaceView();
        }
    };


    private void clearSurfaceView() {
        Canvas canvas = surfaceHolder.lockCanvas();//锁定surfaceView的画布
        //声明一个画笔
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//清空屏幕
        paint.setColor(Color.TRANSPARENT);

        //定义一个矩形,这个矩形代表的是我们清空的大小
        RectF rectF = new RectF();
        rectF.set(0, 0,display.getWidth(),display.getHeight());
        canvas.drawRect(rectF,paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawRed() {
        //绘制
        Canvas canvas = surfaceHolder.lockCanvas();//锁定surfaceView的画布

        //声明一个画笔
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//清空屏幕
        paint.setColor(Color.TRANSPARENT);

        //定义一个矩形,这个矩形代表的是我们清空的大小
        RectF rectF = new RectF();
        rectF.set(0, 0,display.getWidth(),display.getHeight());
        canvas.drawRect(rectF,paint);

        //绘制红心
        //把红色的心脏的图片生成Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.red);
        canvas.drawBitmap(bitmap, x,y,null);
        //释放画布
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private boolean isAdd = false;
    private int clickGiftPosition =0;
    @Override
    public void onUpdataMoneyData(UpdataMoneyBean updataMoneyBean) {
        if (isAdd) {
            Toast.makeText(this, "增加虚拟币成功:" + updataMoneyBean.getResult(), Toast.LENGTH_SHORT).show();
        } else {
            showGiftAnim(clickGiftPosition);
            Toast.makeText(this, "购物礼物成功:" + updataMoneyBean.getResult(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onOrderInfo(final OrderInfoBean orderInfoBean) {
        //第二步 获取了订单信息，去调用支付宝完成支付
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask payTask = new PayTask(IJKVideoViewActivity.this);
                //第三步调用支付宝API去完成支付
                Map<String,String> result = payTask.payV2(orderInfoBean.getResult().getOrderInfo(), true);
                //判断是否支付成功
                //第四步，支付宝通过返回值知会我们支付成功
                if (result.get("resultStatus").equals("9000")) {
                    //第5步，到我们的服务端去更新虚拟币
                    updateMyMoney();
                }

            }
        });

        thread.start();


    }

    private void updateMyMoney() {
        httpPresenter.updateMoney(String.valueOf(addMoneyValue));

    }

    @Override
    public void showError(ErrorBean errorBean) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public static void launch(Activity activity, String videoUrl) {
        Intent intent = new Intent();
        intent.putExtra("videoUrl", videoUrl);
        intent.setClass(activity, IJKVideoViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.giftImage) {
            showGiftPopupWindow();
        }
    }

    private void initPopupWindow() {
        View giftPopupView = LayoutInflater.from(this).inflate(R.layout.gift_popup_window,null,false);
        popupWindow = new PopupWindow(giftPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        GridView gridView = giftPopupView.findViewById(R.id.giftGridView);
        giftAdapter = new GiftAdapter();
        gridView.setAdapter(giftAdapter);
        giftAdapter.updateData(CacheManager.getInstance().getGiftDataList());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //展示礼物
                popupWindow.dismiss();
                clickGiftPosition = position;

                //检查资金是否充足,个人账户的钱在什么地方存着呢?
                int giftValue = Integer.parseInt(((GiftBean.ResultBean)(giftAdapter.getItem(position))).getPrice());
                int myMoneyValue = (KSUserManager.getInstance().getMoney() == null)?0:Integer.parseInt(KSUserManager.getInstance().getMoney());
                if (myMoneyValue < giftValue) {//资金不足
                    //去充值
                    isAdd = true;
                    addMoneyValue = giftValue-myMoneyValue;
                    addMyMoney(giftValue-myMoneyValue);
                } else {
                    //资金充足,可以发送礼物了
                    isAdd = false;
                    httpPresenter.updateMoney(String.valueOf(myMoneyValue-giftValue));
                }

            }
        });
    }

    private void showGiftAnim(int position) {
         giftAnimImage.setVisibility(View.VISIBLE);
                giftAnimImage.setBackgroundColor(Color.TRANSPARENT);
                Glide.with(IJKVideoViewActivity.this).load(Constant.BASE_RESOURCE_URL
                        +((GiftBean.ResultBean)(giftAdapter.getItem(position))).getGif_file()).into(giftAnimImage);
    }
    private void addMyMoney(int addMoneyValue) {
        //去充值
        //第一步先下订单
        httpPresenter.getOrderInfo("buy ks bi", String.valueOf(addMoneyValue));
    }

    private void showGiftPopupWindow() {
        popupWindow.showAtLocation(rootView,Gravity.BOTTOM, 0,0);
    }
}
