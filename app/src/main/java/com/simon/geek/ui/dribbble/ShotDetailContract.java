package com.simon.geek.ui.dribbble;

import com.simon.geek.data.model.ShotEntity;
import com.simon.mvp_frame.BaseView;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/8/31 17:45
 */

public interface ShotDetailContract {

    interface View extends BaseView {
        void showShot(ShotEntity shotsEntity);
    }
}
