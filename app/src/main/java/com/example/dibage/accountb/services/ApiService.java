package com.example.dibage.accountb.services;

import com.example.dibage.accountb.entitys.Goods;
import com.example.dibage.accountb.entitys.ResponseBean;
import com.example.dibage.accountb.entitys.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by zjw on 2020/3/12.
 */
public interface  ApiService {

    //登录
    @POST("user/login")
    //表明传的是json格式
    @Headers({"Content-Type: application/json"})
    Call<ResponseBean<User>> login(@Body User user);


    //注册
    @POST("user/register")
    @Headers({"Content-Type: application/json"})
    Call<ResponseBean<User>> register(@Body User user);


    //查询所有货物
    @POST("goods/all")
    @Headers({"Content-Type: application/json"})
    Call<ResponseBean<List<Goods>>> allGoods();

    //添加货物
    @POST("goods/add")
    @Headers({"Content-Type: application/json"})
    Call<ResponseBean<Goods>> addGoods(@Body Goods goods);

}
