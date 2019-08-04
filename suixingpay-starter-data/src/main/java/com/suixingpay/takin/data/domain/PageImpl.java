/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月24日 下午11:50:39   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页查询结果
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月24日 下午11:54:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月24日 下午11:54:33
 */
@ApiModel
public class PageImpl<T> implements Page<T>, Serializable {

    private static final long serialVersionUID = 867755909294344406L;

    @ApiModelProperty("当前页面数据")
    private final List<T> content = new ArrayList<T>();

    @ApiModelProperty("总记录数")
    private long total;

    @ApiModelProperty("分页条件")
    private IPagination pagination;

    public PageImpl(List<T> content, IPagination pagination, long total) {
        this.content.addAll(content);
        this.pagination = pagination;
        this.total = !content.isEmpty() && pagination != null && pagination.getOffset() + pagination.getLimit() > total
                ? pagination.getOffset() + content.size()
                : total;
    }

    public PageImpl(List<T> content) {
        this(content, null, null == content ? 0 : content.size());
    }

    @Override
    public int getNumber() {
        return pagination == null ? 0 : pagination.getOffset() / pagination.getLimit() + 1;
    }

    @Override
    public int getSize() {
        return pagination == null ? 0 : pagination.getLimit();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return content.size() > 0;
    }

    @Override
    public Sort getSort() {
        return pagination == null ? null : pagination.getSort();
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public Pageable nextPageable() {
        return null;
    }

    @Override
    public Pageable previousPageable() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
        return new PageImpl<S>(getConvertedContent(converter), pagination, total);
    }

    protected <S> List<S> getConvertedContent(Converter<? super T, ? extends S> converter) {
        Assert.notNull(converter, "Converter must not be null!");
        List<S> result = new ArrayList<S>(content.size());
        for (T element : this) {
            result.add(converter.convert(element));
        }
        return result;
    }

}
