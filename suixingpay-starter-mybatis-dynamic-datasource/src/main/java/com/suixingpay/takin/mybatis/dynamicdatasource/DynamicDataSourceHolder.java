/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2017年5月25日 下午3:01:22   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.dynamicdatasource;

/**
 * DynamicDataSourceHolder
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年5月25日 下午3:01:22
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2017年5月25日 下午3:01:22
 */
public final class DynamicDataSourceHolder {

    private static final ThreadLocal<DynamicDataSourceGlobal> holder = new ThreadLocal<DynamicDataSourceGlobal>();

    private DynamicDataSourceHolder() {
        //
    }

    public static void putDataSource(DynamicDataSourceGlobal dynamicDataSourceGlobal) {
        holder.set(dynamicDataSourceGlobal);
    }
    
    public static void putWriteDataSource() {
        holder.set(DynamicDataSourceGlobal.WRITE);
    }

    public static void putQueryDataSource() {
        holder.set(DynamicDataSourceGlobal.QUERY);
    }

    public static DynamicDataSourceGlobal getDataSource() {
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }

}
