package com.example.kuaishou.demokuaishou.cache;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MessageEntity {
    @Id(autoincrement = true)
    private Long id;

    private String type;

    @Generated(hash = 1751223368)
    public MessageEntity(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    @Generated(hash = 1797882234)
    public MessageEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
