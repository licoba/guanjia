package com.example.licoba.guanjia.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.licoba.guanjia.R;
import com.example.licoba.guanjia.adapters.ChangeColorAdapter;
import com.example.licoba.guanjia.adapters.GoodsAdapter;
import com.example.licoba.guanjia.applications.MyApplication;
import com.example.licoba.guanjia.dao.DaoSession;
import com.example.licoba.guanjia.dao.GoodsDao;
import com.example.licoba.guanjia.entitys.Account;
import com.example.licoba.guanjia.entitys.Goods;
import com.example.licoba.guanjia.msg.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private Context context;
    List<Account> accountsList = new ArrayList<>();
    List<Goods> mGoodsList = new ArrayList<>();

    private Button btn_cancel;
    private TextView tv_tip;
    private EditText et_search;
    private RecyclerView mRecyclerView;
    GoodsAdapter mGoodsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = getApplicationContext();
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_tip = findViewById(R.id.tv_tip);
        mRecyclerView = findViewById(R.id.recyclerView);
        et_search = findViewById(R.id.et_search);
        et_search.addTextChangedListener(new mTextWatcher());
//        et_search.getFocusable(true);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        if(mGoodsAdapter==null) {
            mGoodsAdapter = new GoodsAdapter(R.layout.item_goods, mGoodsList);
            mRecyclerView.setAdapter(mGoodsAdapter);
        }
        initEvent();
    }

    public void initData(){
        String s;
        s = et_search.getText().toString().trim();
        if (!s.equals("")) {
            List<Goods> resultList = new ArrayList<>();
            for (int i = 0; i < MyApplication.goodsList.size(); i++) {
                Goods goods = MyApplication.goodsList.get(i);
                if(goods.getName().contains(s)||goods.getCategory().contains(s)){
                    resultList.add(goods);
                }
            }
            mGoodsList.clear();
            mGoodsList.addAll(resultList);
            tv_tip.setVisibility(View.GONE);
            if(mGoodsList.size()==0){
                tv_tip.setText("抱歉，无匹配记录");
                tv_tip.setVisibility(View.VISIBLE);
            }
        }else{
            mGoodsList.clear();
            tv_tip.setText("请输入关键字");
            tv_tip.setVisibility(View.VISIBLE);
        }
        ChangeColorAdapter accountAdapter = new ChangeColorAdapter(context, R.layout.item_listview, accountsList);
        accountAdapter.setsearchString(s);
        if(mGoodsAdapter==null) {
            mGoodsAdapter = new GoodsAdapter(R.layout.item_goods, mGoodsList);
            mRecyclerView.setAdapter(mGoodsAdapter);
        }
        mGoodsAdapter.notifyDataSetChanged();
    }

    public void initEvent(){

        EventBus.getDefault().register(this);
        mGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchActivity.this, EditGoodsActivity.class);
                intent.putExtra("account_data", (mGoodsList.get(position)));
                startActivityForResult(intent,1);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveDataUpdate(MessageEvent messageEvent) {
        if(messageEvent.getContext().getClass().isAssignableFrom(MainActivity.class)) {
            initData();
            Log.e("SearchActivity","响应数据更新事件");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class mTextWatcher implements TextWatcher {
        String s;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //禁止输入空格
            if (s.toString().contains(" ")) {
                String[] str = s.toString().split(" ");
                String str1 = "";
                for (int i = 0; i < str.length; i++) {
                    str1 += str[i];
                }
                et_search.setText(str1);
                et_search.setSelection(start);
            }
        }

        //文本变化之后要做的事：
        // 得到文本输入框中的内容
        // 先从数据库查数据，然后更新到ListView
        //把ListView中所有s字符变色
        @Override
        public void afterTextChanged(Editable editable) {
            s = editable.toString().trim();
            Log.e("SA输入字符：", s);
            if (!s.equals("")) {
                List<Goods> resultList = new ArrayList<>();
                for (int i = 0; i < MyApplication.goodsList.size(); i++) {
                        Goods goods = MyApplication.goodsList.get(i);
                        if(goods.getName().contains(s)||goods.getCategory().contains(s)){
                            resultList.add(goods);
                        }
                }
                mGoodsList.clear();
                mGoodsList.addAll(resultList);

                tv_tip.setVisibility(View.GONE);
                if(mGoodsList.size()==0){
                    tv_tip.setText("抱歉，无匹配记录");
                    tv_tip.setVisibility(View.VISIBLE);
                }
            }else{
                mGoodsList.clear();
                tv_tip.setText("请输入关键字");
                tv_tip.setVisibility(View.VISIBLE);
            }
            ChangeColorAdapter accountAdapter = new ChangeColorAdapter(context, R.layout.item_listview, accountsList);
            accountAdapter.setsearchString(s);
            if(mGoodsAdapter==null) {
                mGoodsAdapter = new GoodsAdapter(R.layout.item_goods, mGoodsList);
                mRecyclerView.setAdapter(mGoodsAdapter);
            }
            mGoodsAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
            if(resultCode==RESULT_OK){
                MessageEvent messageEvent = new MessageEvent("数据更新了",SearchActivity.this);
                EventBus.getDefault().post(messageEvent);
            }
    }
}
