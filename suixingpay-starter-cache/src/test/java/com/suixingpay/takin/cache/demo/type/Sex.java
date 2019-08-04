/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月15日 上午11:27:31   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo.type;

import com.suixingpay.takin.cache.demo.mybatis.handler.Identifiable;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月15日 上午11:27:31
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月15日 上午11:27:31
 */
public enum Sex implements AbstractEnum, Identifiable<Integer> {
    MALE(1, "男"),
    FEMALE(2, "女");
    private Integer id;
    private String name;

    private Sex(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
