package com.xuzhipeng.superlib.module.mylib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.address.Address;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.common.util.PrefUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private EditText mInputUser;
    private EditText mInputPassword;
    private AppCompatButton mBtnLogin;

    private MyLibHttp mLib;
    private TextView mLoginFailTv;
    private HtmlTextView mLoginHintTv;
    private RadioButton mCertNoRb;
    private RadioButton mBarNoRb;
    private RadioButton mEmailRb;
    private Login login;

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mInputUser = findViewById(R.id.input_email);
        mInputPassword = findViewById(R.id.input_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mLoginFailTv = findViewById(R.id.login_fail_tv);
        mLoginHintTv = findViewById(R.id.login_hint_tv);
        mCertNoRb = findViewById(R.id.cert_no_rb);
        mBarNoRb = findViewById(R.id.bar_no_rb);
        mEmailRb = findViewById(R.id.email_rb);
    }

    @Override
    protected void setListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mInputUser.getText().toString();
                final String pwd = mInputPassword.getText().toString();
                getCheck(name, pwd);
            }
        });
    }

    @Override
    protected void initData() {
        mLib = MyLibHttp.getInstance(this);
        getLoginHint();
    }

    /**
     * 请求登陆提示
     */
    private void getLoginHint() {
        Observable.create(new ObservableOnSubscribe<Login>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Login> e) throws Exception {
                String html = mLib.sendOkUrl(Address.getLogin());
                if (html != null) {
                    login = new Login();
                    Document doc = Jsoup.parse(html);
                    Elements container = doc.select("div#mainbox div#container");
                    Elements trs = container.select("div#left_tab tbody tr");
                    Element tr = trs.get(trs.size() - 4);
                    Elements inputs = tr.select("input");
                    for (Element input : inputs) {
                        String type = input.attr("type");
                        String checked = input.attr("checked");
                        String value = input.attr("value");
                        if (type.equals("Radio")) {
                            switch (value) {
                                case "cert_no":
                                    login.certNo = true;
                                    if (checked.equals("checked")) {
                                        login.checked = 0;
                                    }
                                    break;
                                case "bar_no":
                                    login.barNo = true;
                                    if (checked.equals("checked")) {
                                        login.checked = 1;
                                    }
                                    break;
                                case "email":
                                    login.email = true;
                                    if (checked.equals("checked")) {
                                        login.checked = 2;
                                    }
                                    break;
                            }
                        } else if (type.equals("hidden")) {
                            login.select = value;
                        }
                    }

                    Elements lis = container.select("div#right_con div#content_note li");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < lis.size() && i < 3; i++) {
                        sb.append(lis.get(i).toString());
                    }
                    login.hint = sb.toString();

                    e.onNext(login);
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Login login) {
                        mLoginHintTv.setHtml(login.hint);
                        if (!login.certNo) {
                            mCertNoRb.setVisibility(View.GONE);
                        }
                        if (!login.barNo) {
                            mBarNoRb.setVisibility(View.GONE);
                        }
                        if (!login.email) {
                            mEmailRb.setVisibility(View.GONE);
                        }
                        switch (login.checked) {
                            case 0:
                                mCertNoRb.setChecked(true);
                                break;
                            case 1:
                                mBarNoRb.setChecked(true);
                                break;
                            case 2:
                                mEmailRb.setChecked(true);
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 登陆验证
     */
    private void getCheck(final String name, final String pwd) {
        if (!NetWorkUtil.isNetworkConnected(this)) {
            return;
        }

        final String select;
        //验证方式
        if (mBarNoRb.getVisibility() == View.VISIBLE && mBarNoRb.isChecked()) {
            select = "bar_no";
        } else if (mCertNoRb.getVisibility() == View.VISIBLE && mCertNoRb.isChecked()) {
            select = "cert_no";
        } else if (mEmailRb.getVisibility() == View.VISIBLE && mEmailRb.isChecked()) {
            select = "email";
        } else if (login.select.equals("cert_no") || login.select.equals("bar_no")
                || login.select.equals("email")) { //默认的方式
            select = login.select;
        } else {
            //实在没有
            select = "bar_no";
        }

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    final String info = mLib.checkLogin(name, pwd, select);
                    if (info == null) {
                        return;
                    }

                    Document doc = Jsoup.parse(info);
                    Elements ele = doc.select("div#container div#left_tab form");

                    if (ele.size() == 0) {
                        //已不在登陆界面,可能登录成功，也可能需要认证
                        PrefUtil.setUserNo(LoginActivity.this, name);
                        PrefUtil.setPwd(LoginActivity.this, pwd);

                        Elements readCon = doc.select("div.mylib_con_con");
                        if (readCon.size() != 0) {
                            //需要认证
                            startActivity(ReaderConActivity.newIntent(LoginActivity.this, info));
                            finish();
                            return;
                        }

                        //不需要认证
                        startActivity(MyLibActivity.newIntent(LoginActivity.this, info));
                        finish();
                    } else {
                        final Elements trs = ele.select("tr");  //登陆失败信息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoginFailTv.setText(trs.last().text());
                            }
                        });
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
