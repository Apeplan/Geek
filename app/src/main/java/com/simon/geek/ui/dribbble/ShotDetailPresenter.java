package com.simon.geek.ui.dribbble;

import android.support.annotation.NonNull;

import com.simon.common.log.LLog;
import com.simon.common.utils.ToastHelper;
import com.simon.geek.GeekApp;
import com.simon.geek.data.DataManger;
import com.simon.geek.data.model.ShotEntity;
import com.simon.mvp_frame.ResultSubscriber;
import com.simon.mvp_frame.RxPresenter;

import rx.Observable;


/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/8/31 17:46
 */

public class ShotDetailPresenter extends RxPresenter<ShotDetailContract.View> {

    public ShotDetailPresenter(@NonNull ShotDetailContract.View view) {
        super(view);
    }


    public void loadShot(final int action, long id) {

        Observable<ShotEntity> shot = DataManger.getInstance().getShot(id);
        subscribe(shot, new ResultSubscriber<ShotEntity>() {
            @Override
            public void onStartRequest() {
                showDialog();
            }

            @Override
            public void onEndRequest() {
                LLog.d("onCompleted: Shot信息 请求完成");
                closeDialog();
            }

            @Override
            public void onFailed(Throwable e) {
                LLog.d("onCompleted: Shot信息 请求失败\t" + e.getMessage());
                ToastHelper.showLongToast(GeekApp.context(),"");
            }

            @Override
            public void onResult(ShotEntity shotEntity) {
                LLog.d("onCompleted: Shot信息 请求成功");
                getView().showShot(shotEntity);
            }
        });

    }

    public void addLike(long shotId) {

       /* getView().showLoadDia();

        Observable<LikeEntity> likeEntity = DribbbleDataManger.getInstance().addLike(shotId);

        subscribe();

        Subscription subscription = mRemoteDataSource.addLike(shotId)
                .observeOn(mSchedulerProvider.ui())
                .subscribeOn(mSchedulerProvider.io())
                .subscribe(new Subscriber<LikeEntity>() {
                    @Override
                    public void onCompleted() {
                        mShotView.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mShotView.hintMsg("添加喜欢失败");
                    }

                    @Override
                    public void onNext(LikeEntity likeEntity) {
                        mShotView.hintMsg("添加喜欢成功");
                    }
                });

        mSubscriptions.add(subscription);*/
    }

    public void sendComment(long shotId, String content) {
       /* mShotView.showLoadDia();

        mSubscriptions.clear();

        Subscription subscription = mRemoteDataSource.createComment(shotId, content)
                .observeOn(mSchedulerProvider.ui())
                .subscribeOn(mSchedulerProvider.io())
                .subscribe(new Subscriber<CommentEntity>() {
                    @Override
                    public void onCompleted() {
                        LLog.d("Simon", "onCompleted: 评论请求");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LLog.d("Simon", "onCompleted: 评论请求失败");
                        mShotView.hintMsg("评论失败");
                    }

                    @Override
                    public void onNext(CommentEntity commentEntity) {
                        LLog.d("Simon", "onCompleted: 评论请求成功");
                        mShotView.hintMsg("评论发表成功");
                    }
                });
        mSubscriptions.add(subscription);*/
    }

}
