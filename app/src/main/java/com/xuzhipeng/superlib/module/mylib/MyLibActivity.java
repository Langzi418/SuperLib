package com.xuzhipeng.superlib.module.mylib;

import android.content.Context;
import android.content.Intent;
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

    public static Intent newIntent(Context context) {
        return new Intent(context,MyLibActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_lib;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setView() {
        ViewUtil.setToolbar(this, R.string.my_lib);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
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
                MaterialDialog.Builder builder =
                        ViewUtil.showTwoDialog(this,getString(R.string.check_log_out));

                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                    DialogAction which) {
                                PrefUtil.setSuccess(false);
                                PrefUtil.setUserNo(null);
                                PrefUtil.setUserId(0L);
                                PrefUtil.setPwd(null);
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
                        }).build().show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
