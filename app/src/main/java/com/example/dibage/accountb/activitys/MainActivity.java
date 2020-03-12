package com.example.dibage.accountb.activitys;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.dibage.accountb.R;
import com.example.dibage.accountb.adapters.GoodsAdapter;
import com.example.dibage.accountb.applications.MyApplication;
import com.example.dibage.accountb.dao.AccountDao;
import com.example.dibage.accountb.dao.DaoSession;
import com.example.dibage.accountb.dao.GoodsDao;
import com.example.dibage.accountb.entitys.Account;
import com.example.dibage.accountb.entitys.Goods;
import com.example.dibage.accountb.entitys.User;
import com.example.dibage.accountb.services.ApiService;
import com.example.dibage.accountb.utils.RetrofitManager;
import com.example.dibage.accountb.utils.ThreadUtils;
import com.example.dibage.accountb.utils.UIUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;

import static com.example.dibage.accountb.activitys.MoreActivity.RECORVRY_DATA;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    public static final int ADD_ACCOUNT = 1;
    Context context = MainActivity.this;
    static boolean isPause = false;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabAddAccount;
    private FloatingActionButton fabAddIdCard;
//    private WaveSideBar sideBar;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private PopupWindow mPopWindow;
    private PopupWindow mPopTip;
    private LinearLayout ll_empty;
    List<Account> accountsList;
    List<Goods> goodsList;
    QueryBuilder<Goods> qb;
    GoodsAdapter mGoodsAdapter;
    DaoSession daoSession;
    AccountDao mAccountDao;
    GoodsDao mGoodsDao;

    private float alpha = 1.0f;//初始值设为1，为不变暗
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initFBI();
        initData();
        initView();
        initEvent();
    }

    private void initEvent() {

        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {}
            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {}
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {}
            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        };

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mGoodsAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // 开启滑动删除
//        mGoodsAdapter.enableSwipeItem();
//        mGoodsAdapter.setOnItemSwipeListener(onItemSwipeListener);

        mGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, EditGoodsActivity.class);
                intent.putExtra("account_data", (goodsList.get(position)));
                startActivity(intent);
//                mPopWindow.dismiss();
            }
        });
        fabAddAccount.setOnClickListener(FablickListener);
        fabAddIdCard.setOnClickListener(FablickListener);

        //背景变暗的处理
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        UIUtils.darkenBackgroud(MainActivity.this, (float) msg.obj);
                        break;
                }
            }
        };

    }

    private void initData() {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        mAccountDao = daoSession.getAccountDao();
        mGoodsDao = daoSession.getGoodsDao();
        //获取queryBuilder，通过queryBuilder来实现查询功能
        //mAccountDao.queryBuilder()表示查询所有，
        // orderAsc(AccountDao.Properties.Firstchar)表示按照首字母升序排序（#比A大，所以需要用函数重新排序）
//        qb = mAccountDao.queryBuilder().orderAsc(AccountDao.Properties.Firstchar, AccountDao.Properties.Username);
        qb = mGoodsDao.queryBuilder().orderDesc(GoodsDao.Properties.Name, GoodsDao.Properties.Adddate);
        accountsList = new ArrayList<>();
        if(goodsList==null)
            goodsList = new ArrayList<>();
        goodsList.clear();
        goodsList.addAll(qb.list());

        if (goodsList.size() > 0) {
            ll_empty.setVisibility(View.GONE);
        } else {
            ll_empty.setVisibility(View.VISIBLE);
        }
        if(mGoodsAdapter==null)
            mGoodsAdapter = new GoodsAdapter(R.layout.item_goods,goodsList);
        recyclerView.setAdapter(mGoodsAdapter);
        if(mGoodsAdapter!=null)
            mGoodsAdapter.notifyDataSetChanged();



    }

    private void initFBI() {
        floatingActionMenu = findViewById(R.id.fabMenu);
        fabAddAccount = findViewById(R.id.fabAddAcount);
        fabAddIdCard = findViewById(R.id.fabAddIdCard);
        recyclerView = findViewById(R.id.recyclerView);
        ll_empty = findViewById(R.id.ll_empty);
        toolbar = findViewById(R.id.toolbar);

        ApiService apiService = RetrofitManager.getInstance().createService(ApiService.class);
        User loginUser = new User("123","123","123");
        Log.e("开始","开始测试网络请求");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<User> userCall = apiService.login(loginUser);
                try {
                    userCall.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ThreadUtils.getCachedPool().execute(runnable);

        Log.e("开始","结束测试网络请求");

    }


    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("出入库管家");
        toolbar.setOnMenuItemClickListener((android.support.v7.widget.Toolbar.OnMenuItemClickListener) onMenuItemClick);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
        if (floatingActionMenu.isOpened())
            floatingActionMenu.close(true);

    }

    @Override
    protected void onResume() {//每次onResume都会刷新一次数据
        super.onResume();
        Log.d("AAA","onResume了");
        initData();

    }

    public class myItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Account account = (Account) adapterView.getItemAtPosition(i);
            showPopupWindow(account);
        }
    }



    private void showPopupWindow(final Account account) {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.from(MainActivity.this).inflate(R.layout.pop_detail, null);
        tv1 = contentView.findViewById(R.id.tv_description);
        tv2 = contentView.findViewById(R.id.tv_username);
        tv3 = contentView.findViewById(R.id.tv_password);
        tv4 = contentView.findViewById(R.id.tv_remarks);

        tv1.setText(account.getDescription());
        tv2.setText(account.getUsername());
        tv3.setText(account.getPassword());
        tv4.setText(account.getRemark());
        mPopWindow = new PopupWindow(contentView,
                getWindowManager().getDefaultDisplay().getWidth() - 200, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);

        //显示popupWindow
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mPopWindow.setAnimationStyle(R.style.Popupwindow);
        mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        //UIUtils.darkenBackgroud(MainActivity.this, 0.5f);

        darkWindow();

        LinearLayout layout1 = contentView.findViewById(R.id.layout1);
        LinearLayout layout2 = contentView.findViewById(R.id.layout2);
        LinearLayout layout3 = contentView.findViewById(R.id.layout3);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(account.getUsername());
                Toasty.success(context, "账号已复制", Toast.LENGTH_SHORT, true).show();
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(account.getPassword());
                Toasty.success(context, "密码已复制", Toast.LENGTH_SHORT, true).show();
            }
        });

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                brightWindow();

            }
        });
    }

    private void showPopupMenu(final Goods goods) {

        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.from(MainActivity.this).inflate(R.layout.pop_menu, null);

        mPopWindow = new PopupWindow(contentView,
                getWindowManager().getDefaultDisplay().getWidth() - 250, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);

        //显示popupWindow
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        UIUtils.darkenBackgroud(MainActivity.this, 0.5f);

        LinearLayout layout1 = contentView.findViewById(R.id.layout1);
        LinearLayout layout2 = contentView.findViewById(R.id.layout2);

        //修改账号
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditGoodsActivity.class);
                intent.putExtra("account_data", goods);
                startActivity(intent);
                mPopWindow.dismiss();
            }
        });

        //删除账号
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });


        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                UIUtils.darkenBackgroud(MainActivity.this, 1f);
            }
        });
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


    private void brightWindow() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha < 1.0f) {
                    try {
                        Thread.sleep(3);//每0.004s变暗0.01
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    alpha += 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void darkWindow() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        Thread.sleep(3);//每0.004s变暗0.01
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

}
