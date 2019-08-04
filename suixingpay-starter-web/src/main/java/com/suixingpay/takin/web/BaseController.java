/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月22日 下午1:09:09   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.web;

import com.suixingpay.takin.util.json.JsonUtil;
import com.suixingpay.takin.exception.BadRequestException;
import com.suixingpay.takin.exception.BaseException;
import com.suixingpay.takin.exception.ForbiddenException;
import com.suixingpay.takin.exception.NotFoundException;
import com.suixingpay.takin.exception.NotModifiedException;
import com.suixingpay.takin.exception.SystemException;
import com.suixingpay.takin.exception.UnauthorizedException;

/**
 * BaseController
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月22日 下午1:09:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月22日 下午1:09:09
 */
public abstract class BaseController {

    /**
     * 将字符串转为Json 字符串。 由于Spring MVC的方法，直接返回字符串时，不会转成json格式的。
     * 如果接口只返回字符串给客户端，客户端又希望是json格式的，则需要调用此方法，将字符串转成json字符串。
     * 
     * @param string
     * @return
     */
    protected String toJson(String string) {
        return JsonUtil.objectToJson(string);
    }

    /**
     * 请求错误，比如：参数验证失败
     * 
     * @throws BaseException
     */
    protected void badRequest() throws BaseException {
        throw new BadRequestException();
    }

    /**
     * 请求错误，比如：参数验证失败
     * 
     * @param message
     * @throws BaseException
     */
    protected void badRequest(String message) throws BaseException {
        throw new BadRequestException(message);
    }

    /**
     * 没有权限
     * 
     * @throws BaseException
     */
    protected void forbidden() throws BaseException {
        throw new ForbiddenException();
    }

    /**
     * 没有权限
     * 
     * @param message
     * @throws BaseException
     */
    protected void forbidden(String message) throws BaseException {
        throw new ForbiddenException(message);
    }

    /**
     * 没有找到数据
     * 
     * @throws BaseException
     */
    protected void notFound() throws BaseException {
        throw new NotFoundException();
    }

    /**
     * 没有找到数据
     * 
     * @param message
     * @throws BaseException
     */
    protected void notFound(String message) throws BaseException {
        throw new NotFoundException(message);
    }

    /**
     * 未修改
     * 
     * @throws BaseException
     */
    protected void notModified() throws BaseException {
        throw new NotModifiedException();
    }

    /**
     * 未修改
     * 
     * @param message
     * @throws BaseException
     */
    protected void notModified(String message) throws BaseException {
        throw new NotModifiedException(message);
    }

    /**
     * 业务错误或系统错误
     * 
     * @throws BaseException
     */
    protected void error() throws BaseException {
        throw new SystemException();
    }

    /**
     * 业务错误或系统错误
     * 
     * @param message
     * @throws BaseException
     */
    protected void error(String message) throws BaseException {
        throw new SystemException(message);
    }

    /**
     * 未登录
     * 
     * @throws BaseException
     */
    protected void unauthorized() throws BaseException {
        throw new UnauthorizedException();
    }

    /**
     * 未登录
     * 
     * @param message
     * @throws BaseException
     */
    protected void unauthorized(String message) throws BaseException {
        throw new UnauthorizedException(message);
    }
}
