package com.example.dibage.accountb.activitys;

import android.content.Context;
import android.os.Bundle;
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

import com.example.dibage.accountb.R;
import com.example.dibage.accountb.adapters.ChangeColorAdapter;
import com.example.dibage.accountb.adapters.GoodsAdapter;
import com.example.dibage.accountb.applications.MyApplication;
import com.example.dibage.accountb.dao.DaoSession;
import com.example.dibage.accountb.dao.GoodsDao;
import com.example.dibage.accountb.entitys.Account;
import com.example.dibage.accountb.entitys.Goods;

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
                //获取dao实例
                DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
                GoodsDao mGoodsDao = daoSession.getGoodsDao();
                //获取queryBuilder，通过queryBuilder来实现查询功能
                QueryBuilder<Goods> qb = mGoodsDao.queryBuilder();
                WhereCondition whereCondition1 = GoodsDao.Properties.Name.like('%' + s + '%');
                WhereCondition whereCondition2 = GoodsDao.Properties.Category.like('%' + s + '%');
                qb.whereOr(whereCondition1, whereCondition2);
                qb.orderAsc(GoodsDao.Properties.Name, GoodsDao.Properties.Category);
                mGoodsList.clear();
                mGoodsList.addAll(qb.list());
                Log.e("SA结果:", mGoodsList.toString());
                Log.e("SA结果总数:", mGoodsList.size() + "");
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
}
