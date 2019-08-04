/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: guohongjian[guo_hj@suixingpay.com] 
 * @date: 2017年10月10日 上午11:55:25   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.math;

import java.math.BigDecimal;

/**  
 * BigDecimal计算工具类
 * @author: guohongjian[guo_hj@suixingpay.com]
 * @date: 2017年10月10日 上午11:55:25
 * @version: V1.0
 * @review: guohongjian[guo_hj@suixingpay.com]/2017年10月10日 上午11:55:25
 */
public class BigDecimalUtils {
    
    public static BigDecimal add(Integer a1,Integer a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.add(b2);
    }
    
    public static BigDecimal add(Long a1,Long a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.add(b2);
    }
    
    public static BigDecimal add(Float a1,Float a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.add(b2);
    }
    
    public static BigDecimal add(Double a1,Double a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.add(b2);
    }
    
    public static BigDecimal subtract(Integer a1,Integer a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.subtract(b2);
    }
    
    public static BigDecimal subtract(Long a1,Long a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.subtract(b2);
    }
    
    public static BigDecimal subtract(Float a1,Float a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.subtract(b2);
    }
    
    public static BigDecimal subtract(Double a1,Double a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.subtract(b2);
    }
    
    public static BigDecimal multiply(Integer a1,Integer a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.multiply(b2);
    }
    
    public static BigDecimal multiply(Long a1,Long a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.multiply(b2);
    }
    
    public static BigDecimal multiply(Float a1,Float a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.multiply(b2);
    }
    
    public static BigDecimal multiply(Double a1,Double a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.multiply(b2);
    }
    
    public static BigDecimal divide(Integer a1,Integer a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.divide(b2);
    }
    
    public static BigDecimal divide(Long a1,Long a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.divide(b2);
    }
    
    public static BigDecimal divide(Float a1,Float a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.divide(b2);
    }
    
    public static BigDecimal divide(Double a1,Double a2){
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        return b1.divide(b2);
    }
}
