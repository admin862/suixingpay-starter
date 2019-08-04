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
 * @date: 2018年1月24日 下午11:42:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月24日 下午11:42:16
 */
public class OracleDialect extends AbstractDialect {

    @Override
    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql boundSql,
            RowBounds rowBounds, CacheKey pageKey) {
        int offset = rowBounds.getOffset();
        int endRow = offset + rowBounds.getLimit();
        paramMap.put(PAGEPARAMETER_FIRST, endRow);
        paramMap.put(PAGEPARAMETER_SECOND, offset);
        // 处理pageKey
        pageKey.update(endRow);
        pageKey.update(offset);
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
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("SELECT * FROM ( ");
        sqlBuilder.append(" SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TMP_PAGE WHERE ROWNUM <= ? ");
        sqlBuilder.append(" ) WHERE ROW_ID > ? ");
        return sqlBuilder.toString();
    }

}
