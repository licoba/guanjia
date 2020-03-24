package com.example.licoba.guanjia.services;

import com.example.licoba.guanjia.entitys.Goods;
import com.example.licoba.guanjia.entitys.ResponseBean;
import com.example.licoba.guanjia.entitys.User;

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

    //删除货物
    @POST("goods/delete")
    @Headers({"Content-Type: application/json"})
    Call<ResponseBean<Object>> deleteGoods(@Body Goods goods);


    //修改货物信息
    @POST("goods/update")
    @Headers({"Content-Type: application/json"})
    Call<ResponseBean<Object>> updateGoods(@Body Goods goods);

}
