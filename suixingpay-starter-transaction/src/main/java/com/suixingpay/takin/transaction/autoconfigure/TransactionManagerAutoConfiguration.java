/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月31日 上午11:26:19   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction.autoconfigure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.config.TransactionManagementConfigUtils;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.suixingpay.takin.transaction.TransactionHelper;
import com.suixingpay.takin.transaction.TransactionManagerProperties;
import com.suixingpay.takin.transaction.TransactionManagerProperties.DataSourceTransaction;
import com.suixingpay.takin.transaction.advisor.ExpressionAdvisor;
import com.suixingpay.takin.transaction.condition.MultiTransactionManagerCondition;
import com.suixingpay.takin.transaction.condition.SingletonTransactionManagerCondition;
import com.suixingpay.takin.transaction.dynamicdatasource.DynamicDatasourceHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 事务自动配置<br/>
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月31日 上午11:26:19
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月31日 上午11:26:19
 */
@Configuration
@ConditionalOnBean(DataSource.class)
// 如果没有使用 @EnableTransactionManagement
@ConditionalOnMissingBean(name = { TransactionManagementConfigUtils.TRANSACTION_ASPECT_BEAN_NAME,
        TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME })
@EnableConfigurationProperties(TransactionManagerProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
        DynamicDatasourceHelperConfiguration.class })
@ConditionalOnProperty(value = TransactionManagerProperties.TRANSACTION_ENABLED, matchIfMissing = true)
public class TransactionManagerAutoConfiguration {
    private static final String CUSTOMIZE_TRANSACTION_INTERCEPTOR_NAME = "customizeTransactionInterceptor";
    private static final String DEFAULT_TRANSACTION_MANAGER = "transactionManager";

    /**
     * 单个事务管理器配置
     * 
     * @author: qiujiayu[qiu_jy@suixingpay.com]
     * @date: 2018年2月11日 上午11:05:07
     * @version: V1.0
     * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月11日 上午11:05:07
     */
    @Slf4j
    @Configuration
    @ConditionalOnBean(DataSource.class)
    @Conditional(SingletonTransactionManagerCondition.class)
    @EnableConfigurationProperties(TransactionManagerProperties.class)
    @AutoConfigureAfter({ DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
            DynamicDatasourceHelperConfiguration.class })
    @ConditionalOnProperty(value = TransactionManagerProperties.TRANSACTION_ENABLED, matchIfMissing = true)
    public static class SingletonTransactionManagerConfiguration implements BeanFactoryAware {

        private BeanFactory beanFactory;

        private final TransactionManagerProperties properties;

        private final DataSourceTransactionManager[] transactionManagers;

        private final DataSource[] dataSources;

        private final DynamicDatasourceHelper dynamicDatasourceHelper;

        private final TransactionHelper transactionHelper;

        public SingletonTransactionManagerConfiguration(TransactionManagerProperties properties, //
                ObjectProvider<DataSourceTransactionManager[]> transactionManagersProvider, //
                ObjectProvider<DataSource[]> dataSourcesProvider, //
                ObjectProvider<DynamicDatasourceHelper> dynamicDatasourceHelperProvider) {
            this.properties = properties;
            this.transactionHelper = new TransactionHelper(properties);
            if (null != transactionManagersProvider) {
                this.transactionManagers = transactionManagersProvider.getIfAvailable();
            } else {
                this.transactionManagers = null;
            }
            if (null != dataSourcesProvider) {
                this.dataSources = dataSourcesProvider.getIfAvailable();
            } else {
                this.dataSources = null;
            }
            if (null != dynamicDatasourceHelperProvider) {
                this.dynamicDatasourceHelper = dynamicDatasourceHelperProvider.getIfAvailable();
            } else {
                this.dynamicDatasourceHelper = null;
            }
        }

        private void checkDynamicDatasource(DataSourceTransactionManager transactionManager) {
            if (!dynamicDatasourceHelper.isDynamicDataSourceTransactionManager(transactionManager)) {
                throw new RuntimeException("dynamicDatasource 为true时，必须使用DynamicDataSourceTransactionManager");
            }
            if (!dynamicDatasourceHelper.isDynamicDatasource(transactionManager.getDataSource())) {
                throw new RuntimeException("DynamicDataSourceTransactionManager 必须使用DynamicDataSource");
            }
        }

        /**
         * 配置事务拦截器
         *
         * @param transactionManager : 事务管理器
         */
        @Bean(CUSTOMIZE_TRANSACTION_INTERCEPTOR_NAME)
        public TransactionInterceptor sxfCustomizeTransactionInterceptor() {
            ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
            List<DataSourceTransaction> multi = properties.getMulti();
            int size = 0;
            if (null != multi && multi.size() > 0) {
                throw new RuntimeException("SingletonTransactionManagerCondition 没有生效!");
            }
            if (properties.isDynamicDatasource() && null == dynamicDatasourceHelper) {
                throw new RuntimeException("需要导入 suixingpay-starter-mybatis-dynamic-datasource 依赖");
            }

            DataSourceTransactionManager transactionManager;
            if (null != transactionManagers && transactionManagers.length > 0) {
                size = transactionManagers.length;
                if (size > 1) {
                    log.error(
                            "系统中已经配置多个 PlatformTransactionManager Bean, 请删除 PlatformTransactionManager 配置或将suixingpay.transaction.enabled 设置为false");
                    throw new RuntimeException("系统中已经配置多个 PlatformTransactionManager Bean");
                }
                if (size == 1) {
                    transactionManager = transactionManagers[0];
                    if (properties.isDynamicDatasource()) {
                        checkDynamicDatasource(transactionManager);
                    }
                    return this.transactionHelper.buildTransactionInterceptor(transactionManager);
                }
            }
            if (null == dataSources || dataSources.length == 0) {
                log.error("没有配置数据源，请先配置数据源");
                throw new RuntimeException("没有配置数据源，请先配置数据源");
            }
            DataSource dataSource = null;
            if (null != properties.getDataSourceName() && properties.getDataSourceName().length() > 0) {
                dataSource = (DataSource) beanFactory.getBean(properties.getDataSourceName());
                if (null == dataSource) {
                    log.error(properties.getDataSourceName() + "数据源不存在");
                    throw new RuntimeException(properties.getDataSourceName() + "数据源不存在");
                }
            }
            if (null == dataSource) {
                dataSource = beanFactory.getBean(DataSource.class);
            }
            if (dataSources.length == 1) {
                dataSource = dataSources[0];
            }
            if (null != dataSource) {
                if (properties.isDynamicDatasource()) {
                    if (!dynamicDatasourceHelper.isDynamicDatasource(dataSource)) {
                        throw new RuntimeException("需要创建DynamicDatasource 并将其设置为@Primary");
                    }
                    transactionManager = dynamicDatasourceHelper.createDynamicDataSourceTransactionManager(dataSource);
                } else {
                    transactionManager = new DataSourceTransactionManager();
                    transactionManager.setDataSource(dataSource);
                }
                configurableBeanFactory.registerSingleton(DEFAULT_TRANSACTION_MANAGER, transactionManager);
                log.debug("自动创建名为{}的DataSourceTransactionManager", DEFAULT_TRANSACTION_MANAGER);
                return this.transactionHelper.buildTransactionInterceptor(transactionManager);
            }

            throw new RuntimeException(
                    "事务管理自动配置失败，请检查数据源是否配置正确，如果是多数据源需要配置" + TransactionManagerProperties.PREFIX + ".multi");
        }

        /**
         * 配置事务代理
         * <p>
         * {@link #customizeTransactionInterceptor(PlatformTransactionManager)}
         */
        @Bean
        public AbstractAutoProxyCreator customizeTransactionBeanNameAutoProxyCreator() {
            sxfCustomizeTransactionInterceptor();
            return this.transactionHelper
                    .customizeTransactionBeanNameAutoProxyCreator(CUSTOMIZE_TRANSACTION_INTERCEPTOR_NAME);
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }

    @Slf4j
    @Configuration
    @ConditionalOnBean(DataSource.class)
    @Conditional(MultiTransactionManagerCondition.class)
    // 如果没有使用 @EnableTransactionManagement
    @ConditionalOnMissingBean(name = { TransactionManagementConfigUtils.TRANSACTION_ASPECT_BEAN_NAME,
            TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME })
    @EnableConfigurationProperties(TransactionManagerProperties.class)
    @AutoConfigureAfter({ DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
            DynamicDatasourceHelperConfiguration.class })
    @ConditionalOnProperty(value = TransactionManagerProperties.TRANSACTION_ENABLED, matchIfMissing = true)
    public static class MultiTransactionManagerConfiguration implements BeanFactoryAware {

        private static final String PACKAGE_NAME_REGEX = "[A-Za-z._]+";

        private static final String ADVISOR_BEAN_NAME_PREFIX = "sxfTransactionAdvisor_";

        private static final String ADVISORS = "sxfMultiTransactionExpressionAdvisor";

        private ConfigurableBeanFactory beanFactory;

        private final TransactionManagerProperties properties;

        private final DynamicDatasourceHelper dynamicDatasourceHelper;

        private final TransactionHelper transactionHelper;

        public MultiTransactionManagerConfiguration(TransactionManagerProperties properties, //
                ObjectProvider<DynamicDatasourceHelper> dynamicDatasourceHelperProvider) {
            this.properties = properties;
            this.dynamicDatasourceHelper = dynamicDatasourceHelperProvider.getIfAvailable();
            this.transactionHelper = new TransactionHelper(properties);
        }

        private DataSourceTransactionManager getTransactionManager(DataSource dataSource, DataSourceTransaction item) {
            String transactionManagerName = item.getTransactionManagerName();
            String dataSourceName = item.getDataSourceName();
            boolean registerTransactionManager = true;
            if (null == item.getTransactionManagerName() || item.getTransactionManagerName().length() == 0) {
                transactionManagerName = dataSourceName + "TransactionManager";
            }
            DataSourceTransactionManager transactionManager = null;
            if (beanFactory.containsBean(transactionManagerName)) {
                transactionManager = (DataSourceTransactionManager) beanFactory.getBean(transactionManagerName);
                registerTransactionManager = false;
            }
            if (null != transactionManager) {
                if (null != transactionManager.getDataSource() && dataSource != transactionManager.getDataSource()) {
                    log.error(transactionManagerName + "数据源不匹配");
                    throw new RuntimeException(transactionManagerName + "数据源不匹配");
                } else {
                    transactionManager.setDataSource(dataSource);
                }
            } else {
                if (item.isDynamicDatasource()) {
                    transactionManager = dynamicDatasourceHelper.createDynamicDataSourceTransactionManager(dataSource);
                } else {
                    transactionManager = new DataSourceTransactionManager();
                    transactionManager.setDataSource(dataSource);
                }
            }
            if (registerTransactionManager) {
                beanFactory.registerSingleton(transactionManagerName, transactionManager);
            }
            if (item.isDynamicDatasource()
                    && !dynamicDatasourceHelper.isDynamicDataSourceTransactionManager(transactionManager)) {
                throw new RuntimeException(transactionManagerName + "不是DynamicDataSourceTransactionManager");
            }
            return transactionManager;
        }

        private DataSource getDataSource(DataSourceTransaction item) {
            String dataSourceName = item.getDataSourceName();
            if (null == dataSourceName || dataSourceName.length() == 0) {
                throw new RuntimeException("数据源名称不能为空");
            }
            DataSource dataSource = (DataSource) beanFactory.getBean(dataSourceName);
            if (null == dataSource) {
                log.error(dataSourceName + "数据源不存在");
                throw new RuntimeException(dataSourceName + "数据源不存在");
            }
            if (item.isDynamicDatasource() && !dynamicDatasourceHelper.isDynamicDatasource(dataSource)) {
                throw new RuntimeException(dataSourceName + "不是DynamicDatasource");
            }
            return dataSource;
        }

        @Bean(name = ADVISORS)
        public List<ExpressionAdvisor> sxfMultiTransactionExpressionAdvisor() {
            List<DataSourceTransaction> multi = properties.getMulti();
            checkMulti(multi);
            int size = multi.size();
            List<ExpressionAdvisor> advisors = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                DataSourceTransaction item = multi.get(i);
                if (item.isDynamicDatasource() && null == dynamicDatasourceHelper) {
                    throw new RuntimeException("需要导入 suixingpay-starter-mybatis-dynamic-datasource 依赖");
                }
                String dataSourceName = item.getDataSourceName();
                DataSource dataSource = getDataSource(item);
                DataSourceTransactionManager transactionManager = getTransactionManager(dataSource, item);
                TransactionInterceptor interceptor = this.transactionHelper
                        .buildTransactionInterceptor(transactionManager);
                String expression = "execution(* " + item.getBasePackage() + "..*.*(..))";
                ExpressionAdvisor advisor = new ExpressionAdvisor(interceptor, expression);
                String advisorName = ADVISOR_BEAN_NAME_PREFIX + dataSourceName;
                beanFactory.registerSingleton(advisorName, advisor);
                advisors.add(advisor);
            }
            return advisors;
        }

        /**
         * 检查配置
         */
        private void checkMulti(List<DataSourceTransaction> multi) {
            multi.sort(new Comparator<DataSourceTransaction>() {

                @Override
                public int compare(DataSourceTransaction t1, DataSourceTransaction t2) {
                    String p1 = t1.getBasePackage();
                    int len1 = null == p1 ? 0 : p1.trim().length();
                    String p2 = t2.getBasePackage();
                    int len2 = null == p2 ? 0 : p2.trim().length();
                    return len1 - len2;
                }
            });
            int size = multi.size();
            for (int i = 0; i < size; i++) {
                DataSourceTransaction item = multi.get(i);
                String basePackage = item.getBasePackage();
                String dataSourceName = item.getDataSourceName();
                if (null == dataSourceName || dataSourceName.length() == 0) {
                    throw new RuntimeException("数据源名称不能为空");
                }
                if (null == basePackage || basePackage.trim().length() == 0) {
                    throw new RuntimeException(dataSourceName + "的basePackage不能为空");
                }
                if (!basePackage.matches(PACKAGE_NAME_REGEX)) {
                    throw new RuntimeException(basePackage + "不是合法包名");
                }
                for (int j = i + 1; j < size; j++) {
                    DataSourceTransaction item2 = multi.get(j);
                    String basePackage2 = item2.getBasePackage();
                    String dataSourceName2 = item2.getDataSourceName();
                    if (null == dataSourceName2 || dataSourceName2.length() == 0) {
                        throw new RuntimeException("数据源名称不能为空");
                    }
                    if (null == basePackage2 || basePackage2.trim().length() == 0) {
                        throw new RuntimeException(dataSourceName2 + "的basePackage不能为空");
                    }
                    // 检查package是否有重叠
                    if (basePackage2.startsWith(basePackage)) {
                        throw new RuntimeException(basePackage + "与" + basePackage2 + "有重叠!");
                    }
                }
            }
        }

        @Bean
        @ConditionalOnBean(name = ADVISORS)
        public AbstractAutoProxyCreator sxfMultiTransactionAdvisorAutoProxyCreator() {
            // 为了保证AOP配置正常生效，必须先创建Advisor
            sxfMultiTransactionExpressionAdvisor();
            DefaultAdvisorAutoProxyCreator proxy = new DefaultAdvisorAutoProxyCreator();
            proxy.setAdvisorBeanNamePrefix(ADVISOR_BEAN_NAME_PREFIX);
            proxy.setProxyTargetClass(properties.isProxyTargetClass());
            proxy.setOrder(properties.getAopOrder());
            proxy.setOptimize(properties.isAopOptimize());
            return proxy;
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = (ConfigurableBeanFactory) beanFactory;
        }
    }

}
