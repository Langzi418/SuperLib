package com.xuzhipeng.superlib.module.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.model.DouComment;
import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/13
 * Desc:
 */

public class DouCmtAdapter extends BaseQuickAdapter<DouComment, BaseViewHolder> {
    public DouCmtAdapter(@LayoutRes int layoutResId, @Nullable List<DouComment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DouComment item) {
        helper.setText(R.id.comment_title_tv, item.getTitle())
                .setText(R.id.comment_summary_tv, item.getSummary())
                .setText(R.id.comment_update_tv, item.getUpdated());
    }
}
