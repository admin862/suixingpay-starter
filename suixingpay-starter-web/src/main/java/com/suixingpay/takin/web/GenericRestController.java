/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月9日 下午2:34:58   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.web;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.suixingpay.takin.exception.BaseException;
import com.suixingpay.takin.exception.NotFoundException;
import com.suixingpay.takin.mybatis.service.AbstractService;

import io.swagger.annotations.ApiOperation;

/**
 * RestController
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月22日 下午1:10:40
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月22日 下午1:10:40
 * @param <T> Domain
 * @param <Pk> 主键
 * @param <C> 分页查询条件
 */
public abstract class GenericRestController<T, Pk extends Serializable, C extends T> extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getcalsses());

    protected Class<?> getcalsses() {
        return this.getClass();
    }

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public void add(@Validated @RequestBody T object) throws BaseException {
        getService().add(object);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public void update(@PathVariable("id") Pk id, @Validated @RequestBody T object) throws BaseException {
        getService().updateById(object);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public void delete(@PathVariable("id") Pk id) {
        getService().deleteById(id);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public T info(@PathVariable("id") Pk id) {
        T t = getService().findOneById(id);
        if (t == null) {
            throw new NotFoundException();
        }
        return t;
    }

    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public Page<T> list(C t, //
            @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum, //
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return getService().findByPage(t, pageNum, pageSize, null);
    }

    protected abstract AbstractService<T, Pk> getService();

}
