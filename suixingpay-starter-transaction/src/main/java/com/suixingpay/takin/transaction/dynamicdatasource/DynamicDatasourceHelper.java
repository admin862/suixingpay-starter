/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月11日 上午9:55:50   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction.dynamicdatasource;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSource;
import com.suixingpay.takin.mybatis.dynamicdatasource.DynamicDataSourceTransactionManager;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月11日 上午9:55:50
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月11日 上午9:55:50
 */
public class DynamicDatasourceHelper {

    /**
     * 判断是否为 DynamicDataSource
     * 
     * @param dataSource
     * @return
     */
    public boolean isDynamicDatasource(DataSource dataSource) {
        return dataSource instanceof DynamicDataSource;
    }

    /**
     * 判断是否为 DynamicDataSourceTransactionManager
     * 
     * @param transactionManager
     * @return
     */
    public boolean isDynamicDataSourceTransactionManager(PlatformTransactionManager transactionManager) {
        return transactionManager instanceof DynamicDataSourceTransactionManager;
    }

    public DataSourceTransactionManager createDynamicDataSourceTransactionManager(DataSource dataSource) {
        if (!isDynamicDatasource(dataSource)) {
            throw new RuntimeException(
                    "创建DynamicDataSourceTransactionManager 必须使用 DynamicDataSourceTransactionManager");
        }
        DynamicDataSourceTransactionManager transactionManager = new DynamicDataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
