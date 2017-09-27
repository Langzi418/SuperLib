package com.xuzhipeng.superlib.module.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.model.HotBook;
import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/15
 * Desc:
 */

public class HotBookAdapter extends BaseQuickAdapter<HotBook,BaseViewHolder> {
    public HotBookAdapter(@LayoutRes int layoutResId, @Nullable List<HotBook> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotBook item) {
        helper.setText(R.id.item1,item.getName())
            .setText(R.id.item2_left_tv,item.getAuthor())
        .setText(R.id.item2_right_tv,item.getPressYears())
        .setText(R.id.item3_left_tv,item.getIndex())
        .setText(R.id.item3_right_tv,mContext.getString(R.string.page_views
                ,item.getViews()));
    }
}
