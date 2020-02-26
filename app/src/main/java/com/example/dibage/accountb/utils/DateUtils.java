package com.example.dibage.accountb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zjw on 2020/2/26.
 */

public class DateUtils {
    //取得当前系统时间，返回yyyy-MM-dd HH:mm:ss字符串
    public static  String  getNowTimeString(){
        Date date=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMddhhmmss");
        String time=formatter.format(date);
        return time;
    }

}
