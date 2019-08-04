package com.suixingpay.takin.mybatis.page.dialect.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import com.suixingpay.takin.mybatis.page.dialect.AbstractDialect;
import com.suixingpay.takin.mybatis.page.util.MetaObjectUtil;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月24日 下午11:41:24
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月24日 下午11:41:24
 */
public class HsqldbDialect extends AbstractDialect {

    @Override
    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql boundSql,
            RowBounds rowBounds, CacheKey pageKey) {
        int limit = rowBounds.getLimit();
        int offset = rowBounds.getOffset();
        paramMap.put(PAGEPARAMETER_FIRST, limit);
        paramMap.put(PAGEPARAMETER_SECOND, offset);
        // 处理pageKey
        pageKey.update(limit);
        pageKey.update(offset);
        // 处理参数配置
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
            if (boundSql != null && boundSql.getParameterMappings() != null) {
                newParameterMappings.addAll(boundSql.getParameterMappings());
            }
            if (limit > 0) {
                newParameterMappings
                        .add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_FIRST, Integer.class)
                                .build());
            }
            if (offset > 0) {
                newParameterMappings
                        .add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class)
                                .build());
            }
            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
        return paramMap;
    }

    @Override
    public String getPageSql(String sql, RowBounds rowBounds, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 20);
        sqlBuilder.append(sql);
        if (rowBounds.getLimit() > 0) {
            sqlBuilder.append(" LIMIT ? ");
        }
        if (rowBounds.getOffset() > 0) {
            sqlBuilder.append(" OFFSET ? ");
        }
        return sqlBuilder.toString();
    }
}
