package com.xuzhipeng.superlib.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.xuzhipeng.superlib.common.util.HttpUtil;
import com.xuzhipeng.superlib.db.Book;
import com.xuzhipeng.superlib.db.Collect;
import com.xuzhipeng.superlib.db.DBUtil;
import com.xuzhipeng.superlib.model.BookStatus;
import com.xuzhipeng.superlib.model.DouBanInfo;
import com.xuzhipeng.superlib.model.DouComment;
import com.xuzhipeng.superlib.model.LibInfo;
import com.xuzhipeng.superlib.view.IBookInfoView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

public class BookInfoPresenter extends BasePresenter<IBookInfoView> {

    private static final String TAG = "BookInfoPresenter";

    private static final String BASE_DOU_BAN = "https://api.douban.com/v2/book/isbn/";

    public BookInfoPresenter(IBookInfoView view) {
        attachView(view);
    }

    /**
     * 图书馆网站抓取图书信息
     */
    public void loadLibInfo(final String url) {

        Observable.create(new ObservableOnSubscribe<LibInfo>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<LibInfo> e) throws Exception {
                String html = HttpUtil.sendOkHttp(url);
                LibInfo libInfo = new LibInfo();

                if (html != null) {
                    Document doc = Jsoup.parse(html);
                    libInfo.setName(doc.select("div#item_detail dd").first().text());
                    Elements elements = doc.select("dl.booklist");
                    Elements bookStatusInfo = doc.select("table#item tbody tr");
                    for (int i = 2; i < elements.size(); i++) {
                        Elements dt = elements.get(i).select("dt");
                        String dtText = dt.text();
                        if (dtText.equals("ISBN及定价:")) {
                            libInfo.setIsbn
                                    (elements.get(i).select("dd").text().split(" |/")[0]);
                            //" "或"/" 分割
                        } else if (dtText.equals("中图法分类号:")) {
                            libInfo.setCategory(elements.get(i).select("dd").text());
                            break;
                        }
                    }

                    List<BookStatus> statusList = new ArrayList<>();
                    for (int i = 1; i < bookStatusInfo.size(); i++) {
                        Elements books = bookStatusInfo.get(i).select("td");
                        if (books.size() >= 5) { //正常状态
                            BookStatus bs = new BookStatus();
                            bs.setIndex(books.get(0).text());
                            bs.setPlace(books.get(3).text());
                            bs.setStatus(books.get(4).text());
                            statusList.add(bs);
                        }
                    }
                    libInfo.setStatusList(statusList);
                }
                e.onNext(libInfo);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LibInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull LibInfo info) {
                        mView.setLibInfo(info);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.setLibInfo(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 检测是否持久化
     */
    public void loadBook(final String isbn) {
        Observable.create(new ObservableOnSubscribe<Book>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Book> e) throws Exception {
                Book book = DBUtil.queryBookByIsbn(isbn);
                DBUtil.closeDB();
                e.onNext(book);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Book book) {
                        mView.setBook(book);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.setBook(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 加载是否收藏
     */
    public void loadIsCollect(final Long userId, final Long bookId) {
        Observable.create(new ObservableOnSubscribe<Collect>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Collect> e) throws Exception {
                Collect collect = DBUtil.queryCollect(userId, bookId);
                DBUtil.closeDB();
                e.onNext(collect);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Collect>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Collect collect) {
                        mView.setCollect(collect);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.setCollect(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 加载豆瓣信息
     */
    public void loadDouBanInfo(String isbn) {

        final String url = BASE_DOU_BAN + isbn;
        Observable.create(new ObservableOnSubscribe<DouBanInfo>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<DouBanInfo> e) throws Exception {
                DouBanInfo douBanInfo = null;
                String html = HttpUtil.sendOkHttp(url);
                if (html != null) {
                    douBanInfo = new Gson().fromJson(html, DouBanInfo.class);

                }

                e.onNext(douBanInfo);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouBanInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull DouBanInfo douBanInfo) {
                        mView.setDouBanInfo(douBanInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onDouInfoError: ", e);
                        mView.setDouBanInfo(null);

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    /**
     * 加载豆瓣评论
     */
    public void loadDouBanCmt(final String isbn) {
        final String url = BASE_DOU_BAN + isbn + "/reviews";
        Observable.create(new ObservableOnSubscribe<List<DouComment>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DouComment>> e) throws Exception {
                List<DouComment> comments = new ArrayList<>();
                String html = HttpUtil.sendOkHttp(url);
                if (html != null) {
                    JSONObject obj = new JSONObject(html);
                    JSONArray reviewArr = obj.getJSONArray("reviews");
                    for (int i = 0; i < reviewArr.length(); i++) {
                        String reviewCon = reviewArr.getJSONObject(i).toString();
                        DouComment comment = new Gson().fromJson(reviewCon, DouComment.class);
                        comments.add(comment);
                    }
                }

                e.onNext(comments);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DouComment>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull List<DouComment> douComments) {
                        mView.setDouBanCmt(douComments);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.setDouBanCmt(null);
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });

    }


    /**
     * 加载豆瓣评论细节
     *
     * @param alt 链接
     */
    public void loadDouCmtDetail(final String alt) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                String html = HttpUtil.sendOkHttp(alt);
                if (html != null) {
                    Document doc = Jsoup.parse(html);
                    Element detail = doc.select("div#link-report div").first();
                    e.onNext(detail.toString());
                }

                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        mView.setDouBanCmtDetail(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
