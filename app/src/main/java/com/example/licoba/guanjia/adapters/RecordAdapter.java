package com.example.licoba.guanjia.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.licoba.guanjia.R;
import com.example.licoba.guanjia.entitys.Record;

import java.util.List;


/**
 * 恢复数据页面的适配器
 */
public class RecordAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {


    public RecordAdapter(int layoutResId, @Nullable List<Record> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record item) {
        helper.setText(R.id.name,item.getName());
        helper.setText(R.id.location,item.getLocation());
        helper.setText(R.id.number,item.getNumber()+"条记录");
    }
}
