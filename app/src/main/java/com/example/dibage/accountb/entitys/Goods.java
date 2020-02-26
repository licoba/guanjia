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
    private String category;//货物种类
    private float price;//价格
    private String remark;//描述
    private String firstchar;//名称的首字母
    @Generated(hash = 1592749667)
    public Goods(Long id, @NotNull String name, int remain, @NotNull String category, float price, String remark,
            String firstchar) {
        this.id = id;
        this.name = name;
        this.remain = remain;
        this.category = category;
        this.price = price;
        this.remark = remark;
        this.firstchar = firstchar;
    }

    public Goods(String name, int remain, String category, float price, String remark, String firstchar) {
        this.name = name;
        this.remain = remain;
        this.category = category;
        this.price = price;
        this.remark = remark;
        this.firstchar = firstchar;
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

}
