package com.simon.geek.ui.user;

import com.simon.agiledevelop.mvpframe.RxPresenter;
import com.simon.agiledevelop.ResultSubscriber;
import com.simon.agiledevelop.log.LLog;
import com.simon.geek.data.DribbbleDataManger;
import com.simon.geek.data.model.User;

import rx.Observable;

/**
 * Created by Simon Han on 2016/9/17.
 */

public class UserInfoPresenter extends RxPresenter<UserInfoContract.View, User> {

    public UserInfoPresenter(UserInfoContract.View view) {
        attachView(view);
        view.setPresenter(this);
    }

    public void loadUserInfo(final int action, long userId) {

        Observable<User> usersInfo = DribbbleDataManger.getInstance().getUsersInfo(userId);
        subscribe(usersInfo, new ResultSubscriber<User>() {
            @Override
            public void onStartRequest() {
                getView().showLoading(action, "");
            }

            @Override
            public void onEndRequest() {
                LLog.d("onCompleted: 用户信息请求完成");
                getView().onCompleted(action);
            }

            @Override
            public void onFailed(Throwable e) {
                getView().onFailed(action, e.getMessage());
            }

            @Override
            public void onResult(User user) {
                getView().showUserInfo(user);
            }
        });
    }
}
