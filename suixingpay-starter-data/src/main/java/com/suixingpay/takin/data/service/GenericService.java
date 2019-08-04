/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月9日 下午1:02:16   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.data.service;

import java.io.Serializable;
import java.util.List;

/**
 * GenericService
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月9日 下午1:02:16
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月9日 下午1:02:16
 */
public interface GenericService<T, Pk extends Serializable> {

    /**
     * 添加数据
     * 
     * @param entity
     * @return
     */
    void add(T entity);

    /**
     * 修改数据
     * 
     * @param entity
     * @return
     */
    int updateById(T entity);

    /**
     * 删除数据
     * 
     * @param pk
     * @return
     */
    int deleteById(Pk pk);

    /**
     * 批量删除
     * 
     * @param pkList
     * @return
     */
    int deleteBatchIds(List<? extends Pk> pkList);

    /**
     * 查询所有数据
     * 
     * @return
     */
    List<T> findAll();

    /**
     * 根据条件进行查询
     * 
     * @param entity
     * @return
     */
    List<T> find(T entity);

    /**
     * 根据主键获取详细数据
     * 
     * @param pk
     * @return
     */
    T findOneById(Pk pk);
}
