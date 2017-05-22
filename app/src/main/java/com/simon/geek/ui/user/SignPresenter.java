package com.simon.geek.ui.user;

import android.support.annotation.NonNull;

import com.simon.common.log.LLog;
import com.simon.geek.data.DataManger;
import com.simon.geek.data.model.User;
import com.simon.geek.util.DribbblePrefs;
import com.simon.mvp_frame.ResultSubscriber;
import com.simon.mvp_frame.RxPresenter;

import rx.Observable;

/**
 *
 * Created by: Simon
 * Created on: 2016/8/20 16:51
 * Email: hanzhanxi@01zhuanche.com
 */

public class SignPresenter extends RxPresenter<SignInContract.View> {
    public SignPresenter(@NonNull SignInContract.View view) {
        super(view);
    }

    public void getUserToken(final String token) {
        Observable<User> tokenAndUser = DataManger.getInstance().getTokenAndUser(token);
        subscribe(tokenAndUser, new ResultSubscriber<User>() {
            @Override
            public void onStartRequest() {

            }

            @Override
            public void onEndRequest() {
                LLog.d("onCompleted: 请求用户信息执行完成");
            }

            @Override
            public void onFailed(Throwable e) {
                LLog.d("onError: 请求用户信息  " + e.getMessage());
            }

            @Override
            public void onResult(User user) {
                if (null != user) {
                    DribbblePrefs.getInstance().setLoggedInUser(user);
                    getView().signSuccess();
                } else {
                    LLog.d("onError: 请求用户信息 失败 ");
                }
            }
        });

    }
}
