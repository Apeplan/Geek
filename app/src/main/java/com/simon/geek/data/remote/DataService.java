package com.simon.geek.data.remote;

import android.support.annotation.StringDef;

import com.simon.common.ServiceHelper;
import com.simon.common.log.LLog;
import com.simon.geek.data.Api;
import com.simon.geek.data.SearchConverter;
import com.simon.geek.data.model.BDEntity;
import com.simon.geek.data.model.ShotEntity;
import com.simon.geek.data.model.TokenEntity;
import com.simon.geek.data.model.User;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/8/22 14:09
 */

public interface DataService {

    /**
     * 获取用户已授权的token
     */
    @FormUrlEncoded
    @POST("oauth/token")
    Observable<TokenEntity> getToken(@Field("client_id") String client_id, @Field("client_secret")
            String client_secret, @Field("code") String code, @Field("redirect_uri") String
                                             redirect_uri);

    /**
     * 获取登陆用户信息
     */
    @GET("user")
    Observable<User> getUserInfo();

    /**
     * 返回 Shots 列表
     *
     * @param page      获取第几页数据
     * @param list      什么类型数据的列表
     * @param timeframe 哪个时间段
     * @param sort      什么顺序
     * @return
     */
    @GET("shots")
    Observable<List<ShotEntity>> getShots(@Query("page") int page, @Query("list") @ShotType
            String list, @Query("timeframe") @ShotTimeframe String timeframe, @Query("sort")
                                          @ShotSort String sort);

    /**
     * 返回单个 Shot 信息
     *
     * @param shotId
     * @return
     */
    @GET("shots/{id}")
    Observable<ShotEntity> getShot(@Path("id") long shotId);

    /**
     * 根据用户的Id获取用户的信息
     *
     * @return
     */
    @GET("users/{usersId}")
    Observable<User> getUsers(@Path("usersId") long id);

    @GET("search")
    Observable<List<ShotEntity>> search(@Query("q") String query,
                                        @Query("page") Integer page,
                                        @Query("per_page") Integer pageSize,
                                        @Query("s") @SortOrder String sort);


     /* Magic Constants */

    String SHOT_TYPE_ANIMATED = "animated";
    String SHOT_TYPE_ATTACHMENTS = "attachments";
    String SHOT_TYPE_DEBUTS = "debuts";
    String SHOT_TYPE_PLAYOFFS = "playoffs";
    String SHOT_TYPE_REBOUNDS = "rebounds";
    String SHOT_TYPE_TEAMS = "teams";
    String SHOT_TIMEFRAME_NOW = "now";
    String SHOT_TIMEFRAME_WEEK = "week";
    String SHOT_TIMEFRAME_MONTH = "month";
    String SHOT_TIMEFRAME_YEAR = "year";
    String SHOT_TIMEFRAME_EVER = "ever";
    String SHOT_SORT_COMMENTS = "comments";
    String SHOT_SORT_RECENT = "recent";
    String SHOT_SORT_VIEWS = "views";
    String SHOT_SORT_POPULARITY = "popularity";

    String SORT_POPULAR = "";
    String SORT_RECENT = "latest";

    // Shot Type
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SHOT_TYPE_ANIMATED,
            SHOT_TYPE_ATTACHMENTS,
            SHOT_TYPE_DEBUTS,
            SHOT_TYPE_PLAYOFFS,
            SHOT_TYPE_REBOUNDS,
            SHOT_TYPE_TEAMS
    })
    @interface ShotType {
    }

    // Shot timeframe
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SHOT_TIMEFRAME_NOW,
            SHOT_TIMEFRAME_WEEK,
            SHOT_TIMEFRAME_MONTH,
            SHOT_TIMEFRAME_YEAR,
            SHOT_TIMEFRAME_EVER
    })
    @interface ShotTimeframe {
    }

    // Short sort order
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SHOT_SORT_COMMENTS,
            SHOT_SORT_RECENT,
            SHOT_SORT_VIEWS,
            SHOT_SORT_POPULARITY
    })
    @interface ShotSort {
    }

    /**
     * magic constants
     **/

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SORT_POPULAR,
            SORT_RECENT
    })
    @interface SortOrder {
    }


    /*
    明星
    http://image.baidu.com/channel/listjson?pn=0&rn=30&tag1=明星&tag2=全部&ie=utf8
    http://image.baidu.com/channel/listjson?pn=0&rn=30&tag1=明星&tag2=全部&ftags=女明星&ie=utf8
    http://image.baidu.com/channel/listjson?pn=0&rn=30&tag1=明星&tag2=全部&ftags=女明星##内地&ie=utf8
     */
    @GET("channel/listjson")
    Observable<BDEntity> images(@Query("tag1") String tag1, @Query("tag2") String tag2, @Query
            ("pn") int page, @Query("rn") int row, @Query("ie") String ie);

    /**
     * 设置一个新服务
     */
    class Creator {
        public static DataService dribbbleApi() {
//            String token = DribbbleApp.spHelper().getString(Api.OAUTH_ACCESS_TOKEN);
            Headers.Builder headers = new Headers.Builder();
            Headers authorization = headers.add("Authorization", "Bearer " + Api.ACCESS_TOKEN)
                    .build();

            return ServiceHelper.getInstance().creator(Api.DRIBBBLE_BASE_URL, DataService
                    .class, authorization);
        }

        public static DataService signIn() {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response proceed = chain.proceed(request);
                    LLog.d("intercept: " + proceed.body());
                    return proceed;
                }
            });
            OkHttpClient client = httpClient.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.SIGNIN_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(DataService.class);
        }

        public static DataService searchApi() {
            return new Retrofit.Builder()
                    .baseUrl(Api.SIGNIN_URL)
                    .addConverterFactory(new SearchConverter.Factory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
                    .create((DataService.class));
        }

        public static DataService imageService() {
            return ServiceHelper.getInstance().creator(Api.BAIDU_IMG, DataService.class);
        }

    }

}
