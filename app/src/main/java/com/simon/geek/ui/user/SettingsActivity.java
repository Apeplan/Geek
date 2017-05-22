package com.simon.geek.ui.user;


import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.simon.geek.R;
import com.simon.geek.ui.AboutActivity;
import com.simon.mvp_frame.BaseActivity;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private CardView mAbout;

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setCommonBackToolBack(toolbar, "设置");

        mAbout = (CardView) findViewById(R.id.cv_about);
    }

    @Override
    protected void initObjects() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        super.setListener();
        mAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cv_about:
                AboutActivity.start(SettingsActivity.this);
                break;

            default:

                break;
        }
    }

}
