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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dou361.ijkplayer.widget.IjkVideoView;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.base.BaseMVPActivity;
import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.common.Constant;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.player.mode.GiftBean;
import com.example.kuaishou.demokuaishou.player.mode.OrderInfoBean;
import com.example.kuaishou.demokuaishou.player.mode.UpdataMoneyBean;
import com.example.kuaishou.demokuaishou.player.presenter.IJKVideoViewContract;
import com.example.kuaishou.demokuaishou.player.presenter.IjkPresenterImpl;
import com.example.kuaishou.demokuaishou.user.KSUserManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

//需要和服务端交互，所以使用MVP框架
public class IJKVideoViewMVPActivity extends BaseMVPActivity<IJKVideoViewContract.IjkPresenter, IJKVideoViewContract.IIJKVideoView> implements SurfaceHolder.Callback, IJKVideoViewContract.IIJKVideoView, View.OnClickListener, KSUserManager.IMoneyValueChangedListener {
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
    private TextView moneyTv;


    private boolean isHasStart = false;//当前应用是否之前已经启动过
    private int currentPlayPosition = 0;//代表当前播放器播放的位置
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    private View littleRootView;

    private IjkVideoView littleIjk;

    @Override
    protected void initPresenter() {
        httpPresenter = new IjkPresenterImpl();
    }

    @Override
    protected void initData() {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//设置沙箱环境.

        KSUserManager.getInstance().registerMoneyListener(this);
    }

    @Override
    protected void initView() {
        videoUrl = getIntent().getStringExtra("videoUrl");
        rootView = findViewById(R.id.rootView);
        giftImage = findViewById(R.id.giftImage);
        rootView.setOnClickListener(this);

        giftImage.setOnClickListener(this);
        giftAnimImage = findViewById(R.id.giftAnimImage);


        initIJKVideoView();
        initRedSurfaceView();
        SurfaceView testS = findViewById(R.id.testSurface);
        testS.getHolder().setFormat(PixelFormat.TRANSPARENT);
        testS.setZOrderOnTop(true);
        testS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ijkVideoView.isPlaying()) {//播放器正在播放，停止播放
                    ijkVideoView.pause();
                } else {//如果不处于播放状态，就继续播放
                   ijkVideoView.start();
                }
            }
        });

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
        super.resume();
        if (isHasStart) {//切到后台，重新显示，继续从之前的位置开始播放
            ijkVideoView.setVideoURI(Uri.parse(videoUrl));
            ijkVideoView.setRender(ijkVideoView.RENDER_TEXTURE_VIEW);
            ijkVideoView.seekTo(currentPlayPosition);
        }

        isHasStart = true;//当前页面已经启动ok了
    }

    //该标记的作用，来标记当前是否是我们主动关掉的页面
    private boolean isBack = false;
    @Override
    public void onBackPressed() {
        isBack = true;
        super.onBackPressed();

    }

    @Override
    protected void pause() {
        super.pause();
        currentPlayPosition = ijkVideoView.getCurrentPosition();
        ijkVideoView.stopPlayback();

        //只有当不是我们主动关掉该页面时，当页面切到后台时，立即显示一个小窗口，继续播放视频
        if (!isBack) {
            displayPlayLittleWindow();
        }
    }

    private void displayPlayLittleWindow() {
        //创建小窗口去播放视频
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //用来设置创建窗口的参数
        layoutParams = new WindowManager.LayoutParams();
        //设置窗口的类型,该窗口类型是系统级别,系统级别的窗口，可以显示任何页面的上方。而应用级别的窗口只能显示在当前页面的上方，例如PopupWindow，Dialog
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//这个需要在清单文件里添加权限

        layoutParams.format = PixelFormat.TRANSPARENT;//设置背景透明
        //设置的标记的意思是。在当前小窗口显示时，后面页面的控件依然可以点击
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口的宽度和高度
        layoutParams.width = 300;
        layoutParams.height = 500;

        //设置窗口显示的布局
        littleRootView = LayoutInflater.from(this).inflate(R.layout.window_little,null);
        windowManager.addView(littleRootView, layoutParams);//将小窗口显示出来
        littleRootView.setOnTouchListener(littleOnTouchListener);

        littleIjk = littleRootView.findViewById(R.id.littleIjk);
        SurfaceView littleS = littleRootView.findViewById(R.id.littleS);
        littleS.setZOrderOnTop(true);
        littleS.getHolder().setFormat(PixelFormat.TRANSPARENT);
        //小窗口继续播放视频的内容
        littleIjk.setVideoURI(Uri.parse(videoUrl));
        littleIjk.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                littleIjk.start();
                littleIjk.seekTo(currentPlayPosition);//让播放器在指定的位置继续播放视频
            }
        });

        //点击关闭按钮，去关闭小窗口
        littleRootView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                littleIjk.stopPlayback();
                windowManager.removeView(littleRootView);
            }
        });
    }

    //定义一个OnTouchListener, 获取用户在屏幕上手指移动的坐标
    private View.OnTouchListener littleOnTouchListener = new View.OnTouchListener() {

        private float x = 0,y = 0;//记录手指在屏幕DOWN坐标
        private float littleX = 0, littleY = 0;//记录小窗口在手指DOWN时，在屏幕的坐标值


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getRawX();//获取当前点击的事件相对于屏幕的觉对坐标
                    y = event.getRawY();
                    littleX = layoutParams.x;
                    littleY = layoutParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:

                    float dx = (event.getRawX() - x);//计算手指在X轴的移动偏移量
                    float dy = (event.getRawY() - y);//计算手指在Y轴的移动偏移量

                    layoutParams.x = (int) (littleX + dx);
                    layoutParams.y = (int) (littleY + dy);

                    //更新小窗口在当前屏幕的位置
                    windowManager.updateViewLayout(littleRootView, layoutParams);

                    break;
                case MotionEvent.ACTION_UP:
                    //当手指滑动的距离小于10个像素时，认为是点击事件,否则认为是滑动事件
                    if (Math.abs(event.getRawX() -x) > 20 || Math.abs(event.getRawY()-y)>20) {
                    } else {
                        //现获取小窗口当前播放的位置
                        currentPlayPosition = littleIjk.getCurrentPosition();
                        littleIjk.stopPlayback();//关掉小窗口的播放器
                        windowManager.removeView(littleRootView);//从屏幕里删除小窗口
                        Intent intent = new Intent();//启动播放Activity
                        intent.setClass(IJKVideoViewMVPActivity.this, IJKVideoViewMVPActivity.class);
                        IJKVideoViewMVPActivity.this.startActivity(intent);//会执行activity的onResume函数,该函数会继续播放视
                    }
                    break;
                    default:
                        break;
            }
            return true;
        }
    };

    @Override
    protected void destroy() {
        ijkVideoView.stopPlayback();
        KSUserManager.getInstance().unRegisterMoneyListener(this);
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
                PayTask payTask = new PayTask(IJKVideoViewMVPActivity.this);
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
        intent.setClass(activity, IJKVideoViewMVPActivity.class);
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
        moneyTv = giftPopupView.findViewById(R.id.moneyTv);
        String moneyStr = String.valueOf(KSUserManager.getInstance().getMoney() == null?0:Integer.parseInt(KSUserManager.getInstance().getMoney()));
        moneyTv.setText(moneyStr);
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

    private String getGiftPath(int index) {
        String gifFilePath = null;
        switch (index) {
            case 0:
                gifFilePath = Constant.GIFF_1;
                break;
            case 1:
                gifFilePath = Constant.GIFF_2;
                break;
            case 2:
                gifFilePath = Constant.GIFF_3;
                break;
            case 3:
                gifFilePath = Constant.GIFF_4;
                break;
            case 4:
                gifFilePath = Constant.GIFF_5;
                break;
            case 5:
                gifFilePath = Constant.GIFF_6;
                break;
        }
        return gifFilePath;
    }

    private void showGiftAnim(int position) {
         giftAnimImage.setVisibility(View.VISIBLE);
                giftAnimImage.setBackgroundColor(Color.TRANSPARENT);
            /*    Glide.with(IJKVideoViewMVPActivity.this).load(Constant.BASE_RESOURCE_URL
                        +((GiftBean.ResultBean)(giftAdapter.getItem(position))).getGif_file()).listener(new RequestListener<Drawable>() {
     */   Glide.with(IJKVideoViewMVPActivity.this).load(getGiftPath(position)).listener(new RequestListener<Drawable>() {

            @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //执行一次加载gif动画，然后隐藏它
                        try {
                            Field gifStateField = GifDrawable.class.getDeclaredField("state");
                            gifStateField.setAccessible(true);
                            Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                            Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                            gifFrameLoaderField.setAccessible(true);
                            Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                            Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                            gifDecoderField.setAccessible(true);
                            Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                            Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                            Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                            getDelayMethod.setAccessible(true);
                            ////设置播放次数
                            ((GifDrawable) resource).setLoopCount(1);
                            //获得总帧数
                            int count = ((GifDrawable) resource).getFrameCount();
                            int delay = 0;
                            for (int i = 0; i < count; i++) {
                                //计算每一帧所需要的时间进行累加
                                delay += (int) getDelayMethod.invoke(gifDecoder, i);
                            }
                            rootView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    giftAnimImage.setVisibility(View.GONE);
                                }
                            },delay);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }).into(giftAnimImage);
    }
    private void addMyMoney(int addMoneyValue) {
        //去充值
        //第一步先下订单
        httpPresenter.getOrderInfo("buy ks bi", String.valueOf(addMoneyValue));
    }

    private void showGiftPopupWindow() {
        popupWindow.showAtLocation(rootView,Gravity.BOTTOM, 0,0);
    }

    @Override
    public void onMoneyChanged(String moneyValue) {
        if (moneyTv!=null) {
            moneyTv.setText(moneyValue);
        }
    }


}
