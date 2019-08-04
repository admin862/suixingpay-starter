/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年9月14日 上午9:51:55   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token;

import lombok.Getter;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月14日 上午9:51:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月14日 上午9:51:55
 */
@Getter
public class TokenWapper {
    private final String token;
    private final TokenInfo tokenInfo;

    public TokenWapper(String token, TokenInfo tokenInfo) {
        this.token = token;
        this.tokenInfo = tokenInfo;
    }
}
