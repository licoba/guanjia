package com.example.licoba.guanjia.applications;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.example.licoba.guanjia.R;
import com.example.licoba.guanjia.activitys.MainActivity;
import com.example.licoba.guanjia.dao.DaoMaster;
import com.example.licoba.guanjia.dao.DaoSession;
import com.example.licoba.guanjia.entitys.Goods;
import com.hss01248.dialog.ActivityStackManager;
import com.hss01248.dialog.StyledDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dibage on 2018/3/27.
 */

//初始化数据库和greenDAO核心类

public class MyApplication extends Application {
    private DaoSession daoSession;

    public static List<Goods> goodsList = new ArrayList<>();
    private static  MyApplication instance;
    private static Context context;
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context= getApplicationContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "account-db");
        Database db =  helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        // https://bugly.qq.com/docs/user-guide/advance-features-android-beta/

//        Beta.autoInit = true; // 自动初始化开关
        Beta.autoCheckUpgrade = true; // 自动检查更新开关
        Beta.upgradeCheckPeriod = 1000; // 升级检查周期设置
        Beta.initDelay = 1000;
//        Beta.autoDownloadOnWifi = true;
//        Beta.showInterruptedStrategy = true;//设置点击过确认的弹窗在App下次启动自动检查更新时会再次显示。
//        Beta.canShowUpgradeActs.add(MainActivity.class);//只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。
        Bugly.init(getApplicationContext(), "de2ab8e07a", false);
        StyledDialog.init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                ActivityStackManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStackManager.getInstance().removeActivity(activity);
            }
        });

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
