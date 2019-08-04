/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月25日 下午3:05:20   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.dynamicdatasource.interceptor;

import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSourceGlobal;
import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSourceHolder;

/**
 * 动态切换数据源插件
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月25日 下午3:05:20
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月25日 下午3:05:20
 */
@Intercepts({ //
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }), //
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class })//

})
public class DynamicDatasourceInterceptor implements Interceptor {

    protected static final Logger logger = LoggerFactory.getLogger(DynamicDatasourceInterceptor.class);

    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        DynamicDataSourceGlobal dynamicDataSourceGlobal = DynamicDataSourceHolder.getDataSource();
        Object result;
        // 如果开启事务
        if (synchronizationActive) {
            boolean clearDataSource = false;
            if (null == dynamicDataSourceGlobal) {
                DynamicDataSourceHolder.putWriteDataSource();
                clearDataSource = true;
            }
            result = invocation.proceed();
            if (clearDataSource) {
                DynamicDataSourceHolder.clearDataSource();
            }
        } else {
            Object[] objects = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) objects[0];
            // 读方法
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                // !selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                } else {
                    BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    if (sql.matches(REGEX)) {
                        dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                    } else {
                        dynamicDataSourceGlobal = DynamicDataSourceGlobal.QUERY;
                    }
                }
            } else {
                dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
            }
            logger.debug("设置方法[{}] use [{}] Strategy, SqlCommandType [{}]..", mappedStatement.getId(),
                    dynamicDataSourceGlobal.name(), mappedStatement.getSqlCommandType().name());
            DynamicDataSourceHolder.putDataSource(dynamicDataSourceGlobal);
            result = invocation.proceed();
            DynamicDataSourceHolder.clearDataSource();
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
