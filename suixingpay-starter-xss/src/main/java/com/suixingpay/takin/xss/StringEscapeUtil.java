/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月12日 上午9:31:43   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.xss;

import java.util.Optional;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月12日 上午9:31:43
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月12日 上午9:31:43
 */
public class StringEscapeUtil {
    /**
     * 去除特殊字符，防止XSS
     * 
     * @param input 字符串
     * @return 去除一些危险字符后的内容
     */
    public static String escapeIllegalChars(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        StringBuilder escaped = new StringBuilder(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            Optional<String> reference = convertToReference(character);
            if (reference.isPresent()) {
                escaped.append(reference.get());
            } else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    private static Optional<String> convertToReference(char character) {
        switch (character) {
            case '<':
                return Optional.of("&lt;");
            case '>':
                return Optional.of("&gt;");
            case '"':
                return Optional.of("&quot;");
            // case '&': 避免重复转换
            // return Optional.of("&amp;");
            case '\'':
                return Optional.of("&#39;");
        }
        return Optional.empty();
    }

}
