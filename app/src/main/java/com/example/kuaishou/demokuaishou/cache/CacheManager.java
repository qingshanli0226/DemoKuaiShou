package com.example.kuaishou.demokuaishou.cache;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.example.kuaishou.demokuaishou.cache.dao.DaoMaster;
import com.example.kuaishou.demokuaishou.cache.dao.DaoSession;
import com.example.kuaishou.demokuaishou.cache.dao.HistoryEntityDao;
import com.example.kuaishou.demokuaishou.cache.dao.SearhEntityDao;
import com.example.kuaishou.demokuaishou.cache.history.HistoryEntity;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;
import com.example.kuaishou.demokuaishou.player.mode.GiftBean;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CacheManager {

    private IHomeDataListener iHomeDataListener;

    private FindVideoBean findVideoBean;//首页数据在内存中的缓存
    private ACache aCache;//存储对象.
    private final String HOME_DATA="homeData";
    private final String DB_NAME="ks.db";
    private HistoryEntityDao historyEntityDao;//该实例是实际操作数据库的对象

    private SearhEntityDao searhEntityDao;//对数据库中搜索历史记录操作的类

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String adrTimeName = "adrTime";

    //礼物缓存
    private GiftBean giftBean;

    private ExecutorService singleExecutorService = Executors.newSingleThreadExecutor();//使用线程池进行异步操作

    private static CacheManager instance;

    private CacheService cacheService;
    private boolean isDownloadGiftGif = false;//标志位，来判断当前应用程序是否已经下载了礼物gif图
    private int giftCount;
    private int downloadGiftIndex;

    public static CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences("adr", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //初始化首页数据
        aCache = ACache.get(context);
        getHomeDataFromAcache();//先从本地将首页的数据加载到内存中
        getHomeDataFromServer();//从服务端获取新的首页数据


        //获取礼物数据
        getGiftData();
        //绑定service
        bindDownloadGiftService(context);

        //初始化数据库
        DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        historyEntityDao = daoSession.getHistoryEntityDao();
        setSearhEntityDao(daoSession.getSearhEntityDao());
    }

    private void bindDownloadGiftService(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CacheService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                CacheService.CacheBinder cacheBinder = (CacheService.CacheBinder) service;
                cacheService = cacheBinder.getCacheService();
                cacheService.setiDownloadSuccessListener(iDownloadSuccessListener);
                if (!isDownloadGiftGif && giftBean!=null) {//当礼物没有下载，且获取的礼物bean数据已经准备好了，才可以下载礼物
                    giftCount = giftBean.getResult().size();
                    downloadAllGifts();

                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    private CacheService.IDownloadSuccessListener iDownloadSuccessListener = new CacheService.IDownloadSuccessListener() {
        @Override
        public void onFileDownloadSuceess(String fileName) {
             downloadGiftIndex++;
             if (downloadGiftIndex==giftCount) {
                 isDownloadGiftGif = true;
                 Log.d("LQS:", "所有的礼物文件都下载完毕");
                 return;
             }
             cacheService.downloadGiftGif(giftBean.getResult().get(downloadGiftIndex).getGif_file(),downloadGiftIndex);
        }
    };

    private void downloadAllGifts() {
         downloadGiftIndex = 0;
         cacheService.downloadGiftGif(giftBean.getResult().get(downloadGiftIndex).getGif_file(),0);//先下载第0个文件
    }

    //获取礼物缓存数据
    public List<GiftBean.ResultBean> getGiftDataList() {
        return giftBean.getResult();
    }

    private void getGiftData() {
        RetrofitCreator.getNetApiService().getGiftData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GiftBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GiftBean giftBean) {
                        if (giftBean.getCode() == 200) {
                            CacheManager.this.giftBean = giftBean;
                            if (!isDownloadGiftGif && cacheService!=null) {//当礼物没有下载，且获取的礼物cacheService已经准备好了，才可以下载礼物
                                giftCount = giftBean.getResult().size();
                                downloadAllGifts();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //去服务端获取数据，更新首页数据
    private void getHomeDataFromServer() {
        RetrofitCreator.getNetApiService().findVideo()
                .delay(3,TimeUnit.SECONDS)//模拟网络请求数据返回时间，并且给用户一种感觉数据刷新了
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FindVideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FindVideoBean findVideoBean) {
                        if (CacheManager.this.findVideoBean == null) {
                            CacheManager.this.findVideoBean = findVideoBean;
                        } else {
                            CacheManager.this.findVideoBean.getResult().addAll(0, findVideoBean.getResult());
                        }
                        saveHomeDataIntoAcache(CacheManager.this.findVideoBean);
                        //通知UI刷新数据
                        if (iHomeDataListener !=null) {
                            iHomeDataListener.onNewHomeDataReceivedFromeServer(CacheManager.this.findVideoBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //将从服务端获取的数据存入Acache中
    private void saveHomeDataIntoAcache(final FindVideoBean findVideoBean) {
        getSingleExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                aCache.put(HOME_DATA, findVideoBean);
            }
        });
    }

    //耗时操作放到子线程中
    private void getHomeDataFromAcache() {
        getSingleExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                findVideoBean = (FindVideoBean) aCache.getAsObject(HOME_DATA);
            }
        });
    }

    //获取首页数据
    public FindVideoBean getHomeData() {
        return findVideoBean;
    }

    public void setHomeDataListener(IHomeDataListener listener) {
        this.iHomeDataListener = listener;
    }
    public void removeHomeDataListener() {
        this.iHomeDataListener = null;
    }

    public SearhEntityDao getSearhEntityDao() {
        return searhEntityDao;
    }

    public void setSearhEntityDao(SearhEntityDao searhEntityDao) {
        this.searhEntityDao = searhEntityDao;
    }

    public ExecutorService getSingleExecutorService() {
        return singleExecutorService;
    }

    public void setSingleExecutorService(ExecutorService singleExecutorService) {
        this.singleExecutorService = singleExecutorService;
    }

    //定义接口，去通知页面有新的数据到来
    public interface IHomeDataListener {
        void onNewHomeDataReceivedFromeServer(FindVideoBean findVideoBean);
    }


    //添加浏览的历史记录
    public void addOneHistoryVideo(final HistoryEntity historyEntity) {
        getSingleExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                historyEntityDao.insert(historyEntity);
            }
        });
    }

    //删除浏览历史记录
    public void removeOneHistoryVideo(HistoryEntity historyEntity) {
        historyEntityDao.delete(historyEntity);
    }

    //修改浏览历史记录
    public void updateOneHistoryVideo(HistoryEntity historyEntity) {
        historyEntityDao.update(historyEntity);
    }

    //查询浏览的历史记录
    public List<HistoryEntity> getHistoryVideo() {
        return historyEntityDao.queryBuilder().list();
    }


    //存储当前广告时间, 在Activity的onPause中调用
    public void saveAdrTime(long time) {
        editor.putLong(adrTimeName, time);
        editor.commit();
    }

    //获取当前存储的广告时间,在Activity的onRsume中调用,来实现，当应用页面退到后台5秒钟之后，再次打开应用时，弹出中间广告
    public long getAdrTime() {
        return sharedPreferences.getLong(adrTimeName, -1);
    }


}
