package com.xuzhipeng.superlib.common.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.flyco.tablayout.SlidingTabLayout;
import com.xuzhipeng.superlib.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/8/18
 */

public class ViewUtil {

    /**
     * 设置toolbar
     */
    public static ActionBar setToolbar(AppCompatActivity activity, int titleId) {
        Toolbar toolbar = activity.findViewById(R.id.base_toolbar);
        TextView toolbarTitle = activity.findViewById(R.id.base_toolbar_title);
        toolbarTitle.setText(activity.getString(titleId));
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        return actionBar;
    }

    /**
     * 设置toolbar+iconId
     */
    public static void setToolbar(AppCompatActivity activity, int titleId, int iconId) {
        ActionBar actionBar = setToolbar(activity, titleId);
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(iconId);
        }
    }

    /**
     * 仅titleId
     */
    public static void setToolbarTitle(AppCompatActivity activity, int titleId) {
        ActionBar actionBar = setToolbar(activity, titleId);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * 默认数据加载进度框
     */
    public static MaterialDialog getProgressBar(Context context, int contentId) {
        return new MaterialDialog.Builder(context)
                .content(contentId)
                .progress(true, 0)
                .cancelable(true)
                .show();
    }


    /**
     * 弹出确认对话框
     */
    public static MaterialDialog.Builder showOneDialog(Context context, String content) {
        return new MaterialDialog.Builder(context)
                .content(content)
                .cancelable(false)
                .positiveText(R.string.ok);
    }


    /**
     * 确定，取消对话框
     */
    public static MaterialDialog.Builder showTwoDialog(Context context, String content) {
        return new MaterialDialog.Builder(context)
                .content(content)
                .cancelable(false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel);
    }


    /**
     * 活动中设置viewpager
     */
    public static void setViewPager(PagerAdapter adapter, Activity activity) {
        ViewPager viewPager = activity.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedPosition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedPosition / 2 + 0.5f);
                page.setScaleY(normalizedPosition / 2 + 0.5f);
            }
        });
        SlidingTabLayout viewPagerTab = activity.findViewById(R.id.view_pager_tab);
        viewPagerTab.setViewPager(viewPager);

    }


    /**
     * glide加载url图片
     */
    public static void useGlideUrl(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.default_book)
                .into(view);
    }

    /**
     * glide 图片模糊
     */
    public static void useGlideBlur(Context context, String url, ImageView iv) {
        Glide.with(context).load(url)
                .bitmapTransform(new BlurTransformation(context, 20),
                        new CenterCrop(context))
                .into(iv);

    }

    /**
     * @param recyclerView 分割线
     */
    public static void setRvDivider(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration
                        .Builder(recyclerView.getContext())
                        .colorResId(R.color.white_bg)
                        .margin(0, 0)
                        .size(24)
                        .build());
    }


    /**
     * 弹出可滑动的对话框
     */
    public static void showScrollDialog(String content, Context context) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.comment_detail)
                .customView(R.layout.custom_scroll_dialog, true)
                .positiveText(R.string.back)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        dialog.dismiss();
                    }
                })
                .build();

        View view = dialog.getCustomView();
        if (view != null) {
            HtmlTextView textView = view.findViewById(R.id.detail_tv);
            textView.setHtml(content);
        }

        dialog.show();
    }
}
