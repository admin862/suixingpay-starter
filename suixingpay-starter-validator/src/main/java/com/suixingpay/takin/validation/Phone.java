/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月11日 下午6:02:07   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.suixingpay.takin.validation.validator.PhoneValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Phone
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月11日 下午6:02:07
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月11日 下午6:02:07
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { PhoneValidator.class })
public @interface Phone {

    String message() default "{com.suixingpay.validator.constraints.phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @interface List {
        Phone[] value();
    }
}