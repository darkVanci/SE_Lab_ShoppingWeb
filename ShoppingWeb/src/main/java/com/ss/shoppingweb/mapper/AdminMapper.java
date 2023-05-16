package com.ss.shoppingweb.mapper;

import com.ss.shoppingweb.entity.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    /**展示出数据库中所有待审核的店铺*/
    @Select("Select * from shop where state=#{state}")
    List<Shop> showAllShop(Integer state);


    /**对开店申请驳回*/
    @Delete("delete from shop where id=#{shopId} ")
    Integer refuseOpen(Integer shopId);

    /**对开店申请批准*/
    @Update("update shop set state=1 where id=#{shopId}")
    Integer agreeOpen(Integer shopId);

    /**对关店申请驳回*/
    @Update("update shop set state=1 where id=#{id} ")
    Integer refuseClose(Integer id);

    /**对关店申请批准*/
    @Delete("delete from shop where id=#{shopId}")
    Integer agreeClose(Integer shopId);

    /**寻找商店账号*/
    @Select("Select * from shopaccount where shopId=#{shopId}")
    ShopAccount findShopAccountByShopId(Integer shopId);

    /**寻找商店账号*/
    @Select("Select * from shop where id=#{id}")
    Shop findShopByShopId(Integer id);

    /**删除商店账户*/
    @Delete("Delete from shopaccount where shopId=#{shopId}")
    Integer deleteShopAccount(Integer shopId);

    /**根据id查商户*/
    @Select("Select * from merchant where id=#{id}")
    Merchant findMerchantById(Integer id);

    /**插入中间账户数据*/
    @Insert("Insert into merchantaccountrecorder (merchantId,initiatorRole,initiatorId,initiatorName,receiverRole,receiverId,receiverName,amount,tradeTime,tradeRecord,inAndout) values (#{merchantId},#{initiatorRole},#{initiatorId},#{initiatorName},#{receiverRole},#{receiverId},#{receiverName},#{amount},#{tradeTime},#{tradeRecord},#{inAndout})")
    Integer insertMerchantAccountRecorder(MerchantAccountRecorder merchantAccountRecorder);

    /**寻找商户账号*/
    @Select("Select * from merchantaccount where ownerId=#{ownerId}")
    MerchantAccount findMerchantAccountByOwnerId(Integer ownerId);

    /**更新商户账户*/
    @Update("Update merchantaccount set amount=#{amount} where ownerId=#{ownerId}")
    Integer updateMerchantAmount(double amount,Integer ownerId);

    /**根据id查流水记录*/
    @Select("Select * from adminaccountrecorder where adminId=#{adminId}")
    List<AdminAccountRecorder> findAdminAccountRecorderByAdminId(Integer adminId);

    /**修改购物车中数据*/
    @Update("Update shoppingcart set businessState=-1 where shopId=#{shopId}")
    Integer setShoppingCartState(Integer shopId);


    /**根据商店id查询订单*/
    @Select("Select * from shoporder where shopId=#{shopId} and state=0")
    List<ShopOrder> findShopOrderByShopId(Integer shopId);



    /**删除商品种类*/
    @Delete("delete from commoditycategory where shopId=#{shopId} ")
    Integer deleteCategory(Integer shopId);


    /**根据店铺id查询商品种类*/
    @Select("select categoryName from commoditycategory where shopId=#{shopId}")
    List<String> findCommoditycategoryByShopId(Integer shopId);

    /** 根据用管理员名查询用户数据*/
    @Select("SELECT * FROM admin where name=#{name}")
    Admin findAdminByName(String name);

    /**插入商店账户*/
    @Insert("Insert into shopaccount (shopId,amount) values (#{shopId},#{amount})")
    Integer insertShopAccount(ShopAccount shopAccount);

    @Insert("Insert into adminaccountrecorder (adminId,initiatorRole,initiatorId,initiatorName,receiverRole,receiverId,receiverName,amount,tradeTime,tradeRecord,inAndout) values (#{adminId},#{initiatorRole},#{initiatorId},#{initiatorName},#{receiverRole},#{receiverId},#{receiverName},#{amount},#{tradeTime},#{tradeRecord},#{inAndout})")
    Integer insertAdminAccountRecorder(AdminAccountRecorder adminAccountRecorder);

    /**获取用户账户*/
    @Select("Select * from adminaccount where ownerId=#{ownerId}")
    AdminAccount findAdminAccountByOwnerId(Integer ownerId);

    /**根据id查中间账户*/
    @Select("Select * from middleaccount where id=#{id}")
    MiddleAccount findMiddleAccountById(Integer id);

    /**修改账户*/
    @Update("Update adminaccount set amount=#{amount} where ownerId=#{ownerId}")
    Integer updateAccount(double amount,Integer ownerId);

    /**修改中间账户*/
    @Update("Update middleaccount set amount=#{amount} where id=#{id}")
    Integer updateMiddleAccount(double amount,Integer id);

    /**获取中间账户记录*/
    @Select("Select * from middleaccountrecorder where initiatorRole=#{initiatorRole} and initiatorId=#{initiatorId} and receiverRole=#{receiverRole} and receiverId=#{receiverId}")
    MiddleAccountRecorder findMiddleAccountRecorder(Integer initiatorRole,Integer initiatorId,Integer receiverRole,Integer receiverId);

    /**通过商品ID寻找商品*/
    @Select("SELECT * from commodity where id= #{commodityId}")
    Commodity findCommodityById(Integer commodityId);

    /**通过商品ID寻找待修改的商品*/
    @Select("SELECT * from commodityfixed where fixId= #{commodityId}")
    Commodity findCommodityFixedById(Integer commodityId);

    /**对商品上架批准*/
    @Update("update commodity set applyState =1 , businessState=1, listTime = #{listTime} where id=#{id}  ")
    Integer agreeCommodity(Commodity commodity);

    /**对商品上架驳回*/
    @Delete("delete from commodity where id=#{id}  ")
    Integer refuseCommodity(Commodity commodity);

    /**产生上架成功记录*/
    @Insert("INSERT INTO listrecord (shopId,commodityId,listTime,state,commodityName) VALUES (#{shopId},#{id},#{listTime},#{applyState},#{commodityName})")
    Integer recordListSuccess(Commodity commodity);


    /**产生上架失败记录*/
    @Insert("INSERT INTO listrecord (shopId,commodityId,listTime,state,commodityName) VALUES (#{shopId},#{id},#{listTime},#{applyState},#{commodityName})")
    Integer recordListFail(Commodity commodity);

    /**展示出数据库中所有上架待审核的商品*/
    @Select("Select * from commodity where applyState = 0  ")
    List<Commodity> showAllCommodityToBeReviewed();


    /**对商品修改批准,删除暂存数据*/
    @Delete("DELETE from commodityfixed where id=#{id}")
    Integer agreeFixCommodity(CommodityFixed commodityFixed);

    /**更新批准修改的商品的数据*/
    @Update("UPDATE commodity set categoryName=#{categoryName},commodityName=#{commodityName},introduction=#{introduction},price=#{price},fixTime=#{fixTime},imageUrl=#{imageUrl} where id=#{fixId}  ")
    Integer updateFixedCommodity(CommodityFixed commodityFixed);

    @Update("UPDATE commodity set categoryName=#{categoryName},commodityName=#{commodityName},introduction=#{introduction},price=#{price},fixTime=#{fixTime} where id=#{fixId}  ")
    Integer updateFixedCommodityWithoutImageUrl(CommodityFixed commodityFixed);

    /**对商品修改驳回，删除暂存数据*/
    @Delete("DELETE from commodityfixed where id=#{id}")
    Integer refuseFixCommodity(CommodityFixed commodityFixed);

    /**产生修改成功记录*/
    @Insert("INSERT INTO fixrecord (shopId,commodityId,fixTime,state,commodityName) VALUES (#{shopId},#{fixId},#{fixTime},#{fixState},#{commodityName})")
    Integer recordFixListSuccess(CommodityFixed commodityFixed);


    /**产生修改失败记录*/
    @Insert("INSERT INTO fixrecord (shopId,commodityId,fixTime,state,commodityName) VALUES (#{shopId},#{fixId},#{fixTime},#{fixState},#{commodityName})")
    Integer recordFixListFail(CommodityFixed commodityFixed);

    /**展示出数据库中所有修改待审核的商品*/
    @Select("Select * from commodityfixed where fixState = 0 ")
    List<CommodityFixed> showAllCommodityFixedToBeReviewed();


    /**根据ID查询待修改的商品信息*/
    @Select("Select * from commodityfixed where id = #{id} ")
    CommodityFixed getCommodityFixedDataById(Integer id);

    /**插入中间账户数据*/
    @Insert("Insert into middleaccountrecorder (initiatorRole,initiatorId,initiatorName,receiverRole,receiverId,receiverName,amount,tradeTime,tradeRecord,inAndout) values (#{initiatorRole},#{initiatorId},#{initiatorName},#{receiverRole},#{receiverId},#{receiverName},#{amount},#{tradeTime},#{tradeRecord},#{inAndout})")
    Integer insertMiddleAccountRecorder(MiddleAccountRecorder middleAccountRecorder);
}
