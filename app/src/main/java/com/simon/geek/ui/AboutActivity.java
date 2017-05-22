package com.simon.geek.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import com.simon.geek.R;
import com.simon.mvp_frame.BaseActivity;

public class AboutActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setCommonBackToolBack(toolbar, "关于");
    }

    @Override
    protected void initObjects() {

    }

    @Override
    protected void initData() {

    }
}
