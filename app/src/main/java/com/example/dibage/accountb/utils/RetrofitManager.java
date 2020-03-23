package com.example.dibage.accountb.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zjw on 2020/3/12.
 */
public class RetrofitManager {
    private Retrofit mRetrofit;
    public static final String BASE_URL = "http://192.168.123.218:3000/";

    //构造器私有，这个工具类只有一个实例
    private RetrofitManager() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create()) // 加上这句话
                .baseUrl(BASE_URL)
                .build();
    }


    /**
     * 静态内部类单例模式
     *
     * @return
     */
    public static RetrofitManager getInstance() {
        return Inner.retrofitManager;
    }

    private static class Inner {
        private static final RetrofitManager retrofitManager = new RetrofitManager();
    }


    /**
     * 利用泛型传入接口class返回接口实例
     *
     * @param service 类
     * @param <T> 类的类型
     * @return Observable
     */
    public <T> T createService(Class<T> service) {
        return mRetrofit.create(service);
    }


}
