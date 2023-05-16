package com.ss.shoppingweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getter方法、setter方法、toString方法、hashCode方法、equals方法
@NoArgsConstructor //无参构造
@AllArgsConstructor//全参构造
public class ShopOrder {
    private Integer id;//编号
    private Integer shopId;//商店id
    private Integer userId;//顾客id
    private String userName;//顾客用户名
    private Integer commodityId;//商品id
    private String commodityName;//商品名字
    private double commodityPrice;//商品价格
    private Integer commodityNum;//商品数量
    private Double amountSum;//总价
    private Integer state;//0表示订单尚未完成，1表示订单已完成
}
