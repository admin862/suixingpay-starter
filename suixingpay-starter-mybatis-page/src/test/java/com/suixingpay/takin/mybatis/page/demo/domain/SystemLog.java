/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月8日 下午2:02:25   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.page.demo.domain;

import lombok.Data;

/**  
 * TODO
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月8日 下午2:02:25
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月8日 下午2:02:25
 */
@Data
public class SystemLog {
    String uuid;
    String system;
    String method;
    String action;
}
