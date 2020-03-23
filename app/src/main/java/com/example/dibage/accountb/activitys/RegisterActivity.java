package com.example.dibage.accountb.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dibage.accountb.R;
import com.example.dibage.accountb.entitys.ResponseBean;
import com.example.dibage.accountb.entitys.User;
import com.example.dibage.accountb.services.ApiService;
import com.example.dibage.accountb.utils.RetrofitManager;
import com.example.dibage.accountb.utils.ThreadUtils;
import com.hss01248.dialog.StyledDialog;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {
    final String TAG = "RegisterActivity";
    Context mContext;
    EditText etName;
    EditText etPwd;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        initFBI();
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
    }

    private void doRegister() {
        String pwd = etPwd.getText().toString().trim();
        String phone = etName.getText().toString().trim();
        if(phone.isEmpty()){
            Toasty.warning(mContext,"请输入手机号").show();
            return;
        }else if(pwd.isEmpty()){
            Toasty.warning(mContext,"请输入密码").show();
            return;
        }
        StyledDialog.buildLoading("注册中").show();
        ApiService apiService = RetrofitManager.getInstance().createService(ApiService.class);
        User registerUser = new User(phone,pwd,phone);
        Log.e(TAG,"开始登录网络请求");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBean<User>> userCall = apiService.register(registerUser);
                try {
                    ResponseBean<User> userResponseBean= userCall.execute().body();
                    Log.d("返回：",userResponseBean.toString());
                    final int code = userResponseBean.getCode();
                    final String msg = userResponseBean.getMsg();
                    final User user = userResponseBean.getData();

                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                            StyledDialog.dismissLoading((Activity) mContext);
                            if(user == null){
                                Toasty.warning(mContext,msg).show();
                            }else{
                                Toasty.success(mContext,msg).show();
                                RegisterActivity.this.finish();
                            }
                        }
                    });


                } catch (IOException e) {
                    Log.e(TAG,"请求抛异常了");
                    e.printStackTrace();
                }
            }
        };
        ThreadUtils.getCachedPool().execute(runnable);
    }

    private void initData() {
    }

    private void initView() {
    }

    private void initFBI() {
        etName = findViewById(R.id.etPhone);
        etPwd = findViewById(R.id.etPwd);
        btnRegister = findViewById(R.id.btn_register);
    }
}
