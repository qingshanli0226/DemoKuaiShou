package com.example.kuaishou.demokuaishou.cache;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.common.Constant;
import com.example.kuaishou.demokuaishou.net.RetrofitCreator;
import com.example.kuaishou.demokuaishou.player.mode.GiftBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CacheService extends Service {
    private IDownloadSuccessListener iDownloadSuccessListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public IDownloadSuccessListener getiDownloadSuccessListener() {
        return iDownloadSuccessListener;
    }

    public void setiDownloadSuccessListener(IDownloadSuccessListener iDownloadSuccessListener) {
        this.iDownloadSuccessListener = iDownloadSuccessListener;
    }

    //定义一个CacheBinder，返回service实例
    public class CacheBinder extends Binder {
        public CacheService getCacheService() {
            return CacheService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CacheBinder();
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

    //下载礼物Gif文件,目的是，当用户购买礼物后，能够最快的速度看到礼物动画的播放
    public void downloadGiftGif(String fileUrl, int index) {
        final String giftFilePath = getGiftPath(index);
        File file = new File(giftFilePath);
        if (file.exists()){//先删掉
            file.delete();
        }
        RetrofitCreator.getNetApiService().downloadFile(Constant.BASE_RESOURCE_URL+fileUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        byte[] buffer = new byte[1024];//定义数组
                        InputStream inputStream= responseBody.byteStream();//输入流
                        FileOutputStream fileOutputStream = null;
                        try {
                            fileOutputStream = new FileOutputStream(giftFilePath);
                            int length = 0;
                            while ((length = inputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, length);
                            }
                            //通知，文件下载完毕
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fileOutputStream !=null) {
                                try {
                                    fileOutputStream.flush();
                                    fileOutputStream.close();

                                    Log.d("LQS: ","文件下载成功:" + giftFilePath);
                                    if (iDownloadSuccessListener!=null) {
                                        iDownloadSuccessListener.onFileDownloadSuceess(giftFilePath);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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

    public interface IDownloadSuccessListener{
        void onFileDownloadSuceess(String fileName);
    }
}
