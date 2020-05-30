package com.example.kuaishou.demokuaishou.search.mode;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

//存储搜索历史记录的表
@Entity
public class SearhEntity {

    @Id(autoincrement = true)
    Long id;

    String searchContent;

    long time;//时间戳搜索的历史记录按照时间排序

    @Generated(hash = 151422984)
    public SearhEntity(Long id, String searchContent, long time) {
        this.id = id;
        this.searchContent = searchContent;
        this.time = time;
    }

    @Generated(hash = 106191579)
    public SearhEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchContent() {
        return this.searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
