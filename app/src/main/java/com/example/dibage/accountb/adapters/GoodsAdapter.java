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
                .setText(R.id.goods_remain,"可售："+(remain-sold)+" 件")
                .setText(R.id.goods_category,category);

        if(category.contains("沙发")){
            helper.setImageResource(R.id.goods_image,R.mipmap.shafa);
        }else if(category.contains("床")||category.contains("垫")||category.contains("席梦思")){
            helper.setImageResource(R.id.goods_image,R.mipmap.chuang);
        }else if(category.contains("茶几")){
            helper.setImageResource(R.id.goods_image,R.mipmap.chaji);
        }else if(category.contains("电视柜")){
            helper.setImageResource(R.id.goods_image,R.mipmap.dianshigui);
        }else if(category.contains("衣柜")){
            helper.setImageResource(R.id.goods_image,R.mipmap.yigui);
        }else if(category.contains("妆")){
            helper.setImageResource(R.id.goods_image,R.mipmap.shuzhuangtai);
        }else if(category.contains("餐桌")){
            helper.setImageResource(R.id.goods_image,R.mipmap.canzhuo);
        }else if(category.contains("柜")){
            helper.setImageResource(R.id.goods_image,R.mipmap.gui);
        }else if(category.contains("椅")){
            helper.setImageResource(R.id.goods_image,R.mipmap.yizi);
        }else{
            helper.setImageResource(R.id.goods_image,R.mipmap.jiaju);
        }



        if(remain-sold<=0){

        }else if(remain-sold==1){

        }

    }
}
