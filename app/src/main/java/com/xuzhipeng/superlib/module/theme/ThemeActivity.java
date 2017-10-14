package com.xuzhipeng.superlib.module.theme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuzhipeng.superlib.MainActivity;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;

import java.util.Arrays;
import java.util.List;

public class ThemeActivity extends BaseActivity {
    private static final String ARGS_OLD_THEME = "oldTheme";
    private static final String ARGS_LATEST_THEME = "latestTheme";
    private RecyclerView mThemeRv;
    private ThemeAdapter mAdapter;
    private Integer[] mIntegers;

    //最新的主题id
    private int mLatestThemeId;
    //进入ThemeActivity时的，themeId,用于返回逻辑
    private int mOldThemeId;

    public static Intent newIntent(Context context) {
        return new Intent(context, ThemeActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initView() {
        mThemeRv = findViewById(R.id.theme_rv);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARGS_OLD_THEME, mOldThemeId);
        outState.putInt(ARGS_LATEST_THEME, mLatestThemeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void setView() {
        ViewUtil.setToolbar(this, R.string.theme);

        //示例颜色
        Integer[] colors = new Integer[]{
                R.color.red, R.color.pink, R.color.blue, R.color.purple,
                R.color.green, R.color.green_light, R.color.yellow, R.color.orange
        };

        List<Integer> integerList = Arrays.asList(colors);
        mAdapter = new ThemeAdapter(R.layout.item_theme, integerList);
        mThemeRv.setLayoutManager(new GridLayoutManager(this, 2));
        mThemeRv.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //防止用户多次重复点击
                if (position < mIntegers.length && mLatestThemeId != mIntegers[position]) {
                    PrefUtil.setThemeId(mIntegers[position]);
                    mLatestThemeId = mIntegers[position];
                    recreate();
                }
            }
        });
    }


    protected void initData() {
        mIntegers = new Integer[]{
                R.style.AppTheme, R.style.PinkTheme, R.style.BlueTheme,
                R.style.PurpleTheme, R.style.GreenTheme,
                R.style.GreenLightTheme, R.style.YellowTheme, R.style.OrangeTheme
        };
    }

    @Override
    protected void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mOldThemeId = savedInstanceState.getInt(ARGS_OLD_THEME);
            mLatestThemeId = savedInstanceState.getInt(ARGS_LATEST_THEME);
        } else {
            mOldThemeId = PrefUtil.getThemeId();
            mLatestThemeId = mOldThemeId;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mOldThemeId != mLatestThemeId) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            finish();
        }
    }
}
