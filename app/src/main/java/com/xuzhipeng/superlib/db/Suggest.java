package com.xuzhipeng.superlib.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/17
 * Desc:
 */

@Entity
public class Suggest {
    @org.greenrobot.greendao.annotation.Id(autoincrement = true)
    private Long Id;

    @Unique
    @NotNull
    private String name;

    private Long times;

    @Generated(hash = 1489203012)
    public Suggest(Long Id, @NotNull String name, Long times) {
        this.Id = Id;
        this.name = name;
        this.times = times;
    }

    @Generated(hash = 878441091)
    public Suggest() {
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimes() {
        return this.times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }



}
