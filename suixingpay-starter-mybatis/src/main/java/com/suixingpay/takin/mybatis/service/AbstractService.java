/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月9日 下午1:05:46   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.service;

import java.io.Serializable;
import java.util.List;

import com.suixingpay.takin.data.domain.IPagination;
import com.suixingpay.takin.data.service.GenericService;
import com.suixingpay.takin.data.service.PageableService;
import com.suixingpay.takin.mybatis.domain.Pagination;
import com.suixingpay.takin.mybatis.mapper.GenericMapper;

/**
 * 通常抽象Service
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月9日 下午1:05:46
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月9日 下午1:05:46
 */
public interface AbstractService<T, Pk extends Serializable>
        extends GenericService<T, Pk>, PageableService<T, T> {

    GenericMapper<T, Pk> getMapper();

    @Override
    default void add(T entity) {
        getMapper().add(entity);
    }

    @Override
    default int updateById(T entity) {
        return getMapper().updateById(entity);
    }

    @Override
    default int deleteById(Pk pk) {
        return getMapper().deleteById(pk);
    }

    @Override
    default int deleteBatchIds(List<? extends Pk> pkList) {
        return getMapper().deleteBatchIds(pkList);
    }

    @Override
    default List<T> findAll() {
        return getMapper().findAll();
    }

    @Override
    default T findOneById(Pk pk) {
        return getMapper().findOneById(pk);
    }

    @Override
    default Long count(T condition) {
        return getMapper().count(condition);
    }

    @Override
    default List<T> find(T condition, IPagination pagination) {
        return getMapper().find(condition, (Pagination) pagination);
    }

    @Override
    default List<T> find(T condition) {
        return getMapper().find(condition);
    }

    @Override
    default IPagination buildPagination(int offset, int size) {
        return new Pagination(offset, size);
    }

}
