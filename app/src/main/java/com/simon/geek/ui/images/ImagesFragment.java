package com.simon.geek.ui.images;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.simon.agiledevelop.mvpframe.BaseFragment;
import com.simon.agiledevelop.recycler.adapter.RecycledAdapter;
import com.simon.agiledevelop.state.StateView;
import com.simon.geek.R;
import com.simon.geek.data.Api;
import com.simon.geek.data.model.BDImageEntity;
import com.simon.geek.listener.OnPagerSelectedListener;

import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/10
 * @email hanzx1024@gmail.com
 */

public class ImagesFragment extends BaseFragment<ImagesPresenter> implements ImagesContract.View,
        RecycledAdapter.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
        OnPagerSelectedListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;
    private int mPageNo = 0;
    private int mPageRow = 20;
    private String mCurrCategory = "汽车";

    public static ImagesFragment newInstance() {
        Bundle args = new Bundle();
        ImagesFragment fragment = new ImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_images;
    }

    @Override
    protected ImagesPresenter getPresenter() {
        return new ImagesPresenter(this);
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setColorSchemeResources(R.color.purple_500, R.color.blue_500, R.color
                .orange_500, R.color.pink_500);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_images);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sm);
        if (mAdapter == null) {
            mAdapter = new ImagesAdapter();

            mAdapter.openAnimation(RecycledAdapter.SCALEIN);
            mAdapter.setLoadMoreEnable(true);
            mAdapter.setOnLoadMoreListener(this);
        }
    }

    @Override
    protected StateView getLoadingView(View view) {
        return (StateView) view.findViewById(R.id.stateView_loading);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getImages(mCurrCategory, "全部", mPageNo, mPageRow, Api.ACTION_BEGIN);
    }

    @Override
    public void showImages(List<BDImageEntity> images) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (null == adapter) {
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.setNewData(images);
    }

    @Override
    public void renderMore(List<BDImageEntity> images) {
        if (null != mAdapter) {
            mAdapter.appendData(images);
        }
        mAdapter.loadComplete();
    }

    @Override
    public void renderRefresh(List<BDImageEntity> images) {
        if (null != mAdapter) {
            List<BDImageEntity> data = mAdapter.getData();
            data.clear();
            mAdapter.setNewData(images);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading(int action, String msg) {
        showLoading(msg);
    }

    @Override
    public void onEmpty(String msg) {
        showEmtry(msg, null);
    }

    @Override
    public void onFailed(int action, String msg) {
        if (action != Api.ACTION_MORE) {
            showError(msg, null);
        }
    }

    @Override
    public void onCompleted(int action) {
        showContent();
    }

    @Override
    public void setPresenter(ImagesPresenter presenter) {

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

    @Override
    public void onPagerSelect(String category) {
        mPageNo = 0;
        mCurrCategory = category;
        mPresenter.getImages(mCurrCategory, "全部", mPageNo, mPageRow, Api.ACTION_BEGIN);
    }
}
