package com.example.dibage.accountb.entitys;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

/**
 * Created by dibage on 2018/3/27.
 * 货物实体类
 */

//注解实体类Goods
@Entity
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String name;//货物名称
    @NotNull
    private int remain;//库存数量
    @NotNull
    private int sold;//已售未出库数量
    @NotNull
    private String category;//货物种类
    private float price;//价格
    private String remark;//描述
    private String firstchar;//名称的首字母
    private String adddate;//添加日期
    @Generated(hash = 614867072)
    public Goods(Long id, @NotNull String name, int remain, int sold, @NotNull String category, float price, String remark,
            String firstchar, String adddate) {
        this.id = id;
        this.name = name;
        this.remain = remain;
        this.sold = sold;
        this.category = category;
        this.price = price;
        this.remark = remark;
        this.firstchar = firstchar;
        this.adddate = adddate;
    }

    public Goods(String name, int remain, int sold, String category, float price, String remark, String firstchar, String adddate) {
        this.name = name;
        this.remain = remain;
        this.sold = sold;
        this.category = category;
        this.price = price;
        this.remark = remark;
        this.firstchar = firstchar;
        this.adddate = adddate;
    }

    public Goods(String name, int remain, String category, float price, String remark, String firstchar) {
        this.name = name;
        this.remain = remain;
        this.category = category;
        this.price = price;
        this.remark = remark;
        this.firstchar = firstchar;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "name='" + name + '\'' +
                ", remain=" + remain +
                ", sold=" + sold +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                ", firstchar='" + firstchar + '\'' +
                ", adddate='" + adddate + '\'' +
                '}';
    }

    @Generated(hash = 1770709345)
    public Goods() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public int getRemain() {
        return this.remain;
    }
    public void setRemain(int remain) {
        this.remain = remain;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public float getPrice() {
        return this.price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getFirstchar() {
        return this.firstchar;
    }
    public void setFirstchar(String firstchar) {
        this.firstchar = firstchar;
    }

    public String getAdddate() {
        return this.adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public int getSold() {
        return this.sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

}
