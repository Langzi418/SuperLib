package com.xuzhipeng.superlib.presenter;

import com.xuzhipeng.superlib.db.Book;
import com.xuzhipeng.superlib.db.Collect;
import com.xuzhipeng.superlib.db.DBUtil;
import com.xuzhipeng.superlib.view.ICollectView;

import java.util.ArrayList;
import java.util.Collections;
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
 * Date: 2017/9/14
 * Desc:
 */

public class CollectPresenter extends BasePresenter<ICollectView> {

    private static final String TAG = "CollectPresenter";

    public CollectPresenter(ICollectView view) {
        attachView(view);
    }


    public void loadBooks(final Long userId) {
        Observable.create(new ObservableOnSubscribe<List<Book>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Book>> e) throws Exception {
                List<Book> books = new ArrayList<>();
                List<Collect> collects = DBUtil.queryUserCollect(userId);
                for (Collect collect : collects) {
                    Book book = DBUtil.queryBookById(collect.getBookId());
                    if (book != null) {
                        books.add(book);
                    }
                }
                if (books.size() > 1) { //排序
                    Collections.sort(books);
                }

                DBUtil.closeDB();
                e.onNext(books);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Book>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<Book> books) {
                        mView.setBooks(books);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.setBooks(null);
                        mView.hideProgress();

                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });

    }

}
