/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月15日 下午2:33:53   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.druid.decrypt;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * SecretGenerate key 生成器
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月15日 下午2:33:53
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月15日 下午2:33:53
 */
public final class SecretGenerate {

    public static void main(String[] args) throws Exception {

        int keySize = 512;
        String password = args[0];
        if (args.length >1 && null != args[1]) {
            keySize = Integer.parseInt(args[1]);
        }
        String[] arr = ConfigTools.genKeyPair(keySize);
        System.out.println("privateKey:" + arr[0]);
        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + ConfigTools.encrypt(arr[0], password));
    }

}
