/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月21日 下午4:22:26   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.suixingpay.takin.cache.demo.to.User;
import com.suixingpay.takin.cache.demo.type.Sex;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月21日 下午4:22:26
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月21日 下午4:22:26
 */
public class JsonTest {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        GenericJackson2JsonRedisSerializer s = new GenericJackson2JsonRedisSerializer();
        List<Object> list = new ArrayList<>();
        User user = new User();
        user.setId(11);
        user.setName("test");
        user.setAge(10);
        user.setSex(Sex.FEMALE);
        list.add(user);
        list.add(12);
        list.add("nnnn");
        Map<Object, Object> m = new HashMap<>(16);
        m.put(user, user);
        m.put("int", 123);
        m.put(String.class, "aaa");
        list.add(m);

        byte[] json = s.serialize(list);

        System.out.println(new String(json));

        list = (List<Object>) s.deserialize(json);
        for (Object u : list) {
            System.out.println(u);
            if (u instanceof Map) {
                m = (Map) u;
                for (Map.Entry<Object, Object> item : m.entrySet()) {
                    Object key = item.getKey();
                    System.out.println("key---" + key.getClass().getName() + "--" + key);
                    Object val = item.getValue();
                    System.out.println("val-->" + val.getClass().getName() + "--" + val);

                }

                System.out.println(m.get(user));
            }
        }
    }
}
