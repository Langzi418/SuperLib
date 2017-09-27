package com.xuzhipeng.superlib.presenter;


import com.xuzhipeng.superlib.common.util.HttpUtil;
import com.xuzhipeng.superlib.model.BookIntro;
import com.xuzhipeng.superlib.model.Result;
import com.xuzhipeng.superlib.view.IBookIntroView;

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

public class BookIntroPresenter extends BasePresenter<IBookIntroView> {

    public BookIntroPresenter(IBookIntroView view) {
        attachView(view);
    }

    public void loadBookIntros(final String url) {

        Observable.create(new ObservableOnSubscribe<List<BookIntro>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<BookIntro>> e) throws Exception {
                List<BookIntro> intros = new ArrayList<>();

                String html = HttpUtil.sendOkHttp(url);
                if (html != null) {
                    Document doc = Jsoup.parse(html);
                    Elements elements = doc.select("ol#search_book_list li");
                    //逐条解析数据
                    for (Element element : elements) {
                        BookIntro intro = new BookIntro();

                        String docType = element.select("h3 span").text(); //图书类型
                        intro.setNameIndex(
                                element.select("h3").text().replace(docType, ""));//图书名

                        intro.setInfoUrl(element.select("h3 a").attr("href"));

                        //" "正则表达式处理得到其他信息
                        String[] otherInfo = element.select("p").text().split(" ");
                        intro.setStore(otherInfo[0]); //馆藏
                        intro.setLoanable(otherInfo[1]); //可借
                        intro.setPressYear(otherInfo[otherInfo.length - 3]);

                        StringBuilder authorInfo = new StringBuilder();
                        for (int i = 2; i < otherInfo.length - 3; i++) {
                            authorInfo.append(otherInfo[i]);
                            authorInfo.append(" ");
                        }

                        intro.setAuthor(authorInfo.toString());
                        authorInfo.setLength(0);

                        intros.add(intro); //添加
                    }
                }
                e.onNext(intros);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookIntro>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<BookIntro> bookIntros) {
                        mView.setIntros(bookIntros);
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


    public void loadResult(final String url) {
        Observable.create(new ObservableOnSubscribe<Result>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Result> e) throws Exception {
                String html = HttpUtil.sendOkHttp(url);
                if (html == null) {
                    return;
                }

                Document doc = Jsoup.parse(html);
                Element element = doc.select("div.book_article p").first();
                if (element == null) {
                    return;
                }

                Result result = new Result();
                result.setResult(element.text());
                result.setPageNum(Integer.valueOf(element.select("strong.red").text()));
                e.onNext(result);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Result result) {
                        mView.setResult(result.getResult());
                        mView.setPageNum(result.getPageNum());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
