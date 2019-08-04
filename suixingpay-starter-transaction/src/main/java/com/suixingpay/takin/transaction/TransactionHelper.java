/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月11日 上午10:58:35   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月11日 上午10:58:35
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月11日 上午10:58:35
 */
public class TransactionHelper {
    /**
     * 默认只对 "*Service" , "*ServiceImpl" Bean 进行事务处理,"*"表示模糊匹配, 比如 :
     * userService,orderServiceImpl
     */
    private static final String[] DEFAULT_TRANSACTION_BEAN_NAMES = { "*Service", "*ServiceImpl" };

    /**
     * 可传播事务配置
     */
    private static final String[] DEFAULT_REQUIRED_METHOD_RULE_TRANSACTION_ATTRIBUTES = { "add*", "save*", "insert*",
            "delete*", "update*", "edit*", "batch*", "create*", "remove*", };
    /**
     * 默认的只读事务
     */
    private static final String[] DEFAULT_READ_ONLY_METHOD_RULE_TRANSACTION_ATTRIBUTES = { "get*", "count*", "find*",
            "query*", "select*", "list*", "check*", "*", };

    private final TransactionManagerProperties properties;

    public TransactionHelper(TransactionManagerProperties properties) {
        this.properties = properties;
    }

    public TransactionInterceptor buildTransactionInterceptor(PlatformTransactionManager transactionManager) {
        // 通过方法名来配置事务属性
        NameMatchTransactionAttributeSource nameMatchTransactionAttributeSource = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute readOnly = this.readOnlyTransactionRule();
        RuleBasedTransactionAttribute required = this.transactionRule(TransactionDefinition.PROPAGATION_REQUIRED);

        // 默认的只读事务配置
        for (String methodName : DEFAULT_READ_ONLY_METHOD_RULE_TRANSACTION_ATTRIBUTES) {
            nameMatchTransactionAttributeSource.addTransactionalMethod(methodName, readOnly);
        }
        // 默认的传播事务配置
        for (String methodName : DEFAULT_REQUIRED_METHOD_RULE_TRANSACTION_ATTRIBUTES) {
            nameMatchTransactionAttributeSource.addTransactionalMethod(methodName, required);
        }
        // 定制的只读事务配置
        for (String methodName : properties.getReadOnlyTransactionAttributes()) {
            nameMatchTransactionAttributeSource.addTransactionalMethod(methodName, readOnly);
        }
        // 定制的传播事务配置
        for (String methodName : properties.getRequiredTransactionAttributes()) {
            nameMatchTransactionAttributeSource.addTransactionalMethod(methodName, required);
        }

        String[] requiredNewTransactionAttributes = properties.getRequiredNewTransactionAttributes();
        if (null != requiredNewTransactionAttributes && requiredNewTransactionAttributes.length > 0) {
            RuleBasedTransactionAttribute requiredNew = this
                    .transactionRule(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            // 定制新建事务
            for (String methodName : requiredNewTransactionAttributes) {
                nameMatchTransactionAttributeSource.addTransactionalMethod(methodName, requiredNew);
            }
        }
        
        String[] supportsTransactionAttributes = properties.getSupportsTransactionAttributes();
        if (null != supportsTransactionAttributes && supportsTransactionAttributes.length > 0) {
            RuleBasedTransactionAttribute supports = this.transactionRule(TransactionDefinition.PROPAGATION_SUPPORTS);
            // 定制新建事务
            for (String methodName : supportsTransactionAttributes) {
                nameMatchTransactionAttributeSource.addTransactionalMethod(methodName, supports);
            }
        }
        return new TransactionInterceptor(transactionManager, nameMatchTransactionAttributeSource);
    }

    private RuleBasedTransactionAttribute transactionRule(int propagationBehavior) {
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();
        required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
        required.setPropagationBehavior(propagationBehavior);
        required.setTimeout(TransactionDefinition.TIMEOUT_DEFAULT);
        Isolation isolation = properties.getIsolation();
        if (null == isolation) {
            isolation = Isolation.REPEATABLE_READ;
        }
        required.setIsolationLevel(isolation.value());
        return required;
    }

    /**
     * 只读事务
     * {@link org.springframework.transaction.annotation.Propagation#NOT_SUPPORTED}
     */
    private RuleBasedTransactionAttribute readOnlyTransactionRule() {
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        readOnly.setReadOnly(true);
        return readOnly;
    }

    public AbstractAutoProxyCreator customizeTransactionBeanNameAutoProxyCreator(String... interceptorNames) {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        // 设置定制的事务拦截器
        beanNameAutoProxyCreator.setInterceptorNames(interceptorNames);
        int size = DEFAULT_TRANSACTION_BEAN_NAMES.length + properties.getTransactionBeanNames().length;
        List<String> transactionBeanNames = new ArrayList<>(size);
        // 默认
        transactionBeanNames.addAll(Arrays.asList(DEFAULT_TRANSACTION_BEAN_NAMES));
        // 定制
        transactionBeanNames.addAll(Arrays.asList(properties.getTransactionBeanNames()));
        // 归集
        String[] beanNames = new String[transactionBeanNames.size()];
        beanNames = transactionBeanNames.toArray(beanNames);
        beanNameAutoProxyCreator.setBeanNames(beanNames);
        beanNameAutoProxyCreator.setProxyTargetClass(properties.isProxyTargetClass());
        beanNameAutoProxyCreator.setOrder(properties.getAopOrder());
        beanNameAutoProxyCreator.setOptimize(properties.isAopOptimize());
        return beanNameAutoProxyCreator;
    }
}
