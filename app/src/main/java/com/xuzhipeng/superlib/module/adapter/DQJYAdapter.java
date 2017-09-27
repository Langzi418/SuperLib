package com.xuzhipeng.superlib.module.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.model.DQJY;
import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/16
 * Desc:
 */

public class DQJYAdapter extends BaseQuickAdapter<DQJY, BaseViewHolder> {
    public DQJYAdapter(@LayoutRes int layoutResId, @Nullable List<DQJY> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DQJY item) {
        helper.setText(R.id.book_name_tv, item.getBookName())
                .setText(R.id.book_jhx_tv, item.getBookJHX())
                .setText(R.id.book_gcd_tv, item.getBookGCD())
                .addOnClickListener(R.id.renew_button);
    }
}
