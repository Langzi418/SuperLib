package com.xuzhipeng.superlib.module.info;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.address.Address;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.db.Book;
import com.xuzhipeng.superlib.db.Collect;
import com.xuzhipeng.superlib.db.DBUtil;
import com.xuzhipeng.superlib.model.DouBanInfo;
import com.xuzhipeng.superlib.model.DouComment;
import com.xuzhipeng.superlib.model.LibInfo;
import com.xuzhipeng.superlib.module.adapter.BookStatusAdapter;
import com.xuzhipeng.superlib.module.adapter.DouCmtAdapter;
import com.xuzhipeng.superlib.module.mylib.LoginActivity;
import com.xuzhipeng.superlib.presenter.BookInfoPresenter;
import com.xuzhipeng.superlib.view.IBookInfoView;

import java.util.List;


public class BookInfoActivity extends BaseActivity implements IBookInfoView {

    private static final String TAG = "BookInfoActivity";

    public static final String ARGS_URL_BOOK = "URL_BOOK";
    public static final String ARGS_POS_BOOK = "POS_BOOK";

    private ImageView mBookBgIv;
    private ImageView mBookImageView;
    private CollapsingToolbarLayout mCollapsingTb;
    private RecyclerView mBookStatusRv;

    private BookStatusAdapter mStatusAdapter;
    private BookInfoPresenter mPresenter;

    private DouCmtAdapter mDouAdapter;
    private List<DouComment> mDouComments;
    private Toolbar mToolbar;
    private RecyclerView mDouCmtRv;
    private View mEmptyView;
    private View mEmptyView2;
    private LikeButton mLikeBtn;

    //数据库数据
    private Book mBook;
    private String mIsbn;
    private String mName;
    private String mImgUrl;
    private String mInfoUrl;
    private String mCategory;

    //数据返回
    private int mPos;
    private boolean isBack;
    private CoordinatorLayout mNeedOffsetView;

    private TextView mCollectTv;
    private Collect mCollect;
    private LinearLayout mControlLl;
    private TextView mDouCommentTv;


    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, BookInfoActivity.class);
        intent.putExtra(ARGS_URL_BOOK, url);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_info;
    }


    @Override
    protected void initView() {
        mBookBgIv = findViewById(R.id.book_img_bg);
        mBookImageView = findViewById(R.id.book_image_view);
        mCollapsingTb = findViewById(R.id.collapsing_toolbar);
        mBookStatusRv = findViewById(R.id.book_status_rv);
        mToolbar = findViewById(R.id.toolbar);
        mDouCmtRv = findViewById(R.id.dou_comment_rv);

        mEmptyView = getLayoutInflater().inflate(
                R.layout.view_empty, (ViewGroup) mBookStatusRv.getParent(), false);
        mEmptyView2 = getLayoutInflater().inflate(
                R.layout.view_empty, (ViewGroup) mDouCmtRv.getParent(), false);

        mLikeBtn = findViewById(R.id.art_like_btn);
        mNeedOffsetView = findViewById(R.id.need_offset_view);
        mCollectTv = findViewById(R.id.collect_tv);
        mControlLl = findViewById(R.id.control_ll);
        mDouCommentTv = findViewById(R.id.dou_comment_tv);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, mNeedOffsetView);
    }

    @Override
    protected void setView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mStatusAdapter = new BookStatusAdapter(R.layout.item_book_status, null, this);
        mBookStatusRv.setLayoutManager(new LinearLayoutManager(this));
        mBookStatusRv.setAdapter(mStatusAdapter);
        mBookStatusRv.setNestedScrollingEnabled(false);

        mDouAdapter = new DouCmtAdapter(R.layout.item_dou_comment, null);
        mDouCmtRv.setLayoutManager(new LinearLayoutManager(this));
        mDouCmtRv.setAdapter(mDouAdapter);
        mDouCmtRv.setNestedScrollingEnabled(false);
        ViewUtil.setRvDivider(mDouCmtRv);
    }

    /**
     * 是否返回collectActivity
     */
    @Override
    protected void setListener() {

        mControlLl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //是否返回
        mLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (!isLogin()) {
                    goLogin();
                }
                isBack = false;
                mCollectTv.setText(R.string.collect);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                isBack = true;
                mCollectTv.setText(R.string.un_collect);
            }
        });

        /**
         *  加载豆瓣评论细节
         */
        mDouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.loadDouCmtDetail(mDouComments.get(position).getAlt());
            }
        });
    }

    @Override
    protected void getExtra() {
        mInfoUrl = getIntent().getStringExtra(ARGS_URL_BOOK);
        mPos = getIntent().getIntExtra(ARGS_POS_BOOK, -1);
    }

    @Override
    protected void initData() {
        mBook = new Book();
        mCollect = new Collect();
        mPresenter = new BookInfoPresenter(this);

        //默认不返回
        isBack = false;

        if (!NetWorkUtil.isNetworkConnected(this)) {
            return;
        }

        mPresenter.loadLibInfo(Address.getOpac() + mInfoUrl);
    }


    /**
     * 判断是否登录
     */
    private boolean isLogin() {
        return PrefUtil.getSuccess(this);
    }

    /**
     * 去登录
     */
    private void goLogin() {
        startActivity(LoginActivity.newIntent(BookInfoActivity.this));
        Toast.makeText(BookInfoActivity.this, R.string.please_login, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean isLike = mLikeBtn.isLiked();

        //状态发生变化,并且处于登录状态
        if (isLogin() && mCollect.getLike() != isLike) {
            handleLike(isLike);
        }
    }

    /**
     * 处理 like
     */
    private void handleLike(boolean isLike) {
        if (isLike) {
            if (mBook.getId() == null) {
                //未持久化
                mBook.setIsbn(mIsbn);
                mBook.setCategory(mCategory);
                mBook.setName(mName);
                mBook.setImgUrl(mImgUrl);
                mBook.setInfoUrl(mInfoUrl);
                Long bookId = DBUtil.insertBook(mBook);
                Long userId = PrefUtil.getUserId(this);
                Collect collect = new Collect();
                collect.setUserId(userId);
                collect.setBookId(bookId);
                collect.setLike(true);
                DBUtil.insertCollect(collect);
            } else {
                //book已经持久化
                if (mCollect.getId() == null) {
                    mCollect.setBookId(mBook.getId());
                    mCollect.setUserId(PrefUtil.getUserId(this));
                    mCollect.setLike(true);
                    DBUtil.insertCollect(mCollect);
                } else {
                    //collect已持久化
                    mCollect.setLike(true);
                    DBUtil.updateCollect(mCollect);
                }
            }
        } else {
            DBUtil.unCollect(mCollect);
        }

        DBUtil.closeDB();
    }

    @Override
    public void setLibInfo(LibInfo libInfo) {
        if (libInfo == null) {
            return;
        }

        mName = libInfo.getName();
        mIsbn = libInfo.getIsbn();
        mCategory = libInfo.getCategory();

        mCollapsingTb.setTitle(mName);

        if (libInfo.getStatusList() == null || libInfo.getStatusList().size() == 0) {
            mStatusAdapter.setNewData(null);
            mStatusAdapter.setEmptyView(mEmptyView);
        } else {
            mStatusAdapter.setNewData(libInfo.getStatusList());
        }

        mPresenter.loadBook(mIsbn);
        //加载豆瓣信息
        mPresenter.loadDouBanInfo(mIsbn);

        //加载豆瓣评论
        mPresenter.loadDouBanCmt(mIsbn);
    }

    @Override
    public void setDouBanInfo(DouBanInfo douBanInfo) {

        if (douBanInfo != null) {
            mImgUrl = douBanInfo.getImages().getLarge();
            ViewUtil.useGlideBlur(this, mImgUrl, mBookBgIv);
        }

        ViewUtil.useGlideUrl(this, mImgUrl, mBookImageView);

    }

    @Override
    public void setDouBanCmt(List<DouComment> comments) {
        if (comments == null || comments.size() == 0) {
            mDouAdapter.setNewData(null);
            mDouAdapter.setEmptyView(mEmptyView2);
            mDouCommentTv.setText(getString(R.string.dou_comment_cnt, 0));
        } else {
            mDouComments = comments;
            mDouAdapter.setNewData(mDouComments);
            mDouCommentTv.setText(getString(R.string.dou_comment_cnt, mDouComments.size()));
        }
    }


    @Override
    public void setBook(Book book) {
        if (book != null) {
            mBook = book;
        }

        mPresenter.loadIsCollect(PrefUtil.getUserId(this), mBook.getId());

    }

    @Override
    public void setCollect(Collect collect) {
        if (collect != null) {
            mCollect = collect;
            mLikeBtn.setLiked(mCollect.getLike());
            if (mCollect.getLike()) {
                mCollectTv.setText(R.string.collect);
            }
        }
    }

    /**
     * 设置豆瓣评论细节
     *
     * @param s 内容
     */
    @Override
    public void setDouBanCmtDetail(String s) {
        ViewUtil.showScrollDialog(s, this);
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

        if (isBack) {
            Intent intent = new Intent();
            intent.putExtra(ARGS_POS_BOOK, mPos);
            setResult(RESULT_OK, intent);
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
