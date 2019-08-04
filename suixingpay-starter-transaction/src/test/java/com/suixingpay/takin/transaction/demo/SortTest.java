/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月22日 上午10:03:02   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.transaction.demo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月22日 上午10:03:02
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月22日 上午10:03:02
 */
public class SortTest {

    static Pattern pattern = Pattern.compile("[A-Za-z._]+");

    /**
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        List<String> muti = new ArrayList<>();
        muti.add("a.b.c");
        muti.add("a.b");
        muti.add("a.b.c.d");
        muti.sort(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        for (String t : muti) {
            if (t.matches("[A-Za-z._]+")) {
                System.out.println(t);
            }
        }
    }

}
