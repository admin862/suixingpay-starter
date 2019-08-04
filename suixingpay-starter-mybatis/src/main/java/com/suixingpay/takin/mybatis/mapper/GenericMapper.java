/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月8日 下午6:32:22   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.mapper;

import java.io.Serializable;
import java.util.List;

import com.suixingpay.takin.mybatis.domain.Pagination;

/**
 * GenericMapper
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月8日 下午6:32:22
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月8日 下午6:32:22
 */
public interface GenericMapper<T, Pk extends Serializable> {

    /**
     * 添加数据
     * 
     * @param entity
     */
    void add(T entity);

    /**
     * 修改数据
     * 
     * @param entity
     * @return 更新影响行数
     */
    int updateById(T entity);

    /**
     * 删除数据
     * 
     * @param pk
     * @return 删除影响行数
     */
    int deleteById(Pk pk);

    /**
     * 批量删除功能
     * 
     * @param pkList
     * @return 删除影响行数
     */
    int deleteBatchIds(List<? extends Pk> pkList);

    /**
     * 查询所有数据功能
     * 
     * @return
     */
    List<T> findAll();

    /**
     * 根据ID查询详情
     * 
     * @param pk
     * @return
     */
    T findOneById(Pk pk);

    /**
     * 根据查询条件统计总记录数据
     * 
     * @param condition
     * @return
     */
    Long count(T condition);

    /**
     * 根据查询条件获取数据(带分页功能)
     * 
     * @param condition 查询条件
     * @param pagination 分页信息
     * @return
     */
    List<T> find(T condition, Pagination pagination);

    /**
     * 根据查询条件获取数据(不带分页功能)
     * 
     * @param condition 查询条件
     * @return
     */
    List<T> find(T condition);

}
