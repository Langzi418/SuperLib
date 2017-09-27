package com.xuzhipeng.superlib.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/20
 * Desc: 多对多连接
 */

@Entity
public class Collect {
    @Id
    private Long id;
    private Long userId;
    private Long bookId;
    private boolean like;
    @Generated(hash = 91901610)
    public Collect(Long id, Long userId, Long bookId, boolean like) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.like = like;
    }
    @Generated(hash = 1726975718)
    public Collect() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getBookId() {
        return this.bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public boolean getLike() {
        return this.like;
    }
    public void setLike(boolean like) {
        this.like = like;
    }


}
