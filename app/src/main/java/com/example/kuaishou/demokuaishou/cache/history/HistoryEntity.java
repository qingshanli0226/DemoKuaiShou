package com.example.kuaishou.demokuaishou.cache.history;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

//在数据库中会创建一个表
@Entity
public class HistoryEntity {

    @Id(autoincrement = true)
    Long id;//类型一定是Long不是long

    private String coverImage;//定义该表的字段
    private String videoUrl;
    private String userId;
    @Generated(hash = 1693640769)
    public HistoryEntity(Long id, String coverImage, String videoUrl,
            String userId) {
        this.id = id;
        this.coverImage = coverImage;
        this.videoUrl = videoUrl;
        this.userId = userId;
    }
    @Generated(hash = 1235354573)
    public HistoryEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCoverImage() {
        return this.coverImage;
    }
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
    public String getVideoUrl() {
        return this.videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
