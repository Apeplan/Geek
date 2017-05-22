package com.simon.geek.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simon.geek.R;
import com.simon.mvp_frame.BaseActivity;

public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findViews() {
        LinearLayout rootView = (LinearLayout) findViewById(R.id.activity_splash);
        ImageView drib_logo = (ImageView) findViewById(R.id.imv_drib_logo);
        final TextView tv_logo = (TextView) findViewById(R.id.tv_drib);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        rootView.setAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    mHandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void initObjects() {

    }

    @Override
    protected void initData() {

    }

}
