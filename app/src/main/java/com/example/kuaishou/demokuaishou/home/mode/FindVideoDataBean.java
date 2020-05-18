package com.example.kuaishou.demokuaishou.home.mode;

public class FindVideoDataBean {

    /**
     * vedioUrl : http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4
     * vedioId : 1
     * userId : 10010
     * coverImg : http://image.tupian114.com/20121212/08002510.jpg
     */

    private String vedioUrl;
    private int vedioId;
    private int userId;
    private String coverImg;

    public String getVedioUrl() {
        return vedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }

    public int getVedioId() {
        return vedioId;
    }

    public void setVedioId(int vedioId) {
        this.vedioId = vedioId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}
