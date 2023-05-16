package com.ss.shoppingweb.mapper;

import com.ss.shoppingweb.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    /** 插入用户数据*/
    @Insert( "INSERT INTO user (name, password, phone, idCard, email) VALUES (#{name}, #{password}, #{phone}, #{idCard}, #{email})")
    Integer insertUser(User user);

    /** 根据用户名查询用户数据*/
    @Select("SELECT * FROM user where name=#{name}")
    User findUserByName(String name);

    /**根据手机号查询用户数据*/
    @Select("SELECT * FROM user where phone=#{phone}")
    User findUserByPhone(String phone);

    /**根据身份证号查询用户数据*/
    @Select("SELECT * FROM user where idCard=#{idCard}")
    User findUserByIdcard(String identity);

    /**根据邮箱查询用户数据*/
    @Select("SELECT * FROM user where email=#{email}")
    User findUserByEmail(String email);

    /** 根据用户名查询用户数据*/
    @Select("SELECT * FROM user where name=#{name} and idCard!=#{idCard}")
    User findUserByNameExcpIdcard(String name,String idCard);

    /**根据手机号查询用户数据*/
    @Select("SELECT * FROM user where phone=#{phone} and idCard!=#{idCard}")
    User findUserByPhoneExcpIdcard(String phone,String idCard);

    /**根据邮箱查询用户数据*/
    @Select("SELECT * FROM user where email=#{email} and idCard!=#{idCard}")
    User findUserByEmailExcpIdcard(String email,String idCard);


    /**根据身份证修改用户数据*/
    @Update("update user set name=#{name},password=#{password},phone=#{phone},email=#{email} where idCard=#{idCard}")
    Integer updateData(User user);

    /**插入用户账户数据*/
    @Insert("Insert into useraccount (ownerId,amount) values (#{ownerId},#{amount})")
    Integer insertUserAccount(UserAccount userAccount);

    /**根据id查账户*/
    @Select("Select * from useraccount where ownerId=#{ownerId}")
    UserAccount findUserAccountByOwnerId(Integer ownerId);

    /**根据id查流水记录*/
    @Select("Select * from useraccountrecorder where userId=#{userId}")
    List<UserAccountRecorder> findUserAccountRecorderByUserId(Integer userId);

    /**修改账户*/
    @Update("Update useraccount set amount=#{amount} where ownerId=#{ownerId}")
    Integer updateAccount(double amount,Integer ownerId);



    /**将商品添加到购物车*/
    @Insert("INSERT INTO shoppingcart (userId,shopId,commodityId,commodityName,introduction,price,commodityNum,commodityCategoryName,businessState,imageUrl,commodityShopName) VALUES (#{userId}, #{shopId},#{commodityId},#{commodityName},#{introduction},#{price},#{commodityNum},#{commodityCategoryName},#{businessState},#{imageUrl},#{commodityShopName}) ")
    Integer addToShoppingCart(ShoppingCart shoppingCart);

    /**展示购物车信息*/
    @Select("select * from shoppingcart where userId = #{id}")
    List<ShoppingCart> showShoppingCart(Integer id);

    /**删除单个商品*/
    @Delete("delete from shoppingcart where id=#{id}")
    Integer deleteShoppingCart(Integer id);


    /**根据商品ID查询商品信息*/
    @Select("select * from commodity where id = #{id} ")
    Commodity getCommodityDataById(Integer id);


    /**通过商店ID查询商店信息*/
    @Select("SELECT * from shop where id = #{shopId}")
    Shop getShopDataByShopId(Integer shopId);
}
