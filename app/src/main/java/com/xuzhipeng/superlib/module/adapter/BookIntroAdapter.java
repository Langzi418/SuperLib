package com.xuzhipeng.superlib.module.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.model.BookIntro;
import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class BookIntroAdapter extends BaseQuickAdapter<BookIntro, BaseViewHolder> {

    private Context mContext;

    public BookIntroAdapter(@LayoutRes int layoutResId, @Nullable List<BookIntro> data,
                            Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BookIntro item) {
        helper.setText(R.id.name_and_index_tv, item.getNameIndex())
                .setTextColor(R.id.name_and_index_tv,
                        ContextCompat.getColor(mContext, R.color.blue_light))
                .setText(R.id.author_tv, item.getAuthor())
                .setText(R.id.store_tv, item.getStore())
                .setText(R.id.press_and_year_tv, item.getPressYear())
                .setText(R.id.loanable_tv, item.getLoanable());

        String loanable = item.getLoanable();
        if (loanable != null) {
            String[] cnts = loanable.split("：");
            int cnt = Integer.parseInt(cnts[cnts.length - 1]);
            if (cnt > 0) {
                helper.setTextColor(R.id.loanable_tv,
                        ContextCompat.getColor(mContext, R.color.green_light));
            } else {
                helper.setTextColor(R.id.loanable_tv,
                        ContextCompat.getColor(mContext, R.color.secondary_text_dark));
            }
        }
    }
}
