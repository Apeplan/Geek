package com.simon.geek.ui.user;

import com.simon.geek.data.model.User;
import com.simon.mvp_frame.BaseView;

/**
 *
 * Created by: Simon
 * Created on: 2016/9/17 16:56
 * Email: hanzx1024@gmail.com
 */

public interface UserInfoContract {

    interface View extends BaseView {
        void showUserInfo(User user);
    }

}
