/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月15日 上午11:32:03   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo.mapper;

import com.suixingpay.takin.cache.demo.to.User;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月15日 上午11:32:03
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月15日 上午11:32:03
 */
public interface UserMapper {

    /**
     * TODO
     * 
     * @param user
     */
    void addUser(User user);

    /**
     * TODO
     * 
     * @param id
     * @return
     */
    User getById(Integer id);
}
