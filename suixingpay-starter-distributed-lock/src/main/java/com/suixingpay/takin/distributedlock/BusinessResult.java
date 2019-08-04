/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月18日 下午5:33:43   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock;

/**
 * 业务是否完成检查结果
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月18日 下午5:33:43
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月18日 下午5:33:43
 */
public class BusinessResult<T> {

    private boolean finished;

    private T lastResult;

    /**
     * 业务是否已经完成
     * 
     * @return the finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * @param finished the finished to set
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * @return the lastResult
     */
    public T getLastResult() {
        return lastResult;
    }

    /**
     * @param lastResult the lastResult to set
     */
    public void setLastResult(T lastResult) {
        this.lastResult = lastResult;
    }

}
