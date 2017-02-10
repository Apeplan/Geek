package com.simon.geek.ui.images;

import com.simon.agiledevelop.mvpframe.MvpView;
import com.simon.geek.data.model.BDImageEntity;

import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/10
 * @email hanzx1024@gmail.com
 */

public interface ImagesContract {

    interface View extends MvpView<ImagesPresenter> {
        void showImages(List<BDImageEntity> images);

        void renderMore(List<BDImageEntity> images);

        void renderRefresh(List<BDImageEntity> images);
    }
}
