package com.xuzhipeng.superlib.base;

import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/19
 */

public class MyFragmentPagerAdapter extends FragmentPagerItemAdapter {
    public MyFragmentPagerAdapter(FragmentManager fm, FragmentPagerItems pages) {
        super(fm, pages);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
