/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月17日 下午6:11:58   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSource;
import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSourceTransactionManager;
import com.suixingpay.takin.mybatis.dynamicdatasource.autoconfigure.DynamicDataSourceInterceptorAutoConfiguration;
import com.suixingpay.takin.transaction.dynamicdatasource.DynamicDatasourceHelper;

/**
 * 避免强依赖做特殊处理
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月17日 下午6:11:58
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月17日 下午6:11:58
 */
@Configuration
@ConditionalOnClass({ DynamicDataSourceInterceptorAutoConfiguration.class, DynamicDataSource.class,
        DynamicDataSourceTransactionManager.class })
@AutoConfigureAfter({ DynamicDataSourceInterceptorAutoConfiguration.class })
public class DynamicDatasourceHelperConfiguration {
    @Bean
    public DynamicDatasourceHelper dynamicDatasourceHelper() {
        return new DynamicDatasourceHelper();
    }
}
