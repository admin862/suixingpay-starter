/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月24日 下午11:00:39   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.page.dialect;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import com.suixingpay.takin.mybatis.page.util.MetaObjectUtil;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月24日 下午11:00:39
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月24日 下午11:00:39
 */
public abstract class AbstractDialect implements Dialect {

    @SuppressWarnings("unchecked")
    @Override
    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql,
            RowBounds rowBounds, CacheKey pageKey) {
        Map<String, Object> paramMap = null;
        if (parameterObject == null) {
            paramMap = new HashMap<String, Object>();
        } else if (parameterObject instanceof Map) {
            // 解决不可变Map的情况
            paramMap = new HashMap<String, Object>();
            paramMap.putAll((Map<String, Object>) parameterObject);
        } else {
            paramMap = new HashMap<String, Object>();
            // 动态sql时的判断条件不会出现在ParameterMapping中，但是必须有，所以这里需要收集所有的getter属性
            // TypeHandlerRegistry可以直接处理的会作为一个直接使用的对象进行处理
            boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry()
                    .hasTypeHandler(parameterObject.getClass());
            MetaObject metaObject = MetaObjectUtil.forObject(parameterObject);
            // 需要针对注解形式的MyProviderSqlSource保存原值
            if (!hasTypeHandler) {
                for (String name : metaObject.getGetterNames()) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            // 下面这段方法，主要解决一个常见类型的参数时的问题
            if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                    String name = parameterMapping.getProperty();
                    if (!name.equals(PAGEPARAMETER_FIRST) && !name.equals(PAGEPARAMETER_SECOND)
                            && paramMap.get(name) == null) {
                        if (hasTypeHandler || parameterMapping.getJavaType().equals(parameterObject.getClass())) {
                            paramMap.put(name, parameterObject);
                            break;
                        }
                    }
                }
            }
        }
        return processPageParameter(ms, paramMap, boundSql, rowBounds, pageKey);
    }

    /**
     * 处理分页参数
     *
     * @param ms
     * @param paramMap
     * @param boundSql
     * @param pageKey
     * @return
     */
    public abstract Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql boundSql,
            RowBounds rowBounds, CacheKey pageKey);

}
