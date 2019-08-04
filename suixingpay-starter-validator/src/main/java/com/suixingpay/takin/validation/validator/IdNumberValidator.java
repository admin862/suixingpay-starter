/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月11日 下午5:39:15   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.suixingpay.takin.util.text.IdCardUtils;
import com.suixingpay.takin.validation.IdNumber;

/**
 * IdNumberValidator
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月11日 下午5:39:15
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月11日 下午5:39:15
 */
public class IdNumberValidator implements ConstraintValidator<IdNumber, String> {

    /**
     * initialize
     * 
     * @param constraintAnnotation
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(IdNumber constraintAnnotation) {
        // doNothing

    }

    /**
     * isValid
     * 
     * @param value
     * @param context
     * @return
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
     *      javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }
        return IdCardUtils.validateCard(value);
    }

}
