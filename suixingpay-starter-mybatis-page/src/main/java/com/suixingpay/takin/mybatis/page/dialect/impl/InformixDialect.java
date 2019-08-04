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
 * @date: 2018年1月24日 下午11:41:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月24日 下午11:41:33
 */
public class InformixDialect extends AbstractDialect {

    @Override
    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql boundSql,
            RowBounds rowBounds, CacheKey pageKey) {
        int limit = rowBounds.getLimit();
        int offset = rowBounds.getOffset();
        paramMap.put(PAGEPARAMETER_FIRST, offset);
        paramMap.put(PAGEPARAMETER_SECOND, limit);
        // 处理pageKey
        pageKey.update(offset);
        pageKey.update(limit);
        // 处理参数配置
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
            if (offset > 0) {
                newParameterMappings
                        .add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_FIRST, Integer.class)
                                .build());
            }
            if (limit > 0) {
                newParameterMappings
                        .add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class)
                                .build());
            }
            if (boundSql != null && boundSql.getParameterMappings() != null) {
                newParameterMappings.addAll(boundSql.getParameterMappings());
            }
            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
        return paramMap;
    }

    @Override
    public String getPageSql(String sql, RowBounds rowBounds, CacheKey pageKey) {
        int limit = rowBounds.getLimit();
        int offset = rowBounds.getOffset();
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
        sqlBuilder.append("SELECT ");
        if (offset > 0) {
            sqlBuilder.append(" SKIP ? ");
        }
        if (limit > 0) {
            sqlBuilder.append(" FIRST ? ");
        }
        sqlBuilder.append(" * FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TEMP_T ");
        return sqlBuilder.toString();
    }

}
