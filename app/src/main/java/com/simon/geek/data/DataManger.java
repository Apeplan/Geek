package com.simon.geek.data;

import com.simon.agiledevelop.ServiceHelper;
import com.simon.geek.data.model.BDEntity;
import com.simon.geek.data.model.ShotEntity;
import com.simon.geek.data.model.TokenEntity;
import com.simon.geek.data.model.User;
import com.simon.geek.data.remote.DataService;
import com.simon.geek.util.DribbblePrefs;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Simon Han on 2016/8/20.
 */

public class DataManger {

    private final DataService mDataService;

    private DataManger() {
        mDataService = DataService.Creator.dribbbleApi();
    }

    /**
     * 单例控制器
     */
    private static class SingletonHolder {
        private static final DataManger INSTANCE = new DataManger();
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static DataManger getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 认证用户，并获取用户信息
     *
     * @param code
     * @return
     */
    public Observable<User> getTokenAndUser(String code) {

        return DataService.Creator.signIn().getToken(Api.CLIENT_ID, Api.CLIENT_SECRET, code,
                Api.CALLBACK_URL).concatMap(new Func1<TokenEntity,
                Observable<? extends User>>() {

            @Override
            public Observable<? extends User> call(TokenEntity tokenEntity) {

                if (null != tokenEntity) {
                    String access_token = tokenEntity.access_token;
                    DribbblePrefs.getInstance().setAccessToken(access_token);
                }

                DataService creator = ServiceHelper.getInstance().creator(Api.SIGNIN_URL,
                        DataService.class);
                return creator.getUserInfo();
            }
        });
    }

    /**
     * 获取 Shots 列表
     *
     * @param page      请求的页数
     * @param list      类型限制：animated、attachments、debuts、playoffs、rebounds、teams
     * @param timeframe 时间：week、month、year、ever
     * @param sort      排序：comments、recent、views
     * @return
     */
    public Observable<List<ShotEntity>> getShotsList(int page, @DataService.ShotType String
            list, String timeframe, String sort) {
        return mDataService.getShots(page, list, timeframe, sort);
    }

    /**
     * 根据 id 获取 Shot 信息
     *
     * @param shotId id
     * @return
     */
    public Observable<ShotEntity> getShot(long shotId) {
        return mDataService.getShot(shotId);
    }

    public Observable<User> getUsersInfo(long userId) {
        return mDataService.getUsers(userId);
    }

    public Observable<List<ShotEntity>> search(String key, int resultPage, @DataService
            .SortOrder String sort) {

        return ServiceHelper.getInstance().creator(Api.SIGNIN_URL, DataService.class)
                .search(key, resultPage, 12, sort);
    }

    public Observable<BDEntity> getImages(String tag1, String tag2, int page, int row) {
        return DataService.Creator.imageService().images(tag1, tag2, page, row, "utf8");
    }

}
