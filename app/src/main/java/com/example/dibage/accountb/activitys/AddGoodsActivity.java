package com.example.dibage.accountb.activitys;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dibage.accountb.R;
import com.example.dibage.accountb.applications.MyApplication;
import com.example.dibage.accountb.dao.AccountDao;
import com.example.dibage.accountb.dao.DaoSession;
import com.example.dibage.accountb.dao.GoodsDao;
import com.example.dibage.accountb.entitys.Goods;
import com.example.dibage.accountb.entitys.ResponseBean;
import com.example.dibage.accountb.entitys.User;
import com.example.dibage.accountb.services.ApiService;
import com.example.dibage.accountb.utils.AccountUtils;
import com.example.dibage.accountb.utils.DateUtils;
import com.example.dibage.accountb.utils.RetrofitManager;
import com.example.dibage.accountb.utils.SimpleUtils;
import com.example.dibage.accountb.utils.ThreadUtils;
import com.example.dibage.accountb.utils.UIUtils;
import com.hss01248.dialog.StyledDialog;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;

public class AddGoodsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btn_clear1;
    private ImageButton btn_clear2;
    private ImageButton btn_clear3;
    private ImageButton btn_clear4;
    private ImageButton btn_clear5;
    private ImageButton btn_clear6;
    EditText et_name;
    EditText et_remain;
    EditText et_sold;
    EditText et_category;
    EditText et_remarks;
    EditText et_price;
    Button btn_Submit;
    ListView listView;

    Toolbar toolbar;
    DaoSession daoSession ;
    AccountDao mAccountDao;
    GoodsDao mGoodsDao;
    private PopupWindow mPopupWindow;

    int length = 12;
    boolean big = true;
    boolean small = true;
    boolean special = false;
    private TextView tv_pwd_random;
    private Button btn_refresh;
    private TextView tv_length;
    private SeekBar seekBar;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private Button btn_cancel;
    private Button btn_copy;


    private Handler mHandler;
    private float alpha = 1.0f;//初始值设为1，为不变暗


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initView();
        initData();
        initEvent();

    }

    private void initData() {
        daoSession = ((MyApplication)getApplication()).getDaoSession();
        mGoodsDao = daoSession.getGoodsDao();
    }

    private void initEvent() {
        btn_clear1.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
        btn_clear3.setOnClickListener(this);
        btn_clear4.setOnClickListener(this);
        btn_clear5.setOnClickListener(this);
        btn_clear6.setOnClickListener(this);
        btn_Submit.setOnClickListener(clickListener);

        //背景变暗的处理
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        //backgroundAlpha((float)msg.obj);
                        UIUtils.darkenBackgroud(AddGoodsActivity.this, (float)msg.obj);
                        break;
                }
            }
        };


    }

    private void initView() {
        et_name = findViewById(R.id.etName);
        et_category = findViewById(R.id.etCategory);
        et_remain = findViewById(R.id.etRemain);
        et_sold = findViewById(R.id.etSold);
        et_remarks = findViewById(R.id.etRemark);
        et_price = findViewById(R.id.etPrice);

        btn_Submit = findViewById(R.id.btnSubmit);
        btn_clear1 = findViewById(R.id.btn_clear1);
        btn_clear2 = findViewById(R.id.btn_clear2);
        btn_clear3 = findViewById(R.id.btn_clear3);
        btn_clear4 = findViewById(R.id.btn_clear4);
        btn_clear5 = findViewById(R.id.btn_clear5);
        btn_clear6 = findViewById(R.id.btn_clear6);
        toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.listview);
        findViewById(R.id.btn_delete).setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("添加库存");
        //给toolbar的左上角的按钮注册点击监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGoodsActivity.this.finish();
            }
        });
;

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        String msg;
        boolean VertifyState = false ;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSubmit:
                    if (SimpleUtils.isNotNull(et_name)){
                        if (SimpleUtils.isNotNull(et_remain)){
                            if(SimpleUtils.isNotNull(et_category)) {
                                addRecord();
                                return;
                            }
                            else
                                msg="请填写产品种类";
                        }else
                            msg = "请填写库存数";
                    } else
                        msg="产品名称不能为空";
                        Toasty.warning(AddGoodsActivity.this, msg, Toast.LENGTH_SHORT, true).show();
                    break;
                default:
                    break;

            }
        }
    };

    //向数据库添加一条记录
    private boolean addRecord() {

        String name = SimpleUtils.getStrings(et_name);
        int remain = SimpleUtils.getInt(et_remain);
        int sold = SimpleUtils.getInt(et_sold);
        String category = SimpleUtils.getStrings(et_category);
        float price = SimpleUtils.getFloat(et_price);
        String remark = SimpleUtils.getStrings(et_remarks);
        String firstChar = AccountUtils.getFirstString(name);
        String adddate = DateUtils.getNowTimeString();

        if(price<0){
            Toasty.warning(AddGoodsActivity.this, "价格不得小于0，请检查后重新填写", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(remain<0){
            Toasty.warning(AddGoodsActivity.this, "库存不得小于0，请检查后重新填写", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(sold<0){
            Toasty.warning(AddGoodsActivity.this, "待出库不得小于0，请检查后重新填写", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(sold>remain){
            Toasty.warning(AddGoodsActivity.this, "待出库数不得大于库存总量", Toast.LENGTH_SHORT, true).show();
            return false;
        }

        Goods toSaveGoods = new Goods(name,remain,sold,category,price,remark,firstChar,adddate);
        StyledDialog.buildLoading("正在保存……").show();
        ApiService apiService = RetrofitManager.getInstance().createService(ApiService.class);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBean<Goods>> goodsCall = apiService.addGoods(toSaveGoods);
                try {
                    ResponseBean<Goods> goodsResponseBean= goodsCall.execute().body();
                    Log.d("返回：",goodsResponseBean.toString());
                    final int code = goodsResponseBean.getCode();
                    final String msg = goodsResponseBean.getMsg();
                    final Goods goods = goodsResponseBean.getData();
                    new Handler(AddGoodsActivity.this.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            StyledDialog.dismissLoading(AddGoodsActivity.this);
                            if(code != 1){
                                Toasty.warning(AddGoodsActivity.this,msg).show();
                                StyledDialog.dismissLoading(AddGoodsActivity.this);
                                Toasty.warning(AddGoodsActivity.this,"保存失败，请稍后再试").show();
                            }else{
                                Toasty.success(AddGoodsActivity.this,msg).show();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent();
                                        AddGoodsActivity.this.setResult(RESULT_OK, intent);//RESULT_OK为自定义常量
                                        AddGoodsActivity.this.finish();
                                    }
                                }, 400);
                            }
                        }
                    });
                } catch (IOException e) {
                    StyledDialog.dismissLoading(AddGoodsActivity.this);
                    Toasty.warning(AddGoodsActivity.this,"保存失败，请稍后再试").show();
                    Log.e("AddGoodsActivity","请求抛异常了");
                    e.printStackTrace();
                }
            }
        };
        ThreadUtils.getCachedPool().execute(runnable);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_clear1:
                et_name.setText("");
                break;
            case R.id.btn_clear2:
                et_remain.setText("");
                break;
            case R.id.btn_clear3:
                et_category.setText("");
                break;
            case R.id.btn_clear4:
                et_price.setText("");
            case R.id.btn_clear5:
                et_remarks.setText("");
                break;
            case R.id.btn_clear6:
                et_sold.setText("");
                break;
        }
    }


}
