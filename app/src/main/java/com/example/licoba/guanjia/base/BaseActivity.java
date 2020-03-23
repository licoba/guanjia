package com.example.licoba.guanjia.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    public  final String TAG = this.getClass().getSimpleName();//TAG，ex：BaseActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFBI();//获取控件实例
        initData();//初始化数据
        initView();//讲数据显示到控件上
        initEvent();//初始化控件的事件
    }


    protected abstract void initFBI();

    protected abstract void initEvent();

    protected abstract void initView();

    protected abstract void initData();
}
    