package com.simon.geek.ui.dribbble;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simon.common.state.StateView;
import com.simon.common.utils.App;
import com.simon.common.utils.ImgLoadHelper;
import com.simon.common.utils.ScreenHelper;
import com.simon.geek.R;
import com.simon.geek.data.Api;
import com.simon.geek.data.model.ShotEntity;
import com.simon.geek.data.model.User;
import com.simon.geek.util.DateTimeUtil;
import com.simon.geek.util.DialogHelp;
import com.simon.geek.util.StringUtil;
import com.simon.geek.widget.TagGroup;
import com.simon.geek.widget.loadingdia.SpotsDialog;
import com.simon.mvp_frame.BaseActivityWithUIContract;

import java.io.File;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/8/31 17:44
 */

public class ShotDetailActivity extends BaseActivityWithUIContract implements ShotDetailContract
        .View, View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mDetailPic;// 详情大图
    private ImageView mImv_avatar;// 头像
    private TextView mTv_author;// 创建者
    private TextView mTv_time;// 创建时间
    private TextView mTv_views_count; // 查看次数
    private TextView mTv_comments_count;// 评论数
    private TextView mTv_likes_count;// 喜欢数
    private TextView mTv_buckets_count;// buckets 数量
    private TextView mTv_shot_desc;// Shot 描述
    private LinearLayout mLl_tags; // Shot 标签
    private TagGroup mTag_group;
    private ShotDetailPresenter mPresenter;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private long mShotId;
    private int mCommCount;
    private String mImgUrl;
    private String mTitle;
    private FloatingActionButton mFab;
    private SpotsDialog mLoadingDialog;
    private int mLike_count;
    private int mBuckets_count;
    private ShotEntity mShot;

    public static void start(Context context, long shotId) {
        Intent starter = new Intent(context, ShotDetailActivity.class);
        starter.putExtra("shotId", shotId);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shot_detail;
    }

    @Override
    protected void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mDetailPic = (ImageView) findViewById(R.id.imv_detail_pic);
        mImv_avatar = (ImageView) findViewById(R.id.imv_avatar);
        mTv_author = (TextView) findViewById(R.id.tv_author);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mTv_views_count = (TextView) findViewById(R.id.tv_views_count);
        mTv_comments_count = (TextView) findViewById(R.id.tv_comments_count);
        mTv_likes_count = (TextView) findViewById(R.id.tv_likes_count);
        mTv_buckets_count = (TextView) findViewById(R.id.tv_buckets_count);
        mTv_shot_desc = (TextView) findViewById(R.id.tv_shot_desc);

        mLl_tags = (LinearLayout) findViewById(R.id.ll_tags);
        mTag_group = (TagGroup) findViewById(R.id.tag_group);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void initObjects() {
        StateView stateView = (StateView) findViewById(R.id.stateView_detail);
        mPresenter = new ShotDetailPresenter(this);
    }

    @Override
    protected void initData() {
        mShotId = getIntent().getLongExtra("shotId", 0);
        mPresenter.loadShot(Api.ACTION_BEGIN, mShotId);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mDetailPic.setOnClickListener(this);
        mFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showShot(ShotEntity shot) {
//        hideDialog();
        mShot = shot;
//        showContent();
        mTitle = shot.getTitle();
        mCollapsingToolbarLayout.setTitle(mTitle);
        String normal = shot.getImages().getNormal();
        String hdpi = shot.getImages().getHidpi();
        mImgUrl = StringUtil.isEmpty(hdpi) ? normal : hdpi;

        User user = shot.getUser();
        ImgLoadHelper.loadAvatar(user.avatar_url, mImv_avatar);
        mTv_author.setText(user.name);
        String created_at = shot.getCreated_at();
        String time = DateTimeUtil.formatUTC(created_at);
        String s = DateTimeUtil.friendly_time(time);

        mTv_time.setText(s);
        mTv_views_count.setText(shot.getViews_count() + "");

        mCommCount = shot.getComments_count();
        mLike_count = shot.getLikes_count();
        mBuckets_count = shot.getBuckets_count();

        mTv_comments_count.setText(mCommCount + "");
        mTv_likes_count.setText(mLike_count + "");
        mTv_buckets_count.setText(mBuckets_count + "");

        String bio = user.bio;
        if (!StringUtil.isEmpty(bio)) {
            mTv_shot_desc.setText(Html.fromHtml(bio));
        }
        String[] tags = shot.getTags();
        if (null != tags && tags.length > 0) {
            mLl_tags.setVisibility(View.VISIBLE);
            mTag_group.setTags(tags);
        } else {
            mLl_tags.setVisibility(View.GONE);
        }

        int width = ScreenHelper.getScreenWidth(App.INSTANCE);
        int height = width * 3 / 4;

        ImgLoadHelper.imageW_H(mImgUrl, mDetailPic, width, height, R.drawable.dribbble_p);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            shareMsg("ShotDetailActivity", mTitle, "", mImgUrl);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 分享功能
     *
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param imgPath       图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText, String imgPath) {

        String fileName = mTitle;
//        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        File renamed = new File(imgPath, fileName);
        Uri uri = Uri.fromFile(renamed);
        ShareCompat.IntentBuilder.from(this)
                .setText(getShareText())
                .setType(getImageMimeType(fileName))
                .setSubject(mTitle)
                .setStream(uri)
                .startChooser();
    }

    private String getShareText() {
        return new StringBuilder()
                .append("“")
                .append(mShot.getTitle())
                .append("” by ")
                .append(mShot.getUser().name)
                .append("\n")
                .append(mShot.getImages().getNormal())
                .toString();
    }

    private String getImageMimeType(@NonNull String fileName) {
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }

    private void showCustomDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogHelp.getLoadingDialog(this, "正在加载...");
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void hideCustomDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        mPresenter.unsubscribe();
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        super.onBackPressed();
    }


}
