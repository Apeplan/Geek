package com.simon.geek.ui.user;

import android.support.annotation.NonNull;

import com.simon.common.log.LLog;
import com.simon.common.utils.ToastHelper;
import com.simon.geek.GeekApp;
import com.simon.geek.data.DataManger;
import com.simon.geek.data.model.User;
import com.simon.mvp_frame.ResultSubscriber;
import com.simon.mvp_frame.RxPresenter;

import rx.Observable;

/**
 *
 * Created by: Simon
 * Created on: 2016/9/17 16:55
 * Email: hanzx1024@gmail.com
 */

public class UserInfoPresenter extends RxPresenter<UserInfoContract.View> {
    public UserInfoPresenter(@NonNull UserInfoContract.View view) {
        super(view);
    }

    public void loadUserInfo(final int action, long userId) {

        Observable<User> usersInfo = DataManger.getInstance().getUsersInfo(userId);
        subscribe(usersInfo, new ResultSubscriber<User>() {
            @Override
            public void onStartRequest() {
                showDialog();
            }

            @Override
            public void onEndRequest() {
                LLog.d("onCompleted: 用户信息请求完成");
               closeDialog();
            }

            @Override
            public void onFailed(Throwable e) {
                ToastHelper.showLongToast(GeekApp.context(),e.getMessage());
            }

            @Override
            public void onResult(User user) {
                getView().showUserInfo(user);
            }
        });
    }
}
