/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.pojo;

/**
 * Redis锁信息
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class RedisLockInfo {
    private Long startTime;
    private Integer leaseTime;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(Integer leaseTime) {
        this.leaseTime = leaseTime;
    }

}
