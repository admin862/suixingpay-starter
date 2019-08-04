package com.suixingpay.takin.data.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.suixingpay.takin.data.domain.IPagination;
import com.suixingpay.takin.data.domain.PageImpl;
import com.suixingpay.takin.exception.BadRequestException;

/**
 * 分页查询服务<br/>
 * 支持两种方式的分页：<br/>
 * 1：获取总记录数后进行分页；如果需要显示总页数据，则选此方式<br/>
 * 2：客户端一直加载数据，直到获取数据量小于pageSize时，认为是最后一页；客户端下拉加载数据通常选有此方式<br/>
 * 
 * @author jiayu.qiu
 * @param <C> 查询条件
 * @param <R> 查询结果类
 */
public interface PageableService<C, R> {

    /**
     * 分页查询 <br/>
     * 先查询总记录数据，然后再获取当前页数据<br/>
     * 
     * @param condition 查询条件
     * @param pageNum 从1开始的页码
     * @param pageSize
     * @param sort
     * @return 分页结果
     */
    default Page<R> findByPage(C condition, int page, int size, Sort sort) {
        if (page <= 0) {
            throw new BadRequestException("page 必须大于0");
        }
        if (size <= 0) {
            throw new BadRequestException("size 必须大于0");
        }
        Long totalCnt = count(condition);
        if (null == totalCnt || totalCnt.intValue() <= 0) {
            return null;
        }
        List<R> list = null;
        int offset = (page - 1) * size;
        IPagination pagination = buildPagination(offset, size);
        pagination.setSort(sort);
        if (null != totalCnt && totalCnt.intValue() > 0 && offset < totalCnt) {
            list = find(condition, pagination);
        } else {
            list = new ArrayList<>();
        }
        return new PageImpl<>(list, pagination, totalCnt);
    }

    IPagination buildPagination(int offset, int size);

    /**
     * 根据查询条件，获得总记录数
     * 
     * @param condition 查询条件
     * @return 总记录数
     */
    Long count(C condition);

    /**
     * 根据查询条件，获得数据列表
     * 
     * @param condition 查询条件
     * @param pagination
     * @return 结果集
     */
    List<R> find(C condition, IPagination pagination);
}
