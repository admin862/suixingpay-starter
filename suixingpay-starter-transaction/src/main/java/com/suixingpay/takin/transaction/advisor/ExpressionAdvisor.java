/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月8日 下午10:44:48   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction.advisor;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 使用AspectJ表达式<br/>
 * 此类暂时没有用，用于学习AOP配置<br/>
 * 1. 创建Advice， 如TransactionInterceptor、MethodInterceptor <br/>
 * 2. 创建Pointcut，如：AnnotationMatchingPointcut、AspectJExpressionPointcut <br/>
 * 3. 通过Interceptor和Pointcut 创建Advisor, 如DefaultPointcutAdvisor，或自己实现Advisor接口
 * <br/>
 * 4. 创建AbstractAutoProxyCreator,
 * 比如DefaultAdvisorAutoProxyCreator、BeanNameAutoProxyCreator <br/>
 * 
 * @Bean
 * @ConditionalOnBean(CacheHandler.class) public AbstractAdvisorAutoProxyCreator
 *                                        autoloadCacheAutoProxyCreator() {
 *                                        DefaultAdvisorAutoProxyCreator
 *                                        proxy=new
 *                                        DefaultAdvisorAutoProxyCreator();
 *                                        proxy.setAdvisorBeanNamePrefix("autoloadCache");
 *                                        proxy.setProxyTargetClass(config.isProxyTargetClass());
 *                                        //
 *                                        proxy.setInterceptorNames("cacheAdvisor","cacheDeleteAdvisor","cacheDeleteTransactionalAdvisor");//
 *                                        注意此处不需要设置，否则会执行两次 return proxy; }
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月8日 下午10:44:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月8日 下午10:44:48
 */
public class ExpressionAdvisor extends DefaultPointcutAdvisor {

    private static final long serialVersionUID = -7517485058124127607L;

    /**
     * @param transactionInterceptor
     * @param expression AspectJ表达式，如execution(*
     *            com.suixingpay.portal.service..*.*(..))
     */
    public ExpressionAdvisor(TransactionInterceptor transactionInterceptor, String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        super.setPointcut(pointcut);
        super.setAdvice(transactionInterceptor);
    }
}
