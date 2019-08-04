/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月25日 下午3:00:07   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.dynamicdatasource;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.Getter;
import lombok.Setter;

/**
 * 动态数据源实现读写分离
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月25日 下午3:00:07
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月25日 下午3:00:07
 */
@Slf4j
@Getter
@Setter
public class DynamicDataSource extends AbstractRoutingDataSource {

    private Object writeDataSource; // 写数据源

    private Object queryDataSource; // 读数据源

    @Override
    public void afterPropertiesSet() {
        if (this.writeDataSource == null) {
            throw new IllegalArgumentException("Property 'writeDataSource' is required");
        }
        setDefaultTargetDataSource(writeDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSourceGlobal.WRITE.name(), writeDataSource);
        if (queryDataSource != null) {
            targetDataSources.put(DynamicDataSourceGlobal.QUERY.name(), queryDataSource);
        }
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DynamicDataSourceGlobal global = null;
        DynamicDataSourceGlobal dynamicDataSourceGlobal = DynamicDataSourceHolder.getDataSource();
        if (dynamicDataSourceGlobal == null || dynamicDataSourceGlobal == DynamicDataSourceGlobal.WRITE) {
            global = DynamicDataSourceGlobal.WRITE;
        } else {
            global = DynamicDataSourceGlobal.QUERY;
        }
        log.debug("current datasouce:{}", global);
        return global.name();
    }
}
