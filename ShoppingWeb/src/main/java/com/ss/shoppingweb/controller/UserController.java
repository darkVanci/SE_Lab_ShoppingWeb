package com.ss.shoppingweb.controller;

import com.ss.shoppingweb.entity.ShippingAddress;
import com.ss.shoppingweb.entity.ShoppingCart;
import com.ss.shoppingweb.entity.User;
import com.ss.shoppingweb.entity.UserAccount;
import com.ss.shoppingweb.service.UserService;
import com.ss.shoppingweb.utils.JsonResult;
import com.ss.shoppingweb.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class  UserController extends BaseController{
    @Autowired
    private UserService userService;

    /**用户注册*/
    @PostMapping("/register")
    public JsonResult<Void> register(@RequestBody User user) {
        // 调用业务对象执行注册
        userService.register(user);
        // 返回
        return new JsonResult<>(OK);
    }

    /**用户登录*/
    @PostMapping("/login")
    public JsonResult<String> login(@RequestBody User user){
        // 调用业务对象的方法执行登录，并获取返回值
        User data = userService.login(user.getName(), user.getPassword());
        String jwt;
        if(data!=null){
            Map<String, Object> claims= new HashMap<>();
            claims.put("name",data.getName());
            //标识普通用户的role为1
            claims.put("role","1");
            claims.put("id",data.getId());
            jwt= JwtUtils.generateJwt(claims);
        }
        else{
            jwt=null;
        }
        // 将以上返回值和状态码OK封装到响应结果中并返回
        return new JsonResult<>(OK, jwt);
    }

    /**用户信息修改*/
    @PostMapping("/updateData")
    public JsonResult<Void> updateData(@RequestBody User user){
        //调用业务层执行修改
        userService.updateData(user);
        //返回
        return new JsonResult<>(OK);
    }

    /**账户充值*/
    @PostMapping("/rechargeAccount")
    public JsonResult<Void> rechargeAccount(@RequestBody UserAccount userAccount){
        userService.rechargeAccount(userAccount.getOwnerId(),userAccount.getAmount());
        return new JsonResult<>(OK);
    }

    //将商品添加到购物车
    @PostMapping("/addToShoppingCart")
    public JsonResult<Void> addToShoppingCart(@RequestBody ShoppingCart shoppingCart){
        userService.addToShoppingCart(shoppingCart);
        return  new JsonResult<>(OK);
    };


    //展示购物车信息
    @PostMapping("/showShoppingCart")
    public JsonResult<List<ShoppingCart>> showShoppingCart(@RequestBody ShoppingCart shoppingCart) throws IOException {
        List<ShoppingCart> data= userService.showShoppingCart(shoppingCart.getUserId());
        return new JsonResult<List<ShoppingCart>>(OK,data);
    };

    //删除商品
    @RequestMapping("/deleteShoppingCart")
    public JsonResult<Void> deleteShoppingCart(@RequestParam String ids){
        Integer length = ids.length();
        String realIds = ids.substring(1,length-1);
        String[] strArr = realIds.split(",");
        ArrayList<Integer> intArr = new ArrayList<>();
        for(String str : strArr){
            Integer id = Integer.valueOf(str);
            intArr.add(id);
        }
        userService.deleteShoppingcart(intArr);
        return new JsonResult<>(OK);
    };

    /**根据用户id查找收货地址*/
    @RequestMapping("/findUserShippingAddressByUserId")
    public JsonResult<List<ShippingAddress>> findUserShippingAddressByUserId(@RequestParam Integer userId){
        List<ShippingAddress> data = userService.findUserShippingAddressByUserId(userId);
        return new JsonResult<>(OK,data);
    }


    /**新建收货地址*/
    @RequestMapping("/addShippingAddress")
    public JsonResult<Void> addShippingAddress(@RequestBody ShippingAddress shippingAddress){
        userService.addShippingAddress(shippingAddress);
        return new JsonResult<>(OK);
    };

    /**删除收货地址*/
    @RequestMapping("/deleteShippingAddress")
    public JsonResult<Void> deleteShippingAddress(@RequestParam Integer id){
        userService.deleteShippingAddress(id);
        return new JsonResult<>(OK);
    };

    /**修改收货地址*/
    @RequestMapping("/updateShippingAddress")
    public JsonResult<Void> updateShippingAddress(@RequestBody ShippingAddress shippingAddress){
        userService.updateShippingAddress(shippingAddress);
        return new JsonResult<>(OK);
    };


    /**根据收货地址id查找收货地址*/
    @RequestMapping("/findUserShippingAddressById")
    public JsonResult<ShippingAddress> findUserShippingAddressById(@RequestParam Integer id){
        ShippingAddress data = userService.findUserShippingAddressById(id);
        return new JsonResult<>(OK,data);
    };
}
