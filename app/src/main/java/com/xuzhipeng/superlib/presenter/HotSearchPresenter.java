package com.xuzhipeng.superlib.presenter;


import com.xuzhipeng.superlib.common.util.HttpUtil;
import com.xuzhipeng.superlib.model.HotSearch;
import com.xuzhipeng.superlib.view.IHotSearchView;

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

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class HotSearchPresenter extends BasePresenter<IHotSearchView> {

    private static final String TAG = "HotSearchPresenter";

    public HotSearchPresenter(IHotSearchView view) {
        attachView(view);
    }

    public void loadHotSearch(final String url) {

        Observable.create(new ObservableOnSubscribe<List<HotSearch>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<HotSearch>> e) throws Exception {
                List<HotSearch> hotSearches = new ArrayList<>();
                String html = HttpUtil.sendOkHttp(url);
                if (html != null) {
                    Document doc = Jsoup.parse(html);
                    Elements elements = doc.select("tbody a");
                    for (int i = 0; i < 20; i++) {
                        HotSearch hotKey = new HotSearch();
                        hotKey.setText(elements.get(i).text());
                        hotKey.setUrl(elements.get(i).attr("href"));
                        hotSearches.add(hotKey);
                    }
                }
                e.onNext(hotSearches);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HotSearch>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<HotSearch> hotSearches) {
                        mView.setHotSearch(hotSearches);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }
}
