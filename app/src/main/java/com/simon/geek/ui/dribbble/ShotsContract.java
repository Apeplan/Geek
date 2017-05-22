package com.simon.geek.ui.dribbble;

import com.simon.geek.data.model.ShotEntity;
import com.simon.mvp_frame.BaseView;

import java.util.List;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/8/29 16:17
 */

public interface ShotsContract {

    interface View extends BaseView {

        void renderShotsList(List<ShotEntity> shotsList);

        void renderMoreShotsList(List<ShotEntity> shotsList);

        void renderRefrshShotsList(List<ShotEntity> shotsList);

    }

}
