package com.example.licoba.guanjia.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.licoba.guanjia.R;
import com.example.licoba.guanjia.applications.MyApplication;
import com.example.licoba.guanjia.commonView.PopWindowTip;
import com.example.licoba.guanjia.dao.DaoSession;
import com.example.licoba.guanjia.dao.GoodsDao;
import com.example.licoba.guanjia.entitys.Goods;
import com.example.licoba.guanjia.entitys.ResponseBean;
import com.example.licoba.guanjia.services.ApiService;
import com.example.licoba.guanjia.utils.AccountUtils;
import com.example.licoba.guanjia.utils.DateUtils;
import com.example.licoba.guanjia.utils.RetrofitManager;
import com.example.licoba.guanjia.utils.SimpleUtils;
import com.example.licoba.guanjia.utils.ThreadUtils;
import com.hss01248.dialog.StyledDialog;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;

//编辑库存
public class EditGoodsActivity extends AppCompatActivity implements View.OnClickListener{

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
    Button btn_delete;
    ListView listView;

    Toolbar toolbar;
    DaoSession daoSession ;
    GoodsDao mGoodsDao;
    private PopupWindow mPopupWindow;

    private Goods mGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initData();
        initView();
        initEvent();

    }

    private void initData() {
        daoSession = ((MyApplication)getApplication()).getDaoSession();

        mGoods = (Goods) getIntent().getSerializableExtra("account_data");
    }

    private void initEvent() {
        btn_clear1.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
        btn_clear3.setOnClickListener(this);
        btn_clear4.setOnClickListener(this);
        btn_clear5.setOnClickListener(this);
        btn_clear6.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_Submit.setOnClickListener(clickListener);
    }

    private void initView() {
        et_name = findViewById(R.id.etName);
        et_category = findViewById(R.id.etCategory);
        et_remain = findViewById(R.id.etRemain);
        et_sold = findViewById(R.id.etSold);
        et_remarks = findViewById(R.id.etRemark);
        et_price = findViewById(R.id.etPrice);
        btn_Submit = findViewById(R.id.btnSubmit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_clear1 = findViewById(R.id.btn_clear1);
        btn_clear2 = findViewById(R.id.btn_clear2);
        btn_clear3 = findViewById(R.id.btn_clear3);
        btn_clear4 = findViewById(R.id.btn_clear4);
        btn_clear5 = findViewById(R.id.btn_clear5);
        btn_clear6 = findViewById(R.id.btn_clear6);
        toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.listview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("商品库存详情");

        et_name.setText(mGoods.getName());
        et_remain.setText(mGoods.getRemain()+"");
        et_sold.setText(mGoods.getSold()+"");
        et_category.setText(mGoods.getCategory());
        et_remarks.setText(mGoods.getRemark());
        et_price.setText(mGoods.getPrice()+"");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditGoodsActivity.this.finish();
            }
        });
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
                                msg = "保存成功";
                                VertifyState = true;
                                if(!ModifyRecord()) return;
                            }
                            else
                                msg="请填写产品种类";
                        }else
                            msg = "请填写库存数";
                    }
                    else
                        msg="产品名称不能为空";
                    if (VertifyState)
                        Toasty.success(EditGoodsActivity.this, msg, Toast.LENGTH_SHORT, true).show();
                    else
                        Toasty.warning(EditGoodsActivity.this, msg, Toast.LENGTH_SHORT, true).show();
                    break;
                default:
                    Toasty.warning(EditGoodsActivity.this, msg, Toast.LENGTH_SHORT, true).show();

            }
        }
    };

    //修改记录
    private boolean ModifyRecord() {
        String name = SimpleUtils.getStrings(et_name);
        int remain = SimpleUtils.getInt(et_remain);
        int sold = SimpleUtils.getInt(et_sold);
        String category = SimpleUtils.getStrings(et_category);
        float price = SimpleUtils.getFloat(et_price);
        String remark = SimpleUtils.getStrings(et_remarks);
        String firstChar = AccountUtils.getFirstString(name);
        String adddate = DateUtils.getNowTimeString();

        if(price<0){
            Toasty.warning(EditGoodsActivity.this, "价格不得小于0，请检查后重新填写", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(remain<0){
            Toasty.warning(EditGoodsActivity.this, "库存不得小于0，请检查后重新填写", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(sold<0){
            Toasty.warning(EditGoodsActivity.this, "待出库不得小于0，请检查后重新填写", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(sold>remain){
            Toasty.warning(EditGoodsActivity.this, "待出库数不得大于库存总量", Toast.LENGTH_SHORT, true).show();
            return false;
        }


        Goods goods = new Goods(mGoods.getId(),name,remain,sold,category,price,remark,firstChar,adddate);
        mGoodsDao.update(goods);

        return  true;

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
            case R.id.btn_getRandom:
                ;
            case R.id.btn_delete:
                showDeleteDialog();
                break;
        }
    }

    private void showDeleteDialog() {
        PopWindowTip popTip = new PopWindowTip(EditGoodsActivity.this){
            @Override
            protected void clickCancel() {

            }

            @Override
            protected void dismissTodo() {

            }

            @Override
            public void clickConfirm() {
//                mGoodsDao.delete(mGoods);
                Goods toDeleteGoods = mGoods;
                StyledDialog.buildLoading("正在删除……").show();
                ApiService apiService = RetrofitManager.getInstance().createService(ApiService.class);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.e("删除id：",toDeleteGoods.getId());
                        Call<ResponseBean<Object>> goodsCall = apiService.deleteGoods(toDeleteGoods);
                        try {
                            ResponseBean<Object> goodsResponseBean= goodsCall.execute().body();
                            Log.d("返回：",goodsResponseBean.toString());
                            final int code = goodsResponseBean.getCode();
                            final String msg = goodsResponseBean.getMsg();
                            final Object data = goodsResponseBean.getData();
                            new Handler(EditGoodsActivity.this.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    StyledDialog.dismissLoading(EditGoodsActivity.this);
                                    if(code != 1){
                                        Toasty.warning(EditGoodsActivity.this,msg).show();
                                        StyledDialog.dismissLoading(EditGoodsActivity.this);
                                        Toasty.warning(EditGoodsActivity.this,"删除失败，请稍后再试").show();
                                    }else{
                                        Toasty.success(EditGoodsActivity.this,msg).show();
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                Intent intent = new Intent();
                                                EditGoodsActivity.this.setResult(RESULT_OK, intent);//RESULT_OK为自定义常量
                                                EditGoodsActivity.this.finish();
                                            }
                                        }, 400);
                                    }
                                }
                            });
                        } catch (IOException e) {
                            StyledDialog.dismissLoading(EditGoodsActivity.this);
                            Toasty.warning(EditGoodsActivity.this,"删除失败，请稍后再试").show();
                            Log.e("AddGoodsActivity","请求抛异常了");
                            e.printStackTrace();
                        }
                    }
                };
                ThreadUtils.getCachedPool().execute(runnable);
            }
        };

        popTip.setTitleAndContent("删除警告", "商品被删除之后将无法被找回，确认删除？");

    }





}
