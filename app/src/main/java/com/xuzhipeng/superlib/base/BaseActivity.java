package com.xuzhipeng.superlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.view.ILoadView;


/**
 * Author: xuzhipeng
 * Email: langzi_xzp@foxmail.com
 * Date: 2017/7/7
 */

public abstract class BaseActivity extends AppCompatActivity
        implements ILoadView {

    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        //        setStatusBar();
        getExtra();
        setView();
        setListener();
        initData();
        restoreData(savedInstanceState);
    }


    /**
     * 设置App主题
     */
    private void setAppTheme() {
        int themeId = PrefUtil.getThemeId();
        setTheme(themeId);
    }

    /**
     * 初始设置布局
     */
    protected void setView() {

    }


    /**
     * 设置数据
     */
    protected void initData() {

    }

    /**
     * 获取intent 数据
     */
    protected void getExtra() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }


    /**
     * 恢复数据
     */
    protected void restoreData(Bundle savedInstanceState) {

    }




    /**
     * 工具栏菜单项选择
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showProgress() {
        dialog = ViewUtil.getProgressBar(this, R.string.load_data);
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }


    protected abstract int getLayoutId();

    protected abstract void initView();
}
