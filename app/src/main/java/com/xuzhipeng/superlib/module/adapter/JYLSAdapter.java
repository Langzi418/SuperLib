package com.xuzhipeng.superlib.module.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.model.JYLS;
import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/16
 * Desc:
 */

public class JYLSAdapter extends BaseQuickAdapter<JYLS, BaseViewHolder> {
    public JYLSAdapter(@LayoutRes int layoutResId, @Nullable List<JYLS> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JYLS item) {
        helper.setText(R.id.item1, item.getName())
                .setText(R.id.item2_left_tv, item.getAuthor())
                .setText(R.id.item2_right_tv, item.getGcd())
                .setText(R.id.item3_left_tv, mContext.getString(R.string.lend_date
                        , item.getLendDate()))
                .setText(R.id.item3_right_tv, mContext.getString(R.string.return_date,
                        item.getReturnDate()));
    }
}
