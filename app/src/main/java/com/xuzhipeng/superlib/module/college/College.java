package com.xuzhipeng.superlib.module.college;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/25
 * Desc:
 */

public class College implements MultiItemEntity{
    public String name;
    public String base;
    public int type;

    @Override
    public int getItemType() {
        return type;
    }
}
