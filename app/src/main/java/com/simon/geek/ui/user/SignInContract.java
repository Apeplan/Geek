package com.simon.geek.ui.user;

import com.simon.mvp_frame.BaseView;

/**
 *
 * Created by: Simon
 * Created on: 2016/8/20 17:00
 * Email: hanzx1024@gmail.com
 */

public interface SignInContract {

    interface View extends BaseView {
        /**
         * 登录成功
         */
        void signSuccess();

    }
}
