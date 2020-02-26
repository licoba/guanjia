package com.example.dibage.accountb.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.dibage.accountb.R;
import com.example.dibage.accountb.entitys.Goods;

import java.util.List;

/**
 * 第一个泛型Status是数据实体类型，第二个BaseViewHolder是ViewHolder其目的是为了支持扩展ViewHolder。
 */
public class GoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder>  {


    public GoodsAdapter(int layoutResId, List<Goods> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods goods) {

        String name = goods.getName();
        int remain = goods.getRemain();
        int sold = goods.getSold();
        String category = goods.getCategory();

        helper.setText(R.id.goods_name,name)
                .setText(R.id.goods_remain,"可售:"+(remain-sold)+"件")
                .setText(R.id.goods_category,category);

    }
}
