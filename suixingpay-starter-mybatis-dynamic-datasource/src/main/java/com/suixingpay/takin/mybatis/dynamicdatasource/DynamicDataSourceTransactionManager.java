/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月25日 下午3:02:18   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.dynamicdatasource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import lombok.extern.slf4j.Slf4j;

/**
 * DynamicDataSourceTransactionManager
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月25日 下午3:02:18
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月25日 下午3:02:18
 */
@Slf4j
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    /**
     * @Fields serialVersionUID
     */
    private static final long serialVersionUID = 2910105084279024743L;

    /**
     * 只读事务到读库，读写事务到写库
     * 
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        // 设置数据源
        boolean readOnly = definition.isReadOnly();
        if (readOnly) {
            log.debug("put query datasource on begin Transaction");
            DynamicDataSourceHolder.putQueryDataSource();
        } else {
            log.debug("put write datasource on begin Transaction");
            DynamicDataSourceHolder.putWriteDataSource();
        }
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     * 
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DynamicDataSourceHolder.clearDataSource();
    }
}