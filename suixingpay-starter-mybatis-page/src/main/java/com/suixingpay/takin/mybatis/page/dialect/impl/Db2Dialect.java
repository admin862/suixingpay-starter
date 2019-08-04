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
 * @date: 2018年1月24日 下午11:40:59
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月24日 下午11:40:59
 */
public class Db2Dialect extends AbstractDialect {

    @Override
    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql boundSql,
            RowBounds rowBounds, CacheKey pageKey) {
        int begin = rowBounds.getOffset() + 1;
        int end = rowBounds.getOffset() + rowBounds.getLimit();
        paramMap.put(PAGEPARAMETER_FIRST, begin);
        paramMap.put(PAGEPARAMETER_SECOND, end);
        // 处理pageKey
        pageKey.update(begin);
        pageKey.update(end);
        // 处理参数配置
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
            if (boundSql != null && boundSql.getParameterMappings() != null) {
                newParameterMappings.addAll(boundSql.getParameterMappings());
            }
            newParameterMappings.add(
                    new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_FIRST, Integer.class).build());
            newParameterMappings.add(
                    new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class).build());
            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
        return paramMap;
    }

    @Override
    public String getPageSql(String sql, RowBounds rowBounds, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 140);
        sqlBuilder.append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN ? AND ?");
        return sqlBuilder.toString();
    }

}
