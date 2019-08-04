/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月31日 上午11:33:19   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;

import lombok.Getter;
import lombok.Setter;

/**
 * 事务配置属性
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月31日 上午11:33:19
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月31日 上午11:33:19
 */
@Getter
@Setter
@ConfigurationProperties(prefix = TransactionManagerProperties.PREFIX)
public class TransactionManagerProperties {
    public static final String PREFIX = "suixingpay.transaction";

    public static final String TRANSACTION_ENABLED = PREFIX + ".enabled";

    private boolean enabled;

    private Isolation isolation = Isolation.REPEATABLE_READ;

    /**
     * 是否使用动态数据源(suixingpay-starter-mybatis-dynamic-datasource中的动态数据源)
     */
    private boolean dynamicDatasource = false;

    /**
     * 数据源名称
     */
    private String dataSourceName;

    /**
     * 自定义事务 BeanName 拦截
     */
    private String[] transactionBeanNames = {};
    /**
     * 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法; 只读事务
     */
    private String[] readOnlyTransactionAttributes = {};
    /**
     * 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法;
     * 传播事务(默认的PROPAGATION_REQUIRED){@link TransactionDefinition#PROPAGATION_REQUIRED}
     */
    private String[] requiredTransactionAttributes = {};

    /**
     * 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法;
     * 传播事务(默认的PROPAGATION_REQUIRED){@link TransactionDefinition#PROPAGATION_REQUIRED_NEW}
     */
    private String[] requiredNewTransactionAttributes = {};

    /**
     * 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法;
     * 传播事务(默认的PROPAGATION_SUPPORTS){@link TransactionDefinition#PROPAGATION_SUPPORTS}
     */
    private String[] supportsTransactionAttributes = {};

    private boolean proxyTargetClass = true;

    private int aopOrder = Ordered.LOWEST_PRECEDENCE;

    private boolean aopOptimize = false;
    /**
     * 多数据源时，数据源的BeanName与
     */
    private List<DataSourceTransaction> multi;

    @Getter
    @Setter
    public static class DataSourceTransaction {
        /**
         * 数据源名称(必填)
         */
        private String dataSourceName;

        /**
         * 是否使用动态数据源(suixingpay-starter-mybatis-dynamic-datasource中的动态数据源)
         */
        private boolean dynamicDatasource = false;

        /**
         * 开启事务的pacakge，注意不通带用*等特殊字符
         */
        private String basePackage;
        /**
         * transactionManager Bean名称<br>
         * 选填，如果没有填写，默认使用dataSourceName+"TransactionManager"<br>
         * 然后通过此bean name获取TransactionManager，如果没有获取到则自动新建一个，并注册到Spring 容器中<br>
         */
        private String transactionManagerName;
    }
}
