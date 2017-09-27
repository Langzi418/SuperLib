package com.xuzhipeng.superlib.module.mylib;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.LazyLoadFragment;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.model.JYLS;
import com.xuzhipeng.superlib.module.adapter.JYLSAdapter;
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


public class JYLSFragment extends LazyLoadFragment implements View.OnClickListener {

    private RecyclerView mJylsRecyclerView;
    private Button mPreviousBtn;
    private TextView mPageNumTv;
    private Button mNextBtn;
    private JYLSAdapter mJylsAdapter;
    private List<JYLS> mJylsList;


    private int mCurPage;
    private int mTotalPage;
    private boolean isFirst;
    private MyLibHttp mLib;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_jyls;
    }

    @Override
    protected void initView(View view) {
        mJylsRecyclerView =  view.findViewById(R.id.jyls_recycler_view);
        mPreviousBtn =  view.findViewById(R.id.previous_btn);
        mPageNumTv =  view.findViewById(R.id.page_num_tv);
        mNextBtn =  view.findViewById(R.id.next_btn);
    }

    @Override
    protected void setView() {
        mJylsAdapter = new JYLSAdapter(R.layout.item_hot_book, null);
        mJylsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mJylsRecyclerView.setAdapter(mJylsAdapter);
        ViewUtil.setRvDivider(mJylsRecyclerView);
    }

    @Override
    protected void setListener() {
        mPreviousBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        mJylsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(BookInfoActivity.newIntent(getActivity(),
                        mJylsList.get(position).getInfoUrl()));
            }
        });
    }

    @Override
    protected void initData() {
        mLib = MyLibHttp.getInstance(getActivity());
        isFirst = true;
        mCurPage = 1;
    }

    @Override
    protected void loadData() {
        if(!NetWorkUtil.isNetworkConnected(getActivity())){
            return;
        }

        super.loadData();

        //加载首页
        getJyls(mCurPage);
    }

    /**
     * 加载借阅历史
     */
    private void getJyls(final int page) {

        if(!NetWorkUtil.isNetworkConnected(getActivity())){
            return;
        }

        Observable.create(new ObservableOnSubscribe<List<JYLS>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<JYLS>> e) throws Exception {
                List<JYLS> jylsList = new ArrayList<>();
                String jylsHtml = mLib.loadJYLS(page);
                if (jylsHtml == null) {
                    return;
                }

                Document doc = Jsoup.parse(jylsHtml);
                if (isFirst) {
                    Elements fonts = doc.select("div#mylib_content form p b font");
                    if (fonts.size() >= 2) {
                        mTotalPage = Integer.valueOf(fonts.last().text());
                    } else {
                        //不足一页,网页不显示总页数
                        mTotalPage = 1;
                    }
                    isFirst = false;
                }

                Elements trs = doc.select("div#mylib_content table tr");
                for (int i = 1; i < trs.size(); i++) {
                    JYLS jyls = new JYLS();
                    Elements tds = trs.get(i).getElementsByTag("td");
                    if (tds.size() >= 7) {
                        Elements urlEle = tds.get(2).getElementsByTag("a");
                        if (urlEle.size() == 1) {
                            String url = urlEle.get(0).attr("href");
                            String[] urls = url.split("/");
                            jyls.setInfoUrl(urls[urls.length - 1]);
                        }
                        jyls.setName(tds.get(2).text());
                        jyls.setAuthor(tds.get(3).text());
                        jyls.setLendDate(tds.get(4).text());
                        jyls.setReturnDate(tds.get(5).text());
                        jyls.setGcd(tds.get(6).text());
                    }
                    jylsList.add(jyls);
                }
                e.onNext(jylsList);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<JYLS>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        JYLSFragment.super.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<JYLS> jylses) {
                        mJylsList = jylses;
                        mJylsAdapter.setNewData(mJylsList);
                        setPageNum();
                        setPageButton();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        JYLSFragment.super.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        JYLSFragment.super.hideProgress();
                    }
                });
    }

    /**
     * 设置页面控制按钮
     */
    private void setPageButton() {
        if (mCurPage > 1) {
            mPreviousBtn.setEnabled(true);
        }else{
            mPreviousBtn.setEnabled(false);
        }

        if (mCurPage < mTotalPage) {
            mNextBtn.setEnabled(true);
        }else{
            mNextBtn.setEnabled(false);
        }
    }


    /**
     * 设置页码
     */
    private void setPageNum() {
        mPageNumTv.setText(mCurPage + "/" + mTotalPage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_btn:
                if (mCurPage > 1) {
                    getJyls(--mCurPage);
                }
                break;
            case R.id.next_btn:
                if (mCurPage < mTotalPage) {
                    getJyls(++mCurPage);
                }
                break;
        }
    }
}
