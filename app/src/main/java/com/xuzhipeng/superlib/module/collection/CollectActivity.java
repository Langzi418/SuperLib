package com.xuzhipeng.superlib.module.collection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.db.Book;
import com.xuzhipeng.superlib.db.DBUtil;
import com.xuzhipeng.superlib.module.adapter.CollectAdapter;
import com.xuzhipeng.superlib.module.info.BookInfoActivity;
import com.xuzhipeng.superlib.presenter.CollectPresenter;
import com.xuzhipeng.superlib.view.ICollectView;

import java.util.List;

public class CollectActivity extends BaseActivity implements ICollectView {

    private static final String TAG = "CollectActivity";

    private CollectAdapter mCollectAdapter;
    private List<Book> mBooks;
    private CollectPresenter mPresenter;
    private View mEmptyView;
    private RecyclerView mCollectRv;

    public static Intent newIntent(Context context) {
        return new Intent(context, CollectActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {
        mCollectRv = findViewById(R.id.collect_rv);
        mEmptyView = getLayoutInflater().inflate(R.layout.view_empty,
                (ViewGroup) mCollectRv.getParent(), false);
    }


    @Override
    protected void setView() {
        ViewUtil.setToolbar(this, R.string.collection);
        mCollectAdapter = new CollectAdapter(R.layout.item_collection, null, this);
        mCollectRv.setAdapter(mCollectAdapter);
        mCollectRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void setListener() {

        /**
         * item点击
         */
        mCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Book book = mBooks.get(position);
                Intent intent = new Intent(CollectActivity.this, BookInfoActivity.class);
                intent.putExtra(BookInfoActivity.ARGS_URL_BOOK, book.getInfoUrl());
                intent.putExtra(BookInfoActivity.ARGS_POS_BOOK, position);
                startActivityForResult(intent, 1);
            }
        });

        ItemDragAndSwipeCallback callback = new ItemDragAndSwipeCallback(mCollectAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mCollectRv);
        callback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        mCollectAdapter.enableSwipeItem();
        mCollectAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

                final Long bookId = mBooks.get(pos).getId();
                final Long userId = PrefUtil.getUserId();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBUtil.cancelCollect(userId, bookId);
                    }
                }).start();

                deleteCollectItem(pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder,
                                          float dX, float dY, boolean isCurrentlyActive) {
            }
        });

    }

    @Override
    protected void initData() {
        mPresenter = new CollectPresenter(this);

        long userId = PrefUtil.getUserId();
        if (userId != 0L) {
            mPresenter.loadBooks(userId);
        } else {
            Toast.makeText(CollectActivity.this, R.string.please_login, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void setBooks(List<Book> books) {
        if (books == null || books.size() == 0) {
            mCollectAdapter.setNewData(null);
            mCollectAdapter.setEmptyView(mEmptyView);

        } else {
            mBooks = books;
            mCollectAdapter.setNewData(mBooks);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra(BookInfoActivity.ARGS_POS_BOOK, -1);
                    if (position != -1) {
                        deleteCollectItem(position);
                    }
                }
                break;
        }
    }


    /**
     * 删除收藏项
     */
    private void deleteCollectItem(int pos) {
        mCollectAdapter.notifyItemRemoved(pos);
        mBooks.remove(pos);
        if (mBooks.size() == 0) {
            mCollectAdapter.setEmptyView(mEmptyView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
