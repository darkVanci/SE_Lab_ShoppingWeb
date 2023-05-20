package com.ss.shoppingweb.controller;

import com.ss.shoppingweb.exception.*;
import com.ss.shoppingweb.utils.JsonResult;
import com.ss.shoppingweb.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
/** 控制器类的基类 */
public class BaseController {
    /** 操作成功的状态码 */
    public static final int OK = 200;
    /** @ExceptionHandler用于统一处理方法抛出的异常 */
    @ExceptionHandler(ServiceException.class)
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<Void>(e);
        /**用户名未查询到异常*/
        if (e instanceof UserNotFoundException) {
            result.setState(4000);
        }
        /**用户名重复异常*/
        else if (e instanceof NameDuplicateException) {
            result.setState(4001);
        }
        /**手机号重复异常*/
        else if (e instanceof PhoneDuplicateException) {
            result.setState(4002);
        }
        /**身份证号重复异常*/
        else if(e instanceof IdcardDuplicateException){
            result.setState(4003);
        }
        /**邮箱重复异常*/
        else if(e instanceof EmailDuplicateException){
            result.setState(4004);
        }
        /**密码不正确异常*/
        else if (e instanceof PasswordNotMatchException) {
            result.setState(4005);
        }
        /**非商户注册商铺异常*/
        else if(e instanceof NotMerchantException){
            result.setState(4006);
        }
        /**不能删除异常*/
        else if(e instanceof CannotDeleteException){
            result.setState(4007);
        }
        /**用户余额不足异常*/
        else if(e instanceof  PocketNotAdequateException){
            result.setState(4008);
        }
        /**用户数据插入数据库异常*/
        else if (e instanceof InsertException) {
            result.setState(5000);
        }
        /**修改数据库异常*/
        else if(e instanceof UpdateException){
            result.setState(5001);
        }
        /**删除数据异常*/
        else if(e instanceof  DeleteException){
            result.setState(5002);
        }
        return result;
    }
}