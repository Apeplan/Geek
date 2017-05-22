package com.simon.geek.ui.images;

import android.support.annotation.NonNull;

import com.simon.common.log.LLog;
import com.simon.common.utils.ToastHelper;
import com.simon.geek.GeekApp;
import com.simon.geek.data.Api;
import com.simon.geek.data.DataManger;
import com.simon.geek.data.model.BDEntity;
import com.simon.geek.data.model.BDImageEntity;
import com.simon.mvp_frame.ResultSubscriber;
import com.simon.mvp_frame.RxPresenter;

import java.util.List;

import rx.Observable;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/10
 * @email hanzx1024@gmail.com
 */

public class ImagesPresenter extends RxPresenter<ImagesContract.View> {

    public ImagesPresenter(@NonNull ImagesContract.View view) {
        super(view);
    }

    public void getImages(String tag1, String tag2, int page, int row, final int action) {
        Observable<BDEntity> images = DataManger.getInstance().getImages(tag1, tag2, page, row);
        subscribe(images, new ResultSubscriber<BDEntity>() {
            @Override
            public void onStartRequest() {
                if (action == Api.ACTION_BEGIN) {
                    showDialog();
                }
            }

            @Override
            public void onEndRequest() {
                if (action == Api.ACTION_BEGIN) {
                    closeDialog();
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastHelper.showLongToast(GeekApp.context(), e.getMessage());
                getView().error("网络开小差了~");
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
                } else {
                    getView().empty("什么都没有哦~");
                }
            }
        });
    }
}
