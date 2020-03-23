package com.example.licoba.guanjia.activitys;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.licoba.guanjia.R;
import com.example.licoba.guanjia.adapters.GoodsAdapter;
import com.example.licoba.guanjia.dao.GoodsDao;
import com.example.licoba.guanjia.entitys.Goods;
import com.example.licoba.guanjia.entitys.ResponseBean;
import com.example.licoba.guanjia.services.ApiService;
import com.example.licoba.guanjia.utils.RetrofitManager;
import com.example.licoba.guanjia.utils.ThreadUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ezy.ui.layout.LoadingLayout;
import retrofit2.Call;

import static com.example.licoba.guanjia.activitys.MoreActivity.RECORVRY_DATA;
import static com.scwang.smartrefresh.layout.header.ClassicsHeader.REFRESH_HEADER_UPDATE;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    public static final int ADD_ACCOUNT = 1;
    public static final int EDIT_GOODS = 2;
    Context context = MainActivity.this;
    static boolean isPause = false;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabAddAccount;
    private FloatingActionButton fabAddIdCard;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private PopupWindow mPopWindow;
    private PopupWindow mPopTip;
    private LinearLayout ll_empty;
    List<Goods> goodsList;
    QueryBuilder<Goods> qb;
    GoodsAdapter mGoodsAdapter;
    LoadingLayout loadingLayout;
    GoodsDao mGoodsDao;
    SmartRefreshLayout refreshLayout;
    Context mContext;
    ClassicsHeader header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initFBI();
        initView();
        initEvent();
    }

    private void initEvent() {

        mGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, EditGoodsActivity.class);
                intent.putExtra("account_data", (goodsList.get(position)));
                startActivityForResult(intent,EDIT_GOODS);
            }
        });

        fabAddAccount.setOnClickListener(FablickListener);
        fabAddIdCard.setOnClickListener(FablickListener);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
        refreshLayout.autoRefresh();

    }

    private void initData() {
        ApiService apiService = RetrofitManager.getInstance().createService(ApiService.class);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBean<List<Goods>>> goodsCall = apiService.allGoods();
                try {
                    ResponseBean<List<Goods>> goodsResponseBean= goodsCall.execute().body();
                    Log.d("返回：",goodsResponseBean.toString());
                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(goodsResponseBean.getCode()==1){
                                Log.d("返回：","拉取数据成功");
                                goodsList.clear();
                                goodsList.addAll(goodsResponseBean.getData());
                                mGoodsAdapter.notifyDataSetChanged();
                                refreshLayout.finishRefresh(true);
                                header.setLastUpdateText("产品共计"+goodsList.size()+"种");
                                if(goodsList.size()>0)
                                    loadingLayout.showContent();
                                else
                                    loadingLayout.showEmpty();
                            }else{
                                Log.d("返回：","拉取数据失败");
                                refreshLayout.finishRefresh(false);
                            }
                        }
                    });
                } catch (IOException e) {
                    refreshLayout.finishRefresh(false);
                    Log.e(TAG,"请求抛异常了");
                    e.printStackTrace();
                }
            }
        };
        ThreadUtils.getCachedPool().execute(runnable);
    }



    private void initFBI() {
        mContext = MainActivity.this;
        floatingActionMenu = findViewById(R.id.fabMenu);
        fabAddAccount = findViewById(R.id.fabAddAcount);
        fabAddIdCard = findViewById(R.id.fabAddIdCard);
        recyclerView = findViewById(R.id.recyclerView);
        ll_empty = findViewById(R.id.ll_empty);
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.refreshLayout);
        loadingLayout = findViewById(R.id.loadingLayout);
        header = findViewById(R.id.header);

    }


    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("出入库管家");
        toolbar.setOnMenuItemClickListener((android.support.v7.widget.Toolbar.OnMenuItemClickListener) onMenuItemClick);
        if(goodsList==null)
            goodsList = new ArrayList<>();
        goodsList.clear();
        if(mGoodsAdapter==null)
            mGoodsAdapter = new GoodsAdapter(R.layout.item_goods,goodsList);
        mGoodsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN );//开启加载动画 （渐显、缩放、从下到上，从左到右、从右到左）
        recyclerView.setAdapter(mGoodsAdapter);
        if(mGoodsAdapter!=null)
            mGoodsAdapter.notifyDataSetChanged();
        ll_empty.setVisibility(View.GONE);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableOverScrollBounce(true);
        loadingLayout.showEmpty();
        header.setLastUpdateText("等待库存刷新……");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
        if (floatingActionMenu.isOpened())
            floatingActionMenu.close(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume了");
    }

    //浮动添加按钮的监听器
    private View.OnClickListener FablickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.fabAddAcount:
                    intent = new Intent(context, AddGoodsActivity.class);
                    startActivityForResult(intent,ADD_ACCOUNT);
                    floatingActionMenu.close(true);
                    break;
                case R.id.fabAddIdCard:
                    intent = new Intent(context, AddPhotoActivity.class);
                    intent.putExtra("fromAty", "MainActivity");
                    startActivity(intent);
                    floatingActionMenu.close(true);
                    break;

            }
        }
    };

    private android.support.v7.widget.Toolbar.OnMenuItemClickListener onMenuItemClick = new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    intent = new Intent(context, SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_setting:
                    intent = new Intent(context, MoreActivity.class);
                    startActivityForResult(intent,RECORVRY_DATA);
                    break;
                case R.id.action_card:
                    intent = new Intent(context, CardActivity.class);
                    startActivity(intent);
                    break;
            }
            if (!msg.equals("")) {
                Toasty.success(context, msg, Toast.LENGTH_SHORT, true).show();
            }
            return true;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_ACCOUNT){
            if(resultCode==RESULT_OK) {
                Log.e(TAG, "要刷新数据了");
                refreshLayout.autoRefresh();
            }
        }else  if(requestCode==EDIT_GOODS){
            if(resultCode==RESULT_OK) {
                Log.e(TAG, "要刷新数据了");
                refreshLayout.autoRefresh();
            }
        }
    }
}
