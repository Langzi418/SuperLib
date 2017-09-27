package com.xuzhipeng.superlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaeger.library.StatusBarUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.view.ILoadView;
import com.xuzhipeng.superlib.R;


/**
 * Author: xuzhipeng
 * Email: langzi_xzp@foxmail.com
 * Date: 2017/7/7
 */

public abstract class BaseActivity extends AppCompatActivity
        implements ILoadView {

    private MaterialDialog dialog;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        setStatusBar();
        getExtra();
        setView();
        setListener();
        initData();
    }

    /**
     * 设置状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,
                ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * 初始设置布局
     */
    protected void setView() {

    }


    /*
     * 加载数据
     */
    protected void initData() {

    }

    /*
     * 获取intent 数据
     */
    protected void getExtra() {

    }

    /*
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * toolbar
     *
     * @param titleId 标题id
     */
    protected void setToolbar(int titleId) {
        Toolbar toolbar = findViewById(R.id.base_toolbar);
        TextView toolbarTitle = findViewById(R.id.base_toolbar_title);
        toolbarTitle.setText(getString(titleId));
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }

    }

    /**
     * 设置toolbar和图标
     */
    protected void setToolbar(int titleId, int iconId) {
        setToolbar(titleId);
        if (mActionBar != null) {
            mActionBar.setHomeAsUpIndicator(iconId);
        }
    }

    /**
     * 设置返回不可用
     */
    protected void setToolbarTitle(int titleId) {
        setToolbar(titleId);
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }
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
