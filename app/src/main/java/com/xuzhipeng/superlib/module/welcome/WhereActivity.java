package com.xuzhipeng.superlib.module.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuzhipeng.superlib.MainActivity;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.module.college.CollegeActivity;

public class WhereActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //首次加载是否选择了学校
        if (PrefUtil.getFirstStart()) {
            startActivity(CollegeActivity.newIntent(this));
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();

    }
}
