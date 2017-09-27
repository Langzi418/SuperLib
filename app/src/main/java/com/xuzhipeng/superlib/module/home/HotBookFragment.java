package com.xuzhipeng.superlib.module.home;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.address.Address;
import com.xuzhipeng.superlib.base.LazyLoadFragment;
import com.xuzhipeng.superlib.common.util.HttpUtil;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.model.HotBook;
import com.xuzhipeng.superlib.module.adapter.HotBookAdapter;
import com.xuzhipeng.superlib.module.info.BookInfoActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HotBookFragment extends LazyLoadFragment {

    private static final String TAG = "HotBookFragment";

    private RecyclerView mHotBookRv;
    private HotBookAdapter mHotBookAdapter;
    private List<HotBook> mHotBooks;

    //加载更多控制
    private int mCurNum;
    private int mTotalNum;
    private Elements mBooks;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_book;
    }

    @Override
    protected void initView(View view) {
        mHotBookRv = view.findViewById(R.id.hot_book_rv);
    }

    @Override
    protected void setView() {
        mHotBookAdapter = new HotBookAdapter(R.layout.item_hot_book, null);
        mHotBookRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotBookRv.setAdapter(mHotBookAdapter);
        ViewUtil.setRvDivider(mHotBookRv);
    }

    @Override
    protected void initData() {
        mCurNum = 1;
    }

    @Override
    protected void setListener() {
        mHotBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(BookInfoActivity.newIntent(getActivity(),
                        mHotBooks.get(position).getInfoUrl()));
            }
        });

        /**
         *  加载更多
         */
        mHotBookAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurNum >= mTotalNum) {
                    mHotBookAdapter.loadMoreEnd();
                    Log.d(TAG, "onLoadEnd: ");
                } else {
                    List<HotBook> hotBooks = loadMore();
                    if (hotBooks == null || hotBooks.size() == 0) {
                        mHotBookAdapter.loadMoreFail();
                    } else {
                        mHotBookAdapter.addData(hotBooks);
                        mCurNum = mHotBookAdapter.getData().size() + 1;
                        mHotBookAdapter.loadMoreComplete();
                    }
                }
            }
        }, mHotBookRv);
        mHotBookAdapter.disableLoadMoreIfNotFullPage();

    }

    @Override
    protected void loadData() {
        if (!NetWorkUtil.isNetworkConnected(getActivity())) {
            return;
        }

        Observable.create(new ObservableOnSubscribe<List<HotBook>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<HotBook>> e) throws Exception {
                String html = HttpUtil.sendOkHttp(Address.getHotBook());
                if (html == null) {
                    return;
                }

                Document doc = Jsoup.parse(html);
                mBooks = doc.select("table.table_line tr"); //解析所有tr
                mTotalNum = mBooks.size();
                List<HotBook> hotBooks = loadMore();
                e.onNext(hotBooks);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HotBook>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        HotBookFragment.super.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<HotBook> hotBooks) {
                        mHotBooks = hotBooks;
                        mHotBookAdapter.setNewData(mHotBooks);
                        mCurNum = mHotBookAdapter.getData().size() + 1;
                        HotBookFragment.super.hideProgress();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        HotBookFragment.super.hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 加载更多
     */
    private List<HotBook> loadMore() {
        List<HotBook> hotBooks = new ArrayList<>();
        for (int i = mCurNum; i < mTotalNum && i < mCurNum + 10; i++) {
            Elements bookInfo = mBooks.get(i).select("td"); //拆分书本信息
            HotBook book = new HotBook();
            Elements urlInfo = bookInfo.get(1).select("a"); //a元素
            book.setName(urlInfo.text());

            String url = urlInfo.attr("href");
            String[] urls = url.split("/");
            book.setInfoUrl(urls[urls.length - 1]);
            book.setAuthor(bookInfo.get(2).text());
            book.setPressYears(bookInfo.get(3).text());
            book.setIndex(bookInfo.get(4).text());
            book.setViews(bookInfo.get(5).text());
            hotBooks.add(book);
        }
        return hotBooks;

    }
}
