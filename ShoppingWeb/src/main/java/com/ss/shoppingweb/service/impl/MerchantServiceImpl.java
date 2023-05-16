package com.ss.shoppingweb.service.impl;

import com.ss.shoppingweb.entity.*;
import com.ss.shoppingweb.exception.*;
import com.ss.shoppingweb.mapper.MerchantMapper;
import com.ss.shoppingweb.service.MerchantService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**申请开店*/
    @Override
    public void openShopApply(Shop shop){
        String shopName= shop.getShopName();
        String idCard=shop.getIdCard();
        //查询身份是否为商户
        Merchant merchant=merchantMapper.findMerchantByIdcard(idCard);
        if(merchant==null){
            throw new NotMerchantException("非商户不能申请注册店铺！");
        }
        //查询输入的身份证号是否已经注册
        Shop result= merchantMapper.findShopByIdcard(idCard);
        //若身份证号已注册则发出异常
        if(result!=null){
            throw new IdcardDuplicateException("身份证号"+idCard+"已经注册过了！");
        }
        //判断商店名是否重复
        result=merchantMapper.findShopByShopName(shopName);
        if(result!=null){
            throw new NameDuplicateException("店铺名"+shopName+"重复！");
        }
        //查找商户id
        shop.setMerchantId(merchantMapper.findMerchantByIdcard(idCard).getId());
        shop.setState(0);
        Integer rows1=merchantMapper.insertShop(shop);
        if(rows1!=1) {
            throw new InsertException("添加店铺数据出现未知错误，请联系系统管理员！");
        }
        //将商品种类数据导入commoditycategory数据库
        Shop shopInserted=merchantMapper.findShopByIdcard(idCard);
        Integer shopId= shopInserted.getId();
        String[] categorylist=shop.getCategory().split(",");
        for (String s : categorylist) {
            Integer commoditycategory_rows = merchantMapper.insertCommoditycategory(shopId, s);
            if (commoditycategory_rows != 1) {
                throw new InsertException("添加商品种类数据出现未知错误，请联系系统管理员!");
            }
        }
        //将商店注册资金转移到中间账户
        MiddleAccountRecorder middleAccountRecorder=new MiddleAccountRecorder();
        middleAccountRecorder.setInitiatorRole(3);
        middleAccountRecorder.setInitiatorId(shopId);
        middleAccountRecorder.setInitiatorName(shop.getShopName());
        middleAccountRecorder.setReceiverRole(4);
        middleAccountRecorder.setReceiverId(1);
        middleAccountRecorder.setReceiverName("商城中间账户");
        middleAccountRecorder.setAmount(shopInserted.getFunds());
        middleAccountRecorder.setInAndout(1);
        middleAccountRecorder.setTradeRecord("申请开店注册资金");
        middleAccountRecorder.setTradeTime(LocalDateTime.now());
        Integer rows2=merchantMapper.InsertMiddleAccountRecorder(middleAccountRecorder);
        if(rows2!=1){
            throw new InsertException("添加中间账户记录出现错误，请联系系统管理员！");
        }
        MiddleAccount middleAccount=merchantMapper.findMiddleAccountById(1);
        double amountAdded=middleAccount.getAmount()+shop.getFunds();
        Integer rows3=merchantMapper.updateMiddleAccount(amountAdded, 1);
        if(rows3!=1){
            throw new InsertException("修改账户出现问题，请联系系统管理员！");
        }
    }
    //商户注册
    @Override
    public void register(Merchant merchant) {
        String name = merchant.getName();
        String phone=merchant.getPhone();
        String idCard=merchant.getIdCard();
        String email=merchant.getEmail();
        /*判断用户名是否重复*/
        Merchant merchantSql = merchantMapper.findMerchantByName(name);
        if (merchantSql != null) {
            throw new NameDuplicateException("商户名" + name + "重复！");
        }
        /*判断手机号是否已经注册过*/
        merchantSql = merchantMapper.findMerchantByPhone(phone);
        if (merchantSql != null) {
            throw new PhoneDuplicateException("手机号" + phone + "已经注册过！");
        }
        /*判断身份证号是否已经注册过*/
        merchantSql = merchantMapper.findMerchantByIdcard(idCard);
        if (merchantSql != null) {
            throw new IdcardDuplicateException("身份证号" + idCard + "已经注册过！");
        }
        /*判断邮箱是否已经注册过*/
        merchantSql= merchantMapper.findMerchantByEmail(email);
        if (merchantSql != null) {
            throw new EmailDuplicateException("邮箱" + email + "已经注册过！");
        }
        /**密码加密*/
        String password=passwordEncoder.encode(merchant.getPassword());
        merchant.setPassword(password);
        /*判断用户数据知否正常插入数据库*/
        Integer rows = merchantMapper.insertMerchant(merchant);
        if (rows != 1) {
            throw new InsertException("添加商户数据出现未知错误，请联系系统管理员");
        }
        /*关联个人账户*/
        Merchant merchantInserted=merchantMapper.findMerchantByName(name);
        MerchantAccount merchantAccount=new MerchantAccount();
        merchantAccount.setOwnerId(merchantInserted.getId());
        merchantAccount.setAmount(0);
        Integer rows2 = merchantMapper.insertMerchantAccount(merchantAccount);
        if(rows2!=1){
            throw new InsertException("添加商户账户数据出现未知错误，请联系系统管理员");
        }
    }


    //商户登录
    @Override
    public Merchant login(String name, String password) {
        //根据商名查询商户数据
        Merchant merchant = merchantMapper.findMerchantByName(name);
        //判断用户是否存在
        if (merchant == null) {
            throw new UserNotFoundException("商户"+name+"不存在！");
        }
        //判断密码是否输入正确
        if (!passwordEncoder.matches(password, merchant.getPassword())) {
            throw new PasswordNotMatchException("密码输入不正确！");
        }
        return merchant;
    }

    /**获取数据*/
    @Override
    public Merchant getData(String name){
        Merchant merchantSql=merchantMapper.findMerchantByName(name);
        return merchantSql;
    }

    /**获取账户数据*/
    @Override
    public MerchantAccount getAccount(String name){
        //获取用户数据
        Merchant merchantSql=merchantMapper.findMerchantByName(name);
        //获取账户数据
        MerchantAccount merchantAccountSql=merchantMapper.findMerchantAccountByOwnerId(merchantSql.getId());
        return merchantAccountSql;
    }

    /**信息修改*/
    @Override
    public void updateData(Merchant merchant){
        String name=merchant.getName();
        String phone=merchant.getPhone();
        String email=merchant.getEmail();
        String idCard=merchant.getIdCard();
        /**判断用户名是否重复*/
        Merchant merchantSql = merchantMapper.findMerchantByNameExcpIdcard(name,idCard);
        if (merchantSql != null) {
            throw new NameDuplicateException("商户名" + name + "重复！");
        }
        /**判断手机号是否重复*/
        merchantSql = merchantMapper.findMerchantByPhoneExcpIdcard(phone,idCard);
        if (merchantSql != null) {
            throw new PhoneDuplicateException("手机号" + phone + "重复！");
        }
        /**判断邮箱是否重复*/
        merchantSql= merchantMapper.findMerchantByEmailExcpIdcard(email,idCard);
        if (merchantSql != null) {
            throw new EmailDuplicateException("邮箱" + email + "重复！");
        }
        /**密码加密*/
        String password=passwordEncoder.encode(merchant.getPassword());
        merchant.setPassword(password);
        /**修复数据*/
        Integer rows=merchantMapper.updateData(merchant);
        if(rows!=1){
            throw new InsertException("修改用户数据出现未知错误，请联系系统管理员");
        }
    }

    /**充值*/
    @Override
    public void rechargeAccount(Integer ownerId,double amount){
        //根据id查账户
        MerchantAccount merchantAccount=merchantMapper.findMerchantAccountByOwnerId(ownerId);
        //修改账户
        double amountAdded=amount+merchantAccount.getAmount();
        Integer rows=merchantMapper.updateAccount(amountAdded, ownerId);
        if(rows!=1){
            throw new InsertException("修改账户出现问题，请联系系统管理员！");
        }
    }

    /**获取商店账户*/
    @Override
    public ShopAccount getShopAccount(String name){
        //根据name查商户
        Merchant merchant=merchantMapper.findMerchantByName(name);
        //根据商户id查商店
        Shop shop=merchantMapper.findShopByMerchantId(merchant.getId());
        //根据商店id查商店账户
        return merchantMapper.findShopAccountByShopId(shop.getId());
    }

    /**获取商店信息*/
    @Override
    public Shop getShopData(String name){
        //根据name查商户
        Merchant merchant=merchantMapper.findMerchantByName(name);
        //根据商户id查商店
        Shop shop=merchantMapper.findShopByMerchantId(merchant.getId());
        //根据商店id查商品种类
        List<String> category=merchantMapper.findCommodityCategoryByShopId(shop.getId());
        String categoryStr=String.join(",",category);
        shop.setCategory(categoryStr);
        return shop;
    }

    /**获取商店流水记录*/
    @Override
    public List<ShopAccountRecorder> getShopAccountRecorder(String name){
        //根据name查商户
        Merchant merchant=merchantMapper.findMerchantByName(name);
        //根据商户id查商店
        Shop shop=merchantMapper.findShopByMerchantId(merchant.getId());
        //根据商店id查流水记录
        List<ShopAccountRecorder> shopAccountRecorder=merchantMapper.findShopAccountRecorderByShopId(shop.getId());
        return shopAccountRecorder;
    }

    /**获取流水记录*/
    @Override
    public List<MerchantAccountRecorder> getAccountRecorder(String name){
        //获取用户数据
        Merchant merchantsql=merchantMapper.findMerchantByName(name);
        //获取账户流水
        List<MerchantAccountRecorder> merchantAccountRecorder=merchantMapper.findMerchantAccountRecorderByMerchantId(merchantsql.getId());
        return merchantAccountRecorder;
    }

    /**商店账户充值*/
    @Override
    public void rechargeShopAccount(Integer id,double amount){
        //根据id查账户
        ShopAccount shopAccount=merchantMapper.findShopAccountById(id);
        //修改账户
        double amountAdded=amount+shopAccount.getAmount();
        Integer rows=merchantMapper.updateShopAccount(amountAdded, id);
        if(rows!=1){
            throw new InsertException("修改商店账户出现问题，请联系系统管理员！");
        }
    }

    /**关店申请*/
    @Override
    public void closeShopApply(String name){
        Merchant merchant=merchantMapper.findMerchantByName(name);
        Integer merchantId=merchant.getId();
        //修改状态
        Integer rows=merchantMapper.updateShopStateByMerchantId(merchantId);
        if(rows!=1){
            throw new InsertException("修改数据库出现错误，请联系系统管理员！");
        }
    }

    //商户上架商品
    @Override
    public void addCommodity(Commodity commodity) {
        if(merchantMapper.getCommodityDataByCommodityName(commodity.getCommodityName())!=null)
        {
            throw new InsertException("该名字的商品已经存在");
        }
        else {
            //将商品设置为待申请状态
            commodity.setApplyState(0);
            //将商品上架时间设置为当前
            commodity.setListTime(LocalDateTime.now());
            Integer rows = merchantMapper.addCommodity(commodity);
            if (rows != 1) {
                throw new InsertException("上架商品出现未知错误，请联系系统管理员");
            }
            commodity.setId(merchantMapper.getCommodityDataByCommodityName(commodity.getCommodityName()).getId());
            rows = merchantMapper.addListRecord(commodity);
            if (rows != 1) {
                throw new InsertException("上架商品的记录存储出现未知错误，请联系系统管理员");
            }
        }
    }

    //商户查看申请记录
    @Override
    public List<ListRecord> selectRecords(Integer shopId, Integer pageNum){
        List<ListRecord> listRecords = merchantMapper.findRecords(shopId, pageNum);
        return listRecords;

    }

    //根据商店ID展示已经上架的商品
    @Override
    public List<Commodity> showCommodity(Integer shopId) throws IOException {
        List<Commodity> commodities = merchantMapper.findAllCommodityAlreadyPermitted(shopId);
        for (Commodity commodity:commodities){
            String urls = commodity.getImageUrl();
            String[] realUrls = urls.split(",");
            List<String> images = new ArrayList<String>();
            for(String url:realUrls) {
                File file = new File(url);
                byte[] imageData = Files.readAllBytes(file.toPath());
                String imageDataString = Base64.getEncoder().encodeToString(imageData);
                images.add(imageDataString);
            }
            commodity.setImageString(images);
        }
        return commodities;
    };


    /**修改商品信息*/
    public void fixCommodity(CommodityFixed commodityFixed){
        if(commodityFixed.getBusinessState()!=1)
        {
            throw new InsertException("不能修改未上架的商品");
        }
        else{
            commodityFixed.setFixState(0);
        //将商品上架时间设置为当前
            commodityFixed.setFixTime(LocalDateTime.now());
            CommodityFixed commodityFixed1 = merchantMapper.findCommodityFixedByFixId(commodityFixed);
            if(commodityFixed1!=null){
                merchantMapper.deleteFixCommodity(commodityFixed1);
            }
        Integer rows = merchantMapper.fixCommodity(commodityFixed);
        if(rows != 1){
            throw new InsertException("修改商品出现未知错误，请联系系统管理员");
        }
            commodityFixed.setId(commodityFixed.getFixId());
        rows = merchantMapper.addFixRecord(commodityFixed);
        if(rows != 1){
            throw new InsertException("修改商品的记录存储出现未知错误，请联系系统管理员");
        }
    }
    };


    /**查看该商店商品修改申请记录*/
    public List<FixRecord> findFixRecords(Integer shopId, Integer pageNum){
        List<FixRecord> fixRecords = merchantMapper.findFixRecords(shopId, pageNum);
        return  fixRecords;
    };


    //下架商品，删除商品数据
    @Override
    public void deleteCommodity(Commodity commodity) throws IOException {
        Integer rows = merchantMapper.deleteCommodity(commodity);
        if(rows != 1){
            throw new InsertException("删除商品出现未知错误，请联系系统管理员");
        }
        //删除图片
        String[] strings =commodity.getImageUrl().split(",");
        for(String path:strings){
            Path absolute = Paths.get(path);
            Files.delete(absolute);
        }
        //下架商品，购物车同步信息，可能更新多条数据，不检查rows是否为1
        merchantMapper.updateShoppingCart(commodity);
    };

    /**查询商店信息*/
    public Shop getShopDataByName(String shopName){
       return merchantMapper.getShopDataByName(shopName);
    };

    /**通过商店主人ID查询商店信息*/
    public Shop getShopDataByMerchantId(Integer merchantId){
        return  merchantMapper.getShopDataByMerchantId(merchantId);
    };

    /**通过商店ID查询商店信息*/
    public Shop getShopDataByShopId(Integer shopId){
        return  merchantMapper.getShopDataByShopId(shopId);
    };

    /**根据商品ID查询商品信息*/
    public Commodity getCommodityDataByCommodityId(Integer id){
        return merchantMapper.getCommodityDataByCommodityId(id);
    };


    /**根据商店ID查询上架记录总数*/
    public Integer getTotalNumOfListRecord(Integer shopId) {
        return merchantMapper.getTotalNumOfListRecord(shopId);

    }
    /**根据商店ID查询修改记录总数*/
    public Integer getTotalNumOfFixRecord(Integer shopId){
        return  merchantMapper.getTotalNumOfFixRecord(shopId);
    };






}