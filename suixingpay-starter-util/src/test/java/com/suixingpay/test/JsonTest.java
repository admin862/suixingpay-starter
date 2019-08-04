/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年5月22日 下午2:03:58   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suixingpay.takin.util.json.JsonWrapper;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年5月22日 下午2:03:58
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年5月22日 下午2:03:58
 */
public class JsonTest {

    /**
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        String json = "{\"resCode\":\"000000\",\"resMsg\":\"SUCCESS\"}";
        try {
            for (int i = 0; i < 10; i++) {
                long start = System.currentTimeMillis();
                JsonWrapper jsonWrapper = new JsonWrapper(json);
                jsonWrapper.getValueByPath("resCode");
                System.out.println(System.currentTimeMillis() - start);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < 10; i++) {
                long start = System.currentTimeMillis();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);
                root.get("resCode");
                System.out.println(System.currentTimeMillis() - start);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }

}
