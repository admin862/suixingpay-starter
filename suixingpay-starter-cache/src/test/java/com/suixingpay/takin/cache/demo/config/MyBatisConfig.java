/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月17日 上午9:09:34   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo.config;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.suixingpay.takin.cache.demo.mybatis.handler.EnumHandler;
import com.suixingpay.takin.cache.demo.mybatis.handler.Identifiable;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月17日 上午9:09:34
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月17日 上午9:09:34
 */
@Configuration
public class MyBatisConfig {
    private final MybatisProperties properties;
    private final Interceptor[] interceptors;
    private final ResourceLoader resourceLoader;
    private final DatabaseIdProvider databaseIdProvider;
    private final List<ConfigurationCustomizer> configurationCustomizers;

    public MyBatisConfig(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
            ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
            ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        this.properties = properties;
        if (null != interceptorsProvider) {
            this.interceptors = interceptorsProvider.getIfAvailable();
        } else {
            this.interceptors = null;
        }
        this.resourceLoader = resourceLoader;
        if (null != databaseIdProvider) {
            this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        } else {
            this.databaseIdProvider = null;
        }
        if (null != configurationCustomizersProvider) {
            this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
        } else {
            this.configurationCustomizers = null;
        }
    }

    @PostConstruct
    public void checkConfigFileExists() {
        if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
            Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource
                    + " (please add config file or check your Mybatis configuration)");
        }
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        org.apache.ibatis.session.Configuration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new org.apache.ibatis.session.Configuration();
        }
        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factory.setConfiguration(configuration);
        if (this.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            factory.setMapperLocations(this.properties.resolveMapperLocations());
        }

        // 枚举处理
        // 枚举所在的包
        String packageName = "com.suixingpay.takin.cache.demo.type";

        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
        resolverUtil.find(new ResolverUtil.IsA(Identifiable.class), packageName);
        Set<Class<? extends Class<?>>> handlerSet = resolverUtil.getClasses();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Class<?> type : handlerSet) {
            if (type == Identifiable.class) {
                continue;
            }
            if (type.isEnum()) {
                System.out.println(type.getName());
                typeHandlerRegistry.register(type, EnumHandler.class);
            }
        }

        return factory.getObject();
    }
}
