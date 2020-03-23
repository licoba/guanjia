package com.example.dibage.accountb.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dibage.accountb.R;
import com.example.dibage.accountb.entitys.ResponseBean;
import com.example.dibage.accountb.entitys.User;
import com.example.dibage.accountb.services.ApiService;
import com.example.dibage.accountb.utils.RetrofitManager;
import com.example.dibage.accountb.utils.SPUtils;
import com.example.dibage.accountb.utils.ThreadUtils;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    final String TAG = "LoginActivity";
    EditText etName;
    EditText etPwd;
    Button btnLogin;
    TextView btnToRegister;
    LinearLayout qqLayout;
    LinearLayout weixinLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFBI();
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        weixinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.warning(LoginActivity.this,"功能正在开发中").show();
            }
        });
        qqLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.warning(LoginActivity.this,"功能正在开发中").show();
            }
        });
    }



    //登录方法
    private void doLogin() {
        String pwd = etPwd.getText().toString().trim();
        String phone = etName.getText().toString().trim();
        if(phone.isEmpty()){
            Toasty.warning(LoginActivity.this,"请输入手机号").show();
            return;
        }else if(pwd.isEmpty()){
            Toasty.warning(LoginActivity.this,"请输入密码").show();
            return;
        }
        StyledDialog.buildLoading("登录中").show();
        ApiService apiService = RetrofitManager.getInstance().createService(ApiService.class);
        User loginUser = new User(phone,pwd,phone);
        Log.e(TAG,"开始登录网络请求");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBean<User>> userCall = apiService.login(loginUser);
                try {
                    // userCall.execute().toString():Response{protocol=http/1.1, code=200, message=OK, url=http://10.30.66.25:3000/user/login}
                    // userCall.execute().body().toString():{code=1.0, msg=登录成功, data={_id=5e68ce69aa0652e5cb5994a9, username=licoba, password=123456, phone=17322309201, __v=0.0}}

                    ResponseBean<User> userResponseBean= userCall.execute().body();
                    Log.d("返回：",userResponseBean.toString());
                    final int code = userResponseBean.getCode();
                    final String msg = userResponseBean.getMsg();
                    final User user = userResponseBean.getData();

                    new Handler(LoginActivity.this.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                            StyledDialog.dismissLoading(LoginActivity.this);
                            if(user == null){
                                Toasty.warning(LoginActivity.this,msg).show();
                            }else{
                                Log.e(TAG,"保存的user："+user.toString());
                                SPUtils.put(LoginActivity.this,"localUser",user);
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                LoginActivity.this.finish();
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
        //handler 处理返回的请求结果
        String userString = (String) SPUtils.get(LoginActivity.this,"localUser",null);
//        Log.e("userString",userString);
        if(userString!=null){
            User user =new Gson().fromJson(userString,User.class);
            Log.e("AAAA",SPUtils.get(LoginActivity.this,"localUser",null).toString());
            etName.setText(user.getPhone());
            etPwd.setText(user.getPassword());
        }
    }

    private void initView() {

    }

    private void initFBI() {
        etName = findViewById(R.id.etPhone);
        etPwd = findViewById(R.id.etPwd);
        btnLogin = findViewById(R.id.btn_login);
        btnToRegister = findViewById(R.id.tvRegister);
        qqLayout = findViewById(R.id.qqLayout);
        weixinLayout = findViewById(R.id.wechatLayout);
    }
}
