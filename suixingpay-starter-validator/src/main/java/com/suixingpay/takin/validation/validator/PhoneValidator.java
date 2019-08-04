/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月11日 下午6:02:58   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.validation.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.suixingpay.takin.validation.Phone;

/**
 * PhoneValidator
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月11日 下午6:02:58
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月11日 下午6:02:58
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final Pattern MOBILE_REGEX = Pattern
            .compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    @Override
    public void initialize(Phone constraintAnnotation) {
        // doNothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }
        return MOBILE_REGEX.matcher(value).find();
    }

}