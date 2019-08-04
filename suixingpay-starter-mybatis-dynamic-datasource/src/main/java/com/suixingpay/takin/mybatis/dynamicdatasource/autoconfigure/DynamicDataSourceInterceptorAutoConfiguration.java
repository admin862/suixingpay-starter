/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月25日 上午9:19:42   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.dynamicdatasource.autoconfigure;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSource;
import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSourceTransactionManager;
import com.suixingpay.takin.mybatis.dynamicdatasource.interceptor.DynamicDatasourceInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月25日 上午9:19:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月25日 上午9:19:42
 */
@Slf4j
@Configuration
@ConditionalOnBean({ SqlSessionFactory.class })
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class DynamicDataSourceInterceptorAutoConfiguration {

    private final DataSource[] dataSources;

    private final PlatformTransactionManager[] transactionManagers;

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    public DynamicDataSourceInterceptorAutoConfiguration(ObjectProvider<DataSource[]> dataSourcesProvider, //
            ObjectProvider<PlatformTransactionManager[]> transactionManagersProvider, //
            ObjectProvider<List<SqlSessionFactory>> sqlSessionFactoryProvider) {
        if (null != dataSourcesProvider) {
            this.dataSources = dataSourcesProvider.getIfAvailable();
        } else {
            this.dataSources = null;
        }
        if (null != transactionManagersProvider) {
            this.transactionManagers = transactionManagersProvider.getIfAvailable();
        } else {
            this.transactionManagers = null;
        }
        if (null != sqlSessionFactoryProvider) {
            this.sqlSessionFactoryList = sqlSessionFactoryProvider.getIfAvailable();
        } else {
            this.sqlSessionFactoryList = null;
        }
    }

    @PostConstruct
    public void addPageInterceptor() throws Exception {
        checkDataSourceConfiguration();
        checkTransactionManagerConfiguration();
        DynamicDatasourceInterceptor interceptor = new DynamicDatasourceInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }

    private void checkDataSourceConfiguration() {
        boolean hasDynamicDataSource = false;
        if (null != dataSources) {
            for (DataSource dataSource : dataSources) {
                if (dataSource instanceof DynamicDataSource) {
                    hasDynamicDataSource = true;
                    break;
                }
            }
        }
        if (!hasDynamicDataSource) {
            log.error("没有配置动态数据源(DynamicDataSource)！");
            throw new RuntimeException("没有配置动态数据源(DynamicDataSource)！");
        }
    }

    private void checkTransactionManagerConfiguration() {
        boolean hasDynamicDataSourceTransactionManager = false;
        if (null != transactionManagers) {
            for (PlatformTransactionManager transactionManager : transactionManagers) {
                if (transactionManager instanceof DynamicDataSourceTransactionManager) {
                    hasDynamicDataSourceTransactionManager = true;
                    break;
                }
            }
        }
        if (!hasDynamicDataSourceTransactionManager) {
            log.error("没有配置动态数据源事务管理器(DynamicDataSourceTransactionManager)！");
            throw new RuntimeException("没有配置动态数据源事务管理器(DynamicDataSourceTransactionManager)！");
        }
    }
}
