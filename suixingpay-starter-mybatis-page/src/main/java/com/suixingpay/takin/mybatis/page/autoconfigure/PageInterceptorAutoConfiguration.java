/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月25日 上午9:19:42   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.page.autoconfigure;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.suixingpay.takin.mybatis.page.PageInterceptor;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月25日 上午9:19:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月25日 上午9:19:42
 */
@Configuration
@ConditionalOnBean({ SqlSessionFactory.class })
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class PageInterceptorAutoConfiguration {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void addPageInterceptor() throws Exception {
        PageInterceptor pagePlugin = new PageInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(pagePlugin);
        }
    }
}
