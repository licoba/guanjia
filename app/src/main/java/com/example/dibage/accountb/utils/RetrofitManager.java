package com.example.dibage.accountb.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by zjw on 2020/3/12.
 */
public class RetrofitUtils {
    private Retrofit mRetrofit;
    final String BASE_URL = "http://67.216.195.108:3000";

    //构造器私有，这个工具类只有一个实例
    private RetrofitUtils() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .baseUrl(BASE_URL)
                .build();
    }


    /**
     * 静态内部类单例模式
     *
     * @return
     */
    public static RetrofitUtils getInstance() {
        return Inner.retrofitManager;
    }

    private static class Inner {
        private static final RetrofitUtils retrofitManager = new RetrofitUtils();
    }


    /**
     * 利用泛型传入接口class返回接口实例
     *
     * @param ser 类
     * @param <T> 类的类型
     * @return Observable
     */
    public <T> T createRs(Class<T> ser) {
        return mRetrofit.create(ser);
    }


}
