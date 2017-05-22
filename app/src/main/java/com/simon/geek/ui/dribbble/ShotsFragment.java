package com.simon.geek.ui.dribbble;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.simon.common.recycled.BasicRecycledAdapter;
import com.simon.common.recycled.LoadMoreRecyclerView;
import com.simon.common.state.StateView;
import com.simon.common.utils.StateHelper;
import com.simon.geek.R;
import com.simon.geek.data.Api;
import com.simon.geek.data.model.ShotEntity;
import com.simon.geek.data.remote.DataService;
import com.simon.geek.util.DialogHelp;
import com.simon.geek.widget.loadingdia.SpotsDialog;
import com.simon.mvp_frame.BaseFragmentWithUIContract;

import java.util.List;


public class ShotsFragment extends BaseFragmentWithUIContract implements ShotsContract.View,
        LoadMoreRecyclerView.LoadMoreListener {

    private int mPageNo = 1;
    private LoadMoreRecyclerView mRecyclerView;
    private ShotsAdapter mAdapter;
    private ShotsPresenter mPresenter;
    private
    @DataService.ShotType
    String list = "";
    private
    @DataService.ShotTimeframe
    String timeframe = "";
    private
    @DataService.ShotSort
    String sort = "";
    private SpotsDialog mLoadingDialog;
    private SwipeRefreshLayout mRefreshLayout;
    private StateView mStateView;

    public static ShotsFragment newInstance() {
        ShotsFragment fragment = new ShotsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shots;
    }

    @Override
    protected void findViews() {
        mStateView = findViewById(R.id.stateView_loading);
        setHasOptionsMenu(true);
        mRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setColorSchemeResources(R.color.purple_500, R.color.blue_500, R.color
                .orange_500, R.color.pink_500);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                request(Api.ACTION_REFRESH, false);
            }
        });

        mRecyclerView = findViewById(R.id.xrv_shots);
        mRecyclerView.setLoadMoreListener(this);
    }

    @Override
    protected void initObjects() {
        mPresenter = new ShotsPresenter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setRecycleChildrenOnDetach(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLoadMoreListener(this);
//        RecyclerView可以设置自己所需要的ViewHolder数量，
//        只有超过这个数量的detached ViewHolder才会丢进ViewPool中与别的RecyclerView共享。默认是2
//        mRecyclerView.setItemViewCacheSize(5);
        if (mAdapter == null) {
            mAdapter = new ShotsAdapter();
            mRecyclerView.setRecycledViewPool(mAdapter.getPool());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void initData() {
        mAdapter.setOnItemClickListener(new BasicRecycledAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ShotEntity shot = mAdapter.getItem(position);
                ShotDetailActivity.start(getActivity(),shot.getId());
            }
        });

        mPageNo = 1;
        request(Api.ACTION_BEGIN, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        mPageNo = 1;
        switch (item.getItemId()) {
            case R.id.action_current:
                timeframe = DataService.SHOT_TIMEFRAME_NOW;
                request(Api.ACTION_BEGIN, true);
                break;
            case R.id.action_week:
                timeframe = DataService.SHOT_TIMEFRAME_WEEK;
                request(Api.ACTION_BEGIN, true);
                break;
            case R.id.action_month:
                timeframe = DataService.SHOT_TIMEFRAME_MONTH;
                request(Api.ACTION_BEGIN, true);
                break;
            case R.id.action_year:
                timeframe = DataService.SHOT_TIMEFRAME_YEAR;
                request(Api.ACTION_BEGIN, true);
                break;
            case R.id.action_ever:
                timeframe = DataService.SHOT_TIMEFRAME_EVER;
                request(Api.ACTION_BEGIN, true);
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_shots, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                mPageNo = 1;
                switch (item.getItemId()) {
                    case R.id.filter_debuts:
                        list = DataService.SHOT_TYPE_DEBUTS;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_playoffs:
                        list = DataService.SHOT_TYPE_PLAYOFFS;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_rebounds:
                        list = DataService.SHOT_TYPE_REBOUNDS;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_animated:
                        list = DataService.SHOT_TYPE_ANIMATED;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_attachments:
                        list = DataService.SHOT_TYPE_ATTACHMENTS;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_hot:
                        sort = DataService.SHOT_SORT_POPULARITY;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_recent:
                        sort = DataService.SHOT_SORT_RECENT;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_views:
                        sort = DataService.SHOT_SORT_VIEWS;
                        request(Api.ACTION_BEGIN, true);
                        break;
                    case R.id.filter_comments:
                        sort = DataService.SHOT_SORT_COMMENTS;
                        request(Api.ACTION_BEGIN, true);
                        break;

                    default:

                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    private void request(int event, boolean isDia) {
        if (isDia) {
            showDialog();
        }
        mPresenter.loadShotsList(mPageNo, list, timeframe, sort, event);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void renderShotsList(List<ShotEntity> shotsList) {
        StateHelper.showContent(mStateView);
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mAdapter.addAllAndNotifyChanged(shotsList, true);
    }

    @Override
    public void renderMoreShotsList(List<ShotEntity> shotsList) {
        if (null != mAdapter) {
            mAdapter.addAllAndNotifyChanged(shotsList, false);
        }
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public void renderRefrshShotsList(List<ShotEntity> shotsList) {
        if (null != mAdapter) {
            mAdapter.addAllAndNotifyChanged(shotsList, true);
        }
        mRefreshLayout.setRefreshing(false);
    }

    /*@Override
    public void onEmpty(String msg) {
        StateHelper.showEmtry(mStateView, msg, null);
    }

    @Override
    public void showLoading(int action, String msg) {
        if (Api.ACTION_BEGIN == action) {
            StateHelper.showDialog(mStateView);
        }
    }

    @Override
    public void onFailed(int event, String msg) {
        ToastHelper.showLongToast(App.INSTANCE, msg);
    }

    @Override
    public void onCompleted(int action) {
        LLog.d("请求完成: ");
    }

    @Override
    public void setPresenter(ShotsPresenter presenter) {

    }*/

    @Override
    public void onLoadMore() {
        mPageNo++;
        request(Api.ACTION_MORE, false);
    }

    private void showCustomDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogHelp.getLoadingDialog(getActivity(), "正在加载...");
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

}
