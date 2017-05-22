package com.simon.geek.ui.dribbble;

import android.support.annotation.NonNull;

import com.simon.common.log.LLog;
import com.simon.common.utils.ToastHelper;
import com.simon.geek.GeekApp;
import com.simon.geek.data.Api;
import com.simon.geek.data.DataManger;
import com.simon.geek.data.model.ShotEntity;
import com.simon.geek.data.remote.DataService;
import com.simon.mvp_frame.ResultSubscriber;
import com.simon.mvp_frame.RxPresenter;

import java.util.List;

import rx.Observable;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/8/29 16:54
 */

public class ShotsPresenter extends RxPresenter<ShotsContract.View> {
    public ShotsPresenter(@NonNull ShotsContract.View view) {
        super(view);
    }

    public void loadShotsList(int page, @DataService.ShotType String list, @DataService
            .ShotTimeframe String timeframe, @DataService.ShotSort String sort, final int
                                      action) {

        Observable<List<ShotEntity>> shotsList = DataManger.getInstance().getShotsList
                (page, list, timeframe, sort);

        subscribe(shotsList, new ResultSubscriber<List<ShotEntity>>() {
            @Override
            public void onStartRequest() {
                if (action == Api.ACTION_BEGIN)
                    showDialog();
            }

            @Override
            public void onEndRequest() {
                if (action == Api.ACTION_BEGIN)
                    closeDialog();
                LLog.d("onCompleted: Shots List 请求完成");
            }

            @Override
            public void onFailed(Throwable e) {
                LLog.d("onCompleted: Shots List 请求失败" + e.getMessage());
                ToastHelper.showLongToast(GeekApp.context(), e.getMessage());
            }

            @Override
            public void onResult(List<ShotEntity> shotEntities) {
                if (action == Api.ACTION_REFRESH) {
                    getView().renderRefrshShotsList(shotEntities);
                } else if (action == Api.ACTION_MORE) {
                    getView().renderMoreShotsList(shotEntities);
                } else {
                    getView().renderShotsList(shotEntities);
                }
                LLog.d("onNext: Shots List 请求成功" + shotEntities.size());
            }
        });
    }

}
