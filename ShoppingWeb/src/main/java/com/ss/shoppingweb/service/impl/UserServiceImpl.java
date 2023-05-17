package com.ss.shoppingweb.service.impl;

import com.ss.shoppingweb.entity.*;
import com.ss.shoppingweb.exception.*;
import com.ss.shoppingweb.mapper.UserMapper;
import com.ss.shoppingweb.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**用户注册*/
    @Override
    public void register(User user) {
        String name=user.getName();
        String phone=user.getPhone();
        String idCard=user.getIdCard();
        String email=user.getEmail();
        /**判断用户名是否重复*/
        User userSql = userMapper.findUserByName(name);
        if (userSql != null) {
            throw new NameDuplicateException("用户名" + name + "重复！");
        }
        /**判断手机号是否已经注册过*/
        userSql = userMapper.findUserByPhone(phone);
        if (userSql != null) {
            throw new PhoneDuplicateException("手机号" + phone + "已经注册过！");
        }
        /**判断身份证号是否已经注册过*/
        userSql = userMapper.findUserByIdcard(idCard);
        if (userSql != null) {
            throw new IdcardDuplicateException("身份证号" + idCard + "已经注册过！");
        }
        /**判断邮箱是否已经注册过*/
        userSql= userMapper.findUserByEmail(email);
        if (userSql != null) {
            throw new EmailDuplicateException("邮箱" + email + "已经注册过！");
        }
        /**密码加密*/
        String password=passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        /**判断用户数据知否正常插入数据库*/
        Integer rows1 = userMapper.insertUser(user);
        if (rows1 != 1) {
            throw new InsertException("添加用户数据出现未知错误，请联系系统管理员");
        }
        /**关联个人账户*/
        User userInserted=userMapper.findUserByName(name);
        UserAccount userAccount=new UserAccount();
        userAccount.setOwnerId(userInserted.getId());
        userAccount.setAmount(0);
        Integer rows2 = userMapper.insertUserAccount(userAccount);
        if(rows2!=1){
            throw new InsertException("添加用户账户数据出现未知错误，请联系系统管理员");
        }
    }

    /**用户登录*/
    @Override
    public User login(String name,String password){
        //根据用户名查询用户数据
        User user=userMapper.findUserByName(name);
        //判断用户是否存在
        if(user==null){
            throw new UserNotFoundException("用户"+name+"不存在！");
        }
        //判断密码是否输入正确
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new PasswordNotMatchException("密码输入不正确！");
        }
        return user;
    }

    /**信息修改*/
    @Override
    public void updateData(User user){
        String name=user.getName();
        String phone=user.getPhone();
        String email=user.getEmail();
        String idCard=user.getIdCard();
        /**判断用户名是否重复*/
        User userSql = userMapper.findUserByNameExcpIdcard(name,idCard);
        if (userSql != null) {
            throw new NameDuplicateException("用户名" + name + "重复！");
        }
        /**判断手机号是否重复*/
        userSql = userMapper.findUserByPhoneExcpIdcard(phone,idCard);
        if (userSql != null) {
            throw new PhoneDuplicateException("手机号" + phone + "重复！");
        }
        /**判断邮箱是否重复*/
        userSql= userMapper.findUserByEmailExcpIdcard(email,idCard);
        if (userSql != null) {
            throw new EmailDuplicateException("邮箱" + email + "重复！");
        }
        /**密码加密*/
        String password=passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        /**修复数据*/
        Integer rows=userMapper.updateData(user);
        if(rows!=1){
            throw new InsertException("修改用户数据出现未知错误，请联系系统管理员");
        }
    }

    /**获取数据*/
    @Override
    public User getData(String name){
        User userSql=userMapper.findUserByName(name);
        return userSql;
    }

    /**获取账户数据*/
    @Override
    public UserAccount getAccount(String name){
        //获取用户数据
        User userSql=userMapper.findUserByName(name);
        //获取账户数据
        UserAccount userAccountSql=userMapper.findUserAccountByOwnerId(userSql.getId());
        return userAccountSql;
    }

    /**获取流水记录*/
    @Override
    public List<UserAccountRecorder> getAccountRecorder(Integer id,Integer timeInterval){
        //获取用户数据
        List<UserAccountRecorder> userAccountRecorder;
        //获取全部账户流水
        if(timeInterval==0){
           userAccountRecorder=userMapper.findUserAccountRecorderByUserId(id);
        }
        //获取近七天或一个月的账户流水
        else{
            LocalDateTime today=LocalDateTime.now();
            LocalDateTime previous=today.minusDays(timeInterval);
            userAccountRecorder=userMapper.findUserAccountRecorderByUserIdLimitTime(id,previous);
        }

        return userAccountRecorder;
    }

    /**充值*/
    @Override
    public void rechargeAccount(Integer ownerId,double amount){
        //根据id查账户
        UserAccount userAccount=userMapper.findUserAccountByOwnerId(ownerId);
        //修改账户
        double amountAdded=amount+userAccount.getAmount();
        Integer rows=userMapper.updateAccount(amountAdded, ownerId);
        if(rows!=1){
            throw new InsertException("修改账户出现问题，请联系系统管理员！");
        }
    }


    //将商品添加到购物车
    public void addToShoppingCart(ShoppingCart shoppingCart){
        ShoppingCart shoppingCart1 = userMapper.getShoppingCartDataByUserIdAndCommodityId(shoppingCart.getUserId(),shoppingCart.getCommodityId());
        if(shoppingCart1==null){
            Commodity commodity = userMapper.getCommodityDataById(shoppingCart.getCommodityId());
            Shop shop = userMapper.getShopDataByShopId(commodity.getShopId());
            shoppingCart.setBusinessState(commodity.getBusinessState());
            shoppingCart.setCommodityCategoryName(commodity.getCategoryName());
            shoppingCart.setCommodityName(commodity.getCommodityName());
            shoppingCart.setIntroduction(commodity.getIntroduction());
            shoppingCart.setPrice(commodity.getPrice());
            shoppingCart.setImageUrl(commodity.getImageUrl());
            shoppingCart.setCommodityShopName(shop.getShopName());
            Integer rows = userMapper.addToShoppingCart(shoppingCart);
            if(rows!=1){
                throw new InsertException("商品加入购物车出现问题，请联系系统管理员！");
            }
        }else{
            shoppingCart1.setCommodityNum(shoppingCart.getCommodityNum()+shoppingCart1.getCommodityNum());
            userMapper.addShoppingCartCommodityNumber(shoppingCart1);
        }
    }

    /**将购物车中指定商品数量加1*/
    public void addShoppingCartCommodityNumber(Integer id){
        ShoppingCart shoppingCart = userMapper.getShoppingCartDataById(id);
        shoppingCart.setCommodityNum(shoppingCart.getCommodityNum()+1);
        userMapper.addShoppingCartCommodityNumber(shoppingCart);
    };

    /**将购物车中指定商品数量减1*/
    public void subShoppingCartCommodityNumber(Integer id) {
        ShoppingCart shoppingCart = userMapper.getShoppingCartDataById(id);
        if (shoppingCart.getCommodityNum() == 1) {
            userMapper.deleteShoppingCart(id);

        } else {
            shoppingCart.setCommodityNum(shoppingCart.getCommodityNum() - 1);
            userMapper.subShoppingCartCommodityNumber(shoppingCart);
        }
    };


    //展示购物车信息
    public List<ShoppingCart> showShoppingCart(Integer id) throws IOException {
        List<ShoppingCart> shoppingCarts = userMapper.showShoppingCart(id);
        for (ShoppingCart shoppingCart:shoppingCarts) {
            if (shoppingCart.getBusinessState() == 1) {
                String urls = shoppingCart.getImageUrl();
                String[] realUrls = urls.split(",");
                List<String> images = new ArrayList<String>();
                for (String url : realUrls) {
                    File file = new File(url);
                    byte[] imageData = Files.readAllBytes(file.toPath());
                    String imageDataString = Base64.getEncoder().encodeToString(imageData);
                    images.add(imageDataString);
                }
                shoppingCart.setImageString(images);
            }
        }
        return shoppingCarts;

    };

    //删除商品
    public void deleteShoppingcart(List<Integer> ids){
        for(Integer id : ids){
            Integer rows = userMapper.deleteShoppingCart(id);
            if(rows!=1){
                throw new InsertException("商品移出购物车出现问题，请联系系统管理员！");
            }
        }
    };

    /**根据用户id查找收货地址*/
    public List<ShippingAddress> findUserShippingAddressByUserId(Integer userId){
        return userMapper.findUserShippingAddressByUserId(userId);
    };

    /**新建收货地址*/
    public void addShippingAddress(ShippingAddress shippingAddress){
        userMapper.addShippingAddress(shippingAddress);
    };

    /**删除收货地址*/
    public void deleteShippingAddress(Integer id){
        userMapper.deleteShippingAddress(id);
    };


    /**修改收货地址*/
    public void updateShippingAddress(ShippingAddress shippingAddress){
        userMapper.updateShippingAddress(shippingAddress);
    };

    /**根据收货地址id查找收货地址*/
    public ShippingAddress findUserShippingAddressById(Integer id){
        return  userMapper.findUserShippingAddressById(id);
    };


}
