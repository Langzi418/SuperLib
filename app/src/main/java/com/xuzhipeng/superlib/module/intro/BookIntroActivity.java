package com.xuzhipeng.superlib.module.intro;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.model.BookIntro;
import com.xuzhipeng.superlib.module.adapter.BookIntroAdapter;
import com.xuzhipeng.superlib.module.info.BookInfoActivity;
import com.xuzhipeng.superlib.presenter.BookIntroPresenter;
import com.xuzhipeng.superlib.view.IBookIntroView;

import java.util.List;

public class BookIntroActivity extends BaseActivity implements IBookIntroView, View
        .OnClickListener {

    private static final String ARGS_URL_SEARCH = "search_url";
    private static final String ARGS_DISPLAY_PG = "DISPLAY_PG";
    private TextView mBookTotalTv;
    private RecyclerView mBooksRecyclerView;
    private Button mPreviousBtn;
    private TextView mPageNumTv;
    private Button mNextBtn;

    private BookIntroAdapter mAdapter;
    private List<BookIntro> mIntros;
    private int mDisPg;
    private int mCurPg; //当前页码
    private int mTotalPg; // 页码总数
    private String mUrl;
    private BookIntroPresenter mPresenter;

    public static Intent newIntent(Context context, String url, int display) {
        Intent intent = new Intent(context, BookIntroActivity.class);
        intent.putExtra(ARGS_URL_SEARCH, url);
        intent.putExtra(ARGS_DISPLAY_PG, display);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_intro;
    }

    @Override
    protected void initView() {
        mBookTotalTv =  findViewById(R.id.book_total_tv);
        mBooksRecyclerView =  findViewById(R.id.books_recycler_view);
        mPreviousBtn =  findViewById(R.id.previous_btn);
        mPageNumTv =  findViewById(R.id.page_num_tv);
        mNextBtn =  findViewById(R.id.next_btn);
    }

    @Override
    protected void setView() {
        setToolbar(R.string.search_result);
        mAdapter = new BookIntroAdapter(R.layout.item_book_intro, null, this);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBooksRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mPreviousBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(BookInfoActivity.newIntent(
                        BookIntroActivity.this, mIntros.get(position).getInfoUrl()));
            }
        });
    }

    @Override
    protected void getExtra() {
        mDisPg = getIntent().getIntExtra(ARGS_DISPLAY_PG, 20);
        mUrl = getIntent().getStringExtra(ARGS_URL_SEARCH);
    }

    @Override
    protected void initData() {
        mPresenter = new BookIntroPresenter(this);
        mCurPg = 1; //初始页为 1

        if(!NetWorkUtil.isNetworkConnected(this)){
            return;
        }

        mPresenter.loadResult(mUrl);
        mPresenter.loadBookIntros(mUrl);
    }

    @Override
    public void setResult(String result) {
        mBookTotalTv.setText(result);
    }

    @Override
    public void setIntros(List<BookIntro> intros) {

        if (intros == null || intros.size() == 0) {
            showDialogFinish();
        }

        mIntros = intros;
        mAdapter.setNewData(mIntros);
        mBooksRecyclerView.smoothScrollToPosition(0);

        if (mCurPg > 1) {
            mPreviousBtn.setEnabled(true);
        } else {
            mPreviousBtn.setEnabled(false);
        }
        setNextBtnEnable();
    }

    /**
     *  搜索无果，退出
     */
    private void showDialogFinish() {
        new MaterialDialog.Builder(this)
                .content(R.string.no_result)
                .positiveText(R.string.ok)
                .positiveColorRes(R.color.colorPrimary)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BookIntroActivity.this.finish();
                    }
                })
                .cancelable(false)
                .show();
    }

    @Override
    public void setPageNum(int pageNum) {
        mTotalPg = (int) Math.ceil((double) pageNum / mDisPg);
        setNextBtnEnable();
        setPageText();
    }

    /**
     * 设置 下一页 按钮
     */
    private void setNextBtnEnable() {
        if (mCurPg < mTotalPg) {
            mNextBtn.setEnabled(true);
        } else {
            mNextBtn.setEnabled(false);
        }
    }

    /**
     * 设置页码 textView
     */
    private void setPageText() {
        mPageNumTv.setText(mCurPg + "/" + mTotalPg);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_btn:
                if (mCurPg > 1) {
                    String url = Uri.parse(mUrl).buildUpon()
                            .appendQueryParameter("page", String.valueOf(--mCurPg))
                            .build().toString();
                    mPresenter.loadBookIntros(url);
                    setPageText();
                }
                break;
            case R.id.next_btn:
                if (mCurPg < mTotalPg) {
                    String url = Uri.parse(mUrl).buildUpon()
                            .appendQueryParameter("page", String.valueOf(++mCurPg))
                            .build().toString();
                    mPresenter.loadBookIntros(url);
                    setPageText();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }
}
