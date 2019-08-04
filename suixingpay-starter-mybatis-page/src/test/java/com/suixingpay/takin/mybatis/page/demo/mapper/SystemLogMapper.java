/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月8日 下午2:08:05   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.page.demo.mapper;

import java.util.List;

import com.suixingpay.takin.mybatis.domain.Pagination;
import com.suixingpay.takin.mybatis.page.demo.domain.SystemLog;

public interface SystemLogMapper {

    /**
     * 根据查询条件统计总记录数据
     * 
     * @param condition
     * @return
     */
    Long count(SystemLog condition);

    /**
     * 根据查询条件获取数据
     * 
     * @param condition 查询条件
     * @param pagination 分页信息
     * @return
     */
    List<SystemLog> find(SystemLog condition, Pagination pagination);
    
    /**
     * 
     * @param condition
     * @return
     */
    List<SystemLog> find(SystemLog condition);
}
