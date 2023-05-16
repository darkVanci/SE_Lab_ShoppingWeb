package com.ss.shoppingweb.service;

import com.ss.shoppingweb.entity.ShoppingCart;
import com.ss.shoppingweb.entity.User;
import com.ss.shoppingweb.entity.UserAccount;
import com.ss.shoppingweb.entity.UserAccountRecorder;
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
    void rechargeAccount(Integer OwnerId,double amount);

    //获取流水记录
    List<UserAccountRecorder> getAccountRecorder(Integer id,Integer timeInterval);

    //将商品添加到购物车
    void addToShoppingCart(ShoppingCart shoppingCart);

    //展示购物车信息
    List<ShoppingCart> showShoppingCart(Integer id) throws IOException;

    //删除商品
    void deleteShoppingcart(List<Integer> ids);
}
