package com.ss.shoppingweb.service;

import com.ss.shoppingweb.entity.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.io.IOException;
import java.util.List;

public interface UserService {

    //用户注册
    void register(User user);

    //用户登录
    User login(String name, String password);

    //用户信息修改
    void updateData(User user);

    //获取用户数据
    User getData(String name);

    //获取账户数据
    UserAccount getAccount(String name);

    //账户充值
    void rechargeAccount(Integer OwnerId, double amount);

    //获取流水记录
    List<UserAccountRecorder> getAccountRecorder(Integer id, Integer timeInterval);

    //将商品添加到购物车
    void addToShoppingCart(ShoppingCart shoppingCart);

    /**
     * 将购物车中指定商品数量加1
     */
    void addShoppingCartCommodityNumber(Integer id);

    /**
     * 将购物车中指定商品数量减1
     */
    void subShoppingCartCommodityNumber(Integer id);

    //展示购物车信息
    List<ShoppingCart> showShoppingCart(Integer id) throws IOException;

    //删除商品
    void deleteShoppingcart(List<Integer> ids);

    /**
     * 根据用户id查找收货地址
     */
    List<ShippingAddress> findUserShippingAddressByUserId(Integer userId);

    /**
     * 新建收货地址
     */
    void addShippingAddress(ShippingAddress shippingAddress);

    /**
     * 删除收货地址
     */
    void deleteShippingAddress(Integer id);

    /**
     * 修改收货地址
     */
    void updateShippingAddress(ShippingAddress shippingAddress);

    /**
     * 根据收货地址id查找收货地址
     */
    ShippingAddress findUserShippingAddressById(Integer id);

    /**创建订单*/
    void createOrder(Orders order);

    /**支付订单*/
    void payOrder(Integer orderId);

    /**判断用户余额是否充足*/
    void judgeUserAccount(Integer userId,List<Orders> orders);

    /**撤销订单*/
    void withdrawOrders(Integer orderId);

    /**删除订单*/
    void deleteOrders(Integer orderId);

    /**获取待支付订单*/
    List<Orders> getToPayOrders(Integer userId);

    /**获取已撤销订单*/
    List<Orders> getHaveWithdrawOrders(Integer userId);

    /**获取待发货订单*/
    List<Orders> getToDeliveryOrders(Integer userId);

    /**获取待收货订单*/
    List<Orders> getHaveDeliveryOrders(Integer userId);

    /**请求退款退货*/
    void requestRefund(Integer orderId);

    /**获取待退款订单*/
    List<Orders> getToRefundOrders(Integer userId);

    /**获取已退款订单*/
    List<Orders> getHaveRefundOrders(Integer userId);

    /**获取已完成订单*/
    List<Orders> getHaveFinishOrders(Integer userId);

    /**确认收货*/
    void confirmFinishOrders(Integer orderId);
    /**
     * 根据用户id查找该用户拥有的优惠券
     */
    List<Coupon> findUserCouponByUserId(Integer userId);
}
