package com.simon.geek.ui.main;

import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simon.common.utils.ImgLoadHelper;
import com.simon.geek.GlobalConstant;
import com.simon.geek.R;
import com.simon.geek.ui.dribbble.ShotsFragment;
import com.simon.geek.ui.images.ImagesContainerFragment;
import com.simon.geek.ui.user.SettingsActivity;
import com.simon.geek.util.DribbblePrefs;
import com.simon.mvp_frame.BaseActivity;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/8/31 9:23
 */

public class HomeActivity extends BaseActivity {

    private long mBackPressedTime;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    private ImageView mImv_profile;
    private TextView mUserName;
    private TextView mTv_email;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.dribbble);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.getMenu().findItem(R.id.nav_dribbble).setChecked(true);
        mImv_profile = (ImageView) headerView.findViewById(R.id.imv_header);
        mUserName = (TextView) headerView.findViewById(R.id.tv_username);
        mTv_email = (TextView) headerView.findViewById(R.id.tv_email);

        setNavigationSwitchContent(navigationView);

        ShotsFragment shotsFragment = ShotsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,
                shotsFragment).commit();

        String user_name = DribbblePrefs.getInstance().getUserName();
        String user_profile = DribbblePrefs.getInstance().getUserAvatar();
        if (TextUtils.isEmpty(user_name)) {
            user_name = "Simon.han";
        }
        if (TextUtils.isEmpty(user_profile)) {
            user_profile = GlobalConstant.profile_url;
        }

        mUserName.setText(user_name);
        ImgLoadHelper.loadAvatar(user_profile, mImv_profile);
    }

    @Override
    protected void initObjects() {

    }

    @Override
    protected void initData() {
        mImv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvatarActivity.start(HomeActivity.this);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    private void setNavigationSwitchContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                item.setChecked(true);
                Fragment fragment = null;
                mToolbar.setTitle(item.getTitle());
                if (itemId == R.id.nav_dribbble) {
                    fragment = ShotsFragment.newInstance();
                } else if (itemId == R.id.nav_images) {
                    fragment = ImagesContainerFragment.newInstance();
                } else if (itemId == R.id.nav_settings) {
                    SettingsActivity.start(HomeActivity.this);
                }
                if (null != fragment) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,
                            fragment).commit();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        // 双击退出
        long curTime = SystemClock.uptimeMillis();
        if ((curTime - mBackPressedTime) < (3 * 1000)) {
            finish();
        } else {
            mBackPressedTime = curTime;
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_LONG).show();
        }
//            super.onBackPressed();
    }

}
