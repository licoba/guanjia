package com.example.dibage.accountb.activitys;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.dibage.accountb.dao.DaoSession;
import com.example.dibage.accountb.dao.GoodsDao;
import com.example.dibage.accountb.entitys.Goods;
import com.example.dibage.accountb.utils.AccountUtils;
import com.example.dibage.accountb.utils.DateUtils;
import com.example.dibage.accountb.utils.SimpleUtils;

import es.dmoral.toasty.Toasty;

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
    ListView listView;

    Toolbar toolbar;
    DaoSession daoSession ;
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
//    private Account account;
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
        mGoodsDao = daoSession.getGoodsDao();
        mGoods = (Goods) getIntent().getSerializableExtra("account_data");
    }

    private void initEvent() {
        btn_clear1.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
        btn_clear3.setOnClickListener(this);
        btn_clear4.setOnClickListener(this);
        btn_clear5.setOnClickListener(this);
        btn_clear6.setOnClickListener(this);
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
        toolbar.setTitle("编辑库存");

        et_name.setText(mGoods.getName());
        et_remain.setText(mGoods.getRemain()+"");
        et_sold.setText(mGoods.getSold()+"");
        et_category.setText(mGoods.getCategory());

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
                                ModifyRecord();
                                EditGoodsActivity.this.finish();
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
    private void ModifyRecord() {
        String name = SimpleUtils.getStrings(et_name);
        int remain = SimpleUtils.getInt(et_remain);
        int sold = SimpleUtils.getInt(et_sold);
        String category = SimpleUtils.getStrings(et_category);
        float price = SimpleUtils.getFloat(et_price);
        String remark = SimpleUtils.getStrings(et_remarks);
        String firstChar = AccountUtils.getFirstString(name);
        String adddate = DateUtils.getNowTimeString();
        Goods goods = new Goods(mGoods.getId(),name,remain,sold,category,price,remark,firstChar,adddate);
        mGoodsDao.update(goods);

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
                showPopRandom();
                break;
        }
    }

    private void showPopRandom() {
        mPopupWindow = new PopupWindow();
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.from(EditGoodsActivity.this).inflate(R.layout.pop_random, null);
        View rootview = inflater.from(EditGoodsActivity.this). inflate(R.layout.activity_add_account, null);
        mPopupWindow = new PopupWindow(contentView,
                getWindowManager().getDefaultDisplay().getWidth() - 200, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

        tv_pwd_random = contentView.findViewById(R.id.tv_pwd_random);
        tv_length = contentView.findViewById(R.id.tv_length);
        seekBar = contentView.findViewById(R.id.seekBar);
        checkBox1 = contentView.findViewById(R.id.checkBox1);
        checkBox2 = contentView.findViewById(R.id.checkBox2);
        checkBox3 = contentView.findViewById(R.id.checkBox3);
        btn_cancel = contentView.findViewById(R.id.btn_cancel);
        btn_refresh =contentView.findViewById(R.id.btn_refresh);
        btn_copy = contentView.findViewById(R.id.btn_copy);

        initPopEvent();

    }

    private void initPopEvent() {
        tv_length.setText("["+length+"]");
        seekBar.setProgress(length-4);
        checkBox1.setChecked(big);
        checkBox2.setChecked(small);
        checkBox3.setChecked(special);

        refresh();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) getApplicationContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tv_pwd_random.getText());
                mPopupWindow.dismiss();
                Toasty.success(EditGoodsActivity.this, "已复制："+tv_pwd_random.getText(), Toast.LENGTH_SHORT, false).show();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                length = progress+4;
                tv_length.setText("["+length+"]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                refresh();
            }
        });


        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    big = true;
                    refresh();
                }else{
                    big = false;
                    refresh();
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    small = true;
                    refresh();
                }else{
                    small = false;
                    refresh();
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    special = true;
                    refresh();
                }else{
                    special = false;
                    refresh();
                }
            }
        });
    }

    private void refresh() {
        tv_pwd_random.setText(SimpleUtils.getRandomPwd(length,big,small,special));
    }

}
