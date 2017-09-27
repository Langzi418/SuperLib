package com.xuzhipeng.superlib.module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagsAdapter;
import com.xuzhipeng.superlib.address.Address;
import com.xuzhipeng.superlib.model.HotSearch;
import com.xuzhipeng.superlib.module.intro.BookIntroActivity;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class HotSearchAdapter extends TagsAdapter {


    private List<HotSearch> hotSearches;
    private Context mContext;

    public HotSearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setNewData(List<HotSearch> hotSearches) {
        this.hotSearches = hotSearches;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return hotSearches == null ? 0 : hotSearches.size();
    }

    @Override
    public View getView(Context context, final int position, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(hotSearches.get(position).getText());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(BookIntroActivity.
                        newIntent(mContext,
                                Address.getOpac()+ hotSearches.get(position).getUrl(), 20));
            }
        });
        return view;
    }

    @Override
    public Object getItem(int position) {
        return hotSearches.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position % 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
