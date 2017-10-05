package com.xuzhipeng.superlib.module.mylib;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.common.util.PrefUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ReaderConActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ReaderConActivity";

    private static final String EXTRA_HTML = "HTML";
    private EditText mReaderConInput;
    private Button mReaderConBtn;
    private String hint1;
    private String hint2;

    private MyLibHttp mLib;
    private TextView mReaderHint1Tv;
    private TextView mReaderHint2Tv;

    public static Intent newIntent(Context context, String html) {
        Intent intent = new Intent(context, ReaderConActivity.class);
        intent.putExtra(EXTRA_HTML, html);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reader_con;
    }

    @Override
    protected void initView() {
        mReaderConInput =  findViewById(R.id.reader_con_input);
        mReaderConBtn =  findViewById(R.id.reader_con_btn);
        mReaderHint1Tv =  findViewById(R.id.reader_hint1_tv);
        mReaderHint2Tv =  findViewById(R.id.reader_hint2_tv);
    }

    @Override
    protected void setListener() {
        mReaderConBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String conHtml = getIntent().getStringExtra(EXTRA_HTML);
        Document doc = Jsoup.parse(conHtml);
        Elements hints = doc.select("div.mylib_con_con dl#searchmain dt");
        if (hints.size() >= 4) {
            hint1 = hints.get(0).text();
            hint2 = hints.get(1).text();
            mReaderHint1Tv.setText(hint1);
            mReaderHint2Tv.setText(hint2);
        }

        mLib = MyLibHttp.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        if(!NetWorkUtil.isNetworkConnected(this)){
            return;
        }

        switch (v.getId()) {
            case R.id.reader_con_btn:
                if (!TextUtils.isEmpty(mReaderConInput.getText().toString())) {
                    new loadConTask().execute(mReaderConInput.getText().toString());
                }else {
                    Toast.makeText(ReaderConActivity.this, R.string.input_name, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class loadConTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return mLib.getConRes(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){ //成功
                startActivity(MyLibActivity.newIntent(ReaderConActivity.this));
                finish();
            }else{
                new MaterialDialog.Builder(ReaderConActivity.this)
                        .content(R.string.con_fail)
                        .positiveText(R.string.ok)
                        .positiveColorRes(R.color.green_light)
                        .negativeText(R.string.give_up)
                        .negativeColorRes(R.color.colorPrimary)
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();

                                startActivity(LoginActivity.newIntent(ReaderConActivity.this));
                                finish();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                PrefUtil.setUserNo(ReaderConActivity.this,null); //放弃后清除数据
                                PrefUtil.setPwd(ReaderConActivity.this,null);
                                finish();
                            }
                        })
                        .show();
            }
        }
    }

}
