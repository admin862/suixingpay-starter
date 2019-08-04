/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月23日 下午12:00:50   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.page;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.suixingpay.takin.mybatis.domain.Pagination;
import com.suixingpay.takin.mybatis.page.dialect.Dialect;
import com.suixingpay.takin.mybatis.page.dialect.DialectUtil;

@Intercepts({ //
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }), //
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class })//
})
public class PageInterceptor implements Interceptor {
    private final Field additionalParametersField;

    public PageInterceptor() {
        try {
            // 反射获取 BoundSql 中的 additionalParameters 属性
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler<?> resultHandler = (ResultHandler<?>) args[3];
        Executor executor = (Executor) invocation.getTarget();
        // 判断是否需要进行分页查询，如果不需要，直接返回结果
        if (!isPageableOrOrderByOnly(rowBounds)) {
            return invocation.proceed();
        }
        CacheKey cacheKey;
        BoundSql boundSql;
        // 由于逻辑关系，只会进入一次
        if (args.length == 4) {// 4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {// 6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }
        Configuration configuration = ms.getConfiguration();
        Dialect dialect = DialectUtil.getDialect(configuration.getEnvironment().getDataSource());
        if (null == dialect) {
            throw new RuntimeException("没有找到数据库方言!");
        }
        String sql = boundSql.getSql();
        // 支持 order by
        boolean orderByOnly = false;
        if (rowBounds instanceof Pagination) {
            Pagination pagination = (Pagination) rowBounds;
            Optional<String> orderByOpt = getOrderBySql(pagination);
            if (orderByOpt.isPresent()) {
                String orderBy = orderByOpt.get();
                cacheKey.update(orderBy);
                sql = OrderByParser.converToOrderBySql(sql, orderBy);
            }
            orderByOnly = pagination.isOrderByOnly();
        }
        // 调用方言获取分页 sql
        if (!orderByOnly) {
            parameter = dialect.processParameterObject(ms, parameter, boundSql, rowBounds, cacheKey);
            sql = dialect.getPageSql(sql, rowBounds, cacheKey);
        }
        BoundSql pageBoundSql = new BoundSql(configuration, sql, boundSql.getParameterMappings(), parameter);
        @SuppressWarnings("unchecked")
        Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
        // 设置动态参数
        for (String key : additionalParameters.keySet()) {
            pageBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }
        // 执行分页查询
        return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, pageBoundSql);
    }

    private Optional<String> getOrderBySql(Pagination pagination) {
        Sort sort = pagination.getSort();
        if (null == sort) {
            return Optional.empty();
        }
        int ind = 0;
        StringBuilder orderBy = new StringBuilder();
        Iterator<Order> iterator = sort.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            String property = order.getProperty();
            if (null != property && property.trim().length() > 0) {
                if (ind > 0) {
                    orderBy.append(",");
                }
                orderBy.append(property);
                ind++;
                Direction direction = order.getDirection();
                if (null != direction) {
                    orderBy.append(" ").append(direction.name());
                }
            }
        }

        if (null != orderBy && orderBy.length() > 0) {
            return Optional.of(orderBy.toString());
        }
        return Optional.empty();
    }

    /**
     * 判断是否分页查询或排序查询
     * 
     * @param rowBounds
     * @return
     */
    private boolean isPageableOrOrderByOnly(RowBounds rowBounds) {
        if (null == rowBounds) {
            return false;
        }
        if (rowBounds instanceof Pagination) {
            Pagination pagination = (Pagination) rowBounds;
            if (pagination.isOrderByOnly()) {
                return true;
            }
        }
        return rowBounds.getLimit() > 0 && rowBounds.getLimit() != RowBounds.NO_ROW_LIMIT && rowBounds.getOffset() >= 0;
    }

    @Override
    public Object plugin(Object target) {
        // TODO Spring bean 方式配置时，如果没有配置属性就不会执行下面的 setProperties
        // 方法，就不会初始化，因此考虑在这个方法中做一次判断和初始化
        // TODO https://github.com/pagehelper/Mybatis-PageHelper/issues/26
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
