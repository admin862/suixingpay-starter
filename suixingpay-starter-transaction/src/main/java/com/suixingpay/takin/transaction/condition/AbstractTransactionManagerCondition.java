/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月12日 上午11:32:40   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月12日 上午11:32:40
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月12日 上午11:32:40
 */
public abstract class AbstractTransactionManagerCondition implements Condition {
    private static final String[] keys = { "suixingpay.transaction.multi[0].dataSourceName",
            "suixingpay.transaction.multi[0].data-source-name" };

    @Override
    public final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        boolean finded = false;
        for (String key : keys) {
            if (env.containsProperty(key)) {
                finded = true;
                break;
            }
        }
        return matches(finded);
    }

    protected abstract boolean matches(boolean finded);
}
