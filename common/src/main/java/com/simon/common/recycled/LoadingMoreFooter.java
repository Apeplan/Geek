package com.simon.common.recycled;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simon.common.R;
import com.simon.mvp_frame.wedgit.CustomProgressBar;

/**
 * Created by: Simon
 * Created on: 16/6/19 13:28
 * Email: hanzx1024@gmail.com
 */

public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;

    private TextView mText;
    private String loadingHint;
    private String noMoreHint;
    private String loadingDoneHint;
    private CustomProgressBar mProgressBar;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setLoadingHint(String hint) {
        loadingHint = hint;
    }

    public void setNoMoreHint(String hint) {
        noMoreHint = hint;
    }

    public void setLoadingDoneHint(String hint) {
        loadingDoneHint = hint;
    }

    public void initView() {
        setGravity(Gravity.CENTER);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.listview_footer, this,
                false);
        addView(view);
        mProgressBar = (CustomProgressBar) view.findViewById(R.id.listview_foot_progress);
        mText = (TextView) view.findViewById(R.id.listview_foot_more);
        mText.setText("正在加载...");
        loadingHint = (String) getContext().getText(R.string.listview_loading);
        noMoreHint = (String) getContext().getText(R.string.nomore_loading);
        loadingDoneHint = (String) getContext().getText(R.string.loading_done);
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mProgressBar.setVisibility(VISIBLE);
                mText.setText(loadingHint);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(loadingDoneHint);
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mProgressBar.setVisibility(GONE);
                mText.setText(noMoreHint);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}
