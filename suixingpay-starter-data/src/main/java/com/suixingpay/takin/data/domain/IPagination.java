package com.suixingpay.takin.data.domain;

import org.springframework.data.domain.Sort;

/**
 * 分页查询条件
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月2日 上午9:55:04
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月2日 上午9:55:04
 */
public interface IPagination {

    void setSort(Sort sort);

    /**
     * 排序
     * 
     * @return
     */
    Sort getSort();

    /**
     * 是否只是排序，不进行分页查询
     * 
     * @return
     */
    boolean idOrderByOnly();

    /**
     * offset
     * 
     * @return
     */
    public int getOffset();

    /**
     * limit
     * 
     * @return
     */
    public int getLimit();
}
