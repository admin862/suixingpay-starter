package com.suixingpay.takin.mybatis.domain;

import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Sort;

import com.suixingpay.takin.data.domain.IPagination;
import com.suixingpay.takin.exception.BadRequestException;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询，查询条件
 * 
 * @author jiayu.qiu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Pagination extends RowBounds implements IPagination {

    /**
     * 只是排序，不做分页查询
     */
    private boolean orderByOnly = false;

    /**
     * 排序
     */
    private Sort sort;

    public Pagination() {
        super(NO_ROW_OFFSET, NO_ROW_LIMIT);
    }

    public Pagination(int offset, int limit) {
        super(offset, limit);
    }

    /**
     * 
     * 通过 page(页码） 和 size(每页显示条数) 构建
     * @param page 从1开始的页码
     * @param size 每页显示条数
     * @return
     */
    public static Pagination of(int page, int size) {
        if (page <= 0) {
            throw new BadRequestException("页码 必须大于0");
        }
        if (size <= 0) {
            throw new BadRequestException("size 必须大于0");
        }
        int offset = (page - 1) * size;
        return new Pagination(offset, size);
    }

    @Override
    public boolean idOrderByOnly() {
        return orderByOnly;
    }
}
