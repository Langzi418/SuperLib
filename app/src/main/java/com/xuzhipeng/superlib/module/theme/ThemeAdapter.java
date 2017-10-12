package com.xuzhipeng.superlib.module.theme;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.superlib.R;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/10/11
 * Desc:
 */

public class ThemeAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public ThemeAdapter(@LayoutRes int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.getView(R.id.instance_iv).setBackgroundColor(
                ContextCompat.getColor(mContext, item));
    }
}
