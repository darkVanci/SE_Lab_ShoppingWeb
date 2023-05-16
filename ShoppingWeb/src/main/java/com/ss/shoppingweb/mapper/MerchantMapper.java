package com.ss.shoppingweb.mapper;

import com.ss.shoppingweb.entity.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface MerchantMapper {

    /**查找身份证是否为商户*/
    @Select("Select * from merchant where idCard=#{idCard}")
    Merchant findMerchantByIdcard(String idCard);

    /**根据身份证号查找店铺*/
    @Select("Select * from shop where idCard=#{idCard}")
    Shop findShopByIdcard(String idCard);

    /**根据店铺名查找店铺*/
    @Select("Select * from shop where shopName=#{shopName}")
    Shop findShopByShopName(String shopName);

    /**插入店铺信息*/
    @Insert("Insert into shop (merchantId,shopName,idCard,introduction,address,funds,registerdate," +
            "state) values (#{merchantId},#{shopName},#{idCard},#{introduction},#{address},#{funds},#{registerdate},#{state})")
    Integer insertShop(Shop shop);

    /**根据手机号查询用户数据*/
    @Select("SELECT * FROM merchant where phone=#{phone}")
    Merchant findMerchantByPhone(String phone);

    /**根据邮箱查询用户数据*/
    @Select("SELECT * FROM merchant where email=#{email}")
    Merchant findMerchantByEmail(String email);

    /**插入商品种类信息*/

    @Insert("Insert into commoditycategory (shopId,categoryName) values (#{shopId},#{categoryName})")
    Integer insertCommoditycategory(Integer shopId,String categoryName);

    /** 插入用户数据*/
    @Insert( "INSERT INTO merchant (name, password, phone, email, idCard) VALUES (#{name}, #{password}, #{phone}, #{email}, #{idCard})")
    Integer insertMerchant(Merchant merchant);

    /** 根据用户名查询用户数据*/
    @Select("SELECT * FROM merchant where name=#{name}")
    Merchant findMerchantByName(String name);


    /**插入商户账户数据*/
    @Insert("Insert into merchantaccount (ownerId,amount) values (#{ownerId},#{amount})")
    Integer insertMerchantAccount(MerchantAccount merchantAccount);


    /** 根据用户名查询用户数据*/
    @Select("SELECT * FROM merchant where name=#{name} and idCard!=#{idCard}")
    Merchant findMerchantByNameExcpIdcard(String name,String idCard);

    /**根据手机号查询用户数据*/
    @Select("SELECT * FROM merchant where phone=#{phone} and idCard!=#{idCard}")
    Merchant findMerchantByPhoneExcpIdcard(String phone,String idCard);

    /**根据邮箱查询用户数据*/
    @Select("SELECT * FROM merchant where email=#{email} and idCard!=#{idCard}")
    Merchant findMerchantByEmailExcpIdcard(String email,String idCard);

    /**根据身份证修改用户数据*/
    @Update("update merchant set name=#{name},password=#{password},phone=#{phone},email=#{email} where idCard=#{idCard}")
    Integer updateData(Merchant merchant);


    /**根据id查账户*/
    @Select("Select * from merchantaccount where ownerId=#{ownerId}")
    MerchantAccount findMerchantAccountByOwnerId(Integer ownerId);

    /*查找中间账户*/
    @Select("Select * from middleaccount where id=#{id}")
    MiddleAccount findMiddleAccountById(Integer id);

    /**修改账户*/
    @Update("Update merchantaccount set amount=#{amount} where ownerId=#{ownerId}")
    Integer updateAccount(double amount,Integer ownerId);

    /**修改中间账户*/
    @Update("Update middleaccount set amount=#{amount} where id=#{id}")
    Integer updateMiddleAccount(double amount,Integer id);

    /**根据商户id查商店*/
    @Select("Select * from shop where merchantId=#{merchantId}")
    Shop findShopByMerchantId(Integer merchantId);

    /**根据商店id查商店*/
    @Select("Select * from shopaccount where shopId=#{shopId}")
    ShopAccount findShopAccountByShopId(Integer shopId);

    /**根据商店id查商店流水*/
    @Select("Select * from shopaccountrecorder where shopId=#{shopId}")
    List<ShopAccountRecorder> findShopAccountRecorderByShopId(Integer shopId);

    /**根据商店id查商品种类*/
    @Select("Select categoryName from commoditycategory where shopId=#{shopId}")
    List<String> findCommodityCategoryByShopId(Integer shopId);

    /**根据id查账户*/
    @Select("Select * from shopaccount where id=#{id}")
    ShopAccount findShopAccountById(Integer id);

    /**根据id查流水记录*/
    @Select("Select * from merchantaccountrecorder where merchantId=#{merchantId}")
    List<MerchantAccountRecorder> findMerchantAccountRecorderByMerchantId(Integer merchantId);

    /**修改账户*/
    @Update("Update shopaccount set amount=#{amount} where id=#{id}")
    Integer updateShopAccount(double amount,Integer id);

    /**插入中间账户数据*/
    @Insert("Insert into middleaccountrecorder (initiatorRole,initiatorId,initiatorName,receiverRole,receiverId,receiverName,amount,tradeTime,tradeRecord,inAndout) values (#{initiatorRole},#{initiatorId},#{initiatorName},#{receiverRole},#{receiverId},#{receiverName},#{amount},#{tradeTime},#{tradeRecord},#{inAndout})")
    Integer InsertMiddleAccountRecorder(MiddleAccountRecorder middleAccountRecorder);
    /**新增商品信息*/
    @Insert("INSERT INTO commodity (shopId,categoryName,commodityName,introduction,price,applyState,businessState,listTime,fixTime,imageUrl) VALUES (#{shopId}, #{categoryName},#{commodityName},#{introduction},#{price},#{applyState},#{businessState},#{listTime},#{fixTime},#{imageUrl}) ")
    Integer addCommodity(Commodity commodity);

    /**产生一条上架记录*/
    @Insert("INSERT INTO listrecord (shopId,commodityId,listTime,state,commodityName) VALUES (#{shopId},#{id},#{listTime},#{applyState},#{commodityName})")
    Integer addListRecord(Commodity commodity);

    /**查看该商店商品上架申请记录*/
    @Select("select * from listrecord where shopId =#{shopId} order by listTime desc limit #{pageNum}, 10" )
    List<ListRecord> findRecords(Integer shopId,Integer pageNum);

    /**展示该商店的所有上架商品*/
    @Select("SELECT * from commodity where commodity.businessState=1 and commodity.shopId =#{shopId} ")
    List<Commodity> findAllCommodityAlreadyPermitted(Integer shopId);

    /**修改商品信息*/
    //将信息暂存commodityFixed数据表中
    @Insert("INSERT INTO commodityfixed (shopId,categoryName,commodityName,introduction,price,applyState,businessState,fixState,listTime,fixTime,fixId,imageUrl) VALUES (#{shopId}, #{categoryName},#{commodityName},#{introduction},#{price},#{applyState},#{businessState},#{fixState},#{listTime},#{fixTime},#{fixId},#{imageUrl}) ")
    Integer fixCommodity(CommodityFixed commodityFixed);

    /**根据fixId查询数据*/
    @Select("Select * from commodityfixed where fixId = #{fixId} ")
    CommodityFixed findCommodityFixedByFixId(CommodityFixed commodityFixed);

    /**多次提交修改，删除之前暂存数据*/
    @Delete("DELETE from commodityfixed where fixId=#{fixId}")
    Integer deleteFixCommodity(CommodityFixed commodityFixed);

    /**产生一条修改记录*/
    @Insert("INSERT INTO fixrecord (shopId,commodityId,fixTime,state,commodityName) VALUES (#{shopId},#{fixId},#{fixTime},#{fixState},#{commodityName})")
    Integer addFixRecord(CommodityFixed commodityFixed);

    /**查看该商店商品修改申请记录*/
    @Select("select * from fixrecord where shopId =#{shopId} order by fixTime desc limit #{pageNum}, 10")
    List<FixRecord> findFixRecords(Integer shopId, Integer pageNum);

    /**下架商品，删除商品数据*/
    @Delete("delete from commodity where id=#{id}")
    Integer deleteCommodity(Commodity commodity);

    /**下架商品，购物车同步信息*/
    @Update("update shoppingcart set businessState = -1 where shopId=#{shopId} and commodityId = #{id}")
    Integer updateShoppingCart(Commodity commodity);



    /**修改商店状态*/
    @Update("Update shop set state=-1 where merchantId=#{merchantId}")
    Integer updateShopStateByMerchantId(Integer merchantId);

    /**查询商店信息*/
    @Select("SELECT * from shop where shopName = #{shopName}")
    Shop getShopDataByName(String shopName);

    /**通过商店主人ID查询商店信息*/
    @Select("SELECT * from shop where merchantId = #{merchantId}")
    Shop getShopDataByMerchantId(Integer merchantId);

    /**通过商店ID查询商店信息*/
    @Select("SELECT * from shop where id = #{shopId}")
    Shop getShopDataByShopId(Integer shopId);


    /**根据商品ID查询商品信息*/
    @Select("select * from commodity where id = #{id}" )
    Commodity getCommodityDataByCommodityId(Integer id);

    /**根据商品名称查询商品信息*/
    @Select("select * from commodity where commodityName = #{Name}" )
    Commodity getCommodityDataByCommodityName(String Name);

    /**根据商店ID查询上架记录总数*/
    @Select("select count(*) from listrecord where shopId = #{shopId}")
    Integer getTotalNumOfListRecord(Integer shopId);

    /**根据商店ID查询修改记录总数*/
    @Select("select count(*) from fixrecord where shopId = #{shopId}")
    Integer getTotalNumOfFixRecord(Integer shopId);

}





















