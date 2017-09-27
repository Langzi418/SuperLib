package com.xuzhipeng.superlib.module.mylib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.base.MyFragmentPagerAdapter;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;


public class MyLibActivity extends BaseActivity {

    private static final String EXTRA_HTML = "HTML";
    private static final String BUNDLE_HTML = "html";
    private String mHtml;

    public static Intent newIntent(Context context, String htmlCon) {
        Intent intent = new Intent(context, MyLibActivity.class);
        intent.putExtra(EXTRA_HTML, htmlCon);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_lib;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void getExtra() {
        mHtml = getIntent().getStringExtra(EXTRA_HTML);
    }

    @Override
    protected void setView() {
        setToolbar(R.string.my_lib);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_HTML,mHtml);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.zjxx, ZJXXFragment.class, bundle)
                        .add(R.string.dqjy, DQJYFragment.class)
                        .add(R.string.jyls, JYLSFragment.class)
                        .create()
        );

        ViewUtil.setViewPager(adapter, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_out:
                new MaterialDialog.Builder(this)
                        .content(R.string.check_log_out)
                        .positiveText(R.string.ok)
                        .positiveColorRes(R.color.green_light)
                        .negativeText(R.string.give_up)
                        .negativeColorRes(R.color.colorPrimary)
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                    DialogAction which) {
                                PrefUtil.setSuccess(MyLibActivity.this, false);
                                PrefUtil.setUserName(MyLibActivity.this, null);
                                PrefUtil.setUserNo(MyLibActivity.this, null);
                                PrefUtil.setUserId(MyLibActivity.this, 0L);
                                PrefUtil.setPwd(MyLibActivity.this, null);
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                    DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
