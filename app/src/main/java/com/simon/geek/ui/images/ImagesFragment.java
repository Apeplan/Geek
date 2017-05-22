package com.simon.geek.ui.images;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.simon.common.recycled.LoadMoreRecyclerView;
import com.simon.common.state.StateView;
import com.simon.geek.R;
import com.simon.geek.data.Api;
import com.simon.geek.data.model.BDImageEntity;
import com.simon.mvp_frame.BaseFragmentWithUIContract;

import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/10
 * @email hanzx1024@gmail.com
 */

public class ImagesFragment extends BaseFragmentWithUIContract implements ImagesContract.View,
        SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView
                .LoadMoreListener {

    private SwipeRefreshLayout mRefreshLayout;
    private LoadMoreRecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;
    private int mPageNo = 0;
    private int mPageRow = 20;
    private String mCurrCategory = "汽车";
    private ImagesPresenter mPresenter;
    private StateView mStateView;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_images;
    }

    @Override
    protected void findViews() {
        mStateView = findViewById(R.id.stateView_loading);
        mRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = findViewById(R.id.rv_images);
        mRefreshLayout.setColorSchemeResources(R.color.purple_500, R.color.blue_500, R.color
                .orange_500, R.color.pink_500);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView = findViewById(R.id.rv_images);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sm);
        if (mAdapter == null) {
            mAdapter = new ImagesAdapter();
        }
    }

    @Override
    protected void initObjects() {
        mCurrCategory = getArguments().getString("type");

        mPresenter = new ImagesPresenter(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            mPresenter.getImages(mCurrCategory, "全部", mPageNo, mPageRow, Api.ACTION_BEGIN);
        }
    }

    @Override
    public void showImages(List<BDImageEntity> images) {
        mStateView.setState(StateView.STATE_CONTENT);
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (null == adapter) {
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.addAllAndNotifyChanged(images, true);
    }

    @Override
    public void renderMore(List<BDImageEntity> images) {
        if (null != mAdapter) {
            mAdapter.addAllAndNotifyChanged(images, false);
        }
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public void renderRefresh(List<BDImageEntity> images) {
        mStateView.setState(StateView.STATE_CONTENT);
        if (null != mAdapter) {
            mAdapter.addAllAndNotifyChanged(images, true);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void empty(String msg) {
        mStateView.setState(StateView.STATE_EMPTY);
    }

    @Override
    public void error(String msg) {
        mStateView.setState(StateView.STATE_ERROR);
    }

    @Override
    public void onLoadMore() {
        mPageNo++;
        mPresenter.getImages(mCurrCategory, "全部", mPageNo, mPageRow, Api.ACTION_MORE);
    }

    @Override
    public void onRefresh() {
        mPageNo = 0;
        mPresenter.getImages(mCurrCategory, "全部", mPageNo, mPageRow, Api.ACTION_REFRESH);
    }

}
