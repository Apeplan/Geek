package com.simon.geek.ui.images;

import com.simon.agiledevelop.ResultSubscriber;
import com.simon.agiledevelop.log.LLog;
import com.simon.agiledevelop.mvpframe.RxPresenter;
import com.simon.geek.data.Api;
import com.simon.geek.data.DataManger;
import com.simon.geek.data.model.BDEntity;
import com.simon.geek.data.model.BDImageEntity;

import java.util.List;

import rx.Observable;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/10
 * @email hanzx1024@gmail.com
 */

public class ImagesPresenter extends RxPresenter<ImagesContract.View, BDEntity> {

    public ImagesPresenter(ImagesContract.View view) {
        attachView(view);
        view.setPresenter(this);
    }

    public void getImages(String tag1, String tag2, int page, int row, final int action) {
        Observable<BDEntity> images = DataManger.getInstance().getImages(tag1, tag2, page, row);
        subscribe(images, new ResultSubscriber<BDEntity>() {
            @Override
            public void onStartRequest() {
                if (action == Api.ACTION_BEGIN) {
                    getView().showLoading(action, "");
                }
            }

            @Override
            public void onEndRequest() {
                if (action == Api.ACTION_BEGIN) {
                    getView().onCompleted(action);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                getView().onFailed(action, e.getMessage());
            }

            @Override
            public void onResult(BDEntity bdEntity) {

                List<BDImageEntity> data = bdEntity.getData();
                if (!data.isEmpty()) {
                    data = data.subList(0, data.size() - 1);
                    if (action == Api.ACTION_REFRESH) {
                        getView().renderRefresh(data);
                    } else if (action == Api.ACTION_MORE) {
                        getView().renderMore(data);
                    } else {
                        getView().showImages(data);
                    }
                    LLog.d("images.size()=  " + data.size());
                }

            }
        });
    }
}
